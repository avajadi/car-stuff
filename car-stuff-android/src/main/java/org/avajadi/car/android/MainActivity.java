package org.avajadi.car.android;

import java.util.ArrayList;
import java.util.List;

import org.avajadi.car.domain.Car;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class MainActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;

	private List<Car> cars = new ArrayList<Car>();
	private LocationClient locationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Spinner carSpinner = (Spinner) findViewById(R.id.carSpinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<Car> adapter = new ArrayAdapter<Car>(this,
				android.R.layout.simple_spinner_dropdown_item, cars);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		carSpinner.setAdapter(adapter);
		new CarFetcher(this).execute();
		locationClient = new LocationClient(this, this, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		locationClient.connect();
	}

	@Override
	protected void onStop() {
		locationClient.disconnect();
		super.onStop();
	}

	@SuppressWarnings("unchecked")
	public void setCars(List<Car> cars) {
		if (cars == null || cars.isEmpty())
			return;
		this.cars.clear();
		this.cars.addAll(cars);
		((ArrayAdapter<Car>) ((Spinner) findViewById(R.id.carSpinner))
				.getAdapter()).notifyDataSetChanged();
	}

	public List<Car> getCars() {
		return cars;
	}

	public void report(View buttonClicked) {
		Spinner spinner = (Spinner) findViewById(R.id.carSpinner);
		int carId = ((Car) spinner.getItemAtPosition(spinner
				.getSelectedItemPosition())).getId();
		double litres = Double.valueOf(((EditText) findViewById(R.id.litres))
				.getText().toString());
		int kilometers = Integer
				.valueOf(((EditText) findViewById(R.id.kilometers)).getText()
						.toString());
		double sek = Double.valueOf(((EditText) findViewById(R.id.sek))
				.getText().toString());
		Location place = locationClient.getLastLocation();
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
		pb.setVisibility(ProgressBar.VISIBLE);
		new GasolineReporter(this, carId, litres, kilometers, sek, place)
				.execute();
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	void showErrorDialog(int code) {
		GooglePlayServicesUtil.getErrorDialog(code, this,
				REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
	}
}
