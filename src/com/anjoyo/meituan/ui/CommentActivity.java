package com.anjoyo.meituan.ui;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Comment;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CommentActivity extends BaseActivity implements OnClickListener {

	ImageView mStart1;
	ImageView mStart2;
	ImageView mStart3;
	ImageView mStart4;
	ImageView mStart5;
	EditText mCommentTv;
	Button mCommentBtn;
	View mBackLayout;
	int selectRank;
	int productId;
	int sellerId;

	@Override
	void init() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_activity);

		mStart1 = (ImageView) findViewById(R.id.rank1);
		mStart2 = (ImageView) findViewById(R.id.rank2);
		mStart3 = (ImageView) findViewById(R.id.rank3);
		mStart4 = (ImageView) findViewById(R.id.rank4);
		mStart5 = (ImageView) findViewById(R.id.rank5);
		mCommentBtn = (Button) findViewById(R.id.comment_button);
		mCommentTv = (EditText) findViewById(R.id.comment_content);
		mBackLayout = findViewById(R.id.comment_back);

		mStart1.setOnClickListener(this);
		mStart2.setOnClickListener(this);
		mStart3.setOnClickListener(this);
		mStart4.setOnClickListener(this);
		mStart5.setOnClickListener(this);
		mCommentBtn.setOnClickListener(this);
		mBackLayout.setOnClickListener(this);
		selectRank = 5;
		productId = getIntent().getIntExtra("product_id", -1);
		sellerId = getIntent().getIntExtra("seller_id", -1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.comment_back:
			finish();
			break;
		case R.id.comment_button:
//			Toast.makeText(this, "seller：" + sellerId + "__productid;"
//					+ productId + "__rank:" + selectRank + "__comment:"
//					+ mCommentTv.getText().toString(), Toast.LENGTH_LONG).show();
			
			if (selectRank <4) {
				Intent intent = new Intent(this, QuestionActivity.class);
				intent.putExtra("comment", mCommentTv.getText().toString());
				startActivity(intent);
				finish();
				return;
			}
			
			Comment newComment = new Comment();
			AppContext appContext = (AppContext) this.getApplicationContext();
			newComment.setName(appContext.getUser().getUsername());
			newComment.setContent(mCommentTv.getText().toString());
			newComment.setTime(Utils.getStringTodayFormat("yyyy-MM-dd"));
			newComment.setRank(selectRank);
			if (-1 != productId) {
				ProductDetailActivity.userComments.add(0,newComment);
			}
			
			if (-1 != sellerId) {
				SellerDetailActivity.userComments.add(0,newComment);
			}
			Intent intent = new Intent();
			intent.putExtra("hasComment", true);
			setResult(RESULT_OK, intent);
			if (selectRank >= 4) {
				Toast.makeText(this, "谢谢支持，欢迎下次光临~", Toast.LENGTH_LONG).show();
			}
			finish();
			break;
		case R.id.rank1:
			selectRank = 1;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_off);
			break;

		case R.id.rank2:
			selectRank = 2;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_off);
			break;

		case R.id.rank3:
			selectRank = 3;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_off);
			break;

		case R.id.rank4:
			selectRank = 4;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_off);
			break;

		case R.id.rank5:
			selectRank = 5;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_on);
			break;
		default:
			break;
		}

	}

}
