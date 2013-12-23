package system.service;

import java.io.Serializable;

import system.po.DictionaryValue;

public class DictionaryServiceImpl extends AbstractDataAccessService<DictionaryValue>
implements Serializable{



	public DictionaryServiceImpl() {
		super(DictionaryValue.class);
	}

}
