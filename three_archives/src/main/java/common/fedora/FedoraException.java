package common.fedora;
/*
 * This exception will be thrown whenever issues are experienced with the server/connecting to the server or if the http response is not 200
 */
public class FedoraException extends Exception {
	
	public FedoraException(String message){
		super(message);
	}
	
	public FedoraException(Throwable cause){
		super(cause);
	}

}
