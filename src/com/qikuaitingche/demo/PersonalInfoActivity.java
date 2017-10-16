package com.qikuaitingche.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PersonalInfoActivity extends Activity {
	
	private static final String[] strs = new String[] {
		    "我的订单", "我的钱包", "邀请好友", "加入共享", "我的优惠卷","我的客服"
		    };//定义一个String数组用来显示ListView的内容
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalinfo);
		listView = (ListView) findViewById(R.id.lv_1);
		listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, strs));
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

}
