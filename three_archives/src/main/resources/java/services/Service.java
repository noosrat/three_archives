package services;

import backend.Resource;

public abstract class Service {

	private Resource resource;
	
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Service() {
	}

}
