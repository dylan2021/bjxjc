package com.haocang.bjxjc.ui.equipment.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haocang.bjxjc.R;

/**
 * 创建时间：2019/7/19
 * 编 写 人：ShenC
 * 功能描述：
 */

public class EquPartFragment extends Fragment {
    private View view;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equpart, container, false);
        context = this.getActivity();

        initView();

        initEvent();

        return view;
    }

    private void initView() {

    }

    private void initEvent() {

    }

}
