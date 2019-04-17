package com.ns.main.entity;

import org.apache.commons.lang3.StringUtils;

public class RawDocument {

	private String link;
	
	private String document;
	
	private String title;
	
	private String time;
	
	private String source;

	private String queryString;
	
	public RawDocument(String link, String title, String document, String time, String source, String queryString) {
		this.document = StringUtils.deleteWhitespace(document);
		this.time = time;
		this.link = link;
		this.title = title;
		this.source = source;
		this.queryString = queryString;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return link + "\r\n" + title + "\r\n" + time + "\r\n" + source + "\r\n" + document;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
}
