package system.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import system.po.System;

@FacesConverter(value = "systemConverter")
public class SystemConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		String[] split = arg2.split("\\=|\\,");
		String name = split[1];
		String url = split[3];
		String description = split[5];
		String warPath = split[7];
		String duration = split[9];
		System.Status status = System.Status.valueOf(split[11].substring(0,
				split[11].length() - 1));

		System result = new System(name, url, description, warPath, duration,
				status);
		return result;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		return arg2.toString();
	}

}
