package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.MissionPartition;
import system.po.PartitionDependency;
import system.service.spec.PartitionDependencyService;
import system.vo.PartitionDependencyVo;

@Interceptors(TimerInterceptor.class)
public class PartitionDependencyServiceImpl extends AbstractDataAccessService<PartitionDependency>
implements Serializable, PartitionDependencyService{



	@Inject AbstractDataAccessService<MissionPartition> missionPartitionDataAccessService;

	public PartitionDependencyServiceImpl() {
		super(PartitionDependency.class);
	}

	/* (non-Javadoc)
	 * @see system.service.PartitionDependencyService#generateVo(system.po.PartitionDependency)
	 */
	@Override
	public PartitionDependencyVo generateVo(PartitionDependency dependency) throws Exception
	{
		PartitionDependencyVo result = new PartitionDependencyVo();
		result.setPartitionDependency(dependency);
		MissionPartition prePartition = missionPartitionDataAccessService.get(dependency.getPrePartitionId());
		MissionPartition postPartition = missionPartitionDataAccessService.get(dependency.getPostPartitionId());
		result.setPrePartition(prePartition);
		result.setPostPartition(postPartition);

		return result;
	}


	/* (non-Javadoc)
	 * @see system.service.PartitionDependencyService#check(system.po.PartitionDependency)
	 */
	@Override
	public boolean check(PartitionDependency dependency) throws Exception
	{
		if(dependency.getPostPartitionId().equals(dependency.getPrePartitionId()))
			return false;
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("belonged_mission_id", dependency.getBelongedMissionId());
		sqlMap.put("pre_partition_id", dependency.getPostPartitionId());
		List<PartitionDependency> dataSet = this.findAllByCondition(sqlMap);

		for(PartitionDependency p:dataSet)
		{
			if(!reachCircle(p,dependency))
				continue;
			else
				return false;
		}
		return true;

	}

	private boolean reachCircle(PartitionDependency target, PartitionDependency dependency) throws Exception
	{
		if(target.getPostPartitionId().equals(dependency.getPrePartitionId()))
			return true;
		else
		{
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("belonged_mission_id", dependency.getBelongedMissionId());
			sqlMap.put("pre_partition_id", target.getPostPartitionId());
			List<PartitionDependency> dataSet = this.findAllByCondition(sqlMap);

			for(PartitionDependency p:dataSet)
			{
				if(!reachCircle(p,dependency))
					continue;
				else
					return true;
			}
			return false;
		}
	}
}
