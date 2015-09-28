package uploads;
import java.io.File;

import common.fedora.*;


public class UploadService {
	
	public void upload(String files_string, String storage_path,String archive)
	{	
		UploadClient client=new UploadClient("http://localhost:8080/fedora", "fedoraAdmin","fedoraAdmin",null);
		String filename;
		String title;
		String creator;
		String event;
		String subject="<dc:subject>"+archive+"</dc:subject>";//!!archive
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
		String delimeter="%%";
		File file2;
		String file_path;
		files=files_string.split(delimeter);//split up the different files
				
		for(int i=0;i<files.length;i++)//Iterate through the files to upload each one
		{
			String PID;
			//extract the metadata
			String delimeter2="%";
			String[] metadataArray;
			
			metadataArray =files[i].split(delimeter2);
			
			filename =metadataArray[0].trim();
			title ="<dc:title>"+metadataArray[1].trim()+"</dc:title>";
			creator ="<dc:creator>"+metadataArray[2].trim()+"</dc:creator>";
			event =metadataArray[3].trim();//event
			
			publisher ="<dc:publisher>"+metadataArray[5].trim()+"</dc:publisher>";
			contributor ="<dc:contributor>"+metadataArray[6].trim()+"</dc:contributor>";
			date ="<dc:date>"+metadataArray[7].trim()+"</dc:date>";
			resourcetype ="<dc:type>"+metadataArray[8].trim()+"</dc:type>";
			String noXMLformat=metadataArray[9].trim();
			format ="<dc:format>"+metadataArray[9].trim()+"</dc:format>";
			source ="<dc:source>"+metadataArray[10].trim()+"</dc:source>";
			language ="<dc:language>"+metadataArray[11].trim()+"</dc:language>";
			relation ="<dc:relation>"+metadataArray[12].trim()+"</dc:relation>";
			location =metadataArray[13].trim();
			rights ="<dc:rights>"+metadataArray[14].trim()+"</dc:rights>";
			collection =metadataArray[15].trim();
			cords =metadataArray[16].trim();
			
			description ="<dc:description>"+"% collection:"+collection+ "% event:"+event+"%"+metadataArray[4].trim()+"% annotation: %"+"</dc:description>";
			coverage ="<dc:coverage>"+"% "+location+" % " +cords+" %</dc:coverage>";
		
			
			String meta=title+description+creator+publisher+contributor+date+resourcetype+format+source+language+relation+coverage+rights+subject;
			
			String qot="\"";
			String startTag="<oai_dc:dc xmlns:oai_dc="+qot+"http://www.openarchives.org/OAI/2.0/oai_dc/"+qot+ " xmlns:dc="+qot+"http://purl.org/dc/elements/1.1/" +qot+" xmlns:xsi="+qot+"http://www.w3.org/2001/XMLSchema-instance"+qot+" xsi:schemaLocation="+qot+"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd"+qot+">";
			String endTag="</oai_dc:dc>";
			try {
				//create new object
				file_path=storage_path+filename;
				file2= new File(file_path);//
				//get next PID
				PID="KEL:"+i;
				client.POST("/objects/"+PID+"/");//
				//add the image to the object
				if(noXMLformat.equals("image/jpeg"))
				{
					client.POST("/objects/"+PID+"/datastreams/IMG?controlGroup=M&mimeType="+noXMLformat,file2,false);
				}
				else if (noXMLformat.equals("video/mp4"))
				{
					client.POST("/objects/"+PID+"/datastreams/VID?controlGroup=M&mimeType="+noXMLformat,file2,false);
				}
				else if (noXMLformat.equals("audio/mp3"))
				{
					client.POST("/objects/"+PID+"/datastreams/AUD?controlGroup=M&mimeType="+noXMLformat,file2,false);
				}
				
				//add the metadata to the object
				client.PUT("/objects/"+PID+"/datastreams/DC?controlGroup=M&mimeType=text/xml",startTag+meta+endTag);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//send this information to the client who will then upload the file

		}			
	}
	
	public String getPID(String archive, int pid)
	{
		String tag;
		int num=pid;
		if (archive.equals("Harfield"))
		{
			tag="hv";
		}
		else if(archive.equals("Snaps"))
		{
			tag="ms";
		}
		else if (archive.equals("MissGay"))
		{
			tag="sss";
		}
		else if (archive.equals("SpringQueen"))
		{
			tag="sss";
		}
		else
		{
			tag="nul";
		}
		return tag+":"+num;//TODO
	}

}
