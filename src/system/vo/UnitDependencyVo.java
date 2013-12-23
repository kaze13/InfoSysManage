package system.vo;

import system.po.MissionUnit;
import system.po.UnitDependency;

public class UnitDependencyVo {

	private UnitDependency partitionDependency;
	private MissionUnit preUnit;
	private MissionUnit postUnit;



	public UnitDependencyVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UnitDependencyVo(UnitDependencyVo dependency)
	{
		this.partitionDependency = dependency.getUnitDependency();
		this.preUnit = dependency.getPreUnit();
		this.postUnit = dependency.getPostUnit();
	}
	public UnitDependency getUnitDependency() {
		return partitionDependency;
	}
	public void setUnitDependency(UnitDependency partitionDependency) {
		this.partitionDependency = partitionDependency;
	}
	public MissionUnit getPreUnit() {
		return preUnit;
	}
	public void setPreUnit(MissionUnit preUnit) {
		this.preUnit = preUnit;
	}
	public MissionUnit getPostUnit() {
		return postUnit;
	}
	public void setPostUnit(MissionUnit postUnit) {
		this.postUnit = postUnit;
	}





}
