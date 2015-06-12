package com.jsk.imgtxt;

import java.util.List;

import me.storm.volley.data.RequestManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.anjoyo.meituan.ui.ProductDetailActivity;
import com.anjoyo.meituan.ui.SpaceImageDetailActivity;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImgTxtAdapter extends BaseAdapter implements OnClickListener {

	private Context context;
	private LayoutInflater inf;
	private List<Content> list;
	private String questionAnswer;
	private EditText eText;
	ImageLoader imageLoader = RequestManager.getImageLoader();

	public ImgTxtAdapter(Context context, List<Content> list) {
		this.context = context;
		this.list = list;

		inf = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyView mv;
		if (null == convertView) {
			mv = new MyView();
			convertView = inf.inflate(R.layout.img_txt_item, null);

			mv.img = (NetworkImageView) convertView.findViewById(R.id.img);
			mv.txt = (TextView) convertView.findViewById(R.id.txt);
			mv.title = (TextView) convertView.findViewById(R.id.title);
			mv.video = (ImageView) convertView.findViewById(R.id.video);
			mv.yangzhengLayout = convertView
					.findViewById(R.id.layout_yangzheng);
			mv.question = (TextView) convertView.findViewById(R.id.question);
			mv.commit = (Button) convertView.findViewById(R.id.commit);
			mv.answer = (EditText) convertView.findViewById(R.id.answer);
			convertView.setTag(mv);

		} else {
			mv = (MyView) convertView.getTag();
		}

		mv.img.setVisibility(View.GONE);
		mv.txt.setVisibility(View.GONE);
		mv.video.setVisibility(View.GONE);
		mv.title.setVisibility(View.GONE);
		mv.yangzhengLayout.setVisibility(View.GONE);

		final Content content = list.get(position);
		if (content.isTitle()) {
			mv.title.setText(content.getDetails());
			mv.title.setVisibility(View.VISIBLE);

		} else if (content.isShiping()) {
			// mv.video.setText(content.getDetails());
			mv.video.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (1 == Utils.getConnectedType(context)) {
						Uri uri = Uri.parse(content.getDetails());
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						((Activity) context).startActivity(it);
					} else {
						AlertDialog.Builder builder = new Builder(context);
						builder.setMessage("当前不是wifi环境,是否继续？");
						builder.setTitle("提示");
						builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Uri uri = Uri.parse(content.getDetails());
								Intent it = new Intent(Intent.ACTION_VIEW, uri);
								((Activity) context).startActivity(it);
								dialog.dismiss();
							}
						});
						builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.create().show();
					}
				}
			});
			mv.video.setVisibility(View.VISIBLE);
		} else if (content.isYangzheng()) {
			mv.yangzhengLayout.setVisibility(View.VISIBLE);
			mv.question.setText(content.getDetails());
			mv.commit.setOnClickListener(this);
			questionAnswer = content.getAnswer();
			eText = mv.answer;
		} else {
			if (content.isImg()) {
				mv.img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(context,
								SpaceImageDetailActivity.class);
						intent.putExtra("images",
								content.getDetails());
						int[] location = new int[2];
						location[0] = 0;
						location[1] = 0;
//						mv.img.getLocationOnScreen(location);
						intent.putExtra("locationX", location[0]);
						intent.putExtra("locationY", location[1]);
						intent.putExtra("width", 30);
						intent.putExtra("height", 30);
						context.startActivity(intent);
//						((Activity)content).overridePendingTransition(0, 0);
//						break;
					}
				});
				mv.img.setImageUrl(content.getDetails(), imageLoader);
				mv.img.setVisibility(View.VISIBLE);
			} else {
				mv.txt.setText(content.getDetails());
				mv.txt.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	class MyView {
		NetworkImageView img;
		TextView txt;
		TextView title;
		ImageView video;
		View yangzhengLayout;
		Button commit;
		TextView question;
		EditText answer;
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.commit:
			String answer = null;
			if (null != eText.getText()) {
				answer = eText.getText().toString();
			}
			if (answer != null && answer.length() > 0) {
				if (answer.trim().equals(questionAnswer)) {
					Toast.makeText(context, "谢谢参与,回答正确~~", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "回答错误,请重新回答~~", Toast.LENGTH_SHORT).show();
				}
				
				
				eText.setText("");
			} else {
				Toast.makeText(context, "答案不能为空~~", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

	}
}
