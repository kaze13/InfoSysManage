package system.jsf.converter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import system.po.MissionPartition;
import system.po.MissionUnit;
import system.vo.PartitionWrap;
import system.vo.UnitWrap;

@FacesConverter(value = "missionUnitConverter")
public class MissionUnitConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		String[] split = arg2.split("\\=|\\,");
		String id = split[1];
		String partitionid = split[3];
		String leaderName = split[5];
		String title = split[7];
		String description = null;
		try {
			description = URLDecoder.decode(split[9], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String fileId = split[11];
		int progress;

		// if(arg2.endsWith("*"))
		// {
		progress = Integer.parseInt((split[13]).substring(0,
				split[13].length() - 1));
		MissionUnit tmp = new MissionUnit(partitionid, leaderName, title,
				description, fileId);
		tmp.setId(id);
		tmp.setProgress(progress);
		return new UnitWrap(tmp);
		// }
		// else
		// {
		// progress = Integer.parseInt((split[13]).substring(0,
		// split[13].length()-1));
		// MissionUnit tmp = new MissionUnit(partitionid, leaderName, title,
		// description,fileId);
		// tmp.setId(id);
		// tmp.setProgress(progress);
		// return tmp;
		// }
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof MissionUnit)
			return obj.toString();
		if (obj instanceof UnitWrap)
			return ((UnitWrap) obj).getUnit().toString() + "*";
		return obj.toString();
	}

}
