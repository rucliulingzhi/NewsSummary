package com.ns.main.entity;

public class Word {

	private String link;
	
	private String content;
	
	private double[] wordVec;
	
	private String type;
	
	private int count;

	public Word(String link, String content) {
		this.link = link;
		this.content = content.split("/")[0];
		this.type = content.split("/")[1];
		count = 1;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double[] getWordVec() {
		return wordVec;
	}

	public void setWordVec(double[] wordVec) {
		this.wordVec = wordVec;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void addCount() {
		count = count + 1;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		if(null == wordVec)return "!!!";
		return content;// + "/" + type + " " + wordVec.length;
	}
	
	@Override
	public boolean equals(Object object) {
		if(this == object)return true;
		if(null == object)return false;
		if(getClass() != object.getClass())return false;
		Word otherword = (Word)object;
		return otherword.content == content && otherword.type == type;
	}
	
}
