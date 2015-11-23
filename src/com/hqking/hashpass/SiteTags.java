package com.hqking.hashpass;

import javax.swing.AbstractListModel;

class SiteTags extends AbstractListModel<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MAX_TAG_ALLOWED = 5;
	
	private String[] tags = new String[MAX_TAG_ALLOWED];
	private int count = 0;
	
	public void addTag(String tag) {
		if (count < MAX_TAG_ALLOWED) {
			int i;
			
			for (i = 0; i < count; i++) {
				if (tags[i].equals(tag))
					break;
			}
			
			if (i == count) {
				tags[count] = tag;
				count++;
			
				fireIntervalAdded(this, 0, count);
			}
		}
	}
	
	public void deleteTag(String tag) {
		int i;
		for (i = 0; i < count; i++) {
			if (tags[i].equals(tag)) {
				break;
			}
		}
		
		for (int j = i + 1; j < count; j++, i++) {
			tags[i] = tags[j];
		}
		
		count = i;
	}
	
	@Override
	public String getElementAt(int arg0) {
		return tags[arg0];
	}

	@Override
	public int getSize() {
		return count;
	}

}
