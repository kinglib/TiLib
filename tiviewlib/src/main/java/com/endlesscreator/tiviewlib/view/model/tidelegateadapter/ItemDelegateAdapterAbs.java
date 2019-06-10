package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.endlesscreator.titoollib.utils.LogUtil;
import com.endlesscreator.tiviewlib.view.TiRecyclerView;
import com.endlesscreator.tiviewlib.view.adapter.tool.TiAdapterListProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供了数据集合及基础操作，如果不需要，可仿照本class实现自己需要的ItemAdapter逻辑
 *
 * @param <VH> 可继承自 {@link FullSpanDelegateViewHolderForChange} 或仿照其实现
 * @param <DI> 数据集合的Item类型
 */
public abstract class ItemDelegateAdapterAbs<VH extends RecyclerView.ViewHolder, DI> extends ItemRecyclerViewDelegateAdapterDefault<VH> {

    private TiAdapterListProxy<DI> proxy;
    // 当使用payload更新时，将通过反射回调item对应方法名的函数
    private List<String> payloads;

    public ItemDelegateAdapterAbs() {
        proxy = new TiAdapterListProxy<>(this);
        payloads = new ArrayList<>();
    }

    public TiAdapterListProxy<DI> proxy() {
        return proxy;
    }


    /**
     * 将要收到回调的payload添加到这里
     * 自动检测重复
     */
    public void addPayload(@NonNull String aPayload) {
        if (TextUtils.isEmpty(aPayload) || payloads.contains(aPayload)) return;
        payloads.add(aPayload);
    }

    public void removePayload(String aPayload) {
        payloads.remove(aPayload);
    }

    public List<String> payloads() {
        return payloads;
    }

    public void setDataList(List<DI> aDataList) {
        setDataList(aDataList, null);
    }

    public void setDataList(List<DI> aDataList, TiRecyclerView aRecyclerView) {
        proxy.removeData();
        proxy.addDataList(aDataList);
        if (aRecyclerView != null) {
            int lItemPosition = delegateAdapter().findAdapterFirstItemPosition(this);
            if (lItemPosition >= 0) aRecyclerView.scrollTo(lItemPosition);
        }
    }

    public void addDataList(List<DI> aDataList) {
        proxy.addDataList(aDataList);
    }

    public void add(DI aData) {
        proxy.addData(aData);
    }

    public void add(int aIndex, DI aData) {
        proxy.addData(aIndex, aData);
    }

    public void change(int aIndex) {
        if (aIndex < 0) return;
        proxy().notifyItemChanged(aIndex);
    }

    public void change(DI aData) {
        change(proxy.index(aData));
    }

    public void change(int aIndex, String aPayload) {
        if (aIndex < 0) return;
        addPayload(aPayload); // 自动添加回调
        proxy().notifyItemChanged(aIndex, aPayload);
    }

    public void change(DI aData, String aPayload) {
        change(proxy.index(aData), aPayload);
    }

    public void remove(int aIndex) {
        proxy.removeData(aIndex);
    }

    public void remove(DI aData) {
        proxy.removeData(aData);
    }

    public void remove() {
        proxy.removeData();
    }

    public DI getData(int position) {
        return proxy.getDataItem(position);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty() && handleFirstPayload(holder, position, payloads.get(0))) return;
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     * 默认的payload处理机制
     */
    protected boolean handleFirstPayload(@NonNull VH holder, int position, Object payload) {
        if (payload instanceof String && payloads.contains(payload)) {
            String lMethodName = (String) payload;
            try {
                Method lMethod = null;
                try {
                    lMethod = getClass().getMethod(lMethodName, holder.getClass(), int.class);
                } catch (Exception e) { // 获取不到方法可能是因为父类参数为泛型
                    Method[] lMethods = getClass().getMethods();
                    Class<?>[] lParameterTypes;

                    for (int i = lMethods.length - 1; i >= 0; i--) {
                        Method lItem = lMethods[i];
                        if (!TextUtils.equals(lMethodName, lItem.getName())) continue;
                        lParameterTypes = lItem.getParameterTypes();
                        if (lParameterTypes.length == 2 && lParameterTypes[1] == int.class) {
                            lMethod = lItem;
                            break;
                        }
                    }
                }
                if (lMethod != null) {
                    lMethod.invoke(this, holder, position);
                    return true;
                }
            } catch (Exception e) {
                LogUtil.e(e);
            }
            // 如果子类都没有重写，将走默认处理
            handleFirstPayload(holder, position);
            return true;
        }
        return false;
    }

    public void handleFirstPayload(VH holder, int position) {
        LogUtil.i("请检查是否实现对应的handlePayload函数");
    }

    @Override
    public int getItemCount() {
        return proxy.getItemCount();
    }

}
