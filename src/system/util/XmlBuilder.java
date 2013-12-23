package system.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import system.manager.spec.SessionManager;
import system.po.DependentFunction;
import system.po.DependentSystem;
import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.UploadFile;
import system.service.DependentFunctionServiceImpl;
import system.service.DependentSystemServiceImpl;
import system.service.MissionPartitionServiceImpl;
import system.service.MissionUnitServiceImpl;
import system.service.UploadFileServiceImpl;

public class XmlBuilder implements Serializable {

	@Inject
	private MissionPartitionServiceImpl missionPartitionSerivce;
	@Inject
	private MissionUnitServiceImpl missionUnitService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	private DependentSystemServiceImpl dependentService;
	@Inject
	private DependentFunctionServiceImpl dependentFunctionService;
	@Inject
	private SessionManager sessionManager;

	public UploadFile export(Mission mission) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element missionRoot = doc.createElement("Mission");
		doc.appendChild(missionRoot);

		Attr leaderNameAttr = doc.createAttribute("leaderName");
		leaderNameAttr.setValue(mission.getLeaderName());
		missionRoot.setAttributeNode(leaderNameAttr);

		Attr descriptionAttr = doc.createAttribute("description");
		descriptionAttr.setValue(mission.getDescription());
		missionRoot.setAttributeNode(descriptionAttr);

		Attr titleAttr = doc.createAttribute("title");
		titleAttr.setValue(mission.getTitle());
		missionRoot.setAttributeNode(titleAttr);

		List<MissionPartition> partitionList = missionPartitionSerivce
				.findPartitionWithMission(mission.getId());
		List<DependentFunction> missionDependentFunction = dependentFunctionService
				.findFunctionByTarget(mission.getId());
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("target_id", mission.getId());
		List<DependentSystem> missionDependentSystems = dependentService
				.findAllByCondition(sqlWhereMap);
		for(DependentSystem ds:missionDependentSystems)
		{
			Element systemElement = doc.createElement("System");
			missionRoot.appendChild(systemElement);
			
			Attr systemNameAttr = doc.createAttribute("systemName");
			systemNameAttr.setValue(ds.getSystemName());
			systemElement.setAttributeNode(systemNameAttr);
		}
		for(DependentFunction df:missionDependentFunction)
		{
			Element functionElement = doc.createElement("Function");
			missionRoot.appendChild(functionElement);
			
			Attr functionIdAttr = doc.createAttribute("functionId");
			functionIdAttr.setValue(df.getFunctionId());
			functionElement.setAttributeNode(functionIdAttr);
		}
		for (MissionPartition partition : partitionList) {
			Element partitionElement = doc.createElement("Partition");
			missionRoot.appendChild(partitionElement);

			Attr partitionleaderNameAttr = doc.createAttribute("leaderName");
			partitionleaderNameAttr.setValue(partition.getLeaderName());
			partitionElement.setAttributeNode(partitionleaderNameAttr);

			Attr partitionDescriptionAttr = doc.createAttribute("description");
			partitionDescriptionAttr.setValue(partition.getDescription());
			partitionElement.setAttributeNode(partitionDescriptionAttr);

			Attr partitionTitleAttr = doc.createAttribute("title");
			partitionTitleAttr.setValue(partition.getTitle());
			partitionElement.setAttributeNode(partitionTitleAttr);

			List<MissionUnit> unitList = missionUnitService
					.findUnitWithPartition(partition.getId());

			List<DependentFunction> partitionDependentFunction = dependentFunctionService
					.findFunctionByTarget(partition.getId());
			sqlWhereMap.clear();
			sqlWhereMap.put("target_id", partition.getId());
			List<DependentSystem> partitionDependentSystems = dependentService
					.findAllByCondition(sqlWhereMap);
			for(DependentSystem ds:partitionDependentSystems)
			{
				Element systemElement = doc.createElement("System");
				partitionElement.appendChild(systemElement);
				
				Attr systemNameAttr = doc.createAttribute("systemName");
				systemNameAttr.setValue(ds.getSystemName());
				systemElement.setAttributeNode(systemNameAttr);
			}
			for(DependentFunction df:partitionDependentFunction)
			{
				Element functionElement = doc.createElement("Function");
				partitionElement.appendChild(functionElement);
				
				Attr functionIdAttr = doc.createAttribute("functionId");
				functionIdAttr.setValue(df.getFunctionId());
				functionElement.setAttributeNode(functionIdAttr);
			}
			for (MissionUnit unit : unitList) {
				Element unitElement = doc.createElement("Unit");
				partitionElement.appendChild(unitElement);

				Attr unitleaderNameAttr = doc.createAttribute("leaderName");
				unitleaderNameAttr.setValue(unit.getLeaderName());
				unitElement.setAttributeNode(unitleaderNameAttr);

				Attr unitDescriptionAttr = doc.createAttribute("description");
				unitDescriptionAttr.setValue(unit.getDescription());
				unitElement.setAttributeNode(unitDescriptionAttr);

				Attr unitTitleAttr = doc.createAttribute("title");
				unitTitleAttr.setValue(unit.getTitle());
				unitElement.setAttributeNode(unitTitleAttr);
				
				List<DependentFunction> unitDependentFunction = dependentFunctionService
						.findFunctionByTarget(unit.getId());
				sqlWhereMap.clear();
				sqlWhereMap.put("target_id", unit.getId());
				List<DependentSystem> unitDependentSystems = dependentService
						.findAllByCondition(sqlWhereMap);
				for(DependentSystem ds:unitDependentSystems)
				{
					Element systemElement = doc.createElement("System");
					unitElement.appendChild(systemElement);
					
					Attr systemNameAttr = doc.createAttribute("systemName");
					systemNameAttr.setValue(ds.getSystemName());
					systemElement.setAttributeNode(systemNameAttr);
				}
				for(DependentFunction df:unitDependentFunction)
				{
					Element functionElement = doc.createElement("Function");
					unitElement.appendChild(functionElement);
					
					Attr functionIdAttr = doc.createAttribute("functionId");
					functionIdAttr.setValue(df.getFunctionId());
					functionElement.setAttributeNode(functionIdAttr);
				}
			}
		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		String fileName = "mission_" + mission.getId() + ".xml";
		File file = new File("C:/upload/mission/" + fileName);
		file.mkdirs();
		file.createNewFile();
		StreamResult result = new StreamResult(file);

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);
		UploadFile xmlFile = new UploadFile(sessionManager.getLoginUser()
				.getName(), "C:/upload/mission/", fileName);
		uploadFileService.save(xmlFile);
		return xmlFile;

	}
}
