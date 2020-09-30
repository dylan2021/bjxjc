package com.haocang.bjxjc.ui.waterpoint.utils;

import android.widget.TextView;

import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.TextUtil;

import java.util.List;

/**
 * 创建时间：2019/5/22
 * 编 写 人：ShenC
 * 功能描述：积水计算类
 */
public class ComputeTools {
    /**
     * ===============================================
     * 创建时间：2019/5/22 15:55
     * 编 写 人：ShenC
     * 方法说明：根据深度和积水面积计算积水等级
     * ================================================
     * 修改内容：      修改时间：      修改人：
     * ===============================================
     */
    public static void ComputeWaterLevel(TextView tv, int Depth, int Area) {


        if (Depth <= 15) {
            tv.setText("轻度积水");
        } else if (Depth > 15 && Depth <= 30) {
            if (Area <= 300) {
                tv.setText("轻度积水");
            } else if (Area > 300) {
                tv.setText("中度积水");
            }
        } else if (Depth > 30) {
            if (Area <= 2000) {
                tv.setText("中度积水");
            } else if (Area > 2000) {
                tv.setText("严重积水");
            }
        }
    }


    /**
     * ===============================================
     * 创建时间：2019/5/22 16:05
     * 编 写 人：ShenC
     * 方法说明：统计处置状态和积水等级
     * ================================================
     * 修改内容：      修改时间：      修改人：
     * ===============================================
     */
    public static String[] StatisticsWaterNumber(List<WaterPointBean> list) {
        if (TextUtil.isEmptyList(list)) {
            return new String[]{"0", "0", "0", "0", "0"};
        }
        int a = 0, b = 0, c = 0, d = 0, e = 0;
        for (WaterPointBean model : list) {
            if (model.getIsDelete().equals("S")) {
                switch (model.getStatus()) {
                    case "0":
                        a++;
                        break;
                    case "1":
                        b++;
                        break;
                    case "2":
                        c++;
                        break;
                }
            } else if (model.getIsDelete().equals("N")) {
                d++;
            } else if (model.getIsDelete().equals("Y")) {
                e++;
            }

        }
        return new String[]{a + "", b + "", c + "", d + "", e + ""};
    }


}
