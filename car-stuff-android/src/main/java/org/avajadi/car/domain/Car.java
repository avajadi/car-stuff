package org.avajadi.car.domain;

public class Car {
	private String description;

	private int id;

	private String reg;

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public String getReg() {
		return reg;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", reg, description);
	}

}
