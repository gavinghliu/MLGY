package com.anjoyo.meituan.app;

import java.util.List;

import me.storm.volley.data.RequestManager;

import com.anjoyo.meituan.domain.AllCategories;
import com.anjoyo.meituan.domain.City;
import com.anjoyo.meituan.domain.Good;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.domain.Seller;
import com.anjoyo.meituan.domain.SpecificCategories;
import com.anjoyo.meituan.domain.User;
import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;

public class AppContext extends Application {

	private List<City> cities;
	private List<Good> goods;
	private List<Product> seller;
	private List<SpecificCategories> specificCategories;
	private List<AllCategories> allCategories;
	private String city;
	private SharedPreferences preferences;
	private User user;
	private int screenWidth;
	private int screenHeight;

	public String SIME;
	public String phoneNumber;
	public String phoneMode;
	
	public static String serverIP;
	public static String version = "2.0";
	
	public static String getServerIP() {
		return serverIP;
	}

	public static void setServerIP(String serverIP) {
		AppContext.serverIP = serverIP;
	}

	public static String getVersion() {
		return version;
	}

	public List<SpecificCategories> getSpecificCategories() {
		return specificCategories;
	}

	public void setSpecificCategories(
			List<SpecificCategories> specificCategories) {
		this.specificCategories = specificCategories;
	}

	public List<AllCategories> getAllCategories() {
		return allCategories;
	}

	public void setAllCategories(List<AllCategories> allCategories) {
		this.allCategories = allCategories;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public List<City> getCities() {
		return cities;
	}

	public List<Good> getGoods() {
		return goods;
	}

	public void setGoods(List<Good> goods) {
		this.goods = goods;
	}

	public List<Product> getSeller() {
		return seller;
	}

	public void setProduct(List<Product> seller) {
		this.seller = seller;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		SIME = phoneMgr.getDeviceId();
		phoneNumber = phoneMgr.getLine1Number();
		if (phoneNumber == null || !(phoneNumber.length() > 0)) {
			phoneNumber = "@";
		}
		phoneMode = android.os.Build.MODEL;
		SDKInitializer.initialize(this);
		 RequestManager.init(this);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public SharedPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(SharedPreferences preferences) {
		this.preferences = preferences;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		Editor editor = preferences.edit();
		editor.putString("username", user.getUsername());
		editor.putString("password", user.getPassword());
		editor.commit();

	}

	
	public String getSIME() {
		return SIME;
	}

	public void setSIME(String sIME) {
		SIME = sIME;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneMode() {
		return phoneMode;
	}

	public void setPhoneMode(String phoneMode) {
		this.phoneMode = phoneMode;
	}
}
