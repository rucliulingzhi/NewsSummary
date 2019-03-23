package com.ns.main.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ns.main.entity.RawDocument;
import com.ns.main.service.SpiderService;
import com.ns.main.util.ConvertTools;
import com.ns.main.util.HttpUtil;

@Service
public class SpiderServiceImpl implements SpiderService{

	@Override
	public List<RawDocument> fetchDocumentFromLinks(List<String> links){
		return links.stream().map(T->
		ConvertTools.documentToRawDocument(
						HttpUtil.getDocumentByUrl(T)
						)).collect(Collectors.toList());
 	}
	
}
