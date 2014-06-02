package com.example.baidutracedemo;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;
 
public class LocationOverlay extends Activity {

	/**
	 * 
	 */
	private int seconds = 1000 * 20;
	/**
	 *  经纬度数组
	 */
	private geoPointArray gpArray ;
	
	/**
	 *  定位开始点
	 */
	private GeoPoint start;
	
	/**
	 *  定位结束点
	 */
	private GeoPoint end;
	
	/**
	 *  定位点的集合 即路径
	 */
	private MKRoute mkRoute;
	
	/**
	 *  定位图层
	 */
	private RouteOverlay routeOverlay;
	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	
	MapView mMapView = null;	// 地图View
	
	/**
	 *  地图控制器
	 */
	private MapController mMapController = null; 
	boolean isFirstLoc = true;//是否首次定位
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 使用地图sdk前需先初始化BMapManager.
         * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
         * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
         */
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            app.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
        }
        setContentView(R.layout.activity_locationoverlay);
        CharSequence titleLable="记录轨迹";
        setTitle(titleLable);

        
		//地图初始化
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
        
        //定位初始化
        mLocClient = new LocationClient( this );
        locData = new LocationData();
        mLocClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(seconds);	// 定位间隔时间  单位毫秒
        option.setIsNeedAddress(true); // 需要街道名称信息
        option.setNeedDeviceDirect(false); // 不需要方向信息
        mLocClient.setLocOption(option);
        mLocClient.start();
        
        mkRoute = new MKRoute();  
        routeOverlay = new RouteOverlay(LocationOverlay.this, mMapView);
       
        routeOverlay.setData(mkRoute);
	    //添加定位图层
		mMapView.getOverlays().add(routeOverlay);
	
		//修改定位数据后刷新图层生效
		mMapView.refresh();
		
    }
    /**
     * 修改位置图标
     * @param marker
     */
    public void modifyLocationOverlayIcon(Drawable marker){
    	//当传入marker为null时，使用默认图标绘制
    	routeOverlay.setEnMarker(marker);
    	//修改图层，需要刷新MapView生效
    	mMapView.refresh();
    }

    
    
	/**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
    	
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;   
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();            
            //如果不显示定位精度圈，将accuracy赋值为0即可
            locData.accuracy = 0;
            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
            locData.direction = location.getDerect();
            
            // 是手动触发请求或首次定位时
            if (isFirstLoc){
            	gpArray = new geoPointArray();
            	start = new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6));
            	end = new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6));
            	// 把开始点放在路径中
            	gpArray.addGeoPoint(start);
            	// 构造路径
            	mkRoute.customizeRoute(start, end, gpArray.getGeoPointArray());
            	// 定位图层设置路径
            	routeOverlay.setData(mkRoute);
            	// 更新图层数据执行刷新后生效
            	mMapView.refresh();
            	// 移动地图到定位点
                mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
                isFirstLoc = false;
            }else{
            	Log.i("test:LocationOverlay","loc:"+location.getLatitude()+","+location.getLongitude()+","+location.getAddrStr());
            	end = new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6));
            	// 把当前点放在路径中
            	gpArray.addGeoPoint(end);
            	// 构造路径
            	mkRoute.customizeRoute(start, end, gpArray.getGeoPointArray());
            	// 定位图层设置路径
            	routeOverlay.setData(mkRoute); 
            	// 移动地图到定位点
            	mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
            	// 刷新图层
            	mMapView.refresh();

            }
        }

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
    }
    


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	//退出时销毁定位
        if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
    

}
/**
 *  经纬度数组类
 */
class geoPointArray{
	
	private ArrayList <GeoPoint> mlist;
	geoPointArray(){
		mlist = new ArrayList<GeoPoint>();
	}
	
	public void addGeoPoint(GeoPoint point){
		mlist.add(point);
	}
	
	/**
	 * @return 返回包含经纬度点的一纬数组
	 */
	public GeoPoint[] getGeoPointArray(){
		GeoPoint []gp = new GeoPoint[mlist.size()];
		for(int i=0; i<mlist.size(); i++){
			gp[i] = mlist.get(i);
		}
		return  gp;
	}
}

