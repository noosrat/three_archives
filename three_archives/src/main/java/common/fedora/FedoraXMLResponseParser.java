package common.fedora;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
			initialise();
		} catch (Exception ex) {
			throw new FedoraException("Unable to intialise XML Response Parser ", ex);
		}
	}

	private void initialise() throws ParserConfigurationException,
			SAXException, IOException {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(response);
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
			result.add(element.getElementsByTagName("pid").item(0)
					.getTextContent());
		}

		return result;
	}

	public List<String> parseGetObjectHistory() {
		List<String> result = new ArrayList<String>();
		NodeList nodeList = document
				.getElementsByTagName("fedoraObjectHistory");
		Node node = nodeList.item(0);
		Element element = (Element) node;
		for (int index = 0; index < element.getElementsByTagName(
				"objectChangeDate").getLength(); index++) {
			result.add(element.getElementsByTagName("objectChangeDate")
					.item(index).getTextContent());
		}

		return result;
	}

	public void parseGetObjectProfile(FedoraDigitalObject fedoraDigitalObject) throws FedoraException{
		NodeList nodeList = document.getElementsByTagName("objectProfile");
		Node node = nodeList.item(0);
		Element element = (Element) node;

		String dateCreated = element.getElementsByTagName("objCreateDate")
				.item(0).getTextContent();
		String lastModified = element.getElementsByTagName("objLastModDate")
				.item(0).getTextContent();
		String state = element.getElementsByTagName("objState").item(0)
				.getTextContent();
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
		for (int index = 0; index < element.getElementsByTagName("datastream")
				.getLength(); index++) {
			DatastreamID dsID = DatastreamID.valueOf(element
					.getElementsByTagName("datastream").item(index)
					.getAttributes().getNamedItem("dsid").getTextContent());
			System.out.println("Parsing datastream for ID: "+dsID);
			result.add(dsID);
		}

		return result;
	}

	public DublinCoreDatastream parseDublinCoreDatastream(Datastream datastream)
			throws ParserConfigurationException, SAXException, IOException {
		System.out.println("In parseDublinCore");
		DublinCoreDatastream dublinCoreDatastream = new DublinCoreDatastream(
				datastream);
		NodeList nodeList = document.getElementsByTagName("oai_dc:dc");
		Node node = nodeList.item(0);
		Element element = (Element) node;
		HashMap<DublinCore, String> dublinCoreMetadata = new HashMap<DublinCore, String>();
		for (DublinCore dc : DublinCore.values()) {
			String tagName = "dc:" + dc.getDescription();
			Node tag = element.getElementsByTagName(tagName).item(0);
			if (tag != null) {
				dublinCoreMetadata
						.put(dc, element.getElementsByTagName(tagName).item(0)
								.getTextContent());
			}

		}
		dublinCoreDatastream.setDublinCoreMetadata(dublinCoreMetadata);
		return dublinCoreDatastream;
	}

	private Datastream parseDatastream(int index,Node node,String tagPrefix)  throws FedoraException{
		Element element = (Element) node;
		Datastream datastream = new Datastream();
		DatastreamID dsID = DatastreamID.valueOf(element.getAttributes()
				.getNamedItem("dsID").getTextContent());
		String pid = element.getAttributes().getNamedItem("pid")
				.getTextContent();
		datastream.setPid(pid);
		datastream.setDatastreamID(dsID);
		String label = element.getElementsByTagName(tagPrefix+"dsLabel").item(0)
				.getTextContent();
		String version = element.getElementsByTagName(tagPrefix+"dsVersionID").item(0)
				.getTextContent();
		String created = element.getElementsByTagName(tagPrefix+"dsCreateDate").item(0)
				.getTextContent();
		String state = element.getElementsByTagName(tagPrefix+"dsState").item(0)
				.getTextContent();

		String[] mimeType = element.getElementsByTagName(tagPrefix+"dsMIME").item(0)
				.getTextContent().split("/");
		MediaType mediaType = new MediaType(mimeType[0], mimeType[1]);
		String formatURI = element.getElementsByTagName(tagPrefix+"dsFormatURI").item(0)
				.getTextContent();
		String controlGroup = element.getElementsByTagName(tagPrefix+"dsControlGroup")
				.item(0).getTextContent();
		String size = element.getElementsByTagName(tagPrefix+"dsSize").item(0)
				.getTextContent();
		String versionable = element.getElementsByTagName(tagPrefix+"dsVersionable")
				.item(0).getTextContent();
		String location = element.getElementsByTagName(tagPrefix+"dsLocation").item(0)
				.getTextContent();
		datastream.setLabel(label);
		
		try {
			datastream.setCreation(parseFedoraDateToJavaDate(created));
		} catch (ParseException e) {
			throw new FedoraException("Could not convert date" ,e);
		}
		datastream.setVersionID(version);
		datastream.setState(State.valueOf(state));

		datastream.setMediaType(mediaType);
		datastream.setFormatURI(formatURI);
		datastream.setControlGroup(controlGroup);
		datastream.setSize(Integer.parseInt(size));
		datastream.setVersionable(Boolean.parseBoolean(versionable));
		datastream.setLocation(location);
		System.out.println("Successfully parsed datastream object "
				+ datastream.toString());
		return datastream;
	}
	
	public Datastream parseGetDatastream() throws FedoraException{
		System.out.println("In parseDatastream");
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		Node node = nodeList.item(0);
		return parseDatastream(0, node,"");
		
//		Datastream datastream = new Datastream();
//		DatastreamId dsID = DatastreamId.valueOf(element.getAttributes()
//				.getNamedItem("dsID").getTextContent());
//		String pid = element.getAttributes().getNamedItem("pid")
//				.getTextContent();
//		datastream.setPid(pid);
//		datastream.setDatastreamIdentifier(dsID);
//		String label = element.getElementsByTagName("dsLabel").item(0)
//				.getTextContent();
//		String version = element.getElementsByTagName("dsVersionID").item(0)
//				.getTextContent();
//		String created = element.getElementsByTagName("dsCreateDate").item(0)
//				.getTextContent();
//		String state = element.getElementsByTagName("dsState").item(0)
//				.getTextContent();
//
//		String[] mimeType = element.getElementsByTagName("dsMIME").item(0)
//				.getTextContent().split("/");
//		MediaType mediaType = new MediaType(mimeType[0], mimeType[1]);
//		String formatURI = element.getElementsByTagName("dsFormatURI").item(0)
//				.getTextContent();
//		String controlGroup = element.getElementsByTagName("dsControlGroup")
//				.item(0).getTextContent();
//		String size = element.getElementsByTagName("dsSize").item(0)
//				.getTextContent();
//		String versionable = element.getElementsByTagName("dsVersionable")
//				.item(0).getTextContent();
//		String location = element.getElementsByTagName("dsLocation").item(0)
//				.getTextContent();
//		datastream.setLabel(label);
//		
//		try {
//			datastream.setCreation(parseFedoraDateToJavaDate(created));
//		} catch (ParseException e) {
//			throw new FedoraException(e);
//		}
//		datastream.setVersionID(version);
//		datastream.setState(State.valueOf(state));
//
//		datastream.setMediaType(mediaType);
//		datastream.setFormatURI(formatURI);
//		datastream.setControlGroup(controlGroup);
//		datastream.setSize(Integer.parseInt(size));
//		datastream.setVersionable(Boolean.parseBoolean(versionable));
//		datastream.setLocation(location);
//		System.out.println("Successfully parsed datastream object "
//				+ datastream.toString());
	}

	public List<Datastream> parseGetDatastreamHistory() throws FedoraException {
		List<Datastream> result = new ArrayList<Datastream>();
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		Node node;
		
		for (int index = 0; index < nodeList.getLength(); index++) {
			node = nodeList.item(index);
//			Datastream datastream = new Datastream();
//			Element element = (Element) node;
//			DatastreamId dsID = DatastreamId.valueOf(element
//					.getElementsByTagName("datastream").item(index)
//					.getAttributes().getNamedItem("dsid").getTextContent());
//			String pid = element.getElementsByTagName("datastream").item(index)
//					.getAttributes().getNamedItem("pid").getTextContent();
//			datastream.setPid(pid);
//			datastream.setDatastreamIdentifier(dsID);
//
//			String label = element.getElementsByTagName("dsLabel").item(index)
//					.getTextContent();
//			String version = element.getElementsByTagName("dsVersionID")
//					.item(index).getTextContent();
//			String created = element.getElementsByTagName("dsCreateDate")
//					.item(index).getTextContent();
//			String state = element.getElementsByTagName("dsState").item(index)
//					.getTextContent();
//			String[] mimeType = element.getElementsByTagName("dsMIME").item(index)
//					.getTextContent().split("/");
//			MediaType mediaType = new MediaType(mimeType[0], mimeType[1]);
//			String formatURI = element.getElementsByTagName("dsFormatURI")
//					.item(index).getTextContent();
//			String controlGroup = element
//					.getElementsByTagName("dsControlGroup").item(index)
//					.getTextContent();
//			String size = element.getElementsByTagName("dsSize").item(index)
//					.getTextContent();
//			String versionable = element.getElementsByTagName("dsVersionable")
//					.item(index).getTextContent();
//
//			String location = element.getElementsByTagName("dsLocation")
//					.item(index).getTextContent();
//
//			datastream.setLabel(label);
//			try {
//				datastream.setCreation(parseFedoraDateToJavaDate(created));
//			} catch (ParseException e) {
//				throw new FedoraException(e);
//			}
//			
//			datastream.setVersionID(version);
//			datastream.setState(State.valueOf(state));
//			datastream.setMediaType(mediaType);
//			datastream.setFormatURI(formatURI);
//			datastream.setControlGroup(controlGroup);
//			datastream.setSize(Integer.parseInt(size));
//			datastream.setVersionable(Boolean.parseBoolean(versionable));
//			datastream.setLocation(location);

			result.add(parseDatastream(index, node,""));
		}

		return result;
	}

	public List<Datastream> parseGetDatastreams() throws FedoraException {
		List<Datastream> datastreams = new ArrayList<Datastream>();
		NodeList nodeList = document.getElementsByTagName("datastreamProfile");
		for (int index = 0; index < nodeList.getLength(); index++) {
			Node node = nodeList.item(index);
			Datastream datastream = new Datastream();
//			Element element = (Element) node;
//			DatastreamId dsID = DatastreamId.valueOf(element
//					.getElementsByTagName("datastream").item(index)
//					.getAttributes().getNamedItem("dsid").getTextContent());
//			String pid = element.getElementsByTagName("datastream").item(index)
//					.getAttributes().getNamedItem("pid").getTextContent();
//			datastream.setPid(pid);
//			datastream.setDatastreamIdentifier(dsID);
//			String label = element.getElementsByTagName("apim:dsLabel")
//					.item(index).getTextContent();
//			String version = element.getElementsByTagName("apim:dsVersionID")
//					.item(index).getTextContent();
//			String created = element.getElementsByTagName("apim:dsCreateDate")
//					.item(index).getTextContent();
//			String state = element.getElementsByTagName("apim:dsState")
//					.item(index).getTextContent();
//			String[] mimeType = element.getElementsByTagName("apim:dsMIME")
//					.item(0).getTextContent().split("/");
//			MediaType mediaType = new MediaType(mimeType[0], mimeType[1]);
//			String formatURI = element.getElementsByTagName("apim:dsFormatURI")
//					.item(index).getTextContent();
//			String controlGroup = element
//					.getElementsByTagName("apim:dsControlGroup").item(index)
//					.getTextContent();
//			String size = element.getElementsByTagName("apim:dsSize")
//					.item(index).getTextContent();
//			String versionable = element
//					.getElementsByTagName("apim:dsVersionable").item(index)
//					.getTextContent();
//
//			String location = element.getElementsByTagName("apim:dsLocation")
//					.item(index).getTextContent();
//
//			datastream.setLabel(label);
//			
//			try {
//				datastream.setCreation(parseFedoraDateToJavaDate(created));
//			} catch (ParseException e) {
//				throw new FedoraException(e);
//			}
//			
//			datastream.setVersionID(version);
//			datastream.setState(State.valueOf(state));
//			datastream.setMediaType(mediaType);
//			datastream.setFormatURI(formatURI);
//			datastream.setControlGroup(controlGroup);
//			datastream.setSize(Integer.parseInt(size));
//			datastream.setVersionable(Boolean.parseBoolean(versionable));
//			datastream.setLocation(location);

			datastreams.add(parseDatastream(index, node, "apim:"));
		}
		return datastreams;
	}
	

	private Date parseFedoraDateToJavaDate(String fedoraDate) throws ParseException{
		StringBuilder fed = new StringBuilder(fedoraDate);
		fed.deleteCharAt(fed.indexOf("Z"));
		fed.replace(fed.indexOf("T"),fed.indexOf("T")+1," ");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = formatter.parse(fed.toString());
		return date;
	}
}