package backend;

import java.net.MalformedURLException;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.FedoraCredentials;
import com.yourmediashelf.fedora.client.request.Ingest;
import com.yourmediashelf.fedora.client.response.IngestResponse;

public class FedoraCommunicator {
	private FedoraCredentials credentials;

	public FedoraCommunicator() {
		try {
			FedoraCredentials credentials = new FedoraCredentials(
					"http://localhost:8080/fedora", "fedoraAdmin",
					"fedoraAdmin");
		} catch (MalformedURLException ex) {
			System.out.println("error, credentials incorrect, malformed" + ex);
		}
	}

	public void testUsage() {

	}

	private void testIngestUsage() throws FedoraClientException {
		// FedoraRequest.setDefaultClient(fedora);
		IngestResponse response = new Ingest("test:pid").label("foo").execute();
		String pid = response.getPid();
		System.out.println("PID " + response.getPid());

	}

}
