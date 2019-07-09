package com.endlesscreator.tiviewlib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.endlesscreator.tibaselib.frame.SingleManager;
import com.endlesscreator.titoollib.utils.AlgorithmUtil;
import com.endlesscreator.tiviewlib.view.model.tirecyclerview.ScrollEnabledGridLayoutManager;
import com.endlesscreator.tiviewlib.view.model.tirecyclerview.ScrollEnabledStaggeredGridLayoutManager;
import com.endlesscreator.tiviewlib.view.model.tirecyclerview.TopSmoothScroller;

import java.util.ArrayList;
import java.util.List;


/**
 * 通用RecyclerView控件，封装Item点击、长点击、Item子布局点击等常用操作
 */
public class TiRecyclerView extends RecyclerView implements Runnable {

    private static final int STATE_NONE = 0;// 初始状态
    private static final int STATE_ACTION = 1;// 按下后没有超出mMaxOffset范围
    private static final int STATE_CLICK = 2;// 触发Click，正在处理
    private static final int STATE_LONG_CLICK = 3;// 触发LongClick，正在处理
    private static final int STATE_WAIT_LEFT_DELETE = 4;// 超出mMaxOffset范围，此时仅能触发左滑删除（此状态若未触发左滑，与STATE_NONE状态类似）
    private static final int STATE_LEFT_DELETE = 5;// 触发左滑删除，正在操作
    private int mOperationState = STATE_NONE;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    private OnItemChildViewClickListener mItemChildViewClickListener;
    private OnScrollStateChangedListener mScrollStateChangedListener;
    private OnCancelDownViewTouchListener mOnCancelDownViewTouchListener;

    // 给左滑使用删除使用 start
    private Integer mItemLeftMoveViewID, mItemRightDeleteViewID;
    private View mItemLeftMoveView, mRightDeleteView;
    private int[] mItemMoveDeleteInitLTRB;
    // 给左滑使用删除使用 end

    private List<Integer> mClickChildIDList = null;
    private Integer mClickChildIndexParent = null;// mClickChildIDList != null 时生效， null-全部扫描，!null-扫描某个位置
    private View mDownView;// 当设置 OnCancelDownViewTouchListener 监听时，此变量才在按下时赋值

    private long mLongClickTime = 700;
    private float mMaxOffset = 3;// init 中单位转换为dp
    private float mInitRawX, mInitRawY;
    private float mCurRowX, mCurRowY;
    private float mCurX, mCurY;

    private TopSmoothScroller mScroller;

    public TiRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public TiRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TiRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mMaxOffset = AlgorithmUtil.dp2px(context, 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mItemClickListener != null || mItemLongClickListener != null || (mItemLeftMoveViewID != null && mItemRightDeleteViewID != null) || (mItemChildViewClickListener != null && mClickChildIDList != null)) {
            mCurX = e.getX();
            mCurY = e.getY();
            mCurRowX = e.getRawX();
            mCurRowY = e.getRawY();
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mInitRawX = mCurRowX;
                    mInitRawY = mCurRowY;
                    mOperationState = STATE_ACTION;
                    if (mOnCancelDownViewTouchListener != null) {
                        mDownView = findChildViewUnder(mCurX, mCurY);
                    }

                    if (mItemLeftMoveViewID != null && mItemRightDeleteViewID != null) {
                        View lItemView = mDownView == null ? findChildViewUnder(mCurX, mCurY) : mDownView;
                        if (lItemView != null) {
                            View lItemLeftMoveView = lItemView.findViewById(mItemLeftMoveViewID);
                            if (mItemLeftMoveView != lItemLeftMoveView) {
                                checkRevertItemMoveDeleteView();
                                mItemLeftMoveView = lItemLeftMoveView; // 初始化
                                mRightDeleteView = lItemView.findViewById(mItemRightDeleteViewID);
                                mItemMoveDeleteInitLTRB = new int[]{mItemLeftMoveView.getLeft(), mItemLeftMoveView.getTop(), mItemLeftMoveView.getRight(), mItemLeftMoveView.getBottom()};
                            }
                        } else {
                            checkRevertItemMoveDeleteView();
                        }
                    } else {
                        checkRevertItemMoveDeleteView();
                    }

                    if (mItemLongClickListener != null) {
                        SingleManager.POST_DELAYED_HANDLER.postDelayed(this, mLongClickTime);
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mOperationState == STATE_NONE) break;

                    if (mOperationState == STATE_LEFT_DELETE) {
                        if (mItemMoveDeleteInitLTRB != null) {
                            if (mInitRawX - mCurRowX > mMaxOffset) {
                                mItemLeftMoveView.layout((int) (mItemMoveDeleteInitLTRB[0] + mCurRowX - mInitRawX), mItemMoveDeleteInitLTRB[1], (int) (mItemMoveDeleteInitLTRB[2] + mCurRowX - mInitRawX), mItemMoveDeleteInitLTRB[3]);
                            } else {
                                mItemLeftMoveView.layout(mItemMoveDeleteInitLTRB[0], mItemMoveDeleteInitLTRB[1], mItemMoveDeleteInitLTRB[2], mItemMoveDeleteInitLTRB[3]);
                            }
                        } else {
                            checkRevertItemMoveDeleteView();
                            mOperationState = STATE_NONE;
                        }
                    }

                    if ((mOperationState == STATE_ACTION || mOperationState == STATE_WAIT_LEFT_DELETE) && mItemMoveDeleteInitLTRB != null && mInitRawX - mCurRowX > mMaxOffset) {// 触发左滑
                        mOperationState = STATE_LEFT_DELETE;
                        SingleManager.POST_DELAYED_HANDLER.removeCallbacks(this);
                        checkCancelTouchView();
                        if (getLayoutManager() instanceof ScrollEnabledGridLayoutManager) {// 禁止上下滚动
                            ((ScrollEnabledGridLayoutManager) getLayoutManager()).setScrollEnabled(false);
                        } else if (getLayoutManager() instanceof ScrollEnabledStaggeredGridLayoutManager) {// 禁止上下滚动
                            ((ScrollEnabledStaggeredGridLayoutManager) getLayoutManager()).setScrollEnabled(false);
                        }
                    }

                    if (mOperationState == STATE_ACTION) {
                        if (Math.abs(mCurRowX - mInitRawX) > mMaxOffset) {// 左右超过一定限度时，如果是允许删除，切换到等待左滑状态
                            mOperationState = mItemMoveDeleteInitLTRB != null ? STATE_WAIT_LEFT_DELETE : STATE_NONE;
                        } else if (Math.abs(mCurRowY - mInitRawY) > mMaxOffset) {
                            mOperationState = STATE_NONE;
                        }
                        if (mOperationState != STATE_ACTION) {
                            SingleManager.POST_DELAYED_HANDLER.removeCallbacks(this);
                            checkCancelTouchView();
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (mOperationState == STATE_ACTION) {
                        mOperationState = STATE_CLICK;
                        SingleManager.POST_DELAYED_HANDLER.removeCallbacks(this);
                        checkCancelTouchView();

                        if (mItemClickListener != null || (mItemChildViewClickListener != null && mClickChildIDList != null)) {
                            View lItemView = findChildViewUnder(mCurX, mCurY);
                            if (lItemView != null) {
                                ViewHolder lViewHolder = getChildViewHolder(lItemView);
                                int lPosition = lViewHolder.getAdapterPosition();
                                if (mItemChildViewClickListener != null && mClickChildIDList != null) {
                                    boolean lSatisfyChildClick = false;
                                    View lChildView = null;
                                    if (mClickChildIndexParent == null || mClickChildIndexParent == lPosition) {
                                        for (Integer lClickChildID : mClickChildIDList) {
                                            if (lClickChildID != null) {
                                                lChildView = lItemView.findViewById(lClickChildID);
                                                if (lChildView != null && lChildView.isShown()) {// 子View必须可见，才可点击
                                                    int[] lLTRB = {lChildView.getLeft(), lChildView.getTop(), lChildView.getRight(), lChildView.getBottom()};
                                                    ViewParent lChildParent = lChildView.getParent();
                                                    while (lChildParent != lItemView && lChildParent instanceof ViewGroup) {// 嵌套布局处理
                                                        lLTRB[0] += ((ViewGroup) lChildParent).getLeft();
                                                        lLTRB[1] += ((ViewGroup) lChildParent).getTop();
                                                        lLTRB[2] += ((ViewGroup) lChildParent).getLeft();
                                                        lLTRB[3] += ((ViewGroup) lChildParent).getTop();
                                                        lChildParent = lChildParent.getParent();
                                                    }
                                                    lSatisfyChildClick = mCurX >= lItemView.getLeft() + lLTRB[0] && mCurX <= lItemView.getLeft() + lLTRB[2] && mCurY >= lItemView.getTop() + lLTRB[1] && mCurY <= lItemView.getTop() + lLTRB[3];
                                                }
                                                if (lSatisfyChildClick) {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (lSatisfyChildClick) {
                                        mItemChildViewClickListener.onItemChildViewClick(this, lViewHolder, lChildView, lPosition, mCurRowX, mCurRowY);
                                    } else if (mItemClickListener != null) {
                                        mItemClickListener.onItemClick(this, lViewHolder, lPosition, mCurRowX, mCurRowY);
                                    }
                                } else {
                                    mItemClickListener.onItemClick(this, lViewHolder, lPosition, mCurRowX, mCurRowY);
                                }
                            }
                        }
                        checkRevertItemMoveDeleteView();
                    } else if (mOperationState == STATE_LEFT_DELETE) {
                        if (mItemMoveDeleteInitLTRB[0] - mItemLeftMoveView.getLeft() > mRightDeleteView.getWidth()) {// 保持展开状态
                            mItemLeftMoveView.layout(mItemMoveDeleteInitLTRB[0] - mRightDeleteView.getWidth(), mItemMoveDeleteInitLTRB[1], mItemMoveDeleteInitLTRB[2] - mRightDeleteView.getWidth(), mItemMoveDeleteInitLTRB[3]);
                        } else {// 还原并清空
                            checkRevertItemMoveDeleteView();
                        }
                    } else { // STATE_WAIT_LEFT_DELETE
                        checkRevertItemMoveDeleteView();
                    }

                    mOperationState = STATE_NONE;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mOperationState = STATE_NONE;
                    SingleManager.POST_DELAYED_HANDLER.removeCallbacks(this);
                    checkRevertItemMoveDeleteView();
                    checkCancelTouchView();
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(e);
    }

    public void scrollTo(int aPosition) {
        scrollTo(this, aPosition);
    }

    public void scrollTo(RecyclerView aRecyclerView, int aPosition) {
        RecyclerView.LayoutManager lLayoutManager = aRecyclerView.getLayoutManager();
        if (lLayoutManager != null) {
            if (mScroller == null) mScroller = new TopSmoothScroller(aRecyclerView.getContext());
            mScroller.setTargetPosition(aPosition);
            lLayoutManager.startSmoothScroll(mScroller);
        }
    }

    private void checkCancelTouchView() {
        if (mOnCancelDownViewTouchListener != null && mDownView != null) {
            mOnCancelDownViewTouchListener.onCancelTouchView(this, mDownView);
            mDownView = null;
        }
    }

    @Override
    public void run() {
        if (mOperationState == STATE_ACTION && mItemLongClickListener != null) {
            mOperationState = STATE_LONG_CLICK;
            View lItemView = findChildViewUnder(mCurX, mCurY);
            if (lItemView != null) {
                ViewHolder lViewHolder = getChildViewHolder(lItemView);
                int lPosition = lViewHolder.getAdapterPosition();
                if (mItemLongClickListener != null) {
                    mItemLongClickListener.onItemLongClick(this, lViewHolder, lPosition, mCurRowX, mCurRowY);
                }
            }
        }
        mOperationState = STATE_NONE;
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
//        RecyclerView.SCROLL_STATE_DRAGGING; //开始滑动
//        RecyclerView.SCROLL_STATE_SETTLING; //滚动到某个位置的动画过程
//        RecyclerView.SCROLL_STATE_IDLE; //停止滑动
        if (mScrollStateChangedListener != null) {
            mScrollStateChangedListener.onScrollStateChanged(this, state);
        }
    }

    /**
     * @return 当前位置是否已经显示到了最底部
     */
    public boolean isScrollBottom() {
        return computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange();
    }

    /**
     * @return 当前位置是否已经显示到了最右端（水平布局使用）
     */
    public boolean isScrollEnd() {
        return computeHorizontalScrollExtent() + computeHorizontalScrollOffset() >= computeHorizontalScrollRange();
    }

    public void setOnItemClickListener(OnItemClickListener aItemClickListener) {
        mItemClickListener = aItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener aItemLongClickListener) {
        mItemLongClickListener = aItemLongClickListener;
    }

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener aItemChildViewClickListener, Integer aClickChildID) {
        List<Integer> lClickChildIDList = new ArrayList<>();
        lClickChildIDList.add(aClickChildID);
        setOnItemChildViewClickListener(aItemChildViewClickListener, lClickChildIDList);
    }

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener aItemChildViewClickListener, List<Integer> aClickChildIDList) {
        setOnItemChildViewClickListener(aItemChildViewClickListener, aClickChildIDList, null);
    }

    /**
     * @param aClickChildIDList 注意子item的点击不是按层级的，而是List集合中id放前面的会先触发点击
     */
    public void setOnItemChildViewClickListener(OnItemChildViewClickListener aItemChildViewClickListener, List<Integer> aClickChildIDList, Integer aClickChildIndexParent) {
        mItemChildViewClickListener = aItemChildViewClickListener;
        mClickChildIDList = aClickChildIDList;
        mClickChildIndexParent = aClickChildIndexParent;
    }

    public void setOnScrollStateChangedListener(OnScrollStateChangedListener aScrollStateChangedListener) {
        mScrollStateChangedListener = aScrollStateChangedListener;
    }

    /**
     * 需要取消按下的Item自身相关的Touch事件时使用，比如，给ItemView设置了按下抬起动画，实际上你是收不到Up或Cancel事件的，此监听接口为解决此问题而存在
     */
    public void setOnCancelDownViewTouchListener(OnCancelDownViewTouchListener aOnCancelDownViewTouchListener) {
        mOnCancelDownViewTouchListener = aOnCancelDownViewTouchListener;
    }

    /**
     * 在 {@link #setItemLeftMoveDeleteView(Integer, Integer)} 的基础上设置点击事件
     */
    public void setItemLeftMoveDeleteViewAndChildListener(OnItemChildViewClickListener aItemChildViewClickListener, Integer aItemLeftMoveViewID, Integer aItemRightDeleteViewID) {
        setItemLeftMoveDeleteView(aItemLeftMoveViewID, aItemRightDeleteViewID);
        List<Integer> lClickChildIDList = new ArrayList<>();
        lClickChildIDList.add(aItemLeftMoveViewID);
        lClickChildIDList.add(aItemRightDeleteViewID);
        setOnItemChildViewClickListener(aItemChildViewClickListener, lClickChildIDList);
    }

    /**
     * 设置左滑删除，需要点击事件请与 {@link #setOnItemChildViewClickListener} 或其重载方法配合使用, 或直接使用 {@link #setItemLeftMoveDeleteViewAndChildListener(OnItemChildViewClickListener, Integer, Integer)}
     * 需要禁止滚动请与 {@link ScrollEnabledGridLayoutManager} 或 {@link ScrollEnabledStaggeredGridLayoutManager} 配合使用
     */
    public void setItemLeftMoveDeleteView(Integer aItemLeftMoveViewID, Integer aItemRightDeleteViewID) {
        mItemLeftMoveViewID = aItemLeftMoveViewID;
        mItemRightDeleteViewID = aItemRightDeleteViewID;
    }

    /**
     * 如果用了左滑刷新功能
     * {@link #setItemLeftMoveDeleteView(Integer, Integer)} 或 {@link #setItemLeftMoveDeleteViewAndChildListener(OnItemChildViewClickListener, Integer, Integer)}
     * 有些情况需要检查还原，如刷新列表前
     */
    public void checkRevertItemMoveDeleteView() {
        if (mItemMoveDeleteInitLTRB != null && mItemLeftMoveView != null) {// 还原
            mItemLeftMoveView.layout(mItemMoveDeleteInitLTRB[0], mItemMoveDeleteInitLTRB[1], mItemMoveDeleteInitLTRB[2], mItemMoveDeleteInitLTRB[3]);
        }
        mItemMoveDeleteInitLTRB = null;
        mItemLeftMoveView = null;
        mRightDeleteView = null;
        if (mItemLeftMoveViewID != null || mItemRightDeleteViewID != null) {
            if (getLayoutManager() instanceof ScrollEnabledGridLayoutManager) {
                ((ScrollEnabledGridLayoutManager) getLayoutManager()).revertScrollEnabled();
            } else if (getLayoutManager() instanceof ScrollEnabledStaggeredGridLayoutManager) {
                ((ScrollEnabledStaggeredGridLayoutManager) getLayoutManager()).revertScrollEnabled();
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView aRecyclerView, RecyclerView.ViewHolder aViewHolder, int aPosition, float aRawX, float aRawY);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView aRecyclerView, RecyclerView.ViewHolder aViewHolder, int aPosition, float aRawX, float aRawY);
    }

    public interface OnItemChildViewClickListener {
        void onItemChildViewClick(RecyclerView aRecyclerView, RecyclerView.ViewHolder aViewHolder, View aChildView, int aPosition, float aRawX, float aRawY);
    }

    public interface OnScrollStateChangedListener {
        void onScrollStateChanged(RecyclerView aRecyclerView, int aState);
    }

    public interface OnCancelDownViewTouchListener {
        void onCancelTouchView(RecyclerView aRecyclerView, View aItemView);
    }

    public void clear() {
        mOperationState = STATE_NONE;
        if (mItemLongClickListener != null) {
            SingleManager.POST_DELAYED_HANDLER.removeCallbacks(this);
        }
        checkRevertItemMoveDeleteView();
        mItemClickListener = null;
        mItemLongClickListener = null;
        mItemChildViewClickListener = null;
        mScrollStateChangedListener = null;
        mOnCancelDownViewTouchListener = null;
        mDownView = null;
        mClickChildIDList = null;
        mClickChildIndexParent = null;
    }
}
