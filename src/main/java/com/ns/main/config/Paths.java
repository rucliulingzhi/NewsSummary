package com.ns.main.config;

public enum Paths {

	ROOT("root","C:\\DATA\\sts-bundle\\sts-3.9.7.RELEASE\\NewsSummary\\"),
	NLPROOT("nlproot","C:\\DATA\\sts-bundle\\sts-3.9.7.RELEASE\\NewsSummary\\data\\raw\\"),
	PEOPLECN("people.cn","http://search.people.com.cn/cnpeople/search.do?");
	
	private String key;
	
	private String value;
	
	private Paths(String key, String value) {
		this.setKey(key);
		this.setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
