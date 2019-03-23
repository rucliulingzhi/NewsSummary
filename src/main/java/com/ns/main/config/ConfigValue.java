package com.ns.main.config;

public enum ConfigValue {

	//first paragraph's value
	FIRST_PARA("first_para", 0.4),
	//first sentence's value
	FIRST_SENT("first_sent", 0.2),
	//second paragraph's value
	SECOND_PARA("second_para", 0.1),
	//Cruel value, not used
	CRUEL_VALUE("cruel_value", 1.0),
	//TF/IDF weight
	TFIDF_WEIGHT("tfidf_weight", 0.2),
	//position weight
	POSITION_WEIGHT("position_weight", 0.1),
	//title like weight
	TITLE_WEIGHT("title_like_weight", 0.3),
	//query like weight
	QUERY_WEIGHT("query_like_weight", 0.3),
	//cruel weight
	CRUEL_WEIGHT("cruel_weight", 0.1);
	
	private String name;
	
	private double value;
	
	private ConfigValue(String name, double value) {
		this.setName(name);
		this.setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
