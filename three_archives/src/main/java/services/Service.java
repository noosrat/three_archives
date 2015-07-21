package services;

import backend.FedoraCommunicator;
import backend.Resource;

public abstract class Service {

	private static Resource resource = new Resource();
	private static FedoraCommunicator communicator = new FedoraCommunicator();
	
	public Resource getResource() {
		return resource;
	}

	protected FedoraCommunicator getFedoraCommunicator(){
		return communicator;
	}
	

}
