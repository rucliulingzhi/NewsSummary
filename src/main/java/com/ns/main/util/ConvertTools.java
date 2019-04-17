package com.ns.main.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ns.main.config.NewsSite;
import com.ns.main.config.SpiderFilter;
import com.ns.main.config.Value;
import com.ns.main.entity.RawDocument;
import com.ns.main.entity.Sentence;

public class ConvertTools {
	
	/**
	 * 查询关键字转网页链接
	 * @param query
	 * @return
	 */
	public static List<String> getLinksByQuery(String query){
		List<String> result = new ArrayList<String>();
		String query_gbk = query;
		String query_utf8 = query;
		try {
			query_gbk = URLEncoder.encode(query, "gbk");
			query_utf8 = URLEncoder.encode(query, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int count = 1;
 		while (result.size() < 10 && count < 10) {
			result.addAll(getSinaNews(query_gbk, count));
			count++;
		}
 		System.out.println(result.size());
 		count = 1;
 		while (result.size() < 30 && count < 10) {
			result.addAll(getHuanqiuNews(query_utf8, count));
			count++;
		}
 		count = 1;
 		System.out.println(result.size());
 		while (result.size() < 60 && count < 20) {
 			result.addAll(getXinhuaNews(query_utf8, count));
 			count++;
		}
 		System.out.println(result.size());
 		return result;
	}
	
	/**
	 * 环球网新闻获取
	 * @param query
	 * @param page
	 * @return
	 */
	private static List<String> getHuanqiuNews(String query, int page){
		String url = "http://s.huanqiu.com/s?q=" + query + "&p=" + page;
		List<String> result = new ArrayList<String>();
		result.addAll(HttpUtil.getDocumentByUrl_HQ(url).select("h3 a").stream()
				.map(T->T.attr("href")).filter(T->T.contains("html")).collect(Collectors.toList()));
		return result;
	}
	
	/**
	 * 新浪网新闻获取
	 * @param query
	 * @param page
	 * @return
	 */
	private static List<String> getSinaNews(String query, int page){
		String url = "http://search.sina.com.cn/?c=news&q=" + query 
				+ "&range=all&col=&source=&from=&country=&size=&time=&a=&sort=rel&pf=0&ps=0&dpc=1&page=" + page;
		List<String> result = new ArrayList<String>();
		result.addAll(HttpUtil.getDocumentByUrl_GBK(url).select("h2 a").stream().map(T->T.attr("href")).collect(Collectors.toList()));
		return result;
	}
	
	/**
	 * 新华网链接获取
	 * @param query
	 * @param page
	 * @return
	 */
	private static List<String> getXinhuaNews(String query, int page){
		String url = "http://so.news.cn/getNews?keyword=" + query
				+ "&curPage=" + page +"&sortField=1&searchFields=1&lang=cn";
		String[] news = HttpUtil.getDocumentByUrl(url).select("body").toString().split("\"url\":\"");
		List<String> result = new ArrayList<String>();
		for(int i = 1; i < news.length; i++) {
			String link = news[i].split("\"}")[0];
			if(link.contains("emerinfo.cn") || link.contains("xhwkhdapp") 
					|| link.contains("test.") || link.contains("mrdx") || !link.contains("xinhuanet")
					|| link.contains("video") || link.contains(".com/201")) {
				continue;
			}
			result.add(link);
		}
		return result;
	}
	
	/**
	 * 网页文档(html)转新闻文档
	 * @param document
	 * @param query
	 * @return
	 */
	public static RawDocument documentToRawDocument(Document document, String query) {
		if(null == document)return null;
		SpiderFilter spiderFilter = domainToSite(document.baseUri()).getSpiderFilter();
		StringBuilder doc = new StringBuilder();
		for(Element element : document.select(spiderFilter.getDocFilter())) {
			doc.append(element.text() + Value.LINE_FEED.getName());
		}
		return new RawDocument(
				document.baseUri(), 
				document.select(spiderFilter.getTitleFilter()).text(), 
				doc.toString(),
				document.select(spiderFilter.getTimeFilter()).text(), 
				document.select(spiderFilter.getSourceFilter()).text(),
				query);
	}
	
	/**
	 * 清除看不见的字符
	 * @param sentence
	 * @return
	 */
	public static String trim(String sentence) {
		return sentence.replaceAll("\\s*", "").trim();
	}
	
	/**
	 * 清除标记
	 * @param doc
	 * @return
	 */
	public static String cleanNote(String doc) {
		return doc.replace("“", "").replace("”", "").replace("‘", "").replace("’", "");
	}
	
	/**
	 * 新闻中的简称转换
	 * @param content
	 * @return
	 */
	public static String toFullName(String content) {
		Map<String, String> wordMap = new HashMap<String, String>();
		wordMap.put("中巴", "中国和巴斯坦");
		wordMap.put("中非", "中国和非洲各国");
		wordMap.put("中印", "中国和印度");
		wordMap.put("中俄", "中国和俄罗斯");
		wordMap.put("中韩", "中国和韩国");
		wordMap.put("中欧", "中国和欧洲各国");
		wordMap.put("中美", "中国和美国");
		wordMap.put("中英", "中国和英国");
		wordMap.put("中日", "中国和日本");
		wordMap.put("中德", "中国和德国");
		wordMap.put("中法", "中国和法国");
		wordMap.put("中越", "中国和越国");
		wordMap.put("中澳", "中国和澳大利亚");
		wordMap.put("中朝", "中国和朝鲜");
		wordMap.put("印尼", "印度尼西亚");
		wordMap.put("中希", "中国和希腊");
		wordMap.put("中匈", "中国和匈牙利");
		wordMap.put("中意", "中国和意大利");
		wordMap.put("中葡", "中国和葡萄牙");
		wordMap.put("中加", "中国和加拿大");
		wordMap.put("中墨", "中国和墨西哥");
		wordMap.put("中秘", "中国和秘鲁");
		wordMap.put("中巴", "中国和巴勒斯坦");
		wordMap.put("中朝", "中国和朝鲜");
		wordMap.put("中菲", "中国和菲律宾");
		wordMap.put("中格", "中国和格鲁吉亚");
		wordMap.put("中哈", "中国和哈萨克斯坦");
		wordMap.put("中韩", "中国和韩国");
		wordMap.put("中吉", "中国和吉尔吉斯斯坦");
		wordMap.put("中柬", "中国和柬埔寨");
		wordMap.put("中卡", "中国和卡塔尔");
		wordMap.put("中科", "中国和科威特");
		wordMap.put("中老", "中国和老挝");
		wordMap.put("中黎", "中国和黎巴嫩");
		wordMap.put("中蒙", "中国和蒙古");
		wordMap.put("中孟", "中国和孟加拉国");
		wordMap.put("中缅", "中国和缅甸");
		wordMap.put("中尼", "中国和尼泊尔");
		wordMap.put("中沙", "中国和沙特阿拉伯");
		wordMap.put("中斯", "中国和斯里兰卡");
		wordMap.put("中塔", "中国和塔吉克斯坦");
		wordMap.put("中泰", "中国和泰国");
		wordMap.put("中乌", "中国和乌兹别克斯坦");
		wordMap.put("中新", "中国和新加坡");
		wordMap.put("中叙", "中国和叙利亚");
		wordMap.put("中亚", "中国和亚美尼亚");
		wordMap.put("中也", "中国和也门");
		wordMap.put("中以", "中国和以色列");
		wordMap.put("中约", "中国和约旦");
		wordMap.put("印巴", "印度和巴基斯坦");
		for(Entry<String, String> word : wordMap.entrySet()) {
			if(content.contains(word.getKey())) {
				content = content.replace(word.getKey(), word.getValue());
			}
		}
		return content;
	}
	
	/**
	 * 按照域名匹配抓取方法
	 * @param link
	 * @return
	 */
	private static NewsSite domainToSite(String link) {
		switch (HttpUtil.getDomain(link)) {
		case "www.xinhuanet.com":
			return NewsSite.XINHUANET;
		case "m.xinhuanet.com":
			return NewsSite.MXINHUANET;
		case "news.sina.com.cn":
			return NewsSite.SINA;
		case "k.sina.com.cn":
			return NewsSite.KSINA;
		case "cj.sina.com.cn":
			return NewsSite.KSINA;
		case "military.people.com.cn":
			return NewsSite.MILITARYPEOPLE;
		case "health.people.com.cn":
			return NewsSite.HEALTHPEOPLE;
		case "china.huanqiu.com":
			return NewsSite.HUANQIUC;
		case "world.huanqiu.com":
			return NewsSite.HUANQIUW;
		case "society.huanqiu.com":
			return NewsSite.HUANQIUS;
		default:
			return NewsSite.PEOPLE;
		}
	}

	/**
	 * 数组转字符串(显示)
	 * @param array
	 * @return
	 */
	public static String printArray(double[] array) {
		String outString = "";
		for(int i =0; i < array.length; i++) {
			outString += "\r\n" + array[i];
		}
		return outString;
	}
	
	/**
	 * 句子集转字符串(显示)
	 * @param sentences
	 * @return
	 */
	public static String printOut(List<Sentence> sentences) {
		String outString = "";
		List<Sentence> ordered_sentences = sentences.stream().sorted(Sentence::compareToByTime).collect(Collectors.toList());
		for(int i = 0; i< ordered_sentences.size(); i++) {
			outString += ordered_sentences.get(i).getTime() + " " + ordered_sentences.get(i).getSource() 
					+ "\r\n" + ordered_sentences.get(i).getContent().replaceAll("\\s*", "").replace("    ", "") + "\r\n";
		}
		return outString;
	}
	
}
