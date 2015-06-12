package com.anjoyo.meituan.utils;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;

import android.os.Environment;


public class FtpUtils {
	
	public static String FTP_DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/MLGY";
	
	static public void ftpconnectDownload(String localName, String ftpPath, FTPDataTransferListener listener) {
		try {
			FTPClient ftp = new FTPClient();
			ftp.connect("112.124.180.101", 21);
			ftp.login("hxw0010825", "PALACEB888");
			ftp.setCharset("utf8");
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File fileDir = Environment.getExternalStorageDirectory();
				// 下载文件
				File downloadDir = new File(fileDir, "MLGY"); // 用于保存文件的文件夹
				if (!downloadDir.exists()) {
					downloadDir.mkdirs();
				}
				File localFile = new File(downloadDir.toString() + "/" + localName);
				if(localFile.exists()) {
					localFile.delete();
				}
				localFile.createNewFile();
				ftp.download(ftpPath, localFile, listener); // 第一个参数是远程服务器上的某文件的相对路径
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void ftpDeleteFile(String file) {
		try {
			FTPClient ftp = new FTPClient();
			ftp.connect("112.124.180.101", 21);
			ftp.login("hxw0010825", "PALACEB888");
			ftp.setCharset("utf8");
			ftp.deleteFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public class MyTransferListener implements FTPDataTransferListener {
//		int i = 1;
//		long startTime = System.currentTimeMillis();
//		long firstTime = startTime;
//		long secondTime = 0L;
//		long firstSize = 0L;
//		long secondSize = 0L;
//		long fileSize = 10485760L;
//		long downSize = 0L;
//		double speed = 0.0F;
//		long percent = 0L;
//
//		// 传输放弃时触发
//		@Override
//		public void aborted() {
//			Log.d("H3c", "file aborted");
//			handler.sendEmptyMessage(2);
//		}
//
//		// 文件传输完成时，触发
//		@Override
//		public void completed() {
//			Log.d("H3c", "file completed");
//			handler.sendEmptyMessage(1);
//		}
//
//		// 传输失败时触发
//		@Override
//		public void failed() {
//			Log.d("H3c", "file failed");
//			handler.sendEmptyMessage(2);
//		}
//
//		// 文件开始上传或下载时触发
//		@Override
//		public void started() {
//			Log.d("H3c", "file start");
//		}
//
//		// 显示已经传输的字节数
//		@Override
//		public void transferred(int arg0) {// 以下用于显示进度的
//		}
//	}

//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			COUNT++;
//			super.handleMessage(msg);
//			if (msg.what == 1) {
//				QueryActivity.dialog.dismiss();
//				if (localFile2 != null) {
//					Intent i = new Intent();
//					i.setClass(QueryActivity.this, ExcelRead.class);
//					Bundle bundle = new Bundle();
//					bundle.putString("name", localFile2.getAbsolutePath());
//					bundle.putString("deleteFile", "//Data/HF/" + "NeiBuCaXun_"
//							+ MainActivity.SIME + "_HF_"
//							+ MainActivity.CHAXUNDAIMA + ".xls");
//					i.putExtras(bundle);
//					startActivity(i);
//				} else {
//					Toast.makeText(QueryActivity.this, "查询失败",
//							Toast.LENGTH_LONG).show();
//				}
//			} else if (msg.what == 2) {
//				if (COUNT != 3) {
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							QueryActivity.ftpconnect();
//						}
//					}).start();
//				} else {
//					QueryActivity.dialog.dismiss();
//					Toast.makeText(QueryActivity.this, "查询失败",
//							Toast.LENGTH_LONG).show();
//					return;
//				}
//			}
//		}
//	};

}
