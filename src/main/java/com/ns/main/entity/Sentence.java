package com.ns.main.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ns.main.util.ConvertTools;
import com.ns.main.util.NLPUtil;

public class Sentence implements Comparable<Sentence>{

	private String link;
	
	private String source;
	
	private boolean isFirstPara;
	
	private boolean isFirstSent;
	//段落中的第几句
	private int order;
	//去除句子中人名前缀的内容
	private String content_cut;
	//内容
	private String content;
	//包含的词,有效词与无效词都包含在其中
	private List<Word> wordList;
	//有效词
	private List<Word> meanWordList;
	//句子时间
	private String time;
	/*句子各部分分值 
	0 -- TF/IDF --while single document summary,one document means a sentence
	1 -- Position
	2 -- Title Like
	3 -- Query Like
	4 -- Crul
	*/
	private double tfIdfValue;
	
	private double positionValue;
	
	private double titleValue;
	
	private double queryValue;
	
	private double cruelValue;
	
	private double[] values;
	
	private double result;
	
	public boolean updateSentence(Set<Word> meanSet) {
		if(order == 0)isFirstSent = true;
		meanWordList = new ArrayList<>();
		if(null == wordList || wordList.size() == 0)return false;
		Set<String> meanSetString = meanSet.stream().map(T->T.getContent()).collect(Collectors.toSet());
		for(Word word: wordList) {
			if(meanSetString.contains(word.getContent())){
				meanWordList.add(word);
			}
		}
		return true;
	}
	
	public Sentence(String link, boolean isFirstPara, int order, String content, String time, String source) {
		this.time = time;
		this.content = ConvertTools.trim(content);
		this.order = order;
		this.isFirstPara = isFirstPara;
		this.link = link;
		this.source = source;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isFirstPara() {
		return isFirstPara;
	}

	public void setFirstPara(boolean isFirstPara) {
		this.isFirstPara = isFirstPara;
	}

	public boolean isFirstSent() {
		return isFirstSent;
	}

	public void setFirstSent(boolean isFirstSent) {
		this.isFirstSent = isFirstSent;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Word> getWordList() {
		return wordList;
	}

	public void setWordList(List<Word> wordList) {
		this.wordList = wordList;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		String wordlist = "";
		if(null != wordList)
		for(Word word : meanWordList) {
			wordlist = wordlist + word.toString() + " ";
		}
		if(null == values)values = new double[5];
		return "link:" + link + "\r\nisFirstPara:" + isFirstPara +  "\r\nisFirstSent:" + isFirstSent + "\r\norder:" + order
				+ "\r\ntime:" + time + "\r\nwordlist:" + wordlist + "\r\nweight:" + ConvertTools.printArray(values) + "\r\n"
				+ content + "\r\n" + values[0] + "\r\n" + result;
	}

	public List<Word> getMeanWordList() {
		return meanWordList;
	}

	public void setMeanWordList(List<Word> meanWordList) {
		this.meanWordList = meanWordList;
	}

	public double getTfIdfValue() {
		return tfIdfValue;
	}

	public void setTfIdfValue(double tfIdfValue) {
		this.tfIdfValue = tfIdfValue;
	}

	public double getPositionValue() {
		return positionValue;
	}

	public void setPositionValue(double positionValue) {
		this.positionValue = positionValue;
	}

	public double getTitleValue() {
		return titleValue;
	}

	public void setTitleValue(double titleValue) {
		this.titleValue = titleValue;
	}

	public double getQueryValue() {
		return queryValue;
	}

	public void setQueryValue(double queryValue) {
		this.queryValue = queryValue;
	}

	public double getCruelValue() {
		return cruelValue;
	}

	public void setCruelValue(double cruelValue) {
		this.cruelValue = cruelValue;
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}
	
	@Override
	public int compareTo(Sentence sentence) {
		if(null == sentence || sentence.getResult() > result)return 1;
		return -1;
	}
	
	public int compareToByTime(Sentence sentence) {
		if(null == sentence || NLPUtil.getLongTime(sentence.getTime()) > NLPUtil.getLongTime(time))return 1;
		return -1;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContent_cut() {
		return content_cut;
	}

	public void setContent_cut(String content_cut) {
		this.content_cut = content_cut;
	}
	
}
