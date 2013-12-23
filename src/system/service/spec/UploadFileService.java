package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.UploadFile;

public interface UploadFileService {

	public void save(UploadFile t) throws Exception;

	public void save(List<UploadFile> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(UploadFile t) throws Exception;

	public UploadFile get(String id) throws Exception;

	public List<UploadFile> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

}