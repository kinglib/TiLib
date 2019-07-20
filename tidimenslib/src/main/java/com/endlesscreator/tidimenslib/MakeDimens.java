package com.endlesscreator.tidimenslib;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;


/**
 * Created by Tye on 2018/6/7.
 * 生成dimens适配文件
 */


public class MakeDimens {

    /*
        当前文件为标准1比1： 即使用sw375_375pt可填充屏幕宽度（此处宽度为最小边，由于各手机宽高比不一定相同，所以参考宽度标准，高度按设计比例适配）

        Dimens文件适配参考：
        values-AxB 文件夹中可适配宽为B，高大于A分辨率的手机.
        如：
            values-375x375 文件夹中可适配宽为375，高大于375(如667)分辨率的手机.

        文件查找规则：
        分辨率为 CxD 的手机，匹配values-AxB文件夹按照A<=C, B<=D规则， 往低分辨率查找最近的文件夹。
        如：
            仅有 values，values-320x320，values-375x375，values-410x410 几个文件夹的情况：
            屏幕为1920x1080的手机 ，会选择values-410x410
            屏幕为667x375的手机 ，会选择values-375x375
            屏幕为480x320的手机 ，会选择values-320x320
            屏幕为320x240的手机 ，会选择values

        如果想要文字不跟随系统字号缩放，那字号与文字高度比例应设为： 278 : 375 （3 : 4）
        如：文字给的设计高度为375pt, 那高度为wrap_content的文字对应字号应为278pt
        注意：不同字体会有所差异，上面说的比例是按照Android标准测试出来的比例
        不同字体大小使用规则不一样，实际应测试一下


        查看手机适配到了哪个Dimens目录：(查看对应目录下的sw和sh原始值)
        float dimensPx = getResources().getDimension(R.dimen.sw);

        注： 预览时走默认values文件夹，此文件夹可替换为预览对应分辨率，方便看到具体效果
     */
    private static final double REFERENCE = 375;
//    private static final double OUT_W = 2160;

    public static void make(double OUT_W, boolean useDP) throws Exception {

        String lParentFileName = "values" + (OUT_W <= 0 ? "" : "-" + (int) OUT_W + "x" + (int) OUT_W);
        if (OUT_W <= 0) useDP = true;
        String lUnit = useDP ? "dp" : "px";

        File lParentFile = new File("/Users/tyewang/Documents/AsWork/projects/third/TiLib/tidimenslib/src/main/res/" + lParentFileName);
        if (!lParentFile.exists()) lParentFile.mkdirs();
        File lLogFile = new File(lParentFile, "dimens.xml");
        if (!lLogFile.exists()) lLogFile.createNewFile();

        FileWriter lFileWriter = new FileWriter(lLogFile, false);
        BufferedWriter lBufferedWriter = new BufferedWriter(lFileWriter);

        lBufferedWriter.write("<resources>\n");
        lBufferedWriter.write("    <!-- 详细规则可见：values-" + (int) REFERENCE + "x" + (int) REFERENCE + " 文件夹 -->");
        lBufferedWriter.write("    <dimen name=\"sw\">" + (int) OUT_W + "px</dimen>\n");

        if (useDP) OUT_W = 375;// 按375dp计算
        for (int i = -64; i <= 1025; i++) {
            float result = retentionAccuracy((i * OUT_W / REFERENCE), 1, BigDecimal.ROUND_DOWN);
            if (i < 0)
                lBufferedWriter.write("    <dimen name=\"swf" + Math.abs(i) + "\">" + result + lUnit + "</dimen>");
//                lBufferedWriter.write("    <dimen name=\"sw" + (int) REFERENCE + "_f" + Math.abs(i) + "\">" + result + lUnit + "</dimen>");
            else
                lBufferedWriter.write("    <dimen name=\"sw" + i + "\">" + result + lUnit + "</dimen>");
//                lBufferedWriter.write("    <dimen name=\"sw" + (int) REFERENCE + "_" + i + "\">" + result + lUnit + "</dimen>");
        }

        lBufferedWriter.write("\n</resources>");
        lBufferedWriter.flush();
        lBufferedWriter.close();
    }

    /**
     * @param aSource       要进行处理的值
     * @param aAccuracy     要保留几位小数
     * @param aRoundingMode BigDecimal.ROUND_DOWN 为向下(接近零)取，BigDecimal.ROUND_UP 为向上(远离零)取，BigDecimal.ROUND_HALF_UP 为四舍五入
     * @return
     */
    public static float retentionAccuracy(double aSource, int aAccuracy, int aRoundingMode) {
        if (aSource == 0) {
            return 0;
        }
        try {
            BigDecimal lBigDecimal = new BigDecimal(aSource);
            return lBigDecimal.setScale(aAccuracy, aRoundingMode).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            float lAddFloat;
            switch (aRoundingMode) {
                case BigDecimal.ROUND_DOWN:
                    lAddFloat = 0;
                    break;
                case BigDecimal.ROUND_UP:
                    lAddFloat = 1;
                    break;
                default:
                    lAddFloat = 0.5f;
                    break;
            }
            double lBaseNum = Math.pow(10, aAccuracy);
            return ((long) (aSource * lBaseNum + lAddFloat)) / (float) lBaseNum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (float) aSource;
    }

    public static void main(String[] args) {
        double[] OUT_W_ARR = {
                0
                , 240 , 320, 360, 375, 410, 480, 540, 600, 640, 720, 750,
                768, 800, 1080, 1200, 1440, 1536, 1600, 1800, 1920, 2000, 2160,
                2161
        };

        for (int i = 0, j =OUT_W_ARR.length; i < j; i++) {
            try {
                make(OUT_W_ARR[i], i == j-1); // 最后一个也是用DP
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
