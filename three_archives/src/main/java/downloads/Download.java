package downloads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;

import common.Service;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

/**
 * The {@code Download} is a {@link Service} responsible for the gathering of
 * archive items for personal use during browsing.
 * 
 * @author hssnoo003
 *
 */
public class Download extends Service{

	public Download() {
		super();
	}
	
	/**
	 * The method responsible for retrieving the {@link FedoraDigitalObject} that have
	 * been chosen in the users personal collection
	 * 
	 * @param cart
	 *            {@link ArrayList} of {@link String} holding the pids of the cart items
	 */
	public Set<FedoraDigitalObject> getFedoraDigitalObjects(ArrayList<String> cart) throws FedoraException
	{
		Set<FedoraDigitalObject> obs = new HashSet<FedoraDigitalObject>();
		for (int k=0; k<cart.size(); k++){
		//for (int k=0; k<cart.length; k++){
		obs.add(getFedoraCommunicator().populateFedoraDigitalObject(cart.get(k)));
		}
		return obs;
	}
	
	/**
	 * Failed method used to call FEdora's object dissemination method to download individual items
	 * 
	 * @param obs
	 *            {@link ArrayList} holding the {@link FedoraDigitalObject} representations of the cart items
	 */
	public void downloadFedoraDigitalObjects(Set<FedoraDigitalObject> obs) throws FedoraException, SolrServerException{
//		return getFedoraCommunicator().findFedoraDigitalObjectsUsingSearchTerms(terms);
		getFedoraCommunicator().downloadFedoraDigitalObjectUsingObjects(obs);
	}
	
	/**
	 * Method to remove items from the cart
	 * 
	 * @param cart
	 *            {@link ArrayList} holding the pids of the cart items
	 * @param pid
	 * 		the pid of the item to be removed from cart
	 */
	public void removeFromCart(ArrayList<String> cart, String pid){
		
		cart.remove(cart.indexOf(pid));
	}
	
	/**
	 * Method responsible for saving selected items to the user's personal collection
	 * 
	 * @param selected
	 *            {@link ArrayList} of {@list Stirng} holding the pids of the items wished to be added to the cart.
	 * @param cart
	 * 	      {@link ArrayList} holding the pids of the cart items		
	 */
	public ArrayList<String> addToCart(String selected, ArrayList<String> cart){
		String[] selectedList=selected.split(",");
		
		for (int k=0; k<selectedList.length; k++){
			cart.add(selectedList[k]);
			System.out.println(selectedList[k]);
		}
		return cart;
	}
}
