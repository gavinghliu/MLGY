package com.anjoyo.meituan.ui;

import java.util.ArrayList;
import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.Parser.GroupBuyLikeParser;
import com.anjoyo.meituan.Parser.ProductParser;
import com.anjoyo.meituan.adapter.CollectAdapter;
import com.anjoyo.meituan.adapter.CollectSellerAdapter;
import com.anjoyo.meituan.adapter.ProductAdapter;
import com.anjoyo.meituan.adapter.QuestionAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.domain.Question;
import com.anjoyo.meituan.domain.User;
import com.anjoyo.meituan.myview.MyListView;
import com.anjoyo.meituan.utils.XListView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionListActivity extends BaseActivity implements OnClickListener {
	private ImageView imageview_back;
	private TextView imageview_back2;
	private MyListView listview;
	private TextView textview;
	private QuestionAdapter mAdapter;
	public static ArrayList<Question> mList = new ArrayList<Question>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	void init() {
		setContentView(R.layout.question_list_activity);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back2 = (TextView) findViewById(R.id.imageview_back2);
		listview = (MyListView) findViewById(R.id.listview_collect);
		textview = (TextView) findViewById(R.id.textview_collect);

		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);

		AppContext appContext = (AppContext) getApplicationContext();
		User user = appContext.getUser();
		String username = user.getUsername();
		mAdapter = new QuestionAdapter(QuestionListActivity.this, mList);
		listview.setAdapter(mAdapter);
		
		ProductParser productParer = new ProductParser();
		String res = "{'questions' : [{question_id:1,question_answer:这是客服的反馈1,question_time:2014-08-09,question_done:1,question_content:我反馈一下~~},"
				+ "{question_id:1,question_time:2014-08-09,question_done:1,question_content:我反馈一下22~~},"
				+ "{question_id:1,question_answer:这是客服的反馈jdada,question_time:2014-08-09,question_done:0,question_content:我反馈一下33~~}]}";

		ArrayList<Question> product = Question.parseQuestion(res);
		mList.clear();
		mList.addAll(product);
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
		listview.setVisibility(View.VISIBLE);
		textview.setVisibility(View.GONE);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}

}
