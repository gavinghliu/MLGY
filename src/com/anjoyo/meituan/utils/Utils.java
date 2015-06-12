package com.anjoyo.meituan.utils;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {

	public static boolean writeFile(String str, String filePath) {

		FileOutputStream out = null;
		File wfile = new File(filePath);

		boolean ret = true;
		if (!wfile.exists()) {
			try {
				wfile.createNewFile();
			} catch (IOException e) {
				ret = false;
			}
		}
		try {
			out = new FileOutputStream(wfile, false);
		} catch (FileNotFoundException e) {
			ret = false;
		}
		try {
			str = str + "\r\n";
			if (out != null)
				out.write(str.getBytes());
		} catch (IOException e) {
			ret = false;
		}
		try {
			if (out != null)
				out.flush();
		} catch (IOException e) {
			ret = false;
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			ret = false;
		}
		return ret;
	}

	public static boolean writeFileForPandian(String str, String filePath) {

		FileOutputStream out = null;
		File wfile = new File(filePath);

		boolean ret = true;
		if (!wfile.exists()) {
			try {
				wfile.createNewFile();
			} catch (IOException e) {
				ret = false;
			}
		}
		try {
			out = new FileOutputStream(wfile, true);
		} catch (FileNotFoundException e) {
			ret = false;
		}
		try {
			str = str + "\r\n";
			if (out != null)
				out.write(str.getBytes());
		} catch (IOException e) {
			ret = false;
		}
		try {
			if (out != null)
				out.flush();
		} catch (IOException e) {
			ret = false;
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			ret = false;
		}
		return ret;
	}

	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getStringToday2() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	public static String getStringTodayFormat(String format) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	
	//读取文本文件中的内容
    public static String ReadTxtFile(String strFilePath)
     {
	  String path = strFilePath;
      String content = ""; //文件内容字符串
         //打开文件
         File file = new File(path);
         if(null == file ||  !file.exists()) return null;
          //如果path是传递过来的参数，可以做一个非目录的判断
              try {
                  InputStream instream = new FileInputStream(file); 
                  if (instream != null) 
                  {
                      InputStreamReader inputreader = new InputStreamReader(instream);
                      BufferedReader buffreader = new BufferedReader(inputreader);
                      String line;
                      //分行读取
                     while (( line = buffreader.readLine()) != null) {
                          content += line + "\n";
                      }                
                      instream.close();
                  }
              }
              catch (java.io.FileNotFoundException e) 
              {
                  Log.d("TestFile", "The File doesn't not exist.");
              } 
              catch (IOException e) 
              {
                   Log.d("TestFile", e.getMessage());
              }
          return content;
  }
    
    public static int getConnectedType(Context context) {  
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                    .getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {  
                return mNetworkInfo.getType();  
            }  
        }  
        return -1;  
    }
}
