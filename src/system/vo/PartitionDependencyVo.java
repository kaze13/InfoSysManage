package system.vo;

import system.po.MissionPartition;
import system.po.PartitionDependency;

public class PartitionDependencyVo {

	private PartitionDependency partitionDependency;
	private MissionPartition prePartition;
	private MissionPartition postPartition;



	public PartitionDependencyVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PartitionDependencyVo(PartitionDependencyVo dependency)
	{
		this.partitionDependency = dependency.getPartitionDependency();
		this.prePartition = dependency.getPrePartition();
		this.postPartition = dependency.getPostPartition();
	}
	public PartitionDependency getPartitionDependency() {
		return partitionDependency;
	}
	public void setPartitionDependency(PartitionDependency partitionDependency) {
		this.partitionDependency = partitionDependency;
	}
	public MissionPartition getPrePartition() {
		return prePartition;
	}
	public void setPrePartition(MissionPartition prePartition) {
		this.prePartition = prePartition;
	}
	public MissionPartition getPostPartition() {
		return postPartition;
	}
	public void setPostPartition(MissionPartition postPartition) {
		this.postPartition = postPartition;
	}





}
