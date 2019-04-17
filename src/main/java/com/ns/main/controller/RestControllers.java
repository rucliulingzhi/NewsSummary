package com.ns.main.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ns.main.entity.Document;
import com.ns.main.entity.Sentence;
import com.ns.main.service.SentenceService;
import com.ns.main.service.SpiderService;
import com.ns.main.util.CheckUtil;
import com.ns.main.util.ConvertTools;
import com.ns.main.util.NLPUtil;

@RestController
public class RestControllers {

	Map<String, Object> resMap;
	
	@Autowired
	SpiderService spiderService;
	
	@Autowired
	SentenceService sentenceService;
	
	@RequestMapping("/test")
	public String test() throws Exception {
		String query = "凉山火灾";
		List<String> links = ConvertTools.getLinksByQuery(query);
		List<Document> documentlList = spiderService.fetchDocumentFromLinks(links, query).stream()
				.map(T->new Document(T)).collect(Collectors.toList());
		documentlList = sentenceService.updateDocument(documentlList);
		documentlList = sentenceService.updateSentence(documentlList);
		List<Sentence> sentences = new ArrayList<Sentence>();
		for(Document d : documentlList) {
			if(null == d.getSentences())continue;
			List<Sentence> sentences_doc = d.getSentences().stream()
					.filter(T->(CheckUtil.isSelectedSentence(T)))
					.map(T->{T.setResult(NLPUtil.calcuResult(T.getValues(), true));return T;})
					.sorted(Sentence::compareTo).collect(Collectors.toList());
			if(sentences_doc.size() >= 1) {
				sentences.add(sentences_doc.get(0));
			} else {
				sentences.add(d.getSentences().get(1));
				System.out.println(d.getLink() + "\r\n" + d.toString());
			}
			if(sentences_doc.size() >= 2)sentences.add(sentences_doc.get(1));
			if(sentences_doc.size() >= 3)sentences.add(sentences_doc.get(2));
		}
		return ConvertTools.printOut(NLPUtil.MMR(0, sentences, documentlList));
	}
	
	@RequestMapping("/rest")
	public Map<String, Object> restTest(){
		resMap = null;
		return resMap;
	}
	
}
