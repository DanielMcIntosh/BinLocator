package com.example.binloc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.binloc.R;
import com.example.binloc.MainActivity.mylocationlistener;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {
	File fBins = new File("bins.txt");
	ArrayList<PointF> bins = new ArrayList<PointF>();
	
	PointF myLocation = new PointF(0,0);

	private void loadBins()
	{
        BufferedReader binReader = null;
        try {
			Pattern p = Pattern.compile("(-?\\d+\\.?\\d*), (-?\\d+\\.?\\d*)");
			binReader = new BufferedReader(new FileReader(fBins));
			
			String s;
			while(!(s = binReader.readLine()).isEmpty())
			{
				Matcher m = p.matcher(s);
				double lat = Double.parseDouble(m.group(1)), lon = Double.parseDouble(m.group(2));
				bins.add(new PointF((float)lat, (float)lon));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * calculates the approximate distance within an error of 
	 * points should be in the form x = latitude, y = longitude
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public double getDist(PointF p1, PointF p2, int technique)
	{
		int R = 6371000; // metres
		double lat1 = Math.toRadians(p1.x), lat2 = Math.toRadians(p2.x);
		double deltaLat = Math.toRadians(lat2-lat1);
		double lon1 = Math.toRadians(p1.y), lon2 = Math.toRadians(p2.y);
		double deltaLon = Math.toRadians(-lon1);
		
		double d = 0;
		switch(technique){
			case 0: //pythagorean
			{
				double x = (lon2-lon1) * Math.cos((lat1+lat2)/2);
				double y = (lat2-lat1);
				d = Math.sqrt(x*x + y*y) * R;
				break;
			}
			case 1: //haversine
			{
				double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
				        Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
				double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
				d = R * c;
				break;
			}
			case 2: //Spherical Law of Cosines
			{
				d = Math.acos( Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2) * Math.cos(deltaLon) ) * R;
			}
		}
		return d;
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        updateUserLocation();
        loadBins();
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
    
    public void updateUserLocation(){
//    	 textLat = (TextView)findViewById(R.id.textLat);
//       textLong = (TextView)findViewById(R.id.textLong);
         
         LocationManager Lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
         LocationListener Ll = new mylocationlistener();
         Lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Ll);
    }
    
    class mylocationlistener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if(location != null){
				float pLong = (float)location.getLongitude();
				float pLat = (float)location.getLatitude();
				
				myLocation.set(pLat,pLong);
				
//				textLat.setText(Double.toString(pLat));
//				textLong.setText(Double.toString(pLong));
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onProviderDisabled(String provider) {}
    	
    }
    
}
