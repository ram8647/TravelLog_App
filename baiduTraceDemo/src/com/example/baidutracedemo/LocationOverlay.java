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
	 *  ��γ������
	 */
	private geoPointArray gpArray ;
	
	/**
	 *  ��λ��ʼ��
	 */
	private GeoPoint start;
	
	/**
	 *  ��λ������
	 */
	private GeoPoint end;
	
	/**
	 *  ��λ��ļ��� ��·��
	 */
	private MKRoute mkRoute;
	
	/**
	 *  ��λͼ��
	 */
	private RouteOverlay routeOverlay;
	// ��λ���
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	
	MapView mMapView = null;	// ��ͼView
	
	/**
	 *  ��ͼ������
	 */
	private MapController mMapController = null; 
	boolean isFirstLoc = true;//�Ƿ��״ζ�λ
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * ʹ�õ�ͼsdkǰ���ȳ�ʼ��BMapManager.
         * BMapManager��ȫ�ֵģ���Ϊ���MapView���ã�����Ҫ��ͼģ�鴴��ǰ������
         * ���ڵ�ͼ��ͼģ�����ٺ����٣�ֻҪ���е�ͼģ����ʹ�ã�BMapManager�Ͳ�Ӧ������
         */
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
             */
            app.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
        }
        setContentView(R.layout.activity_locationoverlay);
        CharSequence titleLable="��¼�켣";
        setTitle(titleLable);

        
		//��ͼ��ʼ��
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
        
        //��λ��ʼ��
        mLocClient = new LocationClient( this );
        locData = new LocationData();
        mLocClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//��gps
        option.setCoorType("bd09ll");     //������������
        option.setScanSpan(seconds);	// ��λ���ʱ��  ��λ����
        option.setIsNeedAddress(true); // ��Ҫ�ֵ�������Ϣ
        option.setNeedDeviceDirect(false); // ����Ҫ������Ϣ
        mLocClient.setLocOption(option);
        mLocClient.start();
        
        mkRoute = new MKRoute();  
        routeOverlay = new RouteOverlay(LocationOverlay.this, mMapView);
       
        routeOverlay.setData(mkRoute);
	    //��Ӷ�λͼ��
		mMapView.getOverlays().add(routeOverlay);
	
		//�޸Ķ�λ���ݺ�ˢ��ͼ����Ч
		mMapView.refresh();
		
    }
    /**
     * �޸�λ��ͼ��
     * @param marker
     */
    public void modifyLocationOverlayIcon(Drawable marker){
    	//������markerΪnullʱ��ʹ��Ĭ��ͼ�����
    	routeOverlay.setEnMarker(marker);
    	//�޸�ͼ�㣬��Ҫˢ��MapView��Ч
    	mMapView.refresh();
    }

    
    
	/**
     * ��λSDK��������
     */
    public class MyLocationListenner implements BDLocationListener {
    	
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;   
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();            
            //�������ʾ��λ����Ȧ����accuracy��ֵΪ0����
            locData.accuracy = 0;
            // �˴��������� locData�ķ�����Ϣ, �����λ SDK δ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
            locData.direction = location.getDerect();
            
            // ���ֶ�����������״ζ�λʱ
            if (isFirstLoc){
            	gpArray = new geoPointArray();
            	start = new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6));
            	end = new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6));
            	// �ѿ�ʼ�����·����
            	gpArray.addGeoPoint(start);
            	// ����·��
            	mkRoute.customizeRoute(start, end, gpArray.getGeoPointArray());
            	// ��λͼ������·��
            	routeOverlay.setData(mkRoute);
            	// ����ͼ������ִ��ˢ�º���Ч
            	mMapView.refresh();
            	// �ƶ���ͼ����λ��
                mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
                isFirstLoc = false;
            }else{
            	Log.i("test:LocationOverlay","loc:"+location.getLatitude()+","+location.getLongitude()+","+location.getAddrStr());
            	end = new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6));
            	// �ѵ�ǰ�����·����
            	gpArray.addGeoPoint(end);
            	// ����·��
            	mkRoute.customizeRoute(start, end, gpArray.getGeoPointArray());
            	// ��λͼ������·��
            	routeOverlay.setData(mkRoute); 
            	// �ƶ���ͼ����λ��
            	mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
            	// ˢ��ͼ��
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
    	//�˳�ʱ���ٶ�λ
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
 *  ��γ��������
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
	 * @return ���ذ�����γ�ȵ��һγ����
	 */
	public GeoPoint[] getGeoPointArray(){
		GeoPoint []gp = new GeoPoint[mlist.size()];
		for(int i=0; i<mlist.size(); i++){
			gp[i] = mlist.get(i);
		}
		return  gp;
	}
}

