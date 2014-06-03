package com.cai.chinatravellog;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MainActivity extends ActionBarActivity {
	BMapManager mBMapMan = null;  
	MapView mMapView = null;
	public BDLocationListener myListener;
	public LocationClient mLocationClient = null;
	public final static String TAG = "activity message";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan=new BMapManager(getApplication());  
		mBMapMan.init(null);   
		//注意：请在试用setContentView前初始化BMapManager对象，否则会报错  
		setContentView(R.layout.fragment_main);  
		mMapView=(MapView)findViewById(R.id.bmapsView);  
		mMapView.setBuiltInZoomControls(true);  
	    MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
		LocationData locData = new LocationData();
		
	    mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	    //myListener = new MyLocationListener();
	    mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
	    LocationClientOption option = new LocationClientOption();
	    option.setOpenGps(true);
	    option.setLocationMode(LocationMode.Device_Sensors);//设置定位模式
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
	    option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
	    option.setIsNeedAddress(true);//返回的定位结果包含地址信息
	    option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
	    mLocationClient.setLocOption(option);
		
	    mLocationClient.start();
	    Log.i(TAG,"Debugging started.");
	    if(mLocationClient.isStarted()){
	    	Log.i(TAG,"locClient is started");
	    }
	    else{
	    	Log.i(TAG,"locClient is NOT started");
	    	mLocationClient.start();
	    }
	    
	    if (mLocationClient != null && mLocationClient.isStarted()){
	    	  int m=mLocationClient.requestLocation();
	    	  Log.i(TAG,"request returns "+m);
	    }
	    else{ 
	    	 Log.i(TAG, "locClient is null or not started");
	    	 int m=mLocationClient.requestLocation();
	    	  Log.i(TAG,"request returns "+m);
	    }

	
		//手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
		//locData.latitude = 39.945;
		//locData.longitude = 116.404;
		//locData.direction = 2.0f;
		Log.i(TAG,locData.latitude+","+locData.longitude);
		
		myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		mMapView.refresh();
		
		MapController mMapController=mMapView.getController();  
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		mMapController.animateTo(new GeoPoint((int)(locData.latitude*1e6),
		(int)(locData.longitude* 1e6)));
		mMapController.setZoom(12);//设置地图zoom级别 
		
		
		 
	}
	
	
	public class MyLocationListener implements BDLocationListener {
		
	    @Override
	    public void onReceiveLocation(BDLocation location) {
	      if (location == null)
	          return ;
	      StringBuffer sb = new StringBuffer(256);
	      sb.append("time : ");
	      sb.append(location.getTime());
	      sb.append("\nerror code : ");
	      sb.append(location.getLocType());
	      sb.append("\nlatitude : ");
	      sb.append(location.getLatitude());
	      sb.append("\nlontitude : ");
	      sb.append(location.getLongitude());
	      sb.append("\nradius : ");
	      sb.append(location.getRadius());
	      if (location.getLocType() == BDLocation.TypeGpsLocation){
	           sb.append("\nspeed : ");
	           sb.append(location.getSpeed());
	           sb.append("\nsatellite : ");
	           sb.append(location.getSatelliteNumber());
	           } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
	           sb.append("\naddr : ");
	           sb.append(location.getAddrStr());
	        } 
	      Log.i(TAG,sb.toString());
	      
	      
	    }
	    
	    public void onReceivePoi(BDLocation poiLocation) {
	    	//将在下个版本中去除poi功能
	    	         if (poiLocation == null){
	    	                return ;
	    	          }
	    	         StringBuffer sb = new StringBuffer(256);
	    	          sb.append("Poi time : ");
	    	          sb.append(poiLocation.getTime());
	    	          sb.append("\nerror code : ");
	    	          sb.append(poiLocation.getLocType());
	    	          sb.append("\nlatitude : ");
	    	          sb.append(poiLocation.getLatitude());
	    	          sb.append("\nlontitude : ");
	    	          sb.append(poiLocation.getLongitude());
	    	          sb.append("\nradius : ");
	    	          sb.append(poiLocation.getRadius());
	    	          if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
	    	              sb.append("\naddr : ");
	    	              sb.append(poiLocation.getAddrStr());
	    	         } 
	    	          if(poiLocation.hasPoi()){
	    	               sb.append("\nPoi:");
	    	               sb.append(poiLocation.getPoi());
	    	         }else{             
	    	               sb.append("noPoi information");
	    	          }
	    	         Log.i(TAG,sb.toString());
	    	        }
	
	}
	
	
	@Override  
	protected void onDestroy(){  
	        mMapView.destroy();  
	        if(mBMapMan!=null){  
	                mBMapMan.destroy();  
	                mBMapMan=null;  
	        }  
	        super.onDestroy();  
	}  
	@Override  
	protected void onPause(){  
	        mMapView.onPause();  
	        if(mBMapMan!=null){  
	               mBMapMan.stop();  
	        }  
	        super.onPause();  
	}  
	@Override  
	protected void onResume(){  
	        mMapView.onResume();  
	        if(mBMapMan!=null){  
	                mBMapMan.start();  
	        }  
	       super.onResume();  
	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
