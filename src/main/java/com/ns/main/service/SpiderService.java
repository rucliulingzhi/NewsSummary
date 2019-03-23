package com.ns.main.service;

import java.util.List;
import com.ns.main.entity.RawDocument;

public interface SpiderService {

	public List<RawDocument> fetchDocumentFromLinks(List<String> links);
	
}
