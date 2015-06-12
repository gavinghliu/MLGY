package com.anjoyo.meituan.ui;

import com.anjoyo.mlgy.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class QuestionActivity extends BaseActivity implements OnClickListener {

	EditText mCommentTv;
	Button mCommentBtn;
	View mBackLayout;
	

	@Override
	void init() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity);

		mCommentBtn = (Button) findViewById(R.id.comment_button);
		mCommentTv = (EditText) findViewById(R.id.comment_content);
		mBackLayout = findViewById(R.id.comment_back);

		mCommentBtn.setOnClickListener(this);
		mBackLayout.setOnClickListener(this);
		mCommentTv.setText(getIntent().getStringExtra("comment"));
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
			Toast.makeText(this, "谢谢支持，欢迎下次光临~", Toast.LENGTH_LONG).show();
			finish();
			break;
		default:
			break;
		}

	}

}
