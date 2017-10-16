package com.qikuaitingche.demo;


import android.app.Activity;


//import android.graphics.drawable.Drawable;

import android.os.Bundle;
import com.baidu.mapapi.SDKInitializer;



public class OrderActivity extends Activity {

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_order);
		
	}

}