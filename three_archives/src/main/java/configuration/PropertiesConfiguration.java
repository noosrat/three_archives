package configuration;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesConfiguration {

	public static String generatePropertiesFile(HashMap<String, String> archiveDetails) throws Exception {
		String name = archiveDetails.get("archive.name").replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
		FileOutputStream output = null;
		Properties specificProperties = new Properties();
		try {
			output = new FileOutputStream("src/main/resources/configuration/" + name + ".properties");
			for (String property : archiveDetails.keySet()) {
				specificProperties.put(property, archiveDetails.get(property));
			}
			specificProperties.store(output, null);
		} catch (Exception ex) {
			System.out.println("Something has gone wrong when generating the properties file");
			System.out.println(ex);
			throw ex;
		} finally {
			output.close();
		}

//		copyImageIntoNewCollection(archiveDetails.get("archive.landingpage.image"),name);
		return name;

	}
//
//	private static String copyImageIntoNewCollection(String file, String archive) {
//		// we need to get the file name from the
//		int dot = file.lastIndexOf(".");
//		String newFile = archive + file.substring(dot);
//		File movedImage = new File("src/main/webpapp/images/" + newFile);
//		ImageInputStream in =null;
//		ImageOutputStream out =null;
//		try {
//			in = new FileImageInputStream(new File(file));
//			out = new FileImageOutputStream(movedImage);
//
//			byte[] buf = new byte[1024];
//			int len = 0;
//			while ((len - in.read(buf)) > 0) {
//				out.write(buf, 0, len);
//			}
//		} catch (Exception ex) {
//			System.out.println("Something went wrong");
//		} finally {
//			try {
//				in.close();
//				out.close();
//			} catch (Exception ex) {
//				System.out.println("could not close resources");
//			}
//		}
//
//		System.out.println("PATH OF THE IMAGE " + movedImage.getPath());
//		return movedImage.getPath();
//	}

}
