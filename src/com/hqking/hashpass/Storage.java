package com.hqking.hashpass;

import java.util.Set;


public interface Storage {
	int save(Site site);
	
	Set<Site> search(String pattern);
	
	Site find(String desc);
}
