package org.avajadi.json;

import org.json.JSONException;

public interface JSONMapper<T,S> {

	public T mapFromJSON(S datum) throws JSONException;
	public S mapToJSON( T object) throws JSONException;
}
