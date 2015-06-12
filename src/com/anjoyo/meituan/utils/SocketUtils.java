package com.anjoyo.meituan.utils;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Random;

import com.anjoyo.meituan.app.AppContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

public class SocketUtils {

	// 传给Socket服务器端的上传和下载标志
	public static int SERVICE_PORT = 4455;
	public static String MINGLING_GENFILE = "908462";
	public static String MINGLING_DOWNLOAD = "908431";
	public static String QUERY_XLS_FILE_DIR = Environment
			.getExternalStorageDirectory() + "/MLGY";
	public static String QUERY_XLS_FILE_PATH = QUERY_XLS_FILE_DIR + "/test.xls";

	public Context mContext;

	public SocketUtils(Context ct) {
		this.mContext = ct;
	}

	private String getQueryPortString() {
		AppContext appContext = (AppContext) mContext.getApplicationContext();

		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month + 1;
		int day = t.monthDay;

		String monthString = (month > 9 ? "" : "0") + month;
		String dayString = (day < 10 ? "0" : "") + day;
		Random random = new Random();

		String randomNumber = Math.abs(random.nextInt()) % 10 + "";

		// ***mlgy**yyyy*mm**dd**app 一共长25位字符，*是随机字符或数字，yyyymmdd当日的年 月日
		String queryString = randomNumber + randomNumber + randomNumber
				+ "mlgy" + randomNumber + randomNumber + year + randomNumber
				+ monthString + randomNumber + randomNumber + dayString
				+ randomNumber + randomNumber + "app" + appContext.getSIME();

		return queryString;
	}

	public void query(final String requestString, final SocketListener listener) {
		new Thread() {
			public void run() {
				DataOutputStream socketOut = null;
				DataInputStream inPutStream = null;
				Socket socket = null;
				byte[] buf = null;
				try {
					// 连接Socket
					socket = new Socket(AppContext.getServerIP(), SERVICE_PORT);
					// 向服务端发送请求及数据
					socketOut = new DataOutputStream(socket.getOutputStream());
					byte[] responseBuffer = getQueryPortString().getBytes(
							"GB2312");
					socketOut.write(responseBuffer, 0, responseBuffer.length);

					// 1. 读取Socket的输入流
					inPutStream = new DataInputStream(socket.getInputStream());
					int bufferSize = 1024;
					buf = new byte[bufferSize];
					int b;
					// 顺序读取文件text里的内容并赋值给整型变量b,直到文件结束为止。
					StringBuffer sb = new StringBuffer();
					/* 开始循环读取PC端发送过来的数据 */
					int readCount = inPutStream.read(buf);
					System.out.println("readCount=" + readCount);
					for (int i = 0; i < readCount; i++) {
						b = buf[i];
						sb.append((char) b);
					}
					int downPort = Integer.parseInt(sb.toString());

					String firstRequestString = MINGLING_GENFILE + requestString;
					if (requestString.length() > 0)
						sendQueryFirst(AppContext.getServerIP(), downPort, firstRequestString, listener);
					Thread.sleep(3000);

					String secondRequestString = MINGLING_DOWNLOAD + requestString;
					sendQuerySecond(AppContext.getServerIP(), downPort, secondRequestString, listener);
				} catch (Exception e) {
					e.printStackTrace();
					listener.downLoadFail();
				} finally {
					try {
						buf = null;
						inPutStream.close();
						socket.close();
					} catch (Exception e) {
						listener.downLoadFail();
					}
				}
			};
		}.start();
	}

	private void sendQueryFirst(final String ip, final int port,
			final String requestString, final SocketListener listener) {
		if (requestString.length() <= 0)
			return;
		new Thread() {
			public void run() {
				DataOutputStream socketOut = null;
				Socket socket = null;
				try {
					// 连接Socket
					socket = new Socket(ip, port);
					// 向服务端发送请求及数据
					socketOut = new DataOutputStream(socket.getOutputStream());
					byte[] responseBuffer = requestString.getBytes("GB2312");
					Log.d("test", "sendQueryFirst__" + requestString);
					socketOut.write(responseBuffer, 0, responseBuffer.length);
				} catch (Exception e) {
					listener.downLoadFail();
					return;
				} finally {
					try {
						socket.close();
					} catch (Exception e) {
						listener.downLoadFail();
					}
				}
			};
		}.start();
	}

	private void sendQuerySecond(final String ip, final int port, final String requestString, final SocketListener listener) {
		if (requestString.length() <= 0)
			return;
		new Thread() {
			public void run() {
				Log.i("test", "sendQuerySecond__" + requestString);
				DataOutputStream socketOut = null;
				DataInputStream inPutStream = null;
				Socket socket = null;
				byte[] buf = null;
				try {
					// 连接Socket
					socket = new Socket(ip, port);
					// 向服务端发送请求及数据
					socketOut = new DataOutputStream(socket.getOutputStream());
					byte[] responseBuffer = requestString.getBytes("GB2312");
					socketOut.write(responseBuffer, 0, responseBuffer.length);

					// 本地保存路径，文件名会自动从服务器端继承而来。
					int bufferSize = 1024;
					buf = new byte[bufferSize];
					// 1. 读取Socket的输入流
					inPutStream = new DataInputStream(socket.getInputStream());
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED))
						return;
					
					// 下载文件
					File downloadDir = new File(QUERY_XLS_FILE_DIR); // 用于保存文件的文件夹
					if (!downloadDir.exists()) {
						downloadDir.mkdirs();
					}
					File queryFile = new File(QUERY_XLS_FILE_PATH);
					if (queryFile.exists()) {
						queryFile.delete();
					}
					queryFile.createNewFile();

					DataOutputStream fileOut = new DataOutputStream(
							new BufferedOutputStream(new BufferedOutputStream(
									new FileOutputStream(queryFile))));
					/* 开始循环读取PC端发送过来的数据 */
					while (true) {
						int readCount = inPutStream.read(buf);
						/* 由于Java的Socket是阻塞式的,所以最后需要发送确认信息让循环退出 */
						if (new String(buf, 0, readCount).equals("End!"))
							break;
						fileOut.write(buf, 0, readCount);
						fileOut.flush();
					}
					listener.downLoadSuccess();
					fileOut.close();
				} catch (Exception e) {
					listener.downLoadFail();
					return;
				} finally {
					try {
						// 善后处理
						buf = null;
						inPutStream.close();
						socket.close();
					} catch (Exception e) {
						listener.downLoadFail();
					}
				}
			};
		}.start();
	}
	
	public interface SocketListener {
		public void downLoadSuccess();
		public void downLoadFail();
	}

}
