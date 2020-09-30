package com.haocang.commonlib.network.interfaces;

import java.util.List;

public interface InitDataBeanBack<T> {
	void callbak(boolean isSuccess, List<T> list, String msg);
}
