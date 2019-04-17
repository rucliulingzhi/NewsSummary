package com.ns.main.config;

public enum Value {

	PERIOD("。", -1),
	EXCLAMATORY("！", -2),
	INTERROGATION("？", -3),
	ELLIPSIS("...", -4),
	BLANK("", 0),
	DOC_SEPARATOR("\r\n\r\n", 1),
	PARA_SEPARATOR("@PARA", 2),
	SENT_SEPARATOR("\t", 3),
	LINE_FEED("\r\n", 4),
	SEPARATOR("/", 5),
	
	//first paragraph's value
	FIRST_PARA("first_para", 0.4),
	//first sentence's value
	FIRST_SENT("first_sent", 0.2),
	//second paragraph's value
	SECOND_PARA("second_para", 0.1),
	//Entity value
	ENTITY_VALUE("entity_value", 1.0),
	
	//TF/IDF weight
	TFIDF_WEIGHT("tfidf_weight", 0.066),
	//position weight
	POSITION_WEIGHT("position_weight", 0.197),
	//title like weight
	TITLE_WEIGHT("title_like_weight", 0.516),
	//query like weight
	QUERY_WEIGHT("query_like_weight", 0.221),
	//entity weight
	ENTITY_WEIGHT("entity_weight", 0.0),
	
	//TF/IDF weight -- final
	TFIDF_WEIGHT_F("tfidf_weight", 0.312),
	//position weight -final
	POSITION_WEIGHT_F("position_weight", 0.036),
	//title like weight -final
	TITLE_WEIGHT_F("title_like_weight", 0.106),
	//query like weight -final
	QUERY_WEIGHT_F("query_like_weight", 0.106),
	//entity weight -final
	ENTITY_WEIGHT_F("entity_weight", 0.440);
	
	private String name;
	
	private double value;
	
	private Value(String name, double value) {
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
