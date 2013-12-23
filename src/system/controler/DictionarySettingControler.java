package system.controler;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.event.RowEditEvent;

import system.interceptor.TransactionInterceptor;
import system.manager.impl.DicManager;
import system.po.DictionaryValue;
import system.service.DictionaryServiceImpl;

@Named
@SessionScoped
public class DictionarySettingControler implements Serializable{

	@Inject
	private DictionaryServiceImpl dictionaryService;
	@Inject
	private DicManager dicManager;
	private List<DictionaryValue> dictionaryValues;
	private List<DictionaryValue> filteredValues;
	private DictionaryValue newMapping = new DictionaryValue();

	@PostConstruct
	public void init() throws Exception {
		dictionaryValues = dictionaryService.findAllByCondition(null);
	}

	@Interceptors(TransactionInterceptor.class)
	public void addMapping() throws Exception {
		dictionaryService.save(newMapping);
		dictionaryValues.add(newMapping);
		dicManager.put(newMapping.getDickey(), newMapping.getValue());
		newMapping = new DictionaryValue();
	}

	@Interceptors(TransactionInterceptor.class)
	public void delete(DictionaryValue mapping) throws Exception {
		dictionaryService.delete(mapping.getDickey());
		dicManager.remove(mapping.getDickey());
		dictionaryValues.remove(mapping);
		filteredValues.remove(mapping);
	}


	@Interceptors(TransactionInterceptor.class)
	public void onEdit(RowEditEvent event) throws Exception {
		dictionaryService.update((DictionaryValue) event.getObject());
	}

	public List<DictionaryValue> getDictionaryValues() {
		return dictionaryValues;
	}

	public void setDictionaryValues(List<DictionaryValue> dictionaryValues) {
		this.dictionaryValues = dictionaryValues;
	}

	public DictionaryValue getNewMapping() {
		return newMapping;
	}

	public void setNewMapping(DictionaryValue newMapping) {
		this.newMapping = newMapping;
	}

	public List<DictionaryValue> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<DictionaryValue> filteredValues) {
		this.filteredValues = filteredValues;
	}

}
