package org.avajadi.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ListMapper<T> implements JSONMapper<List<T>,JSONArray>{
	private JSONMapper<T, JSONObject> itemMapper;
	private static final String TAG = ListMapper.class.getName();

	public ListMapper(JSONMapper<T,JSONObject> itemMapper ) {
		this.itemMapper = itemMapper;
	}
	
	public List<T> mapFromJSON(JSONArray data) {
		List<T> items = new ArrayList<T>();
		try {
			for (int i = 0; i < data.length(); i++) {
				JSONObject datum = data.getJSONObject(i);
				items.add(itemMapper.mapFromJSON( datum ));
			}
		} catch (JSONException e) {
			// TODO Handle misformed JSON
			Log.e(TAG, "JSONEXception: " + e.getMessage());
		}
		return items;
	}

	@Override
	public JSONArray mapToJSON(List<T> object) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}
