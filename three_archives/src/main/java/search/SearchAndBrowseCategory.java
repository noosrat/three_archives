package search;

import java.util.Arrays;
import java.util.List;

public enum SearchAndBrowseCategory {
	SEARCH_ALL(Arrays.asList("")),
	//ACADEMIC_PAPER(Arrays.asList("")), //search description nad maybe title
	COLLECTION(Arrays.asList("dc.description")),//search DC description
	CREATOR(Arrays.asList("dc.creator", "dc.contributor","dc.source")),
	EVENT(Arrays.asList("dc.description")),//search description
	DESCRIPTION(Arrays.asList("dc.description")),//search description
	EXHIBITION(Arrays.asList("dc.description")), //this will be a data type search
	LOCATION(Arrays.asList("dc.location")), //search dublin core ds
	FORMAT(Arrays.asList("dc.format","dc.type")), //this just filters according to the media types of the datastream
	TITLE(Arrays.asList("dc.title")), //search in dublin core titile
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
