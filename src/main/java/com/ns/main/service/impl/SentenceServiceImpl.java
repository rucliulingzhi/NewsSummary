package com.ns.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.springframework.stereotype.Service;

import com.ns.main.config.Value;
import com.ns.main.entity.Document;
import com.ns.main.entity.Paragraph;
import com.ns.main.entity.Sentence;
import com.ns.main.entity.Word;
import com.ns.main.service.SentenceService;
import com.ns.main.util.HttpUtil;
import com.ns.main.util.NLPUtil;

@Service
public class SentenceServiceImpl implements SentenceService{
	
	@Override
	public List<Document> updateSentence(List<Document> documents){
		for(Document document : documents) {
			if(null == document.getSentences())continue;
			List<Sentence> sentences = new ArrayList<>();
			for(Sentence sentence : document.getSentences()) {
				//TfIdf
				double tfIdf = 0;
				for(Word word: sentence.getMeanWordList()) {
					double tf = NLPUtil.getTfValueFromSentence(sentence, word);
					double idf = NLPUtil.getIdfValueFromSentence(document.getSentences(), word);
					tfIdf += tf * idf;
				}
				sentence.setTfIdfValue(tfIdf / sentence.getMeanWordList().size());
				//position
				double position = 0;
				if(sentence.isFirstPara())position += Value.FIRST_PARA.getValue();
				if(sentence.isFirstSent())position += Value.FIRST_SENT.getValue();
				if(sentence.getOrder() == 1)position += Value.SECOND_PARA.getValue();
				sentence.setPositionValue(position);
				//title like & query like
				double tSim = NLPUtil.sentSimMain(document.getTitleSent(), sentence);
				double qSim = NLPUtil.sentSimMain(document.getQuerySent(), sentence);
				sentence.setTitleValue(tSim);
				sentence.setQueryValue(qSim);
				//entity value
				double entity = 0;
				sentence.setEntityValue(entity);;
				sentences.add(sentence);
			}
			double[] lambda = new double[5];
			for(Sentence sentence : sentences) {
				lambda[0] += sentence.getTfIdfValue();
				lambda[1] += sentence.getPositionValue();
				lambda[2] += sentence.getTitleValue();
				lambda[3] += sentence.getQueryValue();
				lambda[4] += sentence.getEntityValue();
			}
			for(int i = 0; i < 5; i++) {
				if(Math.pow(lambda[i], 2) < Math.pow(10, -8))continue;
				lambda[i] = sentences.size() / lambda[i];
			}
			for(Sentence sentence: sentences) {
				double[] values = new double[5];
				values[0] = lambda[0] * sentence.getTfIdfValue();
				values[1] = lambda[1] * sentence.getPositionValue();
				values[2] = lambda[2] * sentence.getTitleValue();
				values[3] = lambda[3] * sentence.getQueryValue();
				values[4] = lambda[4] * sentence.getEntityValue();
				sentence.setValues(values);
			}
			document.setLambda(lambda);
			document.setSentences(sentences);
		}
		return documents;
	}
	
	@Override
	public List<Document> updateDocument(List<Document> documents) {
		List<String> wordSetList = new ArrayList<>();
		List<Word> wordList_withVec = new ArrayList<>();
		Word2Vec word2Vec = NLPUtil.word2Vec;
		VocabCache<VocabWord> vocabCache  = word2Vec.getVocab();
		//遍历每一个document
		for(Document document : documents) {
			wordList_withVec.clear();
			List<Sentence> sentences = new ArrayList<>(); 
			//获取段中每一个句子
			String title = document.getTitle()
					.replace(Value.PERIOD.getName(), Value.BLANK.getName())
					.replace(Value.EXCLAMATORY.getName(), Value.BLANK.getName())
					.replace(Value.INTERROGATION.getName(), Value.BLANK.getName())
					.replace(Value.ELLIPSIS.getName(), Value.BLANK.getName());
			sentences.add(new Sentence(document.getLink(), false, -1, document.getQueryString() + Value.PERIOD.getName(), document.getTime(), document.getSource()));
			sentences.add(new Sentence(document.getLink(), false, -1, title + Value.PERIOD.getName(), document.getTime(), document.getSource()));
			for(Paragraph paragraph : document.getParagraphs()) {
				List<String> sentContent = NLPUtil.getSentences(paragraph.getContent(), null);
				boolean isFirstPara = false;
				if(paragraph.getOrder() == 0)isFirstPara = true;
				for(int i = 0; i < sentContent.size(); i++) {
					sentences.add(new Sentence(document.getLink(), isFirstPara, i, sentContent.get(i), 
							NLPUtil.getSentenceTime(sentContent.get(i), paragraph.getTime()), document.getSource()));
				}
			}
			//将全文post到切词接口,获取切完的词集
			StringBuilder content = new StringBuilder();
			for(int i = 0; i < sentences.size(); i++) {
				content.append(sentences.get(i).getContent() + Value.LINE_FEED.getName());
			}
			//System.out.println(content);
			List<String> cuttedContent = HttpUtil.cutter(content.toString());
			//词集回写到句中
			if(cuttedContent.size() == 1)continue;
			for(int i = 0; i < sentences.size(); i++) {
				String[] words = cuttedContent.get(i) .split(" ");
				List<Word> wordList = new ArrayList<>();
				for(int j = 0; j < words.length; j++) {
					if(!words[j].contains(Value.SEPARATOR.getName()))continue;
					Word newWord = new Word(document.getLink(), words[j]);
					if(vocabCache.containsWord(words[j].split(Value.SEPARATOR.getName())[0])) {
						newWord.setWordVec(word2Vec.getWordVector(newWord.getContent()));
						wordList_withVec.add(newWord);
					}
					wordSetList.add(words[j]);
					wordList.add(newWord);
				}
				sentences.get(i).setWordList(wordList);
			}
			document.setSentences(sentences);
			document.setWordSetList(wordList_withVec.stream().map(T->T).collect(Collectors.toList()));
			if(document.listToSet() && document.stringToWordSet()) {
				System.out.println("Document WordSet Finished update");
			}
			else {
				System.out.println("error");
			}
			List<Sentence> sentences2 = new ArrayList<>();
			for(Sentence sentence : document.getSentences()) {
				if(sentence.updateSentence(document.getMeanWordSet())) {
					sentences2.add(sentence);
				}
				else {
					System.out.println("invalid sentence");
				}
			}
			document.setSentences(sentences2);
			document.setQuerySent(sentences2.get(0));
			document.setTitleSent(sentences2.get(1));
		}
		return documents;
	}
}
