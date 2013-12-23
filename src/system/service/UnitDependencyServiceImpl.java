package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.MissionUnit;
import system.po.UnitDependency;
import system.service.spec.UnitDependencyService;
import system.vo.UnitDependencyVo;

@Interceptors(TimerInterceptor.class)
public class UnitDependencyServiceImpl extends AbstractDataAccessService<UnitDependency>
implements Serializable, UnitDependencyService{




	@Inject AbstractDataAccessService<MissionUnit> missionUnitDataAccessService;
	public UnitDependencyServiceImpl() {
		super(UnitDependency.class);
	}

	/* (non-Javadoc)
	 * @see system.service.UnitDependencyService#generateVo(system.po.UnitDependency)
	 */
	@Override
	public UnitDependencyVo generateVo(UnitDependency dependency) throws Exception
	{
		UnitDependencyVo result = new UnitDependencyVo();
		result.setUnitDependency(dependency);
		MissionUnit preUnit = missionUnitDataAccessService.get(dependency.getPreUnitId());
		MissionUnit postUnit = missionUnitDataAccessService.get(dependency.getPostUnitId());
		result.setPreUnit(preUnit);
		result.setPostUnit(postUnit);

		return result;
	}


	/* (non-Javadoc)
	 * @see system.service.UnitDependencyService#check(system.po.UnitDependency)
	 */
	@Override
	public boolean check(UnitDependency dependency) throws Exception
	{
		if(dependency.getPostUnitId().equals(dependency.getPreUnitId()))
			return false;
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("belonged_partition_id", dependency.getBelongedPartitionId());
		sqlMap.put("pre_unit_id", dependency.getPostUnitId());
		List<UnitDependency> dataSet = this.findAllByCondition(sqlMap);

		for(UnitDependency p:dataSet)
		{
			if(!reachCircle(p,dependency))
				continue;
			else
				return false;
		}
		return true;

	}

	private boolean reachCircle(UnitDependency target, UnitDependency dependency) throws Exception
	{
		if(target.getPostUnitId().equals(dependency.getPreUnitId()))
			return true;
		else
		{
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("belonged_partition_id", dependency.getBelongedPartitionId());
			sqlMap.put("pre_unit_id", target.getPostUnitId());
			List<UnitDependency> dataSet = this.findAllByCondition(sqlMap);

			for(UnitDependency p:dataSet)
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
