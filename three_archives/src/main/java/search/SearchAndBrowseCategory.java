package search;

import java.util.Arrays;
import java.util.List;

public enum SearchAndBrowseCategory {
	SEARCH_ALL(Arrays.asList("")), COLLECTION(Arrays.asList("dc.description")), CREATOR(
			Arrays.asList("dc.creator")), CONTRIBUTOR(Arrays.asList("dc.contributor")), SOURCE(
					Arrays.asList("dc.source")), EVENT(Arrays.asList("dc.description")),

	DESCRIPTION(Arrays.asList("dc.description")), LOCATION(Arrays.asList("dc.location")), FORMAT(
			Arrays.asList("dc.format")), TYPE(Arrays.asList("dc.type")), TITLE(Arrays.asList("dc.title")), SUBJECT(
					Arrays.asList("dc.title")), YEAR(Arrays.asList("dc.date"));

	private List<String> dublinCoreField;

	private SearchAndBrowseCategory(List<String> dublinCoreField) {
		this.dublinCoreField = dublinCoreField;
	}

	public List<String> getDublinCoreField() {
		return dublinCoreField;
	}

}
