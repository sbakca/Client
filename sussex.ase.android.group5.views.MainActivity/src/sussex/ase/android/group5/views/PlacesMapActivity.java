package sussex.ase.android.group5.views;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.androidhive.googleplacesandmaps.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import sussex.ase.android.group5.api.GooglePlaces;
import sussex.ase.android.group5.models.Place;
import sussex.ase.android.group5.models.PlacesList;
import sussex.ase.android.group5.service.GPSTracker;
import sussex.ase.android.group5.util.AddItemizedCurrentOverlay;
import sussex.ase.android.group5.util.AddItemizedOverlay;
import sussex.ase.android.group5.util.AlertDialogManager;


public class PlacesMapActivity extends MapActivity {
	private PlacesList nearPlaces;
	private MapView mapView;
	private List<Overlay> mapOverlays;
	private ProgressDialog pDialog;
	private AlertDialogManager alert = new AlertDialogManager();
	private double userLatitude=0;
	private double userLongitude=0;
    private int intZoomLevel=13;
    private GPSTracker gps = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylayout);
        
		mapView =(MapView) findViewById(R.id.mapview);
        mapView.setClickable(true);	
		mapView.setBuiltInZoomControls(true);	
		mapView.setSatellite(false);
		if(CheckGPSTrackerService()==false)
		{
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
			return;
		}
		AddLocationTag();
		SetShowShopsButton();
	}
	private boolean CheckGPSTrackerService()
	{
		// creating GPS Class object
		 gps = new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude()
					+ ", longitude:" + gps.getLongitude());
			return true;
		} else {
			// Cannot get user's current location
			alert.showAlertDialog(PlacesMapActivity.this, "GPS Status",
					"Couldn't get location information. Please enable GPS",
					false);
			return false;
		}
	}
	private void UpdateLocation()
	{
		gps.getLocation();
		userLatitude = gps.getLatitude();
		userLongitude = gps.getLongitude();
	}
	private void AddLocationTag()
	{
	
		Calendar c=Calendar.getInstance();
		int mYear=c.get(Calendar.YEAR);
		int mMonth=c.get(Calendar.MONTH);
		int mDay=c.get(Calendar.DAY_OF_MONTH);
		int mHour=c.get(Calendar.HOUR_OF_DAY);
		int mMinute=c.get(Calendar.MINUTE);
		UpdateLocation();
		GeoPoint gp = new GeoPoint((int)(userLatitude * 1E6), (int) (userLongitude*1E6));
        MapController mapctrl = mapView.getController();
	    mapctrl.setCenter(gp);
	    refreshMapViewByGeoPoint(gp,mapView, intZoomLevel,false); 
        mapOverlays = mapView.getOverlays();
        mapOverlays.clear();
   		Address mAddr = getAddressByGeoPoint(PlacesMapActivity.this, gp);
   		String address = "cannot find the address of this location";
   		if(mAddr != null)
   		{
   			address = mAddr.getCountryName() + ","+mAddr.getLocality();
   		}
   		
		String text =	"latitude:" + userLatitude + "\n" +
						"longitude:" + userLongitude + "\n" +
						"Address:" + address + "\n" +
						"time:" + mYear + "/" + mMonth + "/" + mDay + "/" + mHour + "/" + mMinute;
		
		AddItemizedCurrentOverlay itemizedoverlayR = 
        		new AddItemizedCurrentOverlay(getResources().getDrawable(R.drawable.mark_red), PlacesMapActivity.this);
        OverlayItem overlayitem = new OverlayItem(gp, "My location", text); // put information of the location
        itemizedoverlayR.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlayR);
        itemizedoverlayR.populateNow();
		
	}

	private void AddShopsMark()
	{
        
		if(nearPlaces != null)
		{
			// Get json response status
			String status = nearPlaces.status;
			// Check for all possible status
			if(status.equals("OK")){
				// These values are used to get map boundary area
				// The area where you can see all the markers on screen
				int minLat = Integer.MAX_VALUE;
				int minLong = Integer.MAX_VALUE;
				int maxLat = Integer.MIN_VALUE;
				int maxLong = Integer.MIN_VALUE;

				AddItemizedOverlay itemizedoverlayB = 
		        		new AddItemizedOverlay(getResources().getDrawable(R.drawable.mark_blue), PlacesMapActivity.this);
				// loop through all the places
				for (Place place : nearPlaces.results)
				{
					double latitude = place.geometry.location.lat; // latitude
					double longitude = place.geometry.location.lng; // longitude
					
					// Geopoint to place on map
					GeoPoint geoPoint = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
					
					// Map overlay item
				    OverlayItem	overlayitem = new OverlayItem(geoPoint, place.name, place.vicinity);
					itemizedoverlayB.addOverlay(overlayitem);
					// calculating map boundary area
					minLat  = (int) Math.min( geoPoint.getLatitudeE6(), minLat );
				    minLong = (int) Math.min( geoPoint.getLongitudeE6(), minLong);
				    maxLat  = (int) Math.max( geoPoint.getLatitudeE6(), maxLat );
				    maxLong = (int) Math.max( geoPoint.getLongitudeE6(), maxLong );
				}
				mapOverlays.add(itemizedoverlayB);
				// showing all overlay items
				itemizedoverlayB.populateNow();
				// Adjusting the zoom level so that you can see all the markers on map
				mapView.getController().zoomToSpan(Math.abs( minLat - maxLat ), Math.abs( minLong - maxLong ));
				
			}
			else if(status.equals("ZERO_RESULTS")){
				// Zero results found
				alert.showAlertDialog(PlacesMapActivity.this, "Near Places",
						"Sorry no places found. Try to change the types of places",
						false);
			}
			else if(status.equals("UNKNOWN_ERROR"))
			{
				alert.showAlertDialog(PlacesMapActivity.this, "Places Error",
						"Sorry unknown error occured.",
						false);
			}
			else if(status.equals("OVER_QUERY_LIMIT"))
			{
				alert.showAlertDialog(PlacesMapActivity.this, "Places Error",
						"Sorry query limit to google places is reached",
						false);
			}
			else if(status.equals("REQUEST_DENIED"))
			{
				alert.showAlertDialog(PlacesMapActivity.this, "Places Error",
						"Sorry error occured. Request is denied",
						false);
			}
			else if(status.equals("INVALID_REQUEST"))
			{
				alert.showAlertDialog(PlacesMapActivity.this, "Places Error",
						"Sorry error occured. Invalid Request",
						false);
			}
			else
			{
				alert.showAlertDialog(PlacesMapActivity.this, "Places Error",
						"Sorry error occured.",
						false);
			}
		}
	}
	private void SetShowShopsButton()
	{
		
		Button getAddress = (Button) findViewById(R.id.button1);
		getAddress.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				AddLocationTag();
				new LoadPlaces().execute();
			}
		});
	}


    public void refreshMapViewByGeoPoint(GeoPoint gp,MapView mv, int zoomLevel,boolean bIfSatellite )
    {
    	try
    	{
    		mv.displayZoomControls(true);
    		MapController mc=mv.getController();
    		mc.animateTo(gp);
    		mc.setZoom(17);
    		if(bIfSatellite)
    		{
    			mv.setSatellite(true);
    		}
    		else
    		{
    			mv.setSatellite(false);
    		}
    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
  
  public Address getAddressByGeoPoint(Context context,GeoPoint gp)
    {
    	Address address=null;
    	
    	try
    	{
    		if(gp!=null)
    		{
    			Geocoder gc=new Geocoder(context,Locale.US);
    			double geoLatitude=(int) gp.getLatitudeE6()/1E6;
    			double geoLongtitude=(int) gp.getLongitudeE6()/1E6;
    			List<Address>lstAddress=gc.getFromLocation(geoLatitude,geoLongtitude,1);
    			if(lstAddress.size()>0)
    			{
    				address=lstAddress.get(0);
    			}
    
    		}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return address;
    }
    

	class LoadPlaces extends AsyncTask<String, String, String> {

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
		@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(PlacesMapActivity.this);
		pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}

	/**
	 * getting Places JSON
	 * */
	protected String doInBackground(String... args) {
		// creating Places class object
		GooglePlaces googlePlaces = new GooglePlaces();
		
		try {
			
			// Separeate your place types by PIPE symbol "|"
			// If you want all types places make it as null
			// Check list of types supported by google
			// 
			String types = "cafe|restaurant"; ; // Listing places only cafes, restaurants
			
			// Radius in meters - increase this value if you don't find any places
			double radius = 10000; // 1000 meters 
			UpdateLocation();
			// get nearest places
			nearPlaces = googlePlaces.search(userLatitude, userLongitude, radius, types);
			AddShopsMark();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * and show the data in UI
	 * Always use runOnUiThread(new Runnable()) to update UI from background
	 * thread, otherwise you will get error
	 * **/
	protected void onPostExecute(String file_url) {
		// dismiss the dialog after getting all products
		pDialog.dismiss();
		// updating UI from Background Thread
		runOnUiThread(new Runnable() {
			public void run() {
				/**
				 * Updating parsed Places into LISTVIEW
				 * */
				if(nearPlaces == null)
				{
					alert.showAlertDialog(PlacesMapActivity.this, "Connection error", "Could not get data.", false);
				}
				pDialog.cancel();
			}
		});

	}

}
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
