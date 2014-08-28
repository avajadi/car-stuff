package org.avajadi.car.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.avajadi.car.domain.Car;
import org.avajadi.car.factory.CarMapper;
import org.avajadi.json.ListMapper;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

public class CarFetcher extends AsyncTask<Void, Void, String> {
	private MainActivity main;

	public CarFetcher(MainActivity main) {
		this.setMain(main);
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

	@Override
	protected String doInBackground(Void... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet("http://api.skagelund.se/api/car/get");
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

	protected void onPostExecute(String results) {
		if (results != null) {
			List<Car> cars = null;
			try {
				cars = new ListMapper<Car>( new CarMapper() ).mapFromJSON(new JSONArray( results ));
			} catch (JSONException e) {
				//TODO Handle json parse exception
			}
			this.getMain().setCars(cars);
		}
	}

	public MainActivity getMain() {
		return main;
	}

	public void setMain(MainActivity main) {
		this.main = main;
	}
}
