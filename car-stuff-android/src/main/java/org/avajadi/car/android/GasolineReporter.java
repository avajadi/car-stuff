package org.avajadi.car.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.location.Location;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;


public class GasolineReporter extends AsyncTask<Void, Void, String> {
	private int carId;
	private double litres;
	private int kilometers;
	private double sek;
	private Location place;
	private MainActivity mainActivity;

	/**
	 * Create a new GasolineReporter
	 * @param mainActivity 
	 * @param carId
	 * @param litres
	 * @param kilometers
	 * @param sek
	 */
	public GasolineReporter(MainActivity mainActivity, int carId, double litres, int kilometers, double sek, Location place) {
		this.mainActivity = mainActivity;
		this.carId = carId;
		this.litres = litres;
		this.kilometers = kilometers;
		this.sek = sek;
		this.place = place;
	}

	private String getURL() {
		return String.format(Locale.getDefault(),"http://api.skagelund.se/api/car/put/%d/%.2f/%d/%.2f/%.6f/%.6f", carId, litres, kilometers, sek, place.getLongitude(), place.getLatitude());
	}
	
	@Override
	protected String doInBackground(Void... arg0) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(getURL());
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
		return text;
	}

	protected String getASCIIContentFromEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		InputStream in = entity.getContent();
		StringBuilder out = new StringBuilder();
		int n = 1;
		while (n > 0) {
			byte[] b = new byte[4096];
			n = in.read(b);
			if (n > 0)
				out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public int getCarId() {
		return carId;
	}

	public double getLitres() {
		return litres;
	}

	public int getKilometers() {
		return kilometers;
	}

	public double getSek() {
		return sek;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public void setLitres(double litres) {
		this.litres = litres;
	}

	public void setKilometers(int kilometers) {
		this.kilometers = kilometers;
	}

	public void setSek(double sek) {
		this.sek = sek;
	}

	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(mainActivity, R.string.saved, Toast.LENGTH_SHORT).show();
		((ProgressBar)mainActivity.findViewById(R.id.progressBar)).setVisibility(ProgressBar.INVISIBLE);
	}
}
