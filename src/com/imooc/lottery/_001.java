package com.imooc.lottery;

import com.imooc.lottery.MainActivity.MyLocationListener;
//import com.imooc.lottery.MainActivity.OnClickListenerlmpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class _001 extends Activity {
	private Context context;	//初始化一个全局变量
	
	private Button btBack;
//	private ImageView iv001;
	private TextView tv001;
	private TextView tvphone;
	private TextView tvAddr;
	private TextView tvMyAddr;
	
	public double loc_lat;
	public double loc_lon;
	public double marker_lat;
	public double marker_lon;
	
	//调用拨号服务
	private ImageButton imgbt_call;
	
	//Intent"路径规划"页面跳转相关
	private Button btRoute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xml001);
		this.context=this;
		
		btBack=(Button) findViewById(R.id.button_map_back);
//		iv001=(ImageView) findViewById(R.id.imageView001);
		tv001=(TextView) findViewById(R.id.textView001);
		tvphone=(TextView) findViewById(R.id.phone_num);
		tvAddr=(TextView) findViewById(R.id.address);
		tvMyAddr=(TextView) findViewById(R.id.my_addr);
		
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Intent itIntent=getIntent();
		
		  Bundle bundle=this.getIntent().getExtras();
		  //获取Bundle的信息
		  String name_info=bundle.getString("name");
		  String phonenum_info=bundle.getString("phonenum");
		  String my_addr_info=bundle.getString("myaddr");
		  String marker_addr_info=bundle.getString("addr_marker");
		  loc_lat=bundle.getDouble("st_lat");
		  loc_lon=bundle.getDouble("st_lon");
		  marker_lat=bundle.getDouble("en_lat");
		  marker_lon=bundle.getDouble("en_lon");
		  tv001.setText(name_info);
		  tvphone.setText(phonenum_info);
//		  int info2=bundle.getInt("id");
//		  iv001.setImageResource(img_info);
		  tvAddr.setText(marker_addr_info);
		  tvMyAddr.setText(my_addr_info);
		  

		  
		  //调用拨号服务
			imgbt_call=(ImageButton) findViewById(R.id.imgbt_call);
			imgbt_call.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String telStr=(String) tvphone.getText();
					Uri uri=Uri.parse("tel:"+telStr);
					Intent it=new Intent();
					it.setAction(Intent.ACTION_DIAL);
					it.setData(uri);
					startActivity(it);
				}
			});
	
			
			//调启路径规划功能界面
	        btRoute=(Button) findViewById(R.id.bt_route);
	        btRoute.setOnClickListener(new OnClickListenerlmpl());  
			
	}
	
	private class OnClickListenerlmpl implements OnClickListener{
		@Override
		public void onClick(View v) {
			TextView tvmarker=(TextView) findViewById(R.id.address);
			Intent intent=new Intent(context, RoutePlanDemo.class);
			Bundle bundle = new Bundle();	
		    String addr_marker_info=tvmarker.getText().toString();
		    String addr_my_info=tvMyAddr.getText().toString();;
		    
		    
		    //保存输入的信息
		    bundle.putString("my_addr", addr_my_info);
		    bundle.putString("addr_marker",addr_marker_info);
		    bundle.putDouble("loc_lat",loc_lat);
		    bundle.putDouble("loc_lon",loc_lon);
		    bundle.putDouble("marker_lat",marker_lat);
		    bundle.putDouble("marker_lon",marker_lon);
		    intent.putExtras(bundle);					
			startActivity(intent);
		}
	}
	
	
}
		

