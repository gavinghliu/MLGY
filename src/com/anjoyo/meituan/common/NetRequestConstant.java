package com.anjoyo.meituan.common;

import java.util.Map;

import com.anjoyo.meituan.ui.BaseActivity.HttpRequestType;

import android.content.Context;

public class NetRequestConstant {

	public static Context context;
	public static String requestUrl;
	public static Map<String, Object> map;

	private HttpRequestType type;

	public HttpRequestType getType() {
		return type;
	}

	public void setType(HttpRequestType type) {
		this.type = type;
	}
	
	public static void setMap(Map map){
		NetRequestConstant.map = map;
	}

	

}
