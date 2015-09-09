package common.fedora;

public enum State {
	A("Active"),
	I("Inactive"),
	D("Deleted");
	
	private String description;
	
	private State(String description){
		this.description = description;
	}

	public String getDescritpion(){
		return description;
	}
}
