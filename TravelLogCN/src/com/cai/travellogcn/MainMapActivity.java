package com.cai.travellogcn;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMyLocationChangeListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;



public class MainMapActivity extends ActionBarActivity implements LocationSource,
AMapLocationListener{  
    private MapView mapView;  
    private AMap aMap;
    private UiSettings mUiSettings;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private Marker marker;
	private LatLng point1;
	private LatLng point2;  //used for drawing polyline
	public boolean first = true;
	public final static String TAG = "activity message";
	
	
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        // R 需要引用包import com.amapv2.apis.R;  
        setContentView(R.layout.activity_main_map);  
        mapView = (MapView) findViewById(R.id.map);  
        mapView.onCreate(savedInstanceState);// 必须要写
        Log.i(TAG,"debugging started.");
        init();  
    }  
  
    /** 
     * 初始化AMap对象 
     */  
    private void init() {  
        if (aMap == null) {  
            aMap = mapView.getMap();
            setUpMap();
            
        }  
        
    } 
    
    /**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.defaultMarker());// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setScrollGesturesEnabled(false);//disable gesture scroll
		
		aMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener(){
			
			@Override
			public void onMyLocationChange(Location arg0) {
				// TODO Auto-generated method stub
				drawLines(arg0);
				Log.i(TAG, "location change listener here.");
				
			}
			
		});
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		
	}
	
	private void drawLines(Location aLocation){
		//alternatively draw point1 and point2
		
			Log.i(TAG,"start mapping lines");
			point1 = new LatLng(aLocation.getLatitude(),aLocation.getLongitude());
			
			Log.i(TAG,"point1 location ("+point1.latitude+","+point1.longitude+")");
			if (point2 == null){
				Log.i(TAG,"Point2 is null.");
				point2 = new LatLng(aLocation.getLatitude(),aLocation.getLongitude());
			}
			Log.i(TAG,"point2 location ("+point2.latitude+","+point2.longitude+")");
			
			
			//draw line from point2 to point1
			if ((point1 != null) && (point2 != null) && (!first)){
				Log.i(TAG, "draw line from point2 to point1");
				aMap.addPolyline((new PolylineOptions())
						.add(point2, point1)
						.width(10).setDottedLine(true).geodesic(true)
						.color(Color.argb(255, 1, 1, 1)));
			}
			point2 = new LatLng(point1.latitude, point1.longitude);
			
		
			
		
		
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
        deactivate();
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

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		
		if (mListener != null && aLocation != null) {

			if (first){
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aLocation.getLatitude()
						, aLocation.getLongitude()),18));
				first = false;
			}
			//aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,12));
			Log.i(TAG,"camera animation");
			aMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(aLocation.getLatitude()
					, aLocation.getLongitude())));
			//animation duration is 2000 ms, zoom level=15.

			
			
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			drawLines(aLocation);
			
			
			
			float bearing = aMap.getCameraPosition().bearing;
			aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}




}
