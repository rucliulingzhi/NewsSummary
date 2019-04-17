package com.ns.main.util;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HttpUtil {
	
	private static final String CUTWEBSITE = "http://corpus.zhonghuayuwen.org/CpsWParser.aspx";
	
	public static Document getDocumentByUrl(String url) {
		try {
	        Document document = Jsoup.connect(url).userAgent("Mozilla").timeout(10000)
	        		.ignoreContentType(true).get();
	        return document;
		} catch(Exception e) {
			System.out.println(url);
			e.printStackTrace();
			return null;
		}
	}
	//欺骗s.huanqiu.com,获取真实查询结果
	public static Document getDocumentByUrl_HQ(String url) {
		try {
	        Document document = Jsoup.connect(url)
	        		.header("Referer", "https://" + "www.baidu.com" + "/")
	        		.header("Host", getDomain(url))
	        		.userAgent("Mozilla/5.0")
	        		.timeout(10000)
	        		.ignoreContentType(true).get();
	        return document;
		} catch(Exception e) {
			System.out.println(url);
			e.printStackTrace();
			return null;
		}
	}
	
	public static Document getDocumentByUrl_GBK(String url) {
		try {
			System.out.println(url);
	        Document document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
	        return document;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> cutter(String content) {
		return Arrays.asList(Jsoup.parse(post(content)).select("#TBout").text().split("\r\n"));
	}
	
	private static String post(String content) {
		RestTemplate restTemplate = new RestTemplate();
		LinkedMultiValueMap<String, String> formData =new LinkedMultiValueMap<>();
		formData.add("__VIEWSTATE", "/wEPDwUKMTkxNjQxMjkxOGRk9/66aqWN3F0h8lvlZBxz3uN/OcjS8w7aTPcGVv1a3Jc=");
		formData.add("__VIEWSTATEGENERATOR", "B992DC97");
		formData.add("__EVENTVALIDATION", "/wEWBQKzsbS2CwK5lIXIBAKTmJvSBQK7q7GGCAKliMfhCycWhRFQfONu2k/cCxuzjQ7heJO8d2RWyCZOiS+faaOE");
		formData.add("TBin", content);
		formData.add("BT1", "自动分词&标注词性");
		formData.add("TBout", "");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<LinkedMultiValueMap<String, String>>(formData, headers);
		return restTemplate.exchange(CUTWEBSITE, HttpMethod.POST, entity, String.class).getBody();
	}
	
	public static String getDomain(String url) {
		if(null == url || url.length() == 0) {
			return "";
			//throw new Exception();
		}
		if(url.startsWith("http")) {
			return url.split("[/]")[2];
		}else{
			return url.split("[/]")[0];
		}
	}
}
