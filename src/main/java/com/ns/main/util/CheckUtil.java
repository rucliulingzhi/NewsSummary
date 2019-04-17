package com.ns.main.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ns.main.entity.Sentence;
import com.ns.main.entity.Word;

public class CheckUtil {
	
	public static final String[] NOTES = {
		"。", "！", "？"
	};
	
	private static final String[] MARKS = {
		"。", "！", "？"
	};
	/*
	 * 
	 * 
	 n	名词			nt	时间名词		nd	方位名词		nl	处所名词
	nh	人名			nhf	姓			nhg	名			ns	地名
	ni	机构名		nz	其他专名		v	动词			vd	趋向动词
	vl	联系动词		vu	能愿动词		a	形容词		f	区别词
	m	数词			q	量词			mq	数量结构		d	副词
	r	代词			p	介词			c	连词			u	助词
	e	叹词			o	拟声词		i	习用语		j	缩略语
	h	前接成分		k	后接成分		g	语素字		x	非语素字	
	w	标点符号		ws	非汉字字符		wu	其他符号

	 * */
	
	public static boolean isWordInQuery(Word word, List<Word> queryWords) {
		for(Word queryWord : queryWords) {
			if(word.getContent().equals(queryWord.getContent()))return true;
		}
		return false;
	}
	
	//判断这个词是不是一个句子的结尾符号
	public static boolean isWordAnEndPunc(String word) {
		return (word.equals("。") || word.equals("？") || word.equals("！"));
	}
	
	public static boolean isSelectedSentence(Sentence sentence) {
		return (sentence.getQueryValue() > 0.4 && sentence.getTitleValue() > 0.4 
				&& sentence.getContent().endsWith("。") && sentence.getOrder() != -1)
				&& !sentence.getContent().startsWith("…");
	}
	
	public static boolean isSentence(String sentence) {
		if(null == sentence)return false;
		sentence = sentence.replace("”", "");
		for(int i = 0; i < NOTES.length; i++) {
			if(sentence.endsWith(NOTES[i]))return true;
		}
		return false;
	}
	
	public static boolean isDateCn(String dateTime) {
		return (dateTime.contains("年") || dateTime.contains("月") || dateTime.contains("日"));
	}
	
	public static boolean isCruel(Sentence sentence) {
		String[] cruels = {
				"总而言之","总之","总的来说"
				//,"综上所述","结果是",""
				};
		for(int i = 0; i < cruels.length; i++) {
			if(sentence.getContent().contains(cruels[i])) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isParagraph(String para) {
		for(int i= 0; i < MARKS.length; i++) {
			if(para.replace("“", "").replace("”", "").endsWith(MARKS[i])) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNumber(String Num) {
		try {
			Integer.parseInt(Num);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isMeaningful(Word word) {
		Set<String> UNUSEWORD = new HashSet<String>();
		UNUSEWORD.add("nt");
		UNUSEWORD.add("f");
		UNUSEWORD.add("r");
		UNUSEWORD.add("p");
		UNUSEWORD.add("c");
		UNUSEWORD.add("d");
		UNUSEWORD.add("u");
		UNUSEWORD.add("e");
		UNUSEWORD.add("o");
		UNUSEWORD.add("x");
		UNUSEWORD.add("w");
		UNUSEWORD.add("wu");
		if(UNUSEWORD.contains(word.getType()))return false;
		//if(word.getContent().length() == 1)return false;
		return true;
	}
	
}
