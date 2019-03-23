package com.ns.main.entity;

public class Paragraph {

	private String link;
	
	private String content;
	
	private String time;
	
	private int order;

	public Paragraph(String link, String content, String time, int order) {
		this.link = link;
		this.content = content;
		this.time = time;
		this.order = order;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return link + "\r\n" + order + "\r\n" + time + "\r\n" + content;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public int getOrder() {
		return order;
	}
	
}
