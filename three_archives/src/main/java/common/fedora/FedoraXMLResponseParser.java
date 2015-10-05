package common.fedora;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.http.MediaType;
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
			System.out.println("Initialise");
			System.out.println(response);
			initialise();
			System.out.println("Initialise");
		} catch (Exception ex) {
			throw new FedoraException("Unable to intialise XML Response Parser ", ex);
		}
	}

	private void initialise() throws ParserConfigurationException, SAXException, IOException {
		System.out.println("In Initialise");
		System.out.println(response);
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response);
		System.out.println(document);
		document.getDocumentElement().normalize();
	}

	public void setResponse(InputStream response) {
		this.response = response;
	}

	public InputStream getResponse() {
		return response;
	}

	public List<String> parseFindObjects() {
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
		for (int index = 0; index < element.getElementsByTagName("objectChangeDate").getLength(); index++) {
			result.add(element.getElementsByTagName("objectChangeDate").item(index).getTextContent());
		}

		return result;
	}

	public void parseGetObjectProfile(FedoraDigitalObject fedoraDigitalObject) throws FedoraException {
		NodeList nodeList = document.getElementsByTagName("objectProfile");
		Node node = nodeList.item(0);
		Element element = (Element) node;

		String dateCreated = element.getElementsByTagName("objCreateDate").item(0).getTextContent();
		String lastModified = element.getElementsByTagName("objLastModDate").item(0).getTextContent();
		String state = element.getElementsByTagName("objState").item(0).getTextContent();
		try {
			fedoraDigitalObject.setDateCreated(parseFedoraDateToJavaDate(dateCreated));
			fedoraDigitalObject.setDateLastModified(parseFedoraDateToJavaDate(lastModified));
		} catch (ParseException e) {
			throw new FedoraException("Could not convert date", e);
		}

		fedoraDigitalObject.setState(State.valueOf(state));

	}

	public List<DatastreamID> parseListDataStream() {
		List<DatastreamID> result = new ArrayList<DatastreamID>();
		NodeList nodeList = document.getElementsByTagName("objectDatastreams");
		Node node = nodeList.item(0);
		Element element = (Element) node;

		System.out.println("xxxxxx Parsing for loop");
		for (int index = 0; index < element.getElementsByTagName("datastream").getLength(); index++) {
			DatastreamID dsID = DatastreamID.valueOf(element.getElementsByTagName("datastream").item(index)
					.getAttributes().getNamedItem("dsid").getTextContent());
			System.out.println("Parsing datastream for ID: " + dsID);
			result.add(dsID);
		}

		return result;
	}

	public DublinCoreDatastream parseDublinCoreDatastream(Datastream datastream)
			throws ParserConfigurationException, SAXException, IOException {
		System.out.println("In parseDublinCore");
		DublinCoreDatastream dublinCoreDatastream = new DublinCoreDatastream(datastream);
		NodeList nodeList = document.getElementsByTagName("oai_dc:dc");
		Node node = nodeList.item(0);
		Element element = (Element) node;
		HashMap<String, String> dublinCoreMetadata = new HashMap<String, String>();
		for (DublinCore dc : DublinCore.values()) {
			String tagName = "dc:" + dc.getDescription();
			Node tag = element.getElementsByTagName(tagName).item(0);
			if (tag != null) {
				String textContent = element.getElementsByTagName(tagName).item(0).getTextContent();
				if (dc.equals(DublinCore.DESCRIPTION)) {
					// we want to parse the elements of the description at this
					// point already..we addiitonal fields called EVENT and COLLECTION
					//the description is in the format <dc:description>Collection:xxxxx%Event:xxxxx%other desc%annotations</dc:description>
					String[] description = textContent.split("%");
					System.out.println("split");
					System.out.println("collection: "+description[0]);
					dublinCoreMetadata.put("COLLECTION",description[0]);//collection
					System.out.println("collection: "+description[0]);
					dublinCoreMetadata.put("EVENT", description[1]);//event
					System.out.println("event: " + description[1]);
					dublinCoreMetadata.put(dc.name(), description[2]);//actual description
					System.out.println("split ended for desc: " + description[2]);
					dublinCoreMetadata.put("ANNOTATIONS", description[3]);//annotations
					System.out.println("split ended for desc: " + description[2]);
				} else if (dc.equals(DublinCore.COVERAGE)) {
					// the first part of coverage is the location
					// the second part can remain in co
					//coverage structure: <dc:coverage>Location:%21,22,11</dc:coverage>
					String[] coverage = textContent.split("%");
					System.out.println("split");
					dublinCoreMetadata.put("LOCATION", coverage[0]);
					dublinCoreMetadata.put(dc.name(), coverage[1]);
				} else {
					dublinCoreMetadata.put(dc.name(), textContent);
				}
				System.out.println("finished metadata pop");
			}

		}
		System.out.println("end of metadata for loop");
		dublinCoreDatastream.setDublinCoreMetadata(dublinCoreMetadata);
		return dublinCoreDatastream;
	}

	private Datastream parseDatastream(int index, Node node, String tagPrefix) throws FedoraException {
		Element element = (Element) node;
		Datastream datastream = new Datastream();
		DatastreamID dsID = DatastreamID.valueOf(element.getAttributes().getNamedItem("dsID").getTextContent());
		String pid = element.getAttributes().getNamedItem("pid").getTextContent();
		datastream.setPid(pid);
		datastream.setDatastreamID(dsID);
		String label = element.getElementsByTagName(tagPrefix + "dsLabel").item(0).getTextContent();
		String version = element.getElementsByTagName(tagPrefix + "dsVersionID").item(0).getTextContent();
		String created = element.getElementsByTagName(tagPrefix + "dsCreateDate").item(0).getTextContent();
		String state = element.getElementsByTagName(tagPrefix + "dsState").item(0).getTextContent();

		String[] mimeType = element.getElementsByTagName(tagPrefix + "dsMIME").item(0).getTextContent().split("/");
		MediaType mediaType = new MediaType(mimeType[0], mimeType[1]);
		String formatURI = element.getElementsByTagName(tagPrefix + "dsFormatURI").item(0).getTextContent();
		String controlGroup = element.getElementsByTagName(tagPrefix + "dsControlGroup").item(0).getTextContent();
		String size = element.getElementsByTagName(tagPrefix + "dsSize").item(0).getTextContent();
		String versionable = element.getElementsByTagName(tagPrefix + "dsVersionable").item(0).getTextContent();
		String location = element.getElementsByTagName(tagPrefix + "dsLocation").item(0).getTextContent();
		datastream.setLabel(label);

		try {
			datastream.setCreation(parseFedoraDateToJavaDate(created));
		} catch (ParseException e) {
			throw new FedoraException("Could not convert date", e);
		}
		datastream.setVersionID(version);
		datastream.setState(State.valueOf(state));

		datastream.setMediaType(mediaType);
		datastream.setFormatURI(formatURI);
		datastream.setControlGroup(controlGroup);
		datastream.setSize(Integer.parseInt(size));
		datastream.setVersionable(Boolean.parseBoolean(versionable));
		datastream.setLocation(location);
		System.out.println("Successfully parsed datastream object " + datastream.toString());
		return datastream;
	}

	public Datastream parseGetDatastream() throws FedoraException {
		System.out.println("In parseDatastream");
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		Node node = nodeList.item(0);
		return parseDatastream(0, node, "");
	}

	public List<Datastream> parseGetDatastreamHistory() throws FedoraException {
		List<Datastream> result = new ArrayList<Datastream>();
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		Node node;

		for (int index = 0; index < nodeList.getLength(); index++) {
			node = nodeList.item(index);
			result.add(parseDatastream(index, node, ""));
		}

		return result;
	}

	public List<Datastream> parseGetDatastreams() throws FedoraException {
		List<Datastream> datastreams = new ArrayList<Datastream>();
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		for (int index = 0; index < nodeList.getLength(); index++) {
			Node node = nodeList.item(index);
			datastreams.add(parseDatastream(index, node, "apim:"));
		}
		return datastreams;
	}

	private Date parseFedoraDateToJavaDate(String fedoraDate) throws ParseException {
		StringBuilder fed = new StringBuilder(fedoraDate);
		fed.deleteCharAt(fed.indexOf("Z"));
		fed.replace(fed.indexOf("T"), fed.indexOf("T") + 1, " ");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = formatter.parse(fed.toString());
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return date;
	}
}
