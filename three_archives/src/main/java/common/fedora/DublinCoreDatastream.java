package common.fedora;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DublinCoreDatastream extends Datastream {

	//we have the dublin core metadata fields within this...maybe it should just be an array list? or map...we need to parse the content
	private HashMap<DublinCore,String> dublinCoreMetadata;

	public DublinCoreDatastream(String pid) {
		super(pid, DatastreamId.DC);
	}

	
	public void setDublinCoreMetadata(HashMap<DublinCore, String> dublinCoreMetadata) {
		this.dublinCoreMetadata = dublinCoreMetadata;
	}


	public HashMap<DublinCore,String> getDublinCoreMetadata(){
		return dublinCoreMetadata;
	}
	public void parseXMLContentToMetadataFields() throws ParserConfigurationException, SAXException, IOException{
		String content = getContent();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(content);
		document.getDocumentElement().normalize();
		
		NodeList nodeList = document.getElementsByTagName("oai_dc:dc");
		Node node = nodeList.item(0);
		Element element = (Element)node;
		for (DublinCore dc: DublinCore.values()){
			String tagName = "dc:" + dc.getDescription();
			NodeList tag = element.getElementsByTagName(tagName);
			if (tag!=null){
//				dublinCoreMetadata.put(dc, element.getElementsByTagName(tagName).item(0).getTextContent());
			}
			
		}
	}
}
