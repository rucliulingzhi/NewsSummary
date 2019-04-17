package com.ns.main.config;

public enum NewsSite {
	
	XINHUANET("www.xinhuanet.com", "新华网", SpiderFilter.XINHUANET),
	MXINHUANET("m.xinhuanet.com", "新华网移动端", SpiderFilter.MXINHUANET),
	SINA("news.sina.com.cn", "新浪新闻", SpiderFilter.SINA),
	KSINA("k.sina.com.cn", "新浪新闻看点", SpiderFilter.KSINA),
	HUANQIUC("china.huanqiu.com", "环球网国内", SpiderFilter.HUANQIUC),
	HUANQIUW("world.huanqiu.com", "环球网国际", SpiderFilter.HUANQIUC),
	HUANQIUS("society.huanqiu.com", "环球网社会", SpiderFilter.HUANQIUC),
	PEOPLE("people.com.cn", "人民网", SpiderFilter.PEOPLE),
	MILITARYPEOPLE("military.people.com.cn", "人民网军事频道", SpiderFilter.MILITARYPEOPLE),
	HEALTHPEOPLE("health.people.com.cn", "人民健康网", SpiderFilter.HEALTHPEOPLE);
	
	
	
	private String domain;
	
	private String name;
	
	private SpiderFilter spiderFilter;
	
	private NewsSite(String domain, String name, SpiderFilter spiderFilter) {
		this.setDomain(domain);
		this.setName(name);
		this.setSpiderFilter(spiderFilter);
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SpiderFilter getSpiderFilter() {
		return spiderFilter;
	}

	public void setSpiderFilter(SpiderFilter spiderFilter) {
		this.spiderFilter = spiderFilter;
	}
	
}
