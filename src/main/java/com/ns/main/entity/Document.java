package com.ns.main.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ns.main.util.CheckUtil;
import com.ns.main.util.ConvertTools;
import com.ns.main.util.NLPUtil;

public class Document {

	//文档链接,视作doc的主键
	private String link;
	//文档正文,每一句是一个段落
	private List<String> document;
	//标题
	private String title;
	//规范化的标题"句"
	private Sentence titleSent;
	//查询语句
	private String queryString;
	//查询"句"
	private Sentence querySent;
	//时间
	private String time;
	//来源
	private String source;
	//段落,规范化后
	private List<Paragraph> paragraphs;
	//句子,规范化后
	private List<Sentence> sentences;
    //词列表,没有经过处理
	private List<Word> wordSetList;
	//词汇表,这是这个文档内有效词的集合(有效词指在模型中有词向量且没有被禁用的词)
	private Set<String> wordSet;
	//词汇表,包含了词向量的有效词集
	private Set<Word> meanWordSet;
	//归一化系数
	private double[] lambda;
	
	public boolean stringToWordSet() {
		if(null == wordSetList || null == wordSet)return false;
		meanWordSet = new HashSet<>();
		for(String string : wordSet) {
			for(Word word : wordSetList) {
				if(word.getContent().equals(string)) {
					meanWordSet.add(word);
					break;
				}
			}
		}
		if(meanWordSet.size() != wordSet.size())return false;
		return true;
	}
	
	public boolean listToSet() {
		if(null == wordSetList)return false;
		wordSet = wordSetList.stream().filter(T->CheckUtil.isMeaningful(T)).map(T->T.getContent()).collect(Collectors.toSet());
		return true;
	}
	
	public Document(RawDocument rawDocument) {
		this.document = Arrays.asList(rawDocument.getDocument().split("[\r\n]")).stream()
				.filter(T->CheckUtil.isParagraph(T))
				.map(T->ConvertTools.toFullName(T.replaceAll("&nbsp;", " ")))
				.collect(Collectors.toList());
		this.time = NLPUtil.getSentenceTime("", rawDocument.getTime());
		this.link = rawDocument.getLink();
		this.title = ConvertTools.toFullName(rawDocument.getTitle().replaceAll("&nbsp;", " "));
		this.source = rawDocument.getSource();
		this.queryString = ConvertTools.toFullName(rawDocument.getQueryString());
		if(null != document) {
			paragraphs = new ArrayList<>();
			for(int i = 0; i < document.size(); i++) {
				String content = ConvertTools.cleanNote(ConvertTools.trim(document.get(i)));
				paragraphs.add(new Paragraph(link, content, NLPUtil.getSentenceTime(NLPUtil.getSentences(content, null).get(0), time),i));
			}
		}
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<String> getDocument() {
		return document;
	}

	public void setDocument(List<String> document) {
		this.document = document;
	}
	
	@Override
	public String toString() {
		String documentString = "";
		for(String string : document) {
			documentString = documentString + ConvertTools.trim(string) + "\r\n";
		}
		if(null != paragraphs)
		for(Paragraph paragraph : paragraphs) {
			documentString = documentString + "\r\n" + paragraph.toString();
		}
		return link + "\r\n" + title + "\r\n" + time + "\r\n" + source + "\r\n" + documentString;
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	public List<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(List<Sentence> sentences) {
		this.sentences = sentences;
	}

	public List<Word> getWordSetList() {
		return wordSetList;
	}

	public void setWordSetList(List<Word> wordSetList) {
		this.wordSetList = wordSetList;
	}

	public Set<String> getWordSet() {
		return wordSet;
	}

	public void setWordSet(Set<String> wordSet) {
		this.wordSet = wordSet;
	}



	public Set<Word> getMeanWordSet() {
		return meanWordSet;
	}



	public void setMeanWordSet(Set<Word> meanWordSet) {
		this.meanWordSet = meanWordSet;
	}

	public Sentence getTitleSent() {
		return titleSent;
	}

	public void setTitleSent(Sentence titleSent) {
		this.titleSent = titleSent;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public Sentence getQuerySent() {
		return querySent;
	}

	public void setQuerySent(Sentence querySent) {
		this.querySent = querySent;
	}

	public double[] getLambda() {
		return lambda;
	}

	public void setLambda(double[] lambda) {
		this.lambda = lambda;
	}
	
}
