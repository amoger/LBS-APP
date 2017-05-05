package com.imooc.lottery;

import java.util.List;
import java.util.jar.Attributes.Name;

import javax.crypto.spec.IvParameterSpec;

//import baidumapsdk.demo.GeoCoderDemo;
//import baidumapsdk.demo.R;







import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;






import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.imooc.lottery.MyOrientationListener.OnOrientationListener;
import com.imooc.slidingmenu.view.SlidingMenu;



public class MainActivity extends Activity implements OnGetGeoCoderResultListener {
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	
	//MapView mMapView = null;  
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	
	private Context context;	//初始化一个全局变量
	
	//在MyLocationListener类中，取得“我的位置”的地址
	public String addr_info;
	
	public double lat_marker;
	public double lon_marker;
	public String addr_marker;
	public double  lat_loc;
	public double lon_loc;
	
	//起终点经纬度坐标
	public LatLng st_latlng;
	public LatLng en_latlng;
	
	//定位相关
	//public LocationClient ( Context )        //须在主线程中声明
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn=true;
	private double mLatitude;
	private double mLongitude;
	//自定义定位图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;
	private com.baidu.mapapi.map.MyLocationConfiguration.LocationMode mLocationMode;
	
	//覆盖物相关
	private BitmapDescriptor mMarker;
	private RelativeLayout mMarkerLy;
	
	//侧滑相关
	private SlidingMenu mLeftMenu;	
	
	//Intent页面跳转相关
	private Button btDetail;
	private Button btAbout;
	
	//退出程序
	private Button btExit;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_ACTION_BAR);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题栏
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要在setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());      
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
        setContentView(R.layout.activity_main); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.mycustomtitle);  
        mMapView = (MapView) findViewById(R.id.bmapView); 
        
        this.context=this;
        
        initView();
             
        //初始化定位
        initLocation();
        
        initMarker();
        
        btAbout=(Button) findViewById(R.id.header_left_btn);
        btAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView iv=(ImageView) findViewById(R.id.id_info_img);
				TextView name=(TextView) findViewById(R.id.id_info_name);			
				TextView phonenum=(TextView) mMarkerLy.findViewById(R.id.phone_num);
				/*
				 *第一个参数:上下文对象this
				 *第二个参数：目标文件	
				 */			
				Intent intent=new Intent(context, about_me.class);
				startActivity(intent);				
			}
		});
    	//Intent页面跳转相关
        btDetail=(Button) findViewById(R.id.button_detail);
        //注册点击事件
        btDetail.setOnClickListener(new OnClickListenerlmpl());  		
        
        
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {			
			@Override
			public boolean onMarkerClick(Marker marker) { 
				LatLng ptCenter = new LatLng(lat_marker,lon_marker);
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
				
				//起终点经纬度坐标
				st_latlng=new LatLng(lat_loc,lon_loc);
				en_latlng=new LatLng(lat_marker,lon_marker);
				int distanceof =(int) DistanceUtil. getDistance(st_latlng, en_latlng);
				
				Bundle extraInfo =marker.getExtraInfo();
				Info info=(Info) extraInfo.getSerializable("info");
				lat_marker=info.getLatitude();
				lon_marker=info.getLongitude();
				//拿到所有的控件
				ImageView iv=(ImageView) mMarkerLy.findViewById(R.id.id_info_img);
				TextView distance=(TextView) mMarkerLy.findViewById(R.id.id_info_distance);
				TextView name=(TextView) mMarkerLy.findViewById(R.id.id_info_name);
				Button BtDetail=(Button) mMarkerLy.findViewById(R.id.button_detail);	
				TextView phonenum=(TextView) mMarkerLy.findViewById(R.id.phone_num);
				
				//得到相应的信息
				iv.setImageResource(info.getImgId());
				name.setText(info.getName());
				phonenum.setText(info.getPhone());
				//计算出距离
				distance.setText("距离我有"+distanceof + ""+"米");
				
				
				InfoWindow infoWindow;
				TextView tv=new TextView(context);
				tv.setBackgroundResource(R.drawable.location_tips);
				tv.setPadding(30, 20, 30, 50);
				tv.setText(info.getName());
				tv.setTextColor(Color.parseColor("#ffffff"));
				
				final LatLng latLng=marker.getPosition();
				//将地图上的经纬度转化为屏幕上实际的点
				Point pt=mBaiduMap.getProjection().toScreenLocation(latLng);
				pt.y-=47;			//    y的偏移量
				LatLng ll=mBaiduMap.getProjection().fromScreenLocation(pt);
				
				BitmapDescriptor tvBD = BitmapDescriptorFactory.fromView(tv);

				infoWindow=new InfoWindow(tvBD, ll, 0, new InfoWindow.OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick() {
//					mBaiduMap.hideInfoWindow();		//点击InfoWindow，隐藏InfoWindow
				}
				});
				
				mBaiduMap.showInfoWindow(infoWindow);
				
				mMarkerLy.setVisibility(View.VISIBLE);				//显示布局mMarkerLy
				return true;
			}
        });
        
        mLeftMenu=(SlidingMenu) findViewById(R.id.id_menu);
        
        
       	//对地图的点击，使布局mMarkerLy消失
        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}			
			@Override
			public void onMapClick(LatLng arg0) {
				mMarkerLy.setVisibility(View.GONE);
				mBaiduMap.hideInfoWindow();  
			}
		});
      
        	// 初始化搜索模块，注册事件监听，反向地理编码
     		mSearch = GeoCoder.newInstance();
     		mSearch.setOnGetGeoCodeResultListener(this);
     		
     		btExit=(Button) findViewById(R.id.header_right_btn);
     		btExit.setOnClickListener(new OnClickListenerlmpll());
     		
	}
	
	private class OnClickListenerlmpll implements OnClickListener{
		@Override
		public void onClick(View v) {
			MainActivity.this.exitDialog();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_BACK){
			this.exitDialog();
		}
		return false;
	}
	
			//Intent页面跳转相关
	private class OnClickListenerlmpl implements OnClickListener{
		@Override
		public void onClick(View view){			
//			ImageView iv=(ImageView) findViewById(R.id.id_info_img);
			TextView name=(TextView) findViewById(R.id.id_info_name);			
			TextView phonenum=(TextView) mMarkerLy.findViewById(R.id.phone_num);
			/*
			 *第一个参数:上下文对象this
			 *第二个参数：目标文件	
			 */			
			Intent intent=new Intent(context, _001.class);
			Bundle bundle = new Bundle();		 
		    String name_info = (String) name.getText();		
		    String phonenum_info=(String) phonenum.getText();
		    String myaddr_info=addr_info;
		    String addr_marker_info=addr_marker;
		    double lat_myloc=lat_loc;
		    double lon_myloc=lon_loc;
		    double lat_mymarker=lat_marker;
		    double lon_mymarker=lon_marker;
//			int  img_info=iv.getId();
		    
		    //保存输入的信息
		    bundle.putString("name", name_info);
		    bundle.putString("phonenum",phonenum_info);
		    bundle.putString("myaddr", myaddr_info);
		    bundle.putString("addr_marker",addr_marker_info);
		    bundle.putDouble("st_lat", lat_myloc);
		    bundle.putDouble("st_lon", lon_myloc);
		    bundle.putDouble("en_lat", lat_mymarker);
		    bundle.putDouble("en_lon", lon_mymarker);
//		    bundle.putInt("id", img_info);
		    intent.putExtras(bundle);					
			startActivity(intent);
		}	
	}
	
	private void initMarker() {
		mMarker=BitmapDescriptorFactory.fromResource(R.drawable.fucaimaker);
		mMarkerLy=(RelativeLayout) findViewById(R.id.id_marker_ly);
	}

	public void exitDialog() {
		Dialog dialog = new AlertDialog.Builder(MainActivity.this)
			.setTitle("程序退出？")
			.setMessage("您确定要退出本程序吗？")
			.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,int whichButton){
					MainActivity.this.finish();
				}
			}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			}).create();
		dialog.show();
	}

	public void toggleMenu(View view) {
		mLeftMenu.toggle();
	}

	private void initLocation() {
		mLocationMode=com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL;
		mLocationClient=new LocationClient(this);
		mLocationListener=new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option=new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		//初始化图标
		mIconLocation=BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);		
		myOrientationListener=new MyOrientationListener(context);
		
		myOrientationListener.setOnOrientationListener(new OnOrientationListener(){

			@Override
			public void onOrientationChanged(float x) {
				mCurrentX=x;
			}
		});
	}

	private void initView() {
		mMapView=(MapView) findViewById(R.id.bmapView);
		mBaiduMap=mMapView.getMap();
		MapStatusUpdate msu=MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_map_common:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case R.id.id_map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.id_map_traffic:
			if (mBaiduMap.isTrafficEnabled()) {
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("实时交通(off)");
			}else {
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("实时交通(on)");
			}
			break;
		case R.id.id_map_location:		//定位到我的位置
			initView();
			centerToMyLocation();
			break;
		case R.id.id_map_mode_common:
			mLocationMode=com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL;
			break;
		case R.id.id_map_mode_following:			
			mLocationMode=com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING;
			break;
		case R.id.id_map_mode_compass:
			mLocationMode=com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.COMPASS;
			break;
		case R.id.id_map_add_overlay:
			addOverlays(Info.infos);    //自定义的一个函数（方法）
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	//添加覆盖物的方法（函数）
	private void addOverlays(List<Info> infos) {
		mBaiduMap.clear();
		LatLng latLng=null;
		Marker marker=null;
		OverlayOptions options;		
		for(Info info:infos){		//根据每一个info生成相应的marker,然后把marker放在地图上
			//经纬度
			latLng=new LatLng(info.getLatitude(),info.getLongitude());
			//图标
			options=new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker=(Marker) mBaiduMap.addOverlay(options);
			Bundle arg0=new Bundle();
			arg0.putSerializable("info", info);
			marker.setExtraInfo(arg0);
		}
		
		MapStatusUpdate msu=MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
		centerToMyLocation();		    //添加覆盖物完成后，定位到我的位置
		
	}
	

	/*
	 * 函数——定位到我的位置
	 */
	private void centerToMyLocation() {
		LatLng latLng=new LatLng(mLatitude, mLongitude);
		MapStatusUpdate msu=MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
	
	
	 @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
	        mMapView.onDestroy();  
	        mSearch.destroy();
	}  
	    @Override  
	    protected void onResume() {  
	        super.onResume();  
	        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
	        mMapView.onResume();  
	}  
	    @Override
	    protected void onStart() {
	    	super.onStart();	    	
	    	 //addOverlays(Info.infos);	//添加覆盖物         
	    	//开启定位
	    	mBaiduMap.setMyLocationEnabled(true);
	    	if (!mLocationClient.isStarted()) {
				mLocationClient.start();
			}	
	    	//开启方向传感器
	    	myOrientationListener.start();
	    }
	    @Override
	    protected void onStop() {
	    	super.onStop();
	    	//停止定位
	    	mBaiduMap.setMyLocationEnabled(false);
	    	mLocationClient.stop();
	    	//停止方向传感器
	    	myOrientationListener.stop();
	    }
	    @Override  
	    protected void onPause() {  
	        super.onPause();  
	        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
	        mMapView.onPause();  
	}  
	    
	    
	    
public class MyLocationListener implements BDLocationListener{

	@Override
	public void onReceiveLocation(BDLocation location) {
		MyLocationData data = new MyLocationData.Builder()//
		.direction(mCurrentX)//
		.accuracy(location.getRadius())//
		.latitude(location.getLatitude())//
		.longitude(location.getLongitude())//
		.build();
		mBaiduMap.setMyLocationData(data);
		//设置自定义图标
		MyLocationConfiguration config=new MyLocationConfiguration(mLocationMode,
				true, mIconLocation);
		mBaiduMap.setMyLocationConfigeration(config);
		//更新经纬度
		mLatitude=location.getLatitude();
		mLongitude=location.getLongitude();
		
		if (isFirstIn) {
			LatLng latLng=new LatLng(location.getLatitude(), location.getLongitude());
			MapStatusUpdate msu=MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.animateMapStatus(msu);
			isFirstIn=false;
			
			Toast.makeText(context,location.getAddrStr(), Toast.LENGTH_LONG).show();
			//将我的位置的地址信息储存在public变量addr_info中
			addr_info=location.getAddrStr();
			lat_loc=location.getLatitude();
			lon_loc=location.getLongitude();
		}
	}
  }

@Override
public void onGetGeoCodeResult(GeoCodeResult arg0) {
	
}

@Override
public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_SHORT).show();
		return;
	}
	Toast mytToast = Toast.makeText(MainActivity.this,result.getAddress() ,Toast.LENGTH_SHORT);
	addr_marker=result.getAddress().toString();
	mytToast.setGravity(Gravity.CENTER ,0 , 100 );
	mytToast.show();
}

  
  
  
}
