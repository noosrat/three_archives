package common;

import search.FedoraCommunicator;

/**
 * 
 * The {@code Service} abstract class is the parent class of all services
 * implemented within the application. The service gives access to the
 * fedoraCommunicator to be used to access the digital objects within the fedora
 * digital object repository
 * 
 * @author mthnox003
 */
public abstract class Service {

	/**
	 * The {@link resource} instance to be used by each service to communicate
	 * with the database
	 * 
	 */
	private static Resource resource = new Resource();
	/**
	 * The {@link FedoraCommunicator} instance to be used to communicate with
	 * the Fedora Digital object repository
	 */
	private static FedoraCommunicator communicator = new FedoraCommunicator();

	/**
	 * Gets the {@link Resource} instance
	 * 
	 * @return {@link Resource} instance representing the Resource used for
	 *         database communication
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * Gets the {@link FedoraCommunicator} instance used to communicate with the
	 * digital object repository
	 * 
	 * @return {@link FedoraCommunicator} instance representing the
	 *         communication channel with the digital object repository
	 */
	protected FedoraCommunicator getFedoraCommunicator() {
		return communicator;
	}

}
