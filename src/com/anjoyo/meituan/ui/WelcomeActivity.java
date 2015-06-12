package com.anjoyo.meituan.ui;


import java.io.File;

import me.storm.volley.data.RequestManager;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.utils.FtpUtils;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

/** 欢迎动画activity */
public class WelcomeActivity extends BaseActivity {
	/** Called when the activity is first created. */

	public ProgressDialog dialog;
	private int iCount;
	double fileSize = 2.4 * 1000 * 1024;
	protected static final int STOP = 0x10000;
	protected static final int NEXT = 0x10001;
	ProgressBar progressBar;
	Intent intent;
	View v;
	private NetworkImageView mImageView;
	private NetworkImageView mImageView2;
	
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
	public static final String IMAGES_FOLDER = SDCARD_PATH + File.separator + "demo" + File.separator + "images" + File.separator;

	private static final int CONFIG_DOWN_SUC = 100;
	private static final int CONFIG_DOWN_FAIL = 101;
	private static final int APP_DOWN_SUC = 102;
	private static final int APP_DOWN_FAIL = 103;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		dialog = new ProgressDialog(this);
		intent = new Intent(WelcomeActivity.this, MainActivity.class);
		// 系统会为需要启动的activity寻找与当前activity不同的task;
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mImageView = (NetworkImageView)findViewById(R.id.splash_img);
//		mImageView2 = (NetworkImageView)findViewById(R.id.splash_img2);
		ImageLoader imageLoader = RequestManager.getImageLoader();
		mImageView.setImageUrl("http://www.palaceb.com/data/Splash/splash.jpg", imageLoader);
//		mImageView2.setImageUrl("http://www.lorealparis.com.cn/Images/products/Images/G1055101_404.jpg", imageLoader);
//		
		//		startActivity(intent);
//		finish();
		new Thread(new Runnable() {

			@Override
			public void run() {
				FtpUtils.ftpconnectDownload("config.txt", "//Data/config.txt",
						new MyTransferListener());
				FtpUtils.ftpconnectDownload("zhazhi.txt", "//Data/Wenzhang/zhazhi.txt", null);
				FtpUtils.ftpconnectDownload("tongzhi.txt", "//Data/Wenzhang/tongzhi.txt", null);
				FtpUtils.ftpconnectDownload("product.txt", "//Data/Product/product.txt", null);
				FtpUtils.ftpconnectDownload("service.txt", "//Data/Service/service.txt", null);
				FtpUtils.ftpconnectDownload("mendian.txt", "//Data/Mendian/mendian.txt", null);
			}
		}).start();
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		v = inflater.inflate(
				R.layout.selfdef_welcome_progress_dialog_layout, null);
		progressBar = (ProgressBar) v.findViewById(R.id.rectangleProgressBar);
		progressBar.setIndeterminate(false);
		progressBar.setMax((int) (2.4 * 1000 * 1024));
		progressBar.setProgress(0);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setIndeterminate(true);
//		initImageLoader(this);
	}

	@Override
	void init() {
	}
	
//	private void initImageLoader(Context context) {
//		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
//		MemoryCacheAware<String, Bitmap> memoryCache;
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//			memoryCache = new LruMemoryCache(memoryCacheSize);
//		} else {
//			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
//		}
//
//		mNormalImageOptions = new DisplayImageOptions.Builder().bitmapConfig(Config.RGB_565).cacheInMemory(true).cacheOnDisc(true)
//				.resetViewBeforeLoading(true).build();
//
//		// This
//		// This configuration tuning is custom. You can tune every option, you
//		// may tune some of them,
//		// or you can create default configuration by
//		// ImageLoaderConfiguration.createDefault(this);
//		// method.
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(mNormalImageOptions)
//				.denyCacheImageMultipleSizesInMemory().discCache(new UnlimitedDiscCache(new File(IMAGES_FOLDER)))
//				// .discCacheFileNameGenerator(new Md5FileNameGenerator())
//				.memoryCache(memoryCache)
//				// .memoryCacheSize(memoryCacheSize)
//				.tasksProcessingOrder(QueueProcessingType.LIFO).threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(3).build();
//
//		// Initialize ImageLoader with configuration.
//		ImageLoader.getInstance().init(config);
//	}
	
	public void onCreateDialog() {
		new AlertDialog.Builder(WelcomeActivity.this).setTitle("软件更新").setIcon(
			android.R.drawable.ic_dialog_info).setMessage("是否下载更新软件？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface mdialog, int which) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								public void run() {
									dialog.show();
									dialog.setContentView(v);
									dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
									LayoutParams.WRAP_CONTENT);
								}
							});
							
							FtpUtils.ftpconnectDownload("app.apk",
									"//Data/updateApp.apk",
									new AppTransferListener());
							
						}
					}).start();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(3000);
								// 获取应用的上下文，生命周期是整个应用，应用结束才会结束
								getApplicationContext().startActivity(intent);
								finish();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			}).show();
	}

	// 定义一个Handler
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOP:
				progressBar.setVisibility(View.GONE);
				Thread.currentThread().interrupt();
				break;
			case NEXT:
				if (!Thread.currentThread().isInterrupted()) {
					progressBar.setProgress(iCount);
				}
				break;
			case CONFIG_DOWN_SUC:
				String filePath = SocketUtils.QUERY_XLS_FILE_DIR
						+ "/config.txt";
				String json = Utils.ReadTxtFile(filePath);

				if (null != json) {
					String[] array = json.split(",");
					String[] vesionArray = array[0].split(":");
					String[] ipArray = array[1].split(":");
					String version = vesionArray[1];
					String ip = ipArray[1];

					if (null != ip && ip.length() > 0) {
						AppContext.setServerIP(ip);
					} else {
						AppContext.setServerIP("115.28.85.63");
					}

					if (Float.parseFloat(version) > Float.parseFloat(AppContext
							.getVersion())) {
						onCreateDialog();
					} else {
						// 创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(2000);
									// 获取应用的上下文，生命周期是整个应用，应用结束才会结束
									getApplicationContext().startActivity(
											intent);
									finish();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				}
				break;
			case CONFIG_DOWN_FAIL:
				// 创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
							// 获取应用的上下文，生命周期是整个应用，应用结束才会结束
							getApplicationContext().startActivity(intent);
							finish();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
				break;
			case APP_DOWN_FAIL:
				// 创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
							// 获取应用的上下文，生命周期是整个应用，应用结束才会结束
							getApplicationContext().startActivity(intent);
							finish();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
				break;
			case APP_DOWN_SUC:
				File file = new File(SocketUtils.QUERY_XLS_FILE_DIR + "/app.apk");
				if (null != file && file.exists()) {
					Intent apkIntent = new Intent(); 
		            apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            apkIntent.setAction(android.content.Intent.ACTION_VIEW);
		            apkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		            startActivity(apkIntent);
				}
				
                startActivity(intent);
				finish();
				
				break;
			}
		}
	};

	public class MyTransferListener implements FTPDataTransferListener {

		// 传输放弃时触发
		@Override
		public void aborted() {
			mHandler.sendEmptyMessage(CONFIG_DOWN_FAIL);
		}

		// 文件传输完成时，触发
		@Override
		public void completed() {
			mHandler.sendEmptyMessage(CONFIG_DOWN_SUC);
		}

		// 传输失败时触发
		@Override
		public void failed() {
			mHandler.sendEmptyMessage(CONFIG_DOWN_FAIL);
		}

		// 文件开始上传或下载时触发
		@Override
		public void started() {
		}

		// 显示已经传输的字节数
		@Override
		public void transferred(int arg0) {// 以下用于显示进度的
		}
	}

	public class AppTransferListener implements FTPDataTransferListener {
		long downSize = 0L;
		double speed = 0.0F;
		long percent = 0L;

		// 传输放弃时触发
		@Override
		public void aborted() {
			mHandler.sendEmptyMessage(APP_DOWN_FAIL);
		}

		// 文件传输完成时，触发
		@Override
		public void completed() {
			mHandler.sendEmptyMessage(APP_DOWN_SUC);
		}

		// 传输失败时触发
		@Override
		public void failed() {
			mHandler.sendEmptyMessage(APP_DOWN_FAIL);
		}

		// 文件开始上传或下载时触发
		@Override
		public void started() {
		}

		// 显示已经传输的字节数
		@Override
		public void transferred(int arg0) {// 以下用于显示进度的
			iCount += arg0;
			Message msg = new Message();
			msg.what = NEXT;
			mHandler.sendMessage(msg);
		}
	}

}