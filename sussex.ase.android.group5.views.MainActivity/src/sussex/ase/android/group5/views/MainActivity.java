package sussex.ase.android.group5.views;

import sussex.ase.android.group5.api.GooglePlaces;
import sussex.ase.android.group5.api.Login;
import sussex.ase.android.group5.service.GPSTracker;
import sussex.ase.android.group5.util.AlertDialogManager;
import com.androidhive.googleplacesandmaps.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;

	// GPS Location
	GPSTracker gps;

	// Progress dialog
	ProgressDialog pDialog;

	private LocationManager locationManager;

	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name

	// ///////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 0911 add --->
		ConnectivityManager connectivity = (ConnectivityManager) MainActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// Checking for internet connection
		if (!checkInternet(connectivity)) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}

		// creating GPS Class object
		gps = new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude()
					+ ", longitude:" + gps.getLongitude());
		} else {
			// Cannot get user's current location
			alert.showAlertDialog(MainActivity.this, "GPS Status",
					"Couldn't get location information. Please enable GPS",
					false);
			return;
		}
		// <--- 0911 add

		// <----------------------------------------haiming-----12/11/2012

		// <----------------------------------------haiming-----12/11/2012

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// locationManager= gps.locationManager;

		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				if (location != null) {
					Log.i("superMap", "locationchanged");
				}
				// TODO Auto-generated method stub

			}

			// @Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			// @Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			// @Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}
		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 5, locationListener);

		canButton();
		cnaButton();
		logInButton();
	}

	// 0711 add --->
	public void logInButton() {
		Button logButton = (Button) findViewById(R.id.button1);
		// login button click
		logButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				EditText userName = (EditText) findViewById(R.id.editText1);
				EditText passWord = (EditText) findViewById(R.id.editText2);
				final String uName = userName.getText().toString();
				final String pWord = passWord.getText().toString();

				Thread thread = new Thread(new Runnable() {
					public void run() {
						Login login = new Login();
						if (login.CheckLogin(uName, pWord)) {
							Intent intent = new Intent(getApplicationContext(),
									PlacesMapActivity.class);
							intent.putExtra("userlatitude",
									Double.toString(gps.getLatitude()));
							intent.putExtra("userlongitude",
									Double.toString(gps.getLongitude()));
							startActivity(intent);
						} 
					}
				});
				thread.start();

			}
		});
	}

	public void canButton() {
		// cancel button
		Button canButton = (Button) findViewById(R.id.button2);
		canButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish(); // go back to home
			}
		});
	}

	
	public void cnaButton() {
		// create new account button
		Button cnaButton = (Button) findViewById(R.id.button3);
		cnaButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
	/*
	 * Checking for all possible internet providers parameter
	 * ConnectivityManager connectivity
	 */
	private boolean checkInternet(ConnectivityManager connectivity) {

		// Checking for all possible internet providers
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}

		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}




