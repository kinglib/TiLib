package com.endlesscreator.tiviewlib.view.model.tiexpandabletextview.model;

public class ExpandableTool {

    /**
     * 生成自定义规则的文字
     *
     * @param aContent 要显示的内容
     * @param aOperate 点击内容的回调
     */
    public static String makeSelfLink(String aContent, String aOperate) {
        return "[" + aContent + "](" + aOperate + ")";
    }
}
