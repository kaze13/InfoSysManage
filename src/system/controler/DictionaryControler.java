package system.controler;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import system.manager.spec.Dictionary;


@Named("dic")
@RequestScoped
public class DictionaryControler implements Serializable {



	/**
	 *
	 */
	private static final long serialVersionUID = 5113909141090881256L;
	@Inject
	private Dictionary dic;

	public DictionaryControler(){

	}

	public Map<String, String> getDic(){
		return (Map<String, String>)dic;
	}

	public void reset() {
		dic.clear();
	}

}
