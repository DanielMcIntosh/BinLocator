package com.example.binloc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	Coordinate [] coords;
		
	private void loadBinData()
	{
		File fP = new File(".\\files/ParkBinLocations.kmz");
		File fS = new File(".\\files/StreetBinLocations.kmz");
		Kml kmlP = null;
		Kml kmlS = null;
		try {
			kmlP = Kml.unmarshalFromKmz(fP)[0];
			kmlS = Kml.unmarshalFromKmz(fS)[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Document docP = (Document) kmlP.getFeature();
		Document docS = (Document) kmlS.getFeature();
		Folder folderP = (Folder) docP.getFeature().get(0);
		Folder folderS = (Folder) docS.getFeature().get(0);
		List<Feature> placemarksP = folderP.getFeature();
		List<Feature> placemarksS = folderS.getFeature();
		coords = new Coordinate[placemarksP.size()+placemarksS.size()];
		for (ListIterator<Feature> i = placemarksP.listIterator(); i.hasNext();)
		{
			Placemark p = (Placemark) i.next();
			Point geo = (Point) p.getGeometry();
			coords[i.previousIndex()] = geo.getCoordinates().get(0);
		}
		for (ListIterator<Feature> i = placemarksS.listIterator(); i.hasNext();)
		{
			Placemark p = (Placemark)i.next();
			Point geo = (Point) p.getGeometry();
			coords[i.previousIndex()+placemarksP.size()] = geo.getCoordinates().get(0);
		}
	}
	
	private Coordinate getMyLoc()
	{
		/* temp for testing*/
		double maxLat = -180, minLat = 180, maxLong = -180, minLong = 180;
		for (int i = 0; i < coords.length; ++i)
		{
			maxLat = Math.max(maxLat, coords[i].getLatitude());
			minLat = Math.min(minLat, coords[i].getLatitude());
			maxLong = Math.max(maxLong, coords[i].getLongitude());
			minLong = Math.min(minLong, coords[i].getLongitude());
		}
		
		double myLat = (maxLat+minLat)/2, myLong = (maxLong+minLong)/2;
		return new Coordinate(myLat, myLong);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
