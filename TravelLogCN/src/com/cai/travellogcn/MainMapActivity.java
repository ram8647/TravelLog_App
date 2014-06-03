package com.cai.travellogcn;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;



public class MainMapActivity extends ActionBarActivity {  
    private MapView mapView;  
    private AMap aMap;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        // R 需要引用包import com.amapv2.apis.R;  
        setContentView(R.layout.activity_main_map);  
        mapView = (MapView) findViewById(R.id.map);  
        mapView.onCreate(savedInstanceState);// 必须要写  
        init();  
    }  
  
    /** 
     * 初始化AMap对象 
     */  
    private void init() {  
        if (aMap == null) {  
            aMap = mapView.getMap();  
        }  
    }  
  
    /** 
     * 方法必须重写 
     */  
    @Override  
    protected void onResume() {  
        super.onResume();  
        mapView.onResume();  
    }  
  
    /** 
     * 方法必须重写 
     */  
    @Override  
    protected void onPause() {  
        super.onPause();  
        mapView.onPause();  
    }  
      
    /** 
     * 方法必须重写 
     */  
    @Override  
    protected void onSaveInstanceState(Bundle outState) {  
        super.onSaveInstanceState(outState);  
        mapView.onSaveInstanceState(outState);  
    }  
  
    /** 
     * 方法必须重写 
     */  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        mapView.onDestroy();  
    }  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



}
