package com.ns.main.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.deeplearning4j.models.word2vec.Word2Vec;

import com.ns.main.config.ConfigValue;
import com.ns.main.entity.DateTime;
import com.ns.main.entity.Document;
import com.ns.main.entity.Sentence;
import com.ns.main.entity.Word;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class NLPUtil {
	
	private static final double[] ALPHA = {
		ConfigValue.TFIDF_WEIGHT.getValue(),
		ConfigValue.POSITION_WEIGHT.getValue(),
		ConfigValue.TITLE_WEIGHT.getValue(), 
		ConfigValue.QUERY_WEIGHT.getValue(), 
		ConfigValue.CRUEL_WEIGHT.getValue()
	};
	
	public static Word2Vec word2Vec = ModelUtil.readModel("");
	
	public static StanfordCoreNLP pipeline = ModelUtil.getPropNlp();
	
	public static List<String> getPerson(List<String> sentenceList, StanfordCoreNLP pipeline) {
		List<String> personList = new ArrayList<String>();
		Map<Integer, String> personMap = new HashMap<Integer, String>();
		String text = "";
		for(int i = 0; i < sentenceList.size(); i++) {
			text = text + sentenceList.get(i) + "\r\n";
		}
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        int counter = 0;
        for (int i = 0; i < sentences.size(); i++) {
        	List<CoreLabel> tokens = sentences.get(i).get(CoreAnnotations.TokensAnnotation.class);
        	for (int j = 0; j < tokens.size(); j++) {
        		String word = tokens.get(j).get(CoreAnnotations.TextAnnotation.class);
        		if(CheckUtil.isWordAnEndPunc(word))counter++;
        		String ner = tokens.get(j).get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if(ner.equals("PERSON") && !personMap.containsKey(counter)) {
                	personMap.put(counter, word);
                }
            }
        }
        for(int i = 0; i < sentenceList.size(); i++) {
        	personList.add("");
        }
        for(Entry<Integer, String> entry : personMap.entrySet()) {
        	personList.set(entry.getKey(), entry.getValue());
        }
        return personList;
	}
	
	private static double vecCos(double[] a, double[] b) {
		if(null == a || null == b || a.length != b.length) {
			return 0;
		}
		double aa = 0;
		double bb = 0;
		double cc = 0;
		for(int i = 0; i < a.length; i++) {
			aa = aa + a[i] * a[i];
			bb = bb + b[i] * b[i];
			cc = cc + a[i] * b[i];
		}
		aa = Math.sqrt(aa * bb);
		return cc / aa;
	}
	
	private static double wordSim(Word a, Word b) {
		if(null == a || null == b || null == a.getWordVec() || null == b.getWordVec())return 0;
		return vecCos(a.getWordVec(), b.getWordVec());
	}
	
	public static double sentSimMain(Sentence main, Sentence compare) {
		if(null == main || null == compare || null == main.getMeanWordList() || null == compare.getMeanWordList())return 0;
		int size = main.getMeanWordList().size() + main.getMeanWordList().size();
		if(size == 0)return 0;
		size = main.getMeanWordList().size();
		double maxSumA = 0;
		for(Word wordA : main.getMeanWordList()) {
			double max = 0;
			for(Word wordB : compare.getMeanWordList()) {
				double wordSim = wordSim(wordA, wordB);
				if(wordSim > max) {
					max = wordSim;
				}
			}
			maxSumA += max;
		}
		return maxSumA / size;
	}
	
	public static long getLongTime(String time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return simpleDateFormat.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static double sentSim(Sentence a, Sentence b) {
		if(null == a || null == b || null == a.getMeanWordList() || null == b.getMeanWordList())return 0;
		int size = a.getMeanWordList().size() + b.getMeanWordList().size();
		if(size == 0)return 0;
		double maxSumA = 0;
		for(Word wordA : a.getMeanWordList()) {
			double max = 0;
			for(Word wordB : b.getMeanWordList()) {
				double wordSim = wordSim(wordA, wordB);
				if(wordSim > max) {
					max = wordSim;
				}
			}
			maxSumA += max;
		}
		double maxSumB = 0;
		for(Word wordB : b.getMeanWordList()) {
			double max = 0;
			for(Word wordA : a.getMeanWordList()) {
				double wordSim = wordSim(wordA, wordB);
				if(wordSim > max) {
					max = wordSim;
				}
			}
			maxSumB += max;
		}
		return (maxSumA + maxSumB) / size;
	}
	
	public static List<String> getSentences(String paragraph, List<String> Notes){
		List<String> notes = Notes;
		if(null == notes) {
			notes = Arrays.asList(CheckUtil.NOTES);
		}
		List<String> sentences = new ArrayList<>();
		sentences.add(paragraph);
		for(String note : notes) {
			sentences = getSentences(sentences, note);
		}
		return sentences;
	}
	
	private static List<String> getSentences(List<String> input, String note){
		List<String> sentences = new ArrayList<>();
		for(String string : input) {
			if(string.contains(note)) {
				String[] cuts = string.split(note);
				for(int i = 0; i < cuts.length - 1; i++) {
					
					sentences.add(cuts[i] + note);
				}
				if(string.endsWith(note)) {
					sentences.add(cuts[cuts.length - 1] + note) ;
				}
				else {
					sentences.add(cuts[cuts.length - 1]);
				}
			}else {
				sentences.add(string);
			}
		}
		return sentences;
	}
	
	//将时间格式化为yyyy-mm-dd hh:mm:ss的形式
	public static String formatTime(String dateTime) {
		if(CheckUtil.isDateCn(dateTime)) {
			dateTime = dateTime.replace("年", "-").replace("月", "-").replace("日", " ");
		}
		String date = dateTime.split(" ")[0];
		String hour = "00";
		String min = "00";
		if(dateTime.contains(":") && CheckUtil.isNumber("" + dateTime.charAt(dateTime.indexOf(":") - 1))
				&& CheckUtil.isNumber("" + dateTime.charAt(dateTime.indexOf(":") + 1))
				&& CheckUtil.isNumber("" + dateTime.charAt(dateTime.indexOf(":") + 2))) {
			if(CheckUtil.isNumber("" + dateTime.charAt(dateTime.indexOf(":") - 2))){
				hour = dateTime.substring(dateTime.indexOf(":") - 2,dateTime.indexOf(":"));
				min = dateTime.substring(dateTime.indexOf(":") + 1,dateTime.indexOf(":") + 3);
			}else {
				hour = dateTime.substring(dateTime.indexOf(":") - 1,dateTime.indexOf(":"));
				min = dateTime.substring(dateTime.indexOf(":") + 1,dateTime.indexOf(":") + 3);
			}
		}
		return date + " " + hour + ":" + min + ":" + "00";
	}
	
	public static DateTime getTimeTest(String sentence, String dateTime) {
		if(sentence.equals("") && dateTime.equals(""))return new DateTime();
		Calendar calendar = Calendar.getInstance();
		DateTime sentTime = new DateTime(dateTime);
		String temp = "";
		Pattern pdate = Pattern.compile("\\d+\u65e5");
		Matcher mdate = pdate.matcher(sentence);
		Pattern pdate2 = Pattern.compile("\\d+\u53f7");
		Matcher mdate2 = pdate2.matcher(sentence);
		if(mdate.find()) {
			temp = mdate.group();
			if(Integer.parseInt(temp.substring(0, temp.length() - 1)) > sentTime.getDate()) {
				calendar.setTimeInMillis(sentTime.getBase());
				calendar.add(Calendar.MONTH, -1);
				sentTime.setBase(calendar.getTimeInMillis());
			}
			sentTime.setDate(Integer.parseInt(temp.substring(0, temp.length() - 1)));
		}
		else if(mdate2.find()) {
			temp = mdate2.group();
			if(Integer.parseInt(temp.substring(0, temp.length() - 1)) > sentTime.getDate()) {
				calendar.setTimeInMillis(sentTime.getBase());
				calendar.add(Calendar.MONTH, -1);
				sentTime.setBase(calendar.getTimeInMillis());
			}
			sentTime.setDate(Integer.parseInt(temp.substring(0, temp.length() - 1)));
		}
		else if(sentence.contains("昨天") || sentence.contains("昨日")){
			sentTime.setBase(sentTime.getBase() - 24 * 3600 * 1000);
		}
		else if(sentence.contains("明天") || sentence.contains("明日")){
			sentTime.setBase(sentTime.getBase() + 24 * 3600 * 1000);
		}
		Pattern pyear = Pattern.compile("\\d+\u5e74");
		Matcher myear = pyear.matcher(sentence);
		if(myear.find()) {
			temp = myear.group();
			sentTime.setYear(Integer.parseInt(temp.substring(0, temp.length() - 1)));
		}
		else if(sentence.contains("去年")){
			calendar.setTimeInMillis(sentTime.getBase());
			calendar.add(Calendar.YEAR, -1);
			sentTime.setBase(calendar.getTimeInMillis());
		}
		else if(sentence.contains("明年")){
			calendar.setTimeInMillis(sentTime.getBase());
			calendar.add(Calendar.YEAR, 1);
			sentTime.setBase(calendar.getTimeInMillis());
		}
		Pattern pmonth = Pattern.compile("\\d+\u6708");
		Matcher mmonth = pmonth.matcher(sentence);
		if(mmonth.find()) {
			temp = mmonth.group();
			sentTime.setMonth(Integer.parseInt(temp.substring(0, temp.length() - 1)) - 1);
		}
		else if(sentence.contains("上个月")){
			calendar.setTimeInMillis(sentTime.getBase());
			calendar.add(Calendar.MONTH, -1);
			sentTime.setBase(calendar.getTimeInMillis());
		}
		else if(sentence.contains("下个月")){
			calendar.setTimeInMillis(sentTime.getBase());
			calendar.add(Calendar.MONTH, 1);
			sentTime.setBase(calendar.getTimeInMillis());
		}
		Pattern pmin = Pattern.compile("\\d+\u5206");
		Matcher mmin = pmin.matcher(sentence);
		if(mmin.find()) {
			temp = mmin.group();
			sentTime.setMinute(Integer.parseInt(temp.substring(0, temp.length() - 1)));
		}
		Pattern phour = Pattern.compile("\\d+\u65f6");
		Matcher mhour = phour.matcher(sentence);
		if(mhour.find()) {
			temp = mhour.group();
			int hour = Integer.parseInt(temp.substring(0, temp.length() - 1));
			if(sentence.contains("日晚") || sentence.contains("号晚")) {
				hour += 12;
			}
			sentTime.setHour(hour);
		}
		return sentTime;
	}
	
	public static String getSentenceTime(String sentence, String dateTime) {
		return getTimeTest(sentence, formatTime(dateTime)).toString();
	}

	public static double getTfValueFromSentence(Sentence sentence, Word word) {
		double allWords = sentence.getMeanWordList().size();
		int selectedWord = 0;
		for(String oneWord : sentence.getMeanWordList().stream().map(T->T.getContent()).collect(Collectors.toList())) {
			if(oneWord.equals(word.getContent()))selectedWord++;
		}
		return selectedWord / allWords;
	}
	
	public static double getIdfValueFromSentence(List<Sentence> sentences, Word word) {
		int N_D = sentences.size();
		int N_C = 0;
		for(Sentence sentence :sentences) {
			if(sentence.getMeanWordList().stream().map(T->T.getContent()).collect(Collectors.toList()).contains(word.getContent())) {
				N_C ++;
			}
		}
		return Math.log(N_D) - Math.log(N_C);
	}
	
	public static double calcuResult(double[] values) {
		if(null == values || values.length != 5) return 0;
		double result = 0;
		for(int i = 0; i < 5; i++) {
			result += values[i] * ALPHA[i];
		}
		return result;
	}
	
	private static double getTfValueFromDocument(Document document, Word word) {
		double allWords = document.getMeanWordSet().size();
		int selectedWord = 0;
		for(String oneWord : document.getMeanWordSet().stream().map(T->T.getContent()).collect(Collectors.toList())) {
			if(oneWord.equals(word.getContent()))selectedWord++;
		}
		return selectedWord / allWords;
	}
	
	private static double getIdfValueFromDocument(List<Document> documents, Word word) {
		int N_D = documents.size();
		int N_C = 0;
		for(Document document :documents) {
			if(document.getDocument().toString().contains(word.getContent())) {
				N_C ++;
			}
		}
		return Math.log(N_D) - Math.log(N_C);
	}
	
	public static List<Sentence> MMR(double lambda, List<Sentence> sentences, List<Document> documents) {
		if(lambda <= 0.05 || lambda >= 0.95)lambda = 0.5;
		Set<Sentence> sentenceSet = sentences.stream().collect(Collectors.toSet());
		Set<Sentence> sentenceSet_copy = new HashSet<Sentence>();
		//update TFIDF
		for(Sentence sentence : sentenceSet) {
			if(sentence.getOrder() < 0) {
				sentence.setTfIdfValue(0);
				sentenceSet_copy.add(sentence);
				continue;
			}
			Document document = documents.stream().filter(T->T.getLink().equals(sentence.getLink())).collect(Collectors.toList()).get(0);
			double tfIdf = 0;
			int size = 0;
			for(Word word : sentence.getMeanWordList()) {
				if(CheckUtil.isWordInQuery(word, document.getQuerySent().getMeanWordList()))continue;
				double tf = getTfValueFromDocument(document, word);
				double idf = getIdfValueFromDocument(documents, word);
				size ++;
				tfIdf += tf * idf;
			}
			sentence.setTfIdfValue(tfIdf / size);
			sentenceSet_copy.add(sentence);
		}
		double[] lambdaArr = new double[5];
		for(Sentence sentence : sentenceSet_copy) {
			lambdaArr[0] += sentence.getTfIdfValue();
			lambdaArr[1] += sentence.getPositionValue();
			lambdaArr[2] += sentence.getTitleValue();
			lambdaArr[3] += sentence.getQueryValue();
			lambdaArr[4] += sentence.getCruelValue();
		}
		for(int i = 0; i < 5; i++) {
			if(Math.pow(lambdaArr[i], 2) < Math.pow(10, -8))continue;
			lambdaArr[i] = sentenceSet_copy.size() / lambdaArr[i];
		}
		for(Sentence sentence: sentenceSet_copy) {
			double[] values = new double[5];
			values[0] = lambdaArr[0] * sentence.getTfIdfValue();
			values[1] = lambdaArr[1] * sentence.getPositionValue();
			values[2] = lambdaArr[2] * sentence.getTitleValue();
			values[3] = lambdaArr[3] * sentence.getQueryValue();
			values[4] = lambdaArr[4] * sentence.getCruelValue();
			sentence.setValues(values);
		}
		List<Sentence> sentences_copy = sentenceSet_copy.stream().filter(T->true).map(T->{
					T.setResult(NLPUtil.calcuResult(T.getValues()));
					return T;
				}).sorted(Sentence::compareTo).collect(Collectors.toList());
		double[][] mmrArray = new double[sentenceSet.size()][sentenceSet.size()];
		for(int i = 0; i < mmrArray.length; i++) {
			for(int j = 0; j < mmrArray[0].length; j++) {
				mmrArray[i][j] = sentSim(sentences_copy.get(i), sentences_copy.get(j));
			}
		}
		for(int i = 0; i < mmrArray.length; i++) {
			System.out.println("");
			for(int j = 0; j < mmrArray[0].length; j++) {
				System.out.print((mmrArray[i][j] + "  ").substring(0,4));
			}
		}
		Set<Sentence> selectSentences = new HashSet<Sentence>();
		while (selectSentences.size() < sentenceSet_copy.size() * 0.5) {
			double maxScore = 0;
			Sentence maxSentence = null;
			for(int i = 0; i < sentences_copy.size(); i++) {
				Sentence sentence = sentences_copy.get(i);
				//System.out.println(sentence.toString());
				double sim = 0;
				if(selectSentences.size() == 0) {
					
				}else {
					for(Sentence sentence_select : selectSentences) {
						double simNow = sentSim(sentence, sentence_select);
						if(simNow > sim) {
							sim = simNow;
						}
					}
				}
				if(sim > 0.99)continue;
				sim = lambda * sentence.getResult() - (1 - lambda) * sim;
				if(sim > maxScore) {
					maxScore = sim;
					maxSentence = sentence;
				}
			}
			if(null == maxSentence)break;
			selectSentences.add(maxSentence);
		}
		System.out.println(selectSentences.size());
		return selectSentences.stream().sorted(Sentence::compareToByTime).collect(Collectors.toList());
	}
	
}
