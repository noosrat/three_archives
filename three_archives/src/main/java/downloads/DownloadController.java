package downloads;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import maps.Map;
import common.controller.Controller;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;

public class DownloadController implements Controller{

	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Download download = new Download();
		ArrayList<String> cart = (ArrayList<String>) request.getSession().getAttribute("MEDIA_CART");
		Set<FedoraDigitalObject> cartObjects = download.getFedoraDigitalObjects(cart);
		
		request.setAttribute("cart", cartObjects);
		//FedoraDigitalObject ob = populateFedoraDigitalObject(${cart})
		if (request.getPathInfo().substring(1).contains("checkout")){
			 
			 response.setContentType("application/zip");
		     response.setHeader("Content-Disposition", "attachment; filename=\"allfiles.zip\"");

			
			
			
			/*HttpClient httpClient = new HttpClient();
		        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);


		        HttpGet httpGet = new HttpGet("http://localhost:8080/fedora/objects/ms:1/datastreams/IMG/content?download=true&format=xml");
		       // HttpResponse response = httpClient.execute(httpGet);

		        System.out.println(response.getStatus());

		        InputStream in = ((HttpResponse) response).getEntity().getContent();*/
		     	
		     	
		        
		        
		        //String tDir = System.getProperty("java.io.tmpdir"); String path = tDir + "tmp" + ".pdf"; f = new File(path); f.deleteOnExit();
		        
		       
		        
		        
		        ServletOutputStream out = response.getOutputStream();
				ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));
				int k=0;
				for (FedoraDigitalObject object: cartObjects){
					k++;
					URL url = new URL(object.getDatastreams().get("IMG").getContent());
					File f = new File(((DublinCoreDatastream) object.getDatastreams().get("DC")).getTitle()+".jpg") ;
					
					FileUtils.copyURLToFile(url, f);
					
				System.out.println("Adding " + f.getName());
				zos.putNextEntry(new ZipEntry(f.getName()));
				
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(f);

				} catch (FileNotFoundException fnfe) {
					// If the file does not exists, write an error entry instead of
					// file
					// contents
					zos.write(("ERRORld not find file " + f.getName())
							.getBytes());
					zos.closeEntry();
					System.out.println("Couldfind file "
							+ f.getAbsolutePath());
					
				}

				BufferedInputStream fif = new BufferedInputStream(fis);

				// Write the contents of the file
				int data = 0;
				while ((data = fif.read()) != -1) {
					zos.write(data);
				}
				fif.close();

				zos.closeEntry();
				System.out.println("Finishedng file " + f.getName());
		     	}
				zos.close();
				
				byte []b;


		       /* File []s=f.listFiles();
		        for(int i=0;i<s.length;i++)
		        {
		        	b=new byte[1024];
		            FileInputStream fin=new FileInputStream(s[i]);
		            zout.putNextEntry(new ZipEntry(s[i].getName()));
		            int length;
		            while((length=fin.read(b, 0, 1024))>0)
		            {
		                zout.write(b,0,length);
		            }
		            zout.closeEntry();
		            fin.close();
		        }
		        zout.close();
			
			
			
			
			download.downloadFedoraDigitalObjects(cartObjects);*/
			return "WEB-INF/frontend/downloads/cart.jsp";
		}
		else {
			return "WEB-INF/frontend/downloads/cart.jsp";}
	}

}
