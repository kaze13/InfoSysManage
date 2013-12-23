package system.manager.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import system.manager.spec.Dictionary;
import system.manager.spec.SessionManager;
import system.po.DictionaryValue;
import system.service.DictionaryServiceImpl;
import system.util.PropertyManager;

@Named
@SessionScoped
public class DicManager implements Dictionary, Map<String, String>,
		Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3572895049303185165L;

	@Inject
	SessionManager sessionManager;
	@Inject
	DictionaryServiceImpl dictionaryService;
	private Map<String, String> enMap;
	private Map<String, String> japMap;
	private Properties enProperty;
	private Properties japProperty;

	// private String enPath =
	// "C:/Users/Administrator/Workspaces/rd34/src/resources/application.properties";
	// private String japPath =
	// "C:/Users/Administrator/Workspaces/rd34/src/resources/application_ja.properties";

	public DicManager() {

	}

	@PostConstruct
	public void init() throws Exception {
		enMap = new HashMap<String, String>();
		enProperty = new Properties();
		// InputStream enStream = new FileInputStream(FacesContext
		// .getCurrentInstance().getExternalContext()
		// .getRealPath("application.properties"));
		// enProperty.load(Thread.currentThread().getContextClassLoader()
		// .getResourceAsStream("/resources/application.properties"));
		// Set<Object> keySet = enProperty.keySet();

		// List<DictionaryValue> dictionarys = new ArrayList<DictionaryValue>();
		// for (Object obj : keySet) {
		// enMap.put((String) obj, (String) enProperty.get(obj));
		// dictionarys.add(new DictionaryValue((String) obj,
		// (String) enProperty.get(obj)));
		// }
		// dictionaryService.save(dictionarys);
		List<DictionaryValue> valueList = dictionaryService
				.findAllByCondition(null);
		for (DictionaryValue d : valueList) {
			enMap.put(d.getDickey(), d.getValue());
		}

	}

	@Override
	public String get(Object key) {
		try {
			return get((String) key);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int size() {
		return enMap.size();
	}

	@Override
	public boolean isEmpty() {
		return enMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return enMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return enMap.containsValue(value);
	}

	@Override
	public String remove(Object key) {
		return enMap.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		enMap.putAll(m);
	}

	@Override
	public Set<String> keySet() {
		return enMap.keySet();
	}

	@Override
	public Collection<String> values() {
		return enMap.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return enMap.entrySet();
	}

	@Override
	public String get(String key) throws IOException {
		// TODO You need to implement a program(only R&D4) to get the value of a
		// dictionary based on the information session.
		if (enMap.containsKey(key))
			return enMap.get(key);
		else {

			// OutputStream stream = new FileOutputStream(enPath);
			// String defaultStr = key.substring(0,1);
			// for(int i = 1; i < key.length(); ++i)
			// {
			// if(Character.isUpperCase(key.charAt(i)))
			// {
			// defaultStr += " ";
			// }
			// defaultStr += key.charAt(i);
			// }
			// enMap.put(key, defaultStr);
			// enProperty.setProperty(key, defaultStr);
			// enProperty.store(stream, null);
			return key;
		}
		// return enMap.get(key);

	}

	@Override
	public String put(String key, String value) {
		return enMap.put(key, value);
	}

	@Override
	public void clear() {
		enMap.clear();
	}

}
