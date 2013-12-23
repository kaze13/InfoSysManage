package system.manager.spec;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Dictionary {
	public String get(String key) throws FileNotFoundException, IOException;
	public String put(String key, String value);
	public void clear();
}
