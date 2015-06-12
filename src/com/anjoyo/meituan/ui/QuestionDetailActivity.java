package com.anjoyo.meituan.ui;

import java.util.List;

import com.anjoyo.meituan.domain.Question;
import com.anjoyo.mlgy.R;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionDetailActivity extends BaseActivity implements
		OnClickListener {
	private ImageView imageview_back;
	private TextView imageview_back2;
	private TextView questionContent;
	private TextView questionAnswer;
	private Question datas;
	private Button btnDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	void init() {
		setContentView(R.layout.question_details);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back2 = (TextView) findViewById(R.id.imageview_back2);
		questionAnswer = (TextView) findViewById(R.id.answer_content);
		questionContent = (TextView) findViewById(R.id.question_content);
		btnDone = (Button) findViewById(R.id.btn_done);
		
		
		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);
		
		int allPosition = getIntent().getIntExtra("allPosition", 0);
		datas = QuestionListActivity.mList.get(allPosition);
		
		questionContent.setText("   " + datas.getQuestion_content());
		if (null != questionAnswer) {
			questionAnswer.setText("   " + datas.getQuestion_answer());
		}
		if (datas.isDone()) {
			btnDone.setText("已解决");
			btnDone.setOnClickListener(null);
			btnDone.setBackgroundColor(Color.GRAY);
			btnDone.setClickable(false);
		} else {
			btnDone.setText("确认解决");
			btnDone.setOnClickListener(this);
		}
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();
			break;
		case R.id.btn_done:
			int allPosition = getIntent().getIntExtra("allPosition", 0);
			QuestionListActivity.mList.get(allPosition).setDone(true);
			btnDone.setText("已解决");
			btnDone.setBackgroundColor(Color.GRAY);
			btnDone.setOnClickListener(null);
			btnDone.setClickable(false);
			break;
		default:
			break;
		}

	}

}
