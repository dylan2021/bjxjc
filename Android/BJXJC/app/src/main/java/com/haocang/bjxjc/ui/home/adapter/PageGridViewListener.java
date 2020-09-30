package com.haocang.bjxjc.ui.home.adapter;

import com.haocang.commonlib.pagegrid.PageGridView;
import com.haocang.bjxjc.ui.home.bean.HomeMenuBean;

/**
 * 创建时间：2019/3/20
 * 编 写 人：ShenC
 * 功能描述：
 */

public interface PageGridViewListener {
    void onItemClickChanged(PageGridView pageGridView, int position, HomeMenuBean bean);
}
