package com.hqking.hashpass;

import java.util.List;


public interface Storage {
	int save(Site site);
	
	int delete(Site site);
	
	List<Site> search(String pattern);
	
	Site find(String desc);
}
