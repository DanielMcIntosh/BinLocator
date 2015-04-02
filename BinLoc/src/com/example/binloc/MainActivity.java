package com.example.binloc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {
	ArrayList<Bin> bins = new ArrayList<Bin>();
	
	static Bin myLocation = new Bin(0,0,0B1000);

	private void loadBins() throws IOException
	{

		String str="";
		StringBuffer buf = new StringBuffer();			
		InputStream is = getResources().openRawResource(R.drawable.bins);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		if (is!=null) {							
			while ((str = reader.readLine()) != null) {	
				buf.append(str + "\n" );
			}				
		}		
		is.close();	
		
		
		BufferedReader binReader = null;
        try {
			Pattern p = Pattern.compile("(-?\\d+\\.?\\d*), (-?\\d+\\.?\\d*) types = (\\d)");
			binReader = new BufferedReader(new StringReader(buf.toString()));
			
			String s;
			while(!(s = binReader.readLine()).isEmpty())
			{
				Matcher m = p.matcher(s);
				double lat = Double.parseDouble(m.group(1)), lon = Double.parseDouble(m.group(2));
				int types = Integer.parseInt(m.group(3));
				bins.add(new Bin(lat, lon, types));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Bin [] getNearestFiveBins()
	{
		Bin [] closeBins = new Bin[5];
		List<Bin> tempBins = bins.subList(0, 1);
		for (int i = 1; i < 5; ++i)
		{
			for (int j = 0; j <= i; ++j)
			{
				if (j == i || (tempBins.get(j).compareTo(bins.get(i))) > 0)
				{
					tempBins.add(j, bins.get(i));
					break;
				}
			}
		}
		closeBins = tempBins.toArray(closeBins);
		
		/*for (int i = 5; i < bins.size(); ++i)
		{
			Bin curBin = bins.get(i);
			if (closeBins[closeBins.length-1].compareTo(curBin) > 0)
			{
				closeBins[4] = curBin;
				for (int j = closeBins.length-1; j > 0 && closeBins[j-1].compareTo(curBin) > 0; --j)
				{
					closeBins[j] = closeBins[j-1];
					closeBins[j-1] = curBin;
				}
			}
		}*/
		return closeBins;
	}	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUserLocation();
        try {
			loadBins();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //getNearestFiveBins();
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
				
				myLocation = new Bin(pLat, pLong, 0B1000);
				
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
