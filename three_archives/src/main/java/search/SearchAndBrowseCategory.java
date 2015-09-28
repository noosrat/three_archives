package search;

import java.util.Arrays;
import java.util.List;

public enum SearchAndBrowseCategory {
	SEARCH_ALL(Arrays.asList("")),
	//ACADEMIC_PAPER(Arrays.asList("")), //search description nad maybe title
	COLLECTION(Arrays.asList("dc.description")),//search DC description
	CREATOR(Arrays.asList("dc.creator")),
	EVENT(Arrays.asList("dc.description")),//search description
	EXHIBITION(Arrays.asList("dc.description")), //this will be a data type search
	TITLE(Arrays.asList("dc.title")), //search in dublin core titile
	LOCATION(Arrays.asList("dc.location")), //search dublin core ds
	MEDIA_TYPE(Arrays.asList("dc.format")), //this just filters according to the media types of the datastream
	SUBJECT(Arrays.asList("dc.title")),// search DC
	YEAR(Arrays.asList("dc.date")); //search in dublin core date
	
	private List<String> dublinCoreField;
	
	private SearchAndBrowseCategory(List<String> dublinCoreField){
		this.dublinCoreField = dublinCoreField;
	}

	public List<String> getDublinCoreField() {
		return dublinCoreField;
	}

	
} 
