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
//		Bundle args = new Bundle(); //��������������ĵ��ϸ��������Ϣ
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
		// ��ȡ���п��õ�λ���ṩ��
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
		provider = LocationManager.GPS_PROVIDER;
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
		provider = LocationManager.NETWORK_PROVIDER;
		} else {
		// ��û�п��õ�λ���ṩ��ʱ������Toast��ʾ�û�
		Toast.makeText(this, "No location provider to use",
		Toast.LENGTH_SHORT).show();
		return;
		}
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
		// ��ʾ��ǰ�豸��λ����Ϣ
		showLocation(location);
		navigateTo(location);
		}
		locationManager.requestLocationUpdates(provider, 5000, 1,
		locationListener);
		
		//��ʾ�ٶȵ�ͼ
	    
		// ģ������
        List<CameraLocation> cameraLocations = new ArrayList<CameraLocation>();
        cameraLocations.add(new CameraLocation(31.199604,121.555065,R.drawable.qktc_1,"Ӣ�׹��嶫�ù�", "����209��","����˵��1"));
        cameraLocations.add(new CameraLocation(31.19809,121.555856,R.drawable.qktc_1,"Ӣ�׹������ù�", "����209��","����˵��2"));
        cameraLocations.add(new CameraLocation(31.197728,121.555901,R.drawable.qktc_1,"Ӣ�׹������ù�", "����209��","����˵��3"));
        cameraLocations.add(new CameraLocation(31.199094,121.554095,R.drawable.qktc_1,"Ӣ�׹��山�ù�", "����209��","����˵��4"));
        cameraLocations.add(new CameraLocation(31.198052,121.554418,R.drawable.qktc_1,"Ӣ�׹������ù�", "����209��","����˵��5"));
        cameraLocations.add(new CameraLocation(31.199581,121.555954,R.drawable.qktc_1,"Ӣ�׹��山�ù�", "����209��","����˵��6"));
		
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
		// ���µ�ǰ�豸��λ����Ϣ
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
        // ���վ�γ��ȷ����ͼλ��
        if (ifFrist) {
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // �ƶ���ĳ��γ��
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomBy(5f);
            // �Ŵ�
            baiduMap.animateMapStatus(update);

            ifFrist = false;
        }
        // ��ʾ����λ��ͼ��
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData data = builder.build();
        baiduMap.setMyLocationData(data);
        
        /*baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //�����ͼĳ��λ�û�ȡ��γ��latLng.latitude��latLng.longitude
            	Toast.makeText(getApplicationContext(), "click",
            			Toast.LENGTH_SHORT).show();
            	Intent intent = new Intent(QKTCActivity.this,PersonalInfoActivity.class);
            	startActivity(intent);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                //�����ͼ�ϵ�poiͼ���ȡ������Ϣ��mapPoi.getName()����γ�ȣ�mapPoi.getPosition()
                return false;
            }
        });*/
    }
	
	private void showLocation(final Location location) {
		new Thread(new Runnable() {
		@Override
		public void run() {
			try {
		        // ��װ����������Ľӿڵ�ַ
				StringBuilder url = new StringBuilder();
				
				url.append("http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=");
				url.append(location.getLatitude()).append(",");
				url.append(location.getLongitude());
				url.append("&output=json&pois=1&ak=AdTKh7XH1YZEIn9PBde9TD9zd1yZ4Nfb&mcode=84:D6:D3:83:84:5B:C0:CF:AB:E0:05:BF:67:76:43:BD:60:A2:24:EA;com.qikuaitingche.demo");
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url.toString());
				// ��������Ϣͷ��ָ�����ԣ���֤�������᷵����������
				httpGet.addHeader("Accept-Language", "zh-CN");
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = httpResponse.getEntity();
					String response = EntityUtils.toString(entity,"utf-8");
					String tmp=response.substring(29, response.length()-1);
					JSONObject jsonObject = new JSONObject(tmp);
					
					// ��ȡresults�ڵ��µ�λ����Ϣ
//					JSONArray resultArray = jsonObject.getJSONArray("result");
					JSONObject resultObject = jsonObject.getJSONObject("result");
					if (resultObject != null) {
						// ȡ����ʽ�����λ����Ϣ
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
	        // �ͷ���Դ
	        super.onDestroy();
	        if (locationManager != null) {
	            locationManager.removeUpdates(locationListener);
	        }

	        mapView.onDestroy();

	        baiduMap.setMyLocationEnabled(false);

	    }
		
		 /**
	     * �����ͼ�ϵı��
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
	            // ��γ��
	            latLng = new LatLng(cameraLocation.getLatitude(), cameraLocation.getLongtitude());

	            // ͼ��
	            options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
	            marker = (Marker) baiduMap.addOverlay(options);
	            Bundle bundle = new Bundle();
	            bundle.putSerializable("cameraLocation", cameraLocation);
	            marker.setExtraInfo(bundle);
	            
	            
	        }

	        // �����ͼ״̬(��ȷ��50��)
	        MapStatus mMapStatus = new MapStatus.Builder().target(latLng).zoom(18).build();
	        // ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
	        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
	        // �Ƶ����ĸ�λ��
	        baiduMap.setMapStatus(mapStatusUpdate);

	        // �����ͼ�ϵı��
	        baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

	            @Override
	            public boolean onMarkerClick(Marker marker) {
	                //Bundle bundle = marker.getExtraInfo();
	                //CameraLocation cameraLocation = (CameraLocation) bundle.getSerializable("cameraLocation");
	                
	                //rl_camera_description.findViewById(R.id.tv_description);//���Ҹ���Բ�����Ŀؼ���ʹ��cameraLocation���bean����ϢΪ�ؼ���������
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
	                // ���Ͽ����滻Ϊ��infoWindow = new InfoWindow(textView, latLng, -160);

	                baiduMap.showInfoWindow(infoWindow);

	                //rl_camera_description.setVisibility(View.VISIBLE);
	                return true;
	            }
	        });
	    }

}
