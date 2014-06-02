package com.example.baidutracedemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}	
	public void OnClick(View v){
		switch(v.getId()){
		case R.id.startBtn :
			Intent intent = new Intent(MainActivity.this,LocationOverlay.class);
			startActivity(intent);
			break;
		default :
			break;
		}
	}
}
