package com.ns.main.config;

public enum Entity {

	PERSON("person", 1.0),
	ORGANIZE("organize", 0.9),
	GPE("gpe", 0.8),
	STATE_OR_PROVINCE("province", 0.8),
	CITY("city", 0.8),
	CAUSE_OF_DEATH("event", 0.8),
	TIME("time", 0.6),
	DATE("date", 0.3);
	
	private String key;
	
	private double value;
	
	private Entity(String key, double value) {
		this.setKey(key);
		this.setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
}
