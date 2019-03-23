package com.ns.main.config;

public enum SpiderFilter {
 
	XINHUANET(".h-title", ".h-time", "#source", "#p-detail p"),
	MXINHUANET(".h-title", ".h-time", "#source", "#p-detail p"),
	SINA(".main-title", ".date", ".source", "#article p"),
	PEOPLE("h1", ".box01 .fl", ".box01 .fl a", ".box_con p"),
	MILITARYPEOPLE("#p_title", "#p_publishtime", "#p_origin a", "#p_content p");
	
	
	private String titleFilter;
	
	private String timeFilter;
	
	private String sourceFilter;
	
	private String docFilter;
	
	private SpiderFilter(String titleFilter, String timeFilter, String sourceFilter, String docFilter) {
		this.setTitleFilter(titleFilter);
		this.setTimeFilter(timeFilter);
		this.setSourceFilter(sourceFilter);
		this.setDocFilter(docFilter);
	}

	public String getTitleFilter() {
		return titleFilter;
	}

	public void setTitleFilter(String titleFilter) {
		this.titleFilter = titleFilter;
	}

	public String getTimeFilter() {
		return timeFilter;
	}

	public void setTimeFilter(String timeFilter) {
		this.timeFilter = timeFilter;
	}

	public String getSourceFilter() {
		return sourceFilter;
	}

	public void setSourceFilter(String sourceFilter) {
		this.sourceFilter = sourceFilter;
	}

	public String getDocFilter() {
		return docFilter;
	}

	public void setDocFilter(String docFilter) {
		this.docFilter = docFilter;
	}
	
}
