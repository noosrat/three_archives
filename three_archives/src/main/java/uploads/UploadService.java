package uploads;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.io.FileUtils;
import org.xml.sax.InputSource;
import common.fedora.UploadClient;

public class UploadService {

	public void upload(String files_string, String storage_path, String archive) {
		UploadClient client = new UploadClient("http://personalhistories.cs.uct.ac.za:8089/fedora", "fedoraAdmin", "3Arch", null);
		String filename;
		String title;
		String creator;
		String event;
		String subject = "<dc:subject>" + archive + "</dc:subject>";// !!archive
		String description;
		String publisher;
		String contributor;
		String date;
		String resourcetype;
		String format;
		String source;
		String language;
		String relation;
		String coverage;
		String rights;
		String collection;
		String location;
		String cords;
		String files[];
		String delimeter = "%%";
		File file2;
		String file_path;
		files = files_string.split(delimeter);// split up the different files

		for (int i = 0; i < files.length; i++)// Iterate through the files to
												// upload each one
		{
			String PID;
			// extract the metadata
			String delimeter2 = "%";
			String[] metadataArray;

			metadataArray = files[i].split(delimeter2);
			filename = metadataArray[0].trim();
			//System.out.println(file_path);
			System.out.println(filename);
			title = "<dc:title>" + metadataArray[1].trim() + "</dc:title>";
			creator = "<dc:creator>" + metadataArray[2].trim() + "</dc:creator>";
			event = metadataArray[3].trim();// event
			publisher = "<dc:publisher>" + metadataArray[5].trim() + "</dc:publisher>";
			contributor = "<dc:contributor>" + metadataArray[6].trim() + "</dc:contributor>";
			date = "<dc:date>" + metadataArray[7].trim() + "</dc:date>";
			resourcetype = "<dc:type>" + metadataArray[8].trim() + "</dc:type>";
			String noXMLformat = metadataArray[9].trim();
			format = "<dc:format>" + metadataArray[9].trim() + "</dc:format>";
			source = "<dc:source>" + metadataArray[10].trim() + "</dc:source>";
			language = "<dc:language>" + metadataArray[11].trim() + "</dc:language>";
			relation = "<dc:relation>" + metadataArray[12].trim() + "</dc:relation>";
			location = metadataArray[13].trim();
			rights = "<dc:rights>" + metadataArray[14].trim() + "</dc:rights>";
			collection = metadataArray[15].trim();
			cords = metadataArray[16].trim();
			description = "<dc:description>" + "% collection:" + collection + "% event:" + event + "%"
					+ metadataArray[4].trim() + "% annotation: %" + "</dc:description>";
			coverage = "<dc:coverage>" + "% " + location + " % " + cords + " %</dc:coverage>";

			String meta = title + description + creator + publisher + contributor + date + resourcetype + format
					+ source + language + relation + coverage + rights + subject;

			String qot = "\"";
			String startTag = "<oai_dc:dc xmlns:oai_dc=" + qot + "http://www.openarchives.org/OAI/2.0/oai_dc/" + qot
					+ " xmlns:dc=" + qot + "http://purl.org/dc/elements/1.1/" + qot + " xmlns:xsi=" + qot
					+ "http://www.w3.org/2001/XMLSchema-instance" + qot + " xsi:schemaLocation=" + qot
					+ "http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd" + qot
					+ ">";
			String endTag = "</oai_dc:dc>";
			try {
				// create new object
				file_path = storage_path + filename;
				System.out.println("**Finding the file");
				file2 = new File(file_path);//
				System.out.println(file_path);
				System.out.println("**File found and created");
				// get next PID

				HttpMethod newPIDs = client.POST("/objects/nextPID?format=XML");
				InputStream newPID_xml = newPIDs.getResponseBodyAsStream();
				String newPID_xml_String = getStringFromInputStream(newPID_xml);

				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(newPID_xml_String));

				org.w3c.dom.Document doc = builder.parse(src);
				String changeme_pid = doc.getElementsByTagName("pid").item(0).getTextContent();

				String newPID = getPID(archive, changeme_pid);
				System.out.println(newPID);

				// File url = new File (file_path);

				// File dest= new
				// File("/home/nox/Applications/apache/apache-tomcat-8.0.24/webapps/content/data/test");
				// FileUtils.copyFileToDirectory(url, dest);

				// File useThis=new
				// File("/home/nox/Applications/apache/apache-tomcat-8.0.24/webapps/content/data/test/"+filename);

				// File useThis=new
				// File("/home/nox/Applications/apache/apache-tomcat-8.0.24/webapps/data/content/"+archive+"/"+filename);
				System.out.println("Started adding a new object");
				client.POST("/objects/" + newPID + "/");//
				System.out.println("added a new object");
				// add the image to the object
				if (noXMLformat.equals("image/jpeg")) {
					client.POST("/objects/" + newPID + "/datastreams/IMG?controlGroup=M&mimeType="+noXMLformat, file2,false);
				} else if (noXMLformat.equals("video/mp4")) {
					client.POST("/objects/" + newPID + "/datastreams/VID?controlGroup=M&mimeType=" + noXMLformat, file2,false);
				} else if (noXMLformat.equals("audio/mp3")) {
					client.POST("/objects/" + newPID + "/datastreams/AUD?controlGroup=M&mimeType=" + noXMLformat, file2,false);
				}

				// add the metadata to the object
				client.PUT("/objects/" + newPID + "/datastreams/DC?controlGroup=M&mimeType=text/xml",
						startTag + meta + endTag);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// send this information to the client who will then upload the file
		}
	}

	public static String getPID(String archive, String pid) {
		String[] parameters = pid.split(":");
		String tag;
		String num = parameters[1];
		if (archive.equals("harfield_village")) {
			tag = "hv";
		} else if (archive.equals("snaps")) {
			tag = "ms";
		} else if (archive.equals("miss_gay")) {
			tag = "sss";
		} else if (archive.equals("spring_queen")) {
			tag = "sss";
		} else {
			tag = "nul";
		}
		return tag + ":" + num;
	}

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}
