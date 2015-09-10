package common;

import search.FedoraCommunicatorOld;

public abstract class Service {

	private static Resource resource = new Resource();
	private static FedoraCommunicatorOld communicator = new FedoraCommunicatorOld();
	
	public Resource getResource() {
		return resource;
	}

	protected FedoraCommunicatorOld getFedoraCommunicator(){
		return communicator;
	}
	

}
