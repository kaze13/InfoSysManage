package system.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import system.po.SystemFunction;


@FacesConverter(value="systemFunctionConverter")
public class SystemFunctionConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		String[] split = arg2.split("\\=|\\,");
		String id = split[1];
		String systemName = split[3];
		String functionName = split[5];
		String description = split[7].substring(0,split[7].length()-1);

		SystemFunction result = new SystemFunction(systemName, functionName, description);
		result.setId(id);
		return result;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		return arg2.toString();
	}

}
