package system.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyManager {

	private static Properties property = new Properties();

	public static void init() {

	}

	public static Properties getApplicationProperties() throws IOException {
		property.clear();
		property.load(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("/resources/application.properties"));

		return property;
	}

	public static Properties getAppMessageProperties(String appName) throws IOException
	{
		property.clear();
//		String path = "/resources/appmessage/" + appName + ".properties";
//		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		String path = "c:\\appmessage\\" + appName + ".properties";
		InputStream stream = new FileInputStream(path);
		property.load(stream);
		stream.close();
		return property;



	}

	public static void setAppMessageProperties(String appName, Properties property) throws IOException
	{
		String path = "c:\\appmessage\\" + appName + ".properties";
		OutputStream stream  = new FileOutputStream(path);
		property.store(stream, "test comment");
		stream.close();
	}
}
