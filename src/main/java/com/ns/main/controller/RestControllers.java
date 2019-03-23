package com.ns.main.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ns.main.entity.Document;
import com.ns.main.entity.Sentence;
import com.ns.main.entity.Word;
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
		String[] linkArray = {
				"http://www.xinhuanet.com/world/2019-03/02/c_1210071346.htm", 
				"http://m.xinhuanet.com/2019-03/01/c_1210071116.htm", 
				"http://m.xinhuanet.com/mil/2019-03/01/c_1210071020.htm", 
				"http://news.sina.com.cn/o/2019-03-02/doc-ihrfqzkc0523564.shtml", 
				"http://www.xinhuanet.com/world/2019-02/28/c_1210070033.htm", 
				"http://www.xinhuanet.com/world/2019-03/01/c_1210070390.htm", 
				"http://news.sina.com.cn/c/2019-02-28/doc-ihrfqzkc0077791.shtml",
				"http://world.people.com.cn/n1/2019/0302/c1002-30953437.html",
				"http://gz.people.com.cn/n2/2019/0301/c344101-32697702.html",
				"http://yn.people.com.cn/n2/2019/0228/c372459-32693466.html",
				"http://military.people.com.cn/n/2014/0722/c1011-25315220.html",
				"http://yn.people.com.cn/n2/2019/0227/c378441-32689268.html"
		};
		List<String> links = Arrays.asList(linkArray);
		List<Document> documentlList = spiderService.fetchDocumentFromLinks(links).stream().map(T->new Document(T)).collect(Collectors.toList());
		documentlList = sentenceService.updateDocument(documentlList);
		documentlList = sentenceService.updateSentence(documentlList);
		List<Sentence> sentences = new ArrayList<Sentence>();
		for(Document d : documentlList) {
			List<Sentence> sentences_doc = d.getSentences().stream()
					.filter(T->(CheckUtil.isSelectedSentence(T)))
					.map(T->{T.setResult(NLPUtil.calcuResult(T.getValues()));return T;})
					.sorted(Sentence::compareTo).collect(Collectors.toList());
			sentences.add(sentences_doc.get(0));
			if(sentences_doc.size() >= 2)sentences.add(sentences_doc.get(1));
			if(sentences_doc.size() >= 3)sentences.add(sentences_doc.get(2));
		}
		return ConvertTools.printOut(NLPUtil.MMR(0, sentences, documentlList));
	}
	
	@RequestMapping("/rest")
	public Map<String, Object> restTest(){
		Word word = new Word("", "另外/d");
		System.out.println(word.getType());
		System.out.println(CheckUtil.isMeaningful(word));
		resMap = null;
		return resMap;
	}
	
}
