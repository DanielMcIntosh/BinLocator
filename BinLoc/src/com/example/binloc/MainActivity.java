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

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	File fBins = new File(".\\bins.txt");
	ArrayList<PointF> bins = new ArrayList<PointF>();

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
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
}
