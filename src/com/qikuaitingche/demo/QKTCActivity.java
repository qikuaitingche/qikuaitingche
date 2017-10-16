package com.qikuaitingche.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
//import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.System;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.model.inner.GeoPoint;

public class QKTCActivity extends Activity {

	BMapManager manager;
	MapView mapView;
	private TextView positionTextView;
	private LocationManager locationManager;
	private String provider;
	private BaiduMap baiduMap;
	boolean ifFrist = true;
	
	public static final int SHOW_LOCATION = 0;
	private BitmapDescriptor mMarker;
	SharedPreferences mSharedPreferences;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_qktc);
		mapView = (MapView) findViewById(R.id.mv);
		baiduMap = mapView.getMap();
		baiduMap.setMyLocationEnabled(true);
		baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(LocationMode.COMPASS, true, null));
//		LatLng point = new LatLng(Double.parseDouble("26.786177"),Double.parseDouble("119.260795"));
//		BitmapDescriptor bitmap =  BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
//		Bundle args = new Bundle(); //这个用于在所画的点上附加相关信息
//		args.putString("siteId", "y");
//		args.putString("siteName","n");
//		OverlayOptions option = new MarkerOptions().position(point).extraInfo(args).icon(bitmap);
//		baiduMap.addOverlay(option);
		
		
		positionTextView = (TextView) findViewById(R.id.position_text_view);
		locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
		provider = LocationManager.NETWORK_PROVIDER;
		
		
		locationManager = (LocationManager) getSystemService(Context.
		LOCATION_SERVICE);
		// 获取所有可用的位置提供器
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
		provider = LocationManager.GPS_PROVIDER;
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
		provider = LocationManager.NETWORK_PROVIDER;
		} else {
		// 当没有可用的位置提供器时，弹出Toast提示用户
		Toast.makeText(this, "No location provider to use",
		Toast.LENGTH_SHORT).show();
		return;
		}
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
		// 显示当前设备的位置信息
		showLocation(location);
		navigateTo(location);
		}
		locationManager.requestLocationUpdates(provider, 5000, 1,
		locationListener);
		
		//显示百度地图
	    
		// 模拟数据
        List<CameraLocation> cameraLocations = new ArrayList<CameraLocation>();
        cameraLocations.add(new CameraLocation(31.199604,121.555065,R.drawable.qktc_1,"英伦贵族东旅馆", "距离209米","我是说明1"));
        cameraLocations.add(new CameraLocation(31.19809,121.555856,R.drawable.qktc_1,"英伦贵族西旅馆", "距离209米","我是说明2"));
        cameraLocations.add(new CameraLocation(31.197728,121.555901,R.drawable.qktc_1,"英伦贵族南旅馆", "距离209米","我是说明3"));
        cameraLocations.add(new CameraLocation(31.199094,121.554095,R.drawable.qktc_1,"英伦贵族北旅馆", "距离209米","我是说明4"));
        cameraLocations.add(new CameraLocation(31.198052,121.554418,R.drawable.qktc_1,"英伦贵族南旅馆", "距离209米","我是说明5"));
        cameraLocations.add(new CameraLocation(31.199581,121.555954,R.drawable.qktc_1,"英伦贵族北旅馆", "距离209米","我是说明6"));
		
        initMarker(cameraLocations);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qktc, menu);
		return true;
	}
	
	LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		@Override
		public void onProviderEnabled(String provider) {
		}
		@Override
		public void onProviderDisabled(String provider) {
		}
		@Override
		public void onLocationChanged(Location location) {
		// 更新当前设备的位置信息
			showLocation(location);
			navigateTo(location);
		}
	};
	
//	private void showLocation(Location location) {
//		String currentPosition = "latitude is " + location.getLatitude() + "\n"
//		+ "longitude is " + location.getLongitude();
//		positionTextView.setText(currentPosition);
//		}
	
	private void navigateTo(Location location) {
        // 按照经纬度确定地图位置
        if (ifFrist) {
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // 移动到某经纬度
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomBy(5f);
            // 放大
            baiduMap.animateMapStatus(update);

            ifFrist = false;
        }
        // 显示个人位置图标
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData data = builder.build();
        baiduMap.setMyLocationData(data);
        
        /*baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //点击地图某个位置获取经纬度latLng.latitude、latLng.longitude
            	Toast.makeText(getApplicationContext(), "click",
            			Toast.LENGTH_SHORT).show();
            	Intent intent = new Intent(QKTCActivity.this,PersonalInfoActivity.class);
            	startActivity(intent);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                //点击地图上的poi图标获取描述信息：mapPoi.getName()，经纬度：mapPoi.getPosition()
                return false;
            }
        });*/
    }
	
	private void showLocation(final Location location) {
		new Thread(new Runnable() {
		@Override
		public void run() {
			try {
		        // 组装反向地理编码的接口地址
				StringBuilder url = new StringBuilder();
				
				url.append("http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=");
				url.append(location.getLatitude()).append(",");
				url.append(location.getLongitude());
				url.append("&output=json&pois=1&ak=AdTKh7XH1YZEIn9PBde9TD9zd1yZ4Nfb&mcode=84:D6:D3:83:84:5B:C0:CF:AB:E0:05:BF:67:76:43:BD:60:A2:24:EA;com.qikuaitingche.demo");
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url.toString());
				// 在请求消息头中指定语言，保证服务器会返回中文数据
				httpGet.addHeader("Accept-Language", "zh-CN");
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = httpResponse.getEntity();
					String response = EntityUtils.toString(entity,"utf-8");
					String tmp=response.substring(29, response.length()-1);
					JSONObject jsonObject = new JSONObject(tmp);
					
					// 获取results节点下的位置信息
//					JSONArray resultArray = jsonObject.getJSONArray("result");
					JSONObject resultObject = jsonObject.getJSONObject("result");
					if (resultObject != null) {
						// 取出格式化后的位置信息
						String address = resultObject.getString("formatted_address");
						Message message = new Message();
						message.what = SHOW_LOCATION;
						message.obj = address;
						handler.sendMessage(message);
					}
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			}).start();
		}
	
		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case SHOW_LOCATION:
						String currentPosition = (String) msg.obj;
						positionTextView.setText(currentPosition);
						break;
					default:
						break;
				}
			}
		};
		private TextView tv_name;
		private TextView tv_description;
		private View rl_camera_description;
		
		@Override
	    protected void onDestroy() {
	        // 释放资源
	        super.onDestroy();
	        if (locationManager != null) {
	            locationManager.removeUpdates(locationListener);
	        }

	        mapView.onDestroy();

	        baiduMap.setMyLocationEnabled(false);

	    }
		
		 /**
	     * 点击地图上的标记
	     * @param cameraLocations
	     */
	    private void initMarker(List<CameraLocation> cameraLocations) {
	        // TODO Auto-generated method stub
	        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.qktc_1);
	        baiduMap.clear();
	        LatLng latLng = null;
	        Marker marker = null;
	        OverlayOptions options;
	        
	        for(CameraLocation cameraLocation : cameraLocations) {
	            // 经纬度
	            latLng = new LatLng(cameraLocation.getLatitude(), cameraLocation.getLongtitude());

	            // 图标
	            options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
	            marker = (Marker) baiduMap.addOverlay(options);
	            Bundle bundle = new Bundle();
	            bundle.putSerializable("cameraLocation", cameraLocation);
	            marker.setExtraInfo(bundle);
	            
	            
	        }

	        // 定义地图状态(精确到50米)
	        MapStatus mMapStatus = new MapStatus.Builder().target(latLng).zoom(18).build();
	        // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
	        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
	        // 移到第四个位置
	        baiduMap.setMapStatus(mapStatusUpdate);

	        // 点击地图上的标记
	        baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

	            @Override
	            public boolean onMarkerClick(Marker marker) {
	                //Bundle bundle = marker.getExtraInfo();
	                //CameraLocation cameraLocation = (CameraLocation) bundle.getSerializable("cameraLocation");
	                
	                //rl_camera_description.findViewById(R.id.tv_description);//查找该相对布局里的控件，使用cameraLocation这个bean的信息为控件设置内容
	                //tv_name.setText(cameraLocation.getName());
	                //tv_description.setText(cameraLocation.getDescription());

	                InfoWindow infoWindow;
	                TextView textView = new TextView(getApplicationContext());
	                //textView.setBackgroundResource(R.drawable.qktc_1);
	                textView.setPadding(30, 20, 30, 50);
	                textView.setText("haha");
	                Intent intent = new Intent(QKTCActivity.this,OrderActivity.class);
	                startActivity(intent);
	                //textView.setTextColor(getResources().getColor(R.color.black));

	                final LatLng latLng = marker.getPosition();
	                Point point = baiduMap.getProjection().toScreenLocation(latLng);
	                point.y -= 160;
	                LatLng latLng2 = baiduMap.getProjection().fromScreenLocation(point);
	                infoWindow = new InfoWindow(textView, latLng2, 0);
	                // 以上可以替换为：infoWindow = new InfoWindow(textView, latLng, -160);

	                baiduMap.showInfoWindow(infoWindow);

	                //rl_camera_description.setVisibility(View.VISIBLE);
	                return true;
	            }
	        });
	    }

}
