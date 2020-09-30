package com.haocang.bjxjc.ui.equipment.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;

/**
 * 创建时间：2019/7/19
 * 编 写 人：ShenC
 * 功能描述：
 */

public class EquInfoFragment extends Fragment {
    private View view;
    private Context context;


    private TextView tv_Spec, tv_Models, tv_Brand, tv_ED, tv_Position, tv_Supplier, tv_Manufacturer, tv_Guarantee, tv_ENo, tv_InstalDatel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equinfo, container, false);
        context = this.getActivity();

        initView();

        initData();

        return view;
    }

    private void initView() {

        tv_Spec = view.findViewById(R.id.tv_Spec);
        tv_Models = view.findViewById(R.id.tv_Models);
        tv_Brand = view.findViewById(R.id.tv_Brand);
        tv_ED = view.findViewById(R.id.tv_ED);
        tv_Position = view.findViewById(R.id.tv_Position);
        tv_Supplier = view.findViewById(R.id.tv_Supplier);
        tv_Manufacturer = view.findViewById(R.id.tv_Manufacturer);
        tv_Guarantee = view.findViewById(R.id.tv_Guarantee);
        tv_ENo = view.findViewById(R.id.tv_ENo);
        tv_InstalDatel = view.findViewById(R.id.tv_InstalDatel);

    }

    private void initData() {
        EquipmentBean model = CacheData.mEquipmentBean;
        tv_Spec .setText(model.getSpec());
        tv_Models.setText(model.getModels());
        tv_Brand .setText(model.getBrand());
        tv_ED .setText(MyTextUtils.toYMDHM(model.getED()));
        tv_Position.setText(model.getPosition());
        tv_Supplier .setText(model.getSupplier());
        tv_Manufacturer.setText(model.getManufacturer());
        tv_Guarantee .setText(model.getGuarantee());
        tv_ENo .setText(model.getENo());
        tv_InstalDatel .setText(MyTextUtils.toYMDHM(model.getInstalDatel()));
    }


}
