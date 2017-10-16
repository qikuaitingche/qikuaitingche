package com.qikuaitingche.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PersonalInfoActivity extends Activity {
	
	private static final String[] strs = new String[] {
		    "�ҵĶ���", "�ҵ�Ǯ��", "�������", "���빲��", "�ҵ��Żݾ�","�ҵĿͷ�"
		    };//����һ��String����������ʾListView������
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
