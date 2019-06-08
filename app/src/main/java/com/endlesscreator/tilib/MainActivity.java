package com.endlesscreator.tilib;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.endlesscreator.tibaselib.frame.TApp;
import com.endlesscreator.titoollib.utils.InfoUtil;
import com.endlesscreator.titoollib.utils.LogUtil;
import com.endlesscreator.tiviewlib.view.TiExpandableTextView;
import com.endlesscreator.tiviewlib.view.model.tiexpandabletextview.app.LinkType;
import com.endlesscreator.tiviewlib.view.model.tiexpandabletextview.model.ExpandableTool;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


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
        }
    }


}
