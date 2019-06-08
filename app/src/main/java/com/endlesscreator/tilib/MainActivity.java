package com.endlesscreator.tilib;

import android.view.View;

import com.endlesscreator.tilib.base.BaseActivity;
import com.endlesscreator.tilib.module.etv.TestETVActivity;
import com.endlesscreator.tilib.module.round.TestRoundActivity;
import com.endlesscreator.tilib.module.vp.TestRecyclerViewActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected int makeLayout() {
        return R.layout.activity_main;
    }

    public void click(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.main_test_etv:
                toAct(TestETVActivity.class);
                return;
            case R.id.main_test_round:
                toAct(TestRoundActivity.class);
                return;
            case R.id.main_test_recycler:
                toAct(TestRecyclerViewActivity.class);
                return;
        }
    }


}
