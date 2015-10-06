package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesHandler {

	private final Properties configurationProperties;

	public PropertiesHandler(String fileName) {
		configurationProperties = new Properties();
		try {
			InputStream in = new FileInputStream(new File(fileName));
			configurationProperties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		return configurationProperties.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return configurationProperties.stringPropertyNames();
	}

	public boolean containsKey(String key) {
		return configurationProperties.containsKey(key);
	}

	public String toString(){
		String result = "" ;
		for (String prop: getAllPropertyNames()){
			result += prop + " = " + getProperty(prop) + "\n";
		}
		return result;
	}
}
