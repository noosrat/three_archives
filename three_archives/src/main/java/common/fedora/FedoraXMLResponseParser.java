package common.fedora;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * We will have to parse the response returned dependent on what format it was returned in...Parse it so that we can retrieve the relevant data etc...
 * we need to parse it to fill up our fedora objects so that we can use it in the system...
 */
public class FedoraXMLResponseParser {

	private InputStream response;
	private Document document;

	
	public FedoraXMLResponseParser(InputStream response) throws FedoraException {
		this.response = response;
		try {
			initialise();
		} catch (Exception ex) {
			throw new FedoraException("Unable to intialise XML Response Parser");
		}
	}


	private void initialise() throws ParserConfigurationException,
			SAXException, IOException {
		document= DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response);
		document.getDocumentElement().normalize();
		System.out.println("Succesffully parsed xml");
	}

	public void setResponse(InputStream response) {
		this.response = response;
	}

	public InputStream getResponse() {
		return response;
	}

	public List<String> parseFindObjects(){
		List<String> result = new ArrayList<String>();
		NodeList nodeList = document.getElementsByTagName("objectFields");

		for (int index = 0; index < nodeList.getLength(); index++) {
			Node node = nodeList.item(index);
			Element element = (Element) node;
			result.add(element.getElementsByTagName("pid").item(0).getTextContent());
		}

		return result;
	}

	public List<String> parseGetObjectHistory() {
		List<String> result = new ArrayList<String>();
		NodeList nodeList = document.getElementsByTagName("fedoraObjectHistory");
		Node node = nodeList.item(0);
		Element element = (Element) node;
		for (int index = 0; index < element.getElementsByTagName(
				"objectChangeDate").getLength(); index++) {
			result.add(element.getElementsByTagName("objectChangeDate")
					.item(index).getTextContent());
		}

		return result;
	}

	public void parseGetObjectProfile(FedoraDigitalObject fedoraDigitalObject) {
		NodeList nodeList = document.getElementsByTagName("objectProfile");
		Node node = nodeList.item(0);
		Element element = (Element) node;

		String dateCreated = element.getElementsByTagName("objCreateDate")
				.item(0).getTextContent();
		String lastModified = element.getElementsByTagName("objLastModDate")
				.item(0).getTextContent();
		String state = element.getElementsByTagName("objState").item(0)
				.getTextContent();

		fedoraDigitalObject.setDateCreated(dateCreated);
		fedoraDigitalObject.setDateLastModified(lastModified);
		fedoraDigitalObject.setState(State.valueOf(state));

	}

	public List<String> parseListDataStream() {
		List<String> result = new ArrayList<String>();
		NodeList nodeList = document.getElementsByTagName("objectDatastreams");
		Node node = nodeList.item(0);
		Element element = (Element) node;
		for (int index = 0; index < element.getElementsByTagName("datastream")
				.getLength(); index++) {
			result.add(element.getElementsByTagName("datastream").item(index)
					.getAttributes().getNamedItem("dsid").getTextContent());
		}

		return result;
	}

	public Datastream parseGetDatastream() {
		Datastream datastream = new Datastream();
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		Node node = nodeList.item(0);
		Element element = (Element) node;

		String label = element.getElementsByTagName("dsLabel").item(0)
				.getTextContent();
		String version = element.getElementsByTagName("dsVersionID").item(0)
				.getTextContent();
		String created = element.getElementsByTagName("dsCreateDate").item(0)
				.getTextContent();
		String state = element.getElementsByTagName("dsState").item(0)
				.getTextContent();
		String mimeType = element.getElementsByTagName("dsMIME").item(0)
				.getTextContent();
		String formatURI = element.getElementsByTagName("dsFormatURI").item(0)
				.getTextContent();
		String controlGroup = element.getElementsByTagName("dsControlGroup")
				.item(0).getTextContent();
		String size = element.getElementsByTagName("dsSize").item(0)
				.getTextContent();
		String versionable = element.getElementsByTagName("dsVersionable")
				.item(0).getTextContent();
		String infoType = element.getElementsByTagName("dsInfoType").item(0)
				.getTextContent();
		String location = element.getElementsByTagName("dsLocation").item(0)
				.getTextContent();
		String locationType = element.getElementsByTagName("dsLocationType")
				.item(0).getTextContent();
		String checksumType = element.getElementsByTagName("dsChecksumType")
				.item(0).getTextContent();
		String checksum = element.getElementsByTagName("dsChecksum").item(0).getTextContent();

		datastream.setLabel(label);
		datastream.setCreation(created);
		datastream.setVersionID(version);
		datastream.setState(State.valueOf(state));
		// datastream.setMediaType(MediaType.);
		datastream.setFormatURI(formatURI);
		datastream.setControlGroup(controlGroup);
		datastream.setSize(Integer.parseInt(size));
		datastream.setVersionable(Boolean.parseBoolean(versionable));
		datastream.setLocation(location);

		return datastream;
	}

	public List<Datastream> parseGetDatastreamHistory() {
		List<Datastream> result = new ArrayList<Datastream>();
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		Node node;
		for (int index = 0; index < nodeList.getLength(); index++) {
			node = nodeList.item(index);
			Datastream datastream = new Datastream();
			Element element = (Element) node;
			// do null checks
			String label = element.getElementsByTagName("dsLabel").item(index)
					.getTextContent();
			String version = element.getElementsByTagName("dsVersionID")
					.item(index).getTextContent();
			String created = element.getElementsByTagName("dsCreateDate")
					.item(index).getTextContent();
			String state = element.getElementsByTagName("dsState").item(index)
					.getTextContent();
			String mime = element.getElementsByTagName("dsMIME").item(index)
					.getTextContent();
			String formatURI = element.getElementsByTagName("dsFormatURI")
					.item(index).getTextContent();
			String controlGroup = element
					.getElementsByTagName("dsControlGroup").item(index)
					.getTextContent();
			String size = element.getElementsByTagName("dsSize").item(0)
					.getTextContent();
			String versionable = element.getElementsByTagName("dsVersionable")
					.item(index).getTextContent();
			String infoType = element.getElementsByTagName("dsInfoType")
					.item(index).getTextContent();
			String location = element.getElementsByTagName("dsLocation")
					.item(index).getTextContent();
			String locationType = element
					.getElementsByTagName("dsLocationType").item(index)
					.getTextContent();
			String checksumType = element
					.getElementsByTagName("dsChecksumType").item(index)
					.getTextContent();
			String checksum = element.getElementsByTagName("dsChecksum")
					.item(index).getTextContent();
			datastream.setLabel(label);
			datastream.setCreation(created);
			datastream.setVersionID(version);
			datastream.setState(State.valueOf(state));
			// datastream.setMediaType(MediaType.);
			datastream.setFormatURI(formatURI);
			datastream.setControlGroup(controlGroup);
			datastream.setSize(Integer.parseInt(size));
			datastream.setVersionable(Boolean.parseBoolean(versionable));
			datastream.setLocation(location);
		
			result.add(datastream);
		}

		return result;
	}

	// will not implement
	public List<FedoraDigitalObject> parseGetDatastreamWithProfiles() {
		return null;
	}

}
