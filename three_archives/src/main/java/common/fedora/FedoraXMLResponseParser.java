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

/**
 * The {@code FedoraXMLResponseParser} is responsible for parsing the response
 * received from the Fedora Digital objects repository RESTFUL API into java
 * objects which can be manipulated and interpreted on the front-end of the
 * application. The parsing results in {@link FedoraDigitalObject} instances
 * which can be handled using the frontend
 * 
 * This class parses each of the requests found in {@link FedoraGetRequest} from
 * XML format into Java objects
 * 
 * @author mthnox003
 *
 */

public class FedoraXMLResponseParser {

	/**
	 * The {@link InputStream} instance representing the response obtained from
	 * submitting the {@link FedoraGetRequest} via the {@link FedoraClient}
	 */
	private InputStream response;
	/**
	 * The {@link Document} instance used for parsing {@link #response}
	 */
	private Document document;

	/**
	 * Constructor: This initialises the response as well as the document.
	 * 
	 * @param response
	 *            {@link InputStream} instance retrieved from execution of the
	 *            {@link FedoraGetRequest} using the {@link FedoraClient}
	 *            representing the fedora query response.
	 * @throws FedoraException
	 *             if there is an error with intiialising the FedoraXMLParser
	 */
	public FedoraXMLResponseParser(InputStream response) throws FedoraException {
		this.response = response;
		try {
			initialise();
		} catch (Exception ex) {
			throw new FedoraException("Unable to intialise XML Response Parser ", ex);
		}
	}

	/**
	 * Initialisation of the document to be used throughout the parsing process.
	 * The initialisation occurs by parsing the {@link #response} into the
	 * {@link #document}
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void initialise() throws ParserConfigurationException, SAXException, IOException {
		setDocument(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response));
		getDocument().getDocumentElement().normalize();
	}

	/**
	 * Gets the {@link #document}
	 * 
	 * @return {@link Document} instance representing the document being parsed
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Sets the {@link #document}
	 * 
	 * @param document
	 *            {@link Document} instance representing the document to be
	 *            parsed
	 */
	public void setDocument(Document document) {
		this.document = document;
	}

	/**
	 * Sets the response for the response parser
	 * 
	 * @param response
	 *            {@link InputStream} instance representing the response to be
	 *            parsed
	 */
	public void setResponse(InputStream response) {
		this.response = response;
	}

	/**
	 * Gets the response for the response parser
	 * 
	 * @return {@link InputStream} instance representing the response to be
	 *         parsed
	 */
	public InputStream getResponse() {
		return response;
	}

	/**
	 * This method parses the XML response obtained from executing the
	 * {@link FedoraGetRequest.findObjects} method. The method retrieves the
	 * pid's of all the objects retrieved using the findObjects method.
	 * 
	 * @return {@link List} instance holding the pid values of the objects
	 *         located.
	 */
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

	/**
	 * This method parses the XML response obtained from executing the
	 * {@link FedoraGetRequest.getObjectHistory} method.
	 * 
	 * @return {@link List} instance representing the digital objects history
	 *         which is given as a series of dates.
	 */
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

	/**
	 * This method parses the XML response obtained from executing the
	 * {@link FedoraGetRequest.getObjectProfile} method. The values of the
	 * FedoraDigitalObject which are populated are the objectCreationDate, the
	 * objectLastModifiedDate and the objectState
	 * 
	 * @param fedoraDigitalObject
	 *            {@link FedoraDigitalObject} instance to be populated with the
	 *            results from parsing the XML response.
	 * 
	 * @throws FedoraException
	 */
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

	/**
	 * This method parses results from the
	 * {@link FedoraGetRequest.listDatastreams} into a list of datastream IDs.
	 * 
	 * @return {@link List} instance holding all the datastreamIDs of the
	 *         datastreams contained within a specific fedora digital object.
	 */
	public List<DatastreamID> parseListDataStream() {
		List<DatastreamID> result = new ArrayList<DatastreamID>();
		NodeList nodeList = document.getElementsByTagName("objectDatastreams");
		Node node = nodeList.item(0);
		Element element = (Element) node;

		for (int index = 0; index < element.getElementsByTagName("datastream").getLength(); index++) {
			DatastreamID dsID = DatastreamID.valueOf(element.getElementsByTagName("datastream").item(index)
					.getAttributes().getNamedItem("dsid").getTextContent());
			result.add(dsID);
		}

		return result;
	}

	/**
	 * This method parses the dublinCoreDatastream which is contained in every
	 * fedora digital object. This method parses this datastream by retrieving
	 * the content of the actual datastream which is in XML format and parsing
	 * this XML to fit the Dublin Core metadata fields.
	 * 
	 * @param datastream
	 *            {@link Datastream} instance to be used to initialised the
	 *            dublin core datastream
	 * @return {@link DublinCoreDatastream} instance which has all the fields
	 *         from the XML parsed
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public DublinCoreDatastream parseDublinCoreDatastream(Datastream datastream)
			throws ParserConfigurationException, SAXException, IOException {
		DublinCoreDatastream dublinCoreDatastream = new DublinCoreDatastream(datastream);
		NodeList nodeList = document.getElementsByTagName("oai_dc:dc");
		Node node = nodeList.item(0);
		Element element = (Element) node;
		HashMap<String, String> dublinCoreMetadata = new HashMap<String, String>();
		for (DublinCore dc : DublinCore.values()) {
			String tagName = "dc:" + dc.getDescription();
			Node tag = element.getElementsByTagName(tagName).item(0);
			if (tag != null) {
				String content = element.getElementsByTagName(tagName).item(0).getTextContent();
				if (content != null && !content.trim().isEmpty()) {
					String textContent = content.trim();
					if (dc.equals(DublinCore.DESCRIPTION)) {
						// <dc:description>Collection%Event%Description%Comment</dc:description>
						String[] description = textContent.split("%");
						switch (description.length) {
						case 4:
							if (!description[3].trim().isEmpty())
								dublinCoreMetadata.put("ANNOTATIONS", description[3]);
						case 3:
							if (!description[2].trim().isEmpty())
								dublinCoreMetadata.put(dc.name(), description[2].trim());
						case 2:
							if (!description[1].trim().isEmpty())
								dublinCoreMetadata.put("EVENT", description[1].trim());
						case 1:
							if (!description[0].trim().isEmpty())
								dublinCoreMetadata.put("COLLECTION", description[0].trim());
							break;
						}

					} else if (dc.equals(DublinCore.COVERAGE)) {
						// <dc:coverage>Location:%21,22,11</dc:coverage>
						String[] coverage = textContent.split("%");
						if (coverage.length != 0) {
							if (!coverage[0].trim().isEmpty())
								dublinCoreMetadata.put("LOCATION", coverage[0].trim());
								dublinCoreMetadata.put(dc.name(), coverage[1].trim());
						}
					} else {
						dublinCoreMetadata.put(dc.name(), textContent);
					}
				}
			}

		}
		dublinCoreDatastream.setDublinCoreMetadata(dublinCoreMetadata);
		return dublinCoreDatastream;
	}

	/**
	 * This method is used to parse elements from the XML into a single
	 * datastream. This method is used by other methods to parse a list of
	 * datastreams or only one datastream
	 * 
	 * @param node
	 *            {@link Node} instance representing the node of the XML being
	 *            parsed. Each datastream falls into it's own node
	 * @param tagPrefix
	 *            {@link String} instance representing the tagPrefix for the XML
	 *            element being parsed. This is either nothing or "apim"
	 * @return {@link Datastream} instance containing the parsed information
	 * @throws FedoraException
	 */
	private Datastream parseDatastream(Node node, String tagPrefix) throws FedoraException {
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
		return datastream;
	}

	/**
	 * This method is used to parse a single datastream obtained from executing
	 * the {@link FedoraGetRequest.getDatastream} method
	 * 
	 * @return
	 * @throws FedoraException
	 */
	public Datastream parseGetDatastream() throws FedoraException {
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		Node node = nodeList.item(0);
		return parseDatastream(node, "");
	}

	/**
	 * This method is used to parse a series of datastreams obtained from
	 * executing the {@link FedoraGetRequest.getDatastreamHistory} method
	 * 
	 * @return {@link List} instance of Datastreams with all the values from the
	 *         XML parsed into the java object
	 * @throws FedoraException
	 */
	public List<Datastream> parseGetDatastreamHistory() throws FedoraException {
		List<Datastream> result = new ArrayList<Datastream>();
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		Node node;

		for (int index = 0; index < nodeList.getLength(); index++) {
			node = nodeList.item(index);
			result.add(parseDatastream(node, ""));
		}

		return result;
	}

	/**
	 * This method is used to parse a series of datastreams obtained from
	 * executing the {@link FedoraGetRequest.getDatastreams} method
	 * 
	 * @return {@link List} instance of Datastreams with all the values from the
	 *         XML parsed into the java object
	 * @throws FedoraException
	 */

	public List<Datastream> parseGetDatastreams() throws FedoraException {
		List<Datastream> datastreams = new ArrayList<Datastream>();
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		for (int index = 0; index < nodeList.getLength(); index++) {
			Node node = nodeList.item(index);
			datastreams.add(parseDatastream(node, "apim:"));
		}
		return datastreams;
	}

	/**
	 * This method is used to parse the date contained in Fedora which is of the
	 * format yyyy-MM-dd HH:mm:ss.SSS into the format used within the Java
	 * {@link Date}
	 * 
	 * @param fedoraDate
	 *            {@link String} representation of the date from the Fedora
	 *            Repository
	 * @return {@link Date} instance of the parsed date
	 * @throws ParseException
	 */
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
