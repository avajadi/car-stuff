package org.avajadi.car.factory;

import org.avajadi.car.domain.Car;
import org.avajadi.json.JSONMapper;
import org.json.JSONException;
import org.json.JSONObject;

public class CarMapper implements JSONMapper<Car,JSONObject>{
	public Car mapFromJSON(JSONObject datum) throws JSONException {
		Car car = new Car();
		car.setId(datum.getInt("id"));
		car.setDescription(datum.getString("description"));
		car.setReg(datum.getString("reg"));
		return car;
	}

	@Override
	public JSONObject mapToJSON(Car object) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
