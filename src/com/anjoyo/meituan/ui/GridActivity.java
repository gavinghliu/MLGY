package com.anjoyo.meituan.ui;

import java.util.ArrayList;
import java.util.List;

import com.anjoyo.meituan.adapter.MySimpleCursorAdapter;
import com.anjoyo.mlgy.R;

import android.R.layout;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GridActivity extends Activity implements OnClickListener {

	// DbHelper类在DbHelper.java文件里面创建的
	ListView lv;
	LinearLayout mTitleLayout;
	private ImageView imageview_back;
	private TextView imageview_back2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		lv = (ListView) findViewById(R.id.lv);
		mTitleLayout = (LinearLayout) findViewById(R.id.title);
		mTitleLayout.setBackgroundColor(Color.rgb(219, 238, 244));
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back2 = (TextView) findViewById(R.id.imageview_back2);
		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);
		
		Typeface fontFace = Typeface.createFromAsset(getAssets(),
				"fonts/STXINGKA.TTF");
		
		TextView t1 = (TextView)findViewById(R.id.title1);
		TextView t2 = (TextView)findViewById(R.id.title2);
		TextView t3 = (TextView)findViewById(R.id.title3);
		t1.setTypeface(fontFace);
		t2.setTypeface(fontFace);
		t3.setTypeface(fontFace);
		
		updatelistview();
	}

	// 更新listview
	public void updatelistview() {

		List<String> list = new ArrayList<String>();
		list.add("冥想&放松身心，让人心神愉悦&5");
		list.add("热敷&温热皮肤，行气活血，舒缓疲劳，放松神经，补充能量，行气活血&5");
		list.add("头部松筋(后面)&改善睡眠，增强记忆力，改善头部供氧供血&5");
		list.add("背部&疏通经络，补充能量，调理各种亚健康&30");
		list.add("腿部(后面)&疏通经络，，排寒气，各种湿毒热毒，调理脏腑各种亚健康&15");
		list.add("头部松筋(前面)&改善睡眠，增强记忆力，改善头部供氧供血&5");
		list.add("腹部&排肠毒，改善肠胃功能，预防肠胃疾病，预防妇科疾病&15");
		list.add("腿部(前面)&疏通经络，，排寒气，各种湿毒热毒，调理脏腑各种亚健康&15");
		list.add("药膳&排毒，补充水分，能量& ");
		list.add("头部松筋(前面)&改善睡眠，增强记忆力，改善头部供氧供血&5");
		list.add("腹部&排肠毒，改善肠胃功能，预防肠胃疾病，预防妇科疾病&15");
		list.add("腿部(前面)&疏通经络，，排寒气，各种湿毒热毒，调理脏腑各种亚健康&15");
		list.add("药膳&排毒，补充水分，能量& ");
		ListAdapter adapter = new MySimpleCursorAdapter(this, list);
		// layout为listView的布局文件，包括三个TextView，用来显示三个列名所对应的值
		// ColumnNames为数据库的表的列名
		// 最后一个参数是int[]类型的，为view类型的id，用来显示ColumnNames列名所对应的值。view的类型为TextView
		lv.setAdapter(adapter);

	}

	@Override
	protected void onDestroy() {// 关闭数据库
		// TODO Auto-generated method stub
		super.onDestroy();
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

}