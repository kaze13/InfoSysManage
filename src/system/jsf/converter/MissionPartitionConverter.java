package system.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import system.po.MissionPartition;
import system.vo.PartitionWrap;


@FacesConverter(value="missionPartitionConverter")
public class MissionPartitionConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		String[] split = arg2.split("\\=|\\,");
		String id = split[1];
		String missionid = split[3];
		String leaderName = split[5];
		String title = split[7];
		String description = split[9];
		String fileId = split[11];
		int progress;
		if(arg2.endsWith("*"))
		{
			progress = Integer.parseInt((split[13]).substring(0, split[13].length()-2));
			MissionPartition tmp = new MissionPartition(missionid, leaderName, title, description,fileId);
			tmp.setId(id);
			tmp.setProgress(progress);
			return new PartitionWrap(tmp);
		}
		else
		{
			progress = Integer.parseInt((split[13]).substring(0, split[13].length()-1));
			MissionPartition tmp = new MissionPartition(missionid, leaderName, title, description,fileId);
			tmp.setId(id);
			tmp.setProgress(progress);
			return tmp;
		}

	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {

		if(obj instanceof MissionPartition)
			return obj.toString();
		if(obj instanceof PartitionWrap)
			return ((PartitionWrap)obj).getPartition().toString()+"*";
		return null;
	}

}
