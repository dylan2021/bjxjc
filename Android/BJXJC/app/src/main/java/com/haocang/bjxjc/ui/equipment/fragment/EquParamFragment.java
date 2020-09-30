package com.haocang.bjxjc.ui.equipment.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.equipment.bean.EquParameterBean;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.commonlib.network.interfaces.InitDataOneBeanBack;

/**
 * 创建时间：2019/7/19
 * 编 写 人：ShenC
 * 功能描述：
 */

public class EquParamFragment extends Fragment {
    private View view;
    private Context context;

    private TextView tv_P1,tv_P2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equparam, container, false);
        context = this.getActivity();

        initView();

        initData();

        return view;
    }

    private void initView() {
        tv_P1 = view.findViewById(R.id.tv_P1);
        tv_P2 = view.findViewById(R.id.tv_P2);
    }

    private void initData() {

        EquipmentBean model = CacheData.mEquipmentBean;

        String ApiUrl = ApiConstant.host+ UrlConst.Api_GetEquParameter+model.getEID();
        DataModel.requestGETOneMode("EquParameterBean", ApiUrl, new InitDataOneBeanBack<EquParameterBean>() {
            @Override
            public void callbak(boolean b, EquParameterBean data, String msg) {

                if(b){
                    tv_P1.setText(data.getP1());
                    tv_P2.setText(data.getP2());
                }

            }
        });


    }

}
