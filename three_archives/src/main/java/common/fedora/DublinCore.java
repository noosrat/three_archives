package common.fedora;

public enum DublinCore {
	PID("pid"), LABEL("label"), STATE("state"), OWNER_ID("ownerId"), C_DATE(
			"cDate"), M_DATE("mDate"), DCM_DATE("dcmDate"), TITLE("title"), CREATOR(
			"creator"), SUBJECT("subject"), DESCRIPTION("description"), PUBLISHER(
			"publisher"), CONTRIBUTOR("contributor"), DATE("date"), TYPE("type"), FORMAT(
			"format"), IDENTIFIER("identifier"), SOURCE("source"), LANGUAGE(
			"language"), RELATION("relation"), COVERAGE("coverage"), RIGHTS(
			"rights");

	private String description;

	private DublinCore(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public DublinCore parseDescription(String description){
		if (description!=null){
			return DublinCore.valueOf(description);
		}
		return null;
	}
}
