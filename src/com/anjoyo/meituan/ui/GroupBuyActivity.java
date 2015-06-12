package com.anjoyo.meituan.ui;

import java.util.ArrayList;
import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.Parser.GroupBuyLikeParser;
import com.anjoyo.meituan.Parser.ProductParser;
import com.anjoyo.meituan.adapter.CollectAdapter;
import com.anjoyo.meituan.adapter.CollectSellerAdapter;
import com.anjoyo.meituan.adapter.MainAdapter;
import com.anjoyo.meituan.adapter.ProductAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.domain.User;
import com.anjoyo.meituan.domain.Wenzhang;
import com.anjoyo.meituan.myview.MyListView;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.meituan.utils.XListView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GroupBuyActivity extends BaseActivity implements OnClickListener {
	private Button button_zhazhi, button_tongzhi;
	private LinearLayout line_zhazhi, line_tongzhi;
	private ListView listview;
	private ListView xListView;
	private String zhazhi;
	private String tongzhi;
	public static ArrayList<Wenzhang> mList = new ArrayList<Wenzhang>();
	MainAdapter mAdapter;

	private ImageView ivDeleteText;
	private EditText etSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	void init() {
		setContentView(R.layout.main_activity);
		button_zhazhi = (Button) findViewById(R.id.button_zhazhi);
		button_tongzhi = (Button) findViewById(R.id.button_tongzhi);
		line_zhazhi = (LinearLayout) findViewById(R.id.line_zhazhi);
		line_tongzhi = (LinearLayout) findViewById(R.id.line_tongzhi);
		listview = (ListView) findViewById(R.id.listview_zhazhi);
		xListView = (ListView) findViewById(R.id.listview_tongzhi);

		button_zhazhi.setOnClickListener(this);
		button_tongzhi.setOnClickListener(this);

		mAdapter = new MainAdapter(GroupBuyActivity.this, mList);
		mAdapter.setAction("zhazhi");
		listview.setAdapter(mAdapter);

		String filePath = SocketUtils.QUERY_XLS_FILE_DIR + "/zhazhi.txt";
		zhazhi = Utils.ReadTxtFile(filePath);

		String filePath2 = SocketUtils.QUERY_XLS_FILE_DIR + "/tongzhi.txt";
		tongzhi = Utils.ReadTxtFile(filePath2);

		if (zhazhi != null) {
			ArrayList<Wenzhang> product = Wenzhang.parsewenzhang(zhazhi);
			mList.clear();
			mList.addAll(product);
			mAdapter.setList(mList);
			mAdapter.notifyDataSetChanged();
			listview.setVisibility(View.VISIBLE);
		}

		ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
		etSearch = (EditText) findViewById(R.id.etSearch);

		ivDeleteText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				etSearch.setText("");
			}
		});

		etSearch.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {

				ArrayList<Wenzhang> product;
				mList.clear();
				if (line_tongzhi.isShown()) {
					product = Wenzhang.parsewenzhang(tongzhi);
				} else {
					product = Wenzhang.parsewenzhang(zhazhi);
				}

				if (s.length() == 0) {
					ivDeleteText.setVisibility(View.GONE);
					mList.addAll(product);
				} else {
					ivDeleteText.setVisibility(View.VISIBLE);

					for (Wenzhang wenzhang : product) {
						if (wenzhang.getwenzhang_title().contains(
								s.toString().trim())
								|| wenzhang.getDetail().contains(
										s.toString().trim())) {
							mList.add(wenzhang);
						}
					}
				}

				mAdapter.setList(mList);
				mAdapter.notifyDataSetChanged();
				listview.setVisibility(View.VISIBLE);
			}
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_zhazhi:
			button_tongzhi.setTextColor(getResources().getColor(R.color.black));
			button_zhazhi.setTextColor(getResources()
					.getColor(R.color.textgray));
			line_tongzhi.setVisibility(View.INVISIBLE);
			line_zhazhi.setVisibility(View.VISIBLE);

			ArrayList<Wenzhang> product = Wenzhang.parsewenzhang(zhazhi);

			if (product != null && !product.isEmpty()) {
				mList.clear();
				mList.addAll(product);
				listview.setVisibility(View.VISIBLE);
				xListView.setVisibility(View.GONE);
			} else {
				listview.setVisibility(View.GONE);
				xListView.setVisibility(View.GONE);
			}
			mAdapter.setList(mList);
			mAdapter.notifyDataSetChanged();

			break;
		case R.id.button_tongzhi:
			button_zhazhi.setTextColor(getResources().getColor(R.color.black));
			button_tongzhi.setTextColor(getResources().getColor(
					R.color.textgray));
			line_zhazhi.setVisibility(View.INVISIBLE);
			line_tongzhi.setVisibility(View.VISIBLE);

			product = Wenzhang.parsewenzhang(tongzhi);
			mAdapter.setAction("tongzhi");

			if (product != null && !product.isEmpty()) {
				mList.clear();
				mList.addAll(product);
				listview.setVisibility(View.VISIBLE);
				xListView.setVisibility(View.GONE);
			} else {
				listview.setVisibility(View.GONE);
				xListView.setVisibility(View.GONE);
			}
			mAdapter.setList(mList);
			mAdapter.notifyDataSetChanged();

			break;
		default:
			break;
		}

	}

}
