package com.jsk.imgtxt;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hslf.model.TitleMaster;

import com.anjoyo.meituan.Parser.ProductParser;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.domain.Wenzhang;
import com.anjoyo.meituan.ui.GroupBuyActivity;
import com.anjoyo.meituan.ui.MerchantActivity;
import com.anjoyo.meituan.ui.WelcomeActivity.MyTransferListener;
import com.anjoyo.meituan.utils.FtpUtils;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class WenzhangDetailActivity extends Activity implements OnClickListener {

	private static final int CONFIG_DOWN_SUC = 100;
	private static final int CONFIG_DOWN_FAIL = 101;

	private String jsonString;
	List<Content> list;
	private ListView img_txt_list;
	ImgTxtAdapter adapter;
	private ImageView imageview_back;
	private TextView imageview_back2;
	private Wenzhang datas;
	private ImageView detail_share;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		datas = new Wenzhang();
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);// 获得IWXAPI实例
		String fromMerchant = getIntent().getAction();
		int allPosition = getIntent().getIntExtra("allPosition", 0);
		datas = GroupBuyActivity.mList.get(allPosition);

		String filePath = SocketUtils.QUERY_XLS_FILE_DIR + "/wenzhang"
				+ datas.getwenzhang_id() + ".txt";
		File file = new File(filePath);
		if (null != file && file.exists() && file.length() > 0) {
			jsonString = Utils.ReadTxtFile(filePath);
			getAssetsData();
			img_txt_list = (ListView) findViewById(R.id.img_txt_list);
			adapter = new ImgTxtAdapter(this, list);
			img_txt_list.setAdapter(adapter);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					FtpUtils.ftpconnectDownload(
							"wenzhang" + datas.getwenzhang_id() + ".txt",
							"//Data" + "/Wenzhang/wenzhang"
									+ datas.getwenzhang_id() + ".txt",
							new MyTransferListener());
				}
			}).start();
		}
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back2 = (TextView) findViewById(R.id.imageview_back2);
		detail_share = (ImageView) findViewById(R.id.detail_share);
		detail_share.setOnClickListener(this);
		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);
	}

	public List<Content> getAssetsData() {
		list = AnalysisJSON.getProvinceCities(jsonString);
		Content titleContent = new Content(datas.getwenzhang_title(), false);
		titleContent.setTitle(true);
		list.add(0, titleContent);

		if (null != datas.getVideo() && datas.getVideo().length() > 0) {
			titleContent = new Content(datas.getVideo(), false);
			titleContent.setShiping(true);
			list.add(list.size(), titleContent);
		}

		if (null != datas.getYangzheng() && datas.getYangzheng().length() > 0) {
			titleContent = new Content("验证问题:" + datas.getYangzheng() + " ",
					false);
			titleContent.setYangzheng(true);
			titleContent.setAnswer(datas.getAnswer());
			list.add(list.size(), titleContent);
		}

		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return list;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();
			break;
		case R.id.detail_share:
			// myShare = new MyShare(this);
			// myShare.share("商家分享");
			shareToWechat();
			break;
		default:
			break;
		}
	}

	// *************************************************************************
	// share to wechat Method
	// *************************************************************************

	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001; // 支持分享到朋友圈的微信版本
	public static final String APP_ID = "wxf7f986ad87da0b2f";
	private static final int THUMB_SIZE = 150;

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	private void shareToWechat() {
		api.registerApp(APP_ID);

		if (!api.isWXAppInstalled()) {
			Toast.makeText(this, "没有安装微信~", Toast.LENGTH_LONG).show();
			return;
		}

		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
			Toast.makeText(this, "微信版本太旧不支持分享~", Toast.LENGTH_LONG).show();
		}

		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.test);
		WXImageObject imgObj = new WXImageObject(bmp);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
				THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = bmpToByteArray(thumbBmp, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

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

	// 定义一个Handler
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CONFIG_DOWN_SUC:
				String filePath = SocketUtils.QUERY_XLS_FILE_DIR + "/wenzhang"
						+ datas.getwenzhang_id() + ".txt";
				jsonString = Utils.ReadTxtFile(filePath);
				getAssetsData();
				img_txt_list = (ListView) findViewById(R.id.img_txt_list);
				adapter = new ImgTxtAdapter(WenzhangDetailActivity.this, list);
				img_txt_list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;

			case CONFIG_DOWN_FAIL:
				break;
			}
		}
	};
}
