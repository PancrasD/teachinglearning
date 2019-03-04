package teachinglearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义一个任务类，属性包含：
 * 任务ID，任务工期，执行任务所需技能，紧前任务IDs
 * 
 *
 */
public class Task implements Cloneable {
	//任务ID
	private int taskID;
	//任务工期
	private int duration;
	//执行任务所需技能
	private String skill;
	//紧前任务IDs
	private List<Integer> predecessorIDs = new  ArrayList<>();
	//紧前任务数
	private int pretasknum;
	//紧后任务IDS
	private List<Integer> successorTaskIDS = new  ArrayList<>();
	//任务可用的资源
	private List<Integer> resourceIDs = new ArrayList<>();
	
	private Map<Integer,Double> capaleResource = new HashMap<Integer,Double>();
	
	//任务的开始时间
	private int startTime;
	//任务的结束时间
	private int finishTime;
	
	
	public Task(){}
	public Task(int taskID,int duaration,String skill){
		this.taskID=taskID;
		this.duration=duaration;
		this.skill=skill;
		this.pretasknum = 0;
	}
	public Task(int taskID,int duaration,String skill,String pretaskIDs){
		this.taskID=taskID;
		this.duration=duaration;
		this.skill=skill;
		String[] pre_IDs = pretaskIDs.trim().split(" ");
		this.pretasknum =  pre_IDs.length;
		for (int i = 0; i<pretasknum; i++){
			this.predecessorIDs.add(Integer.valueOf(pre_IDs[i]));	
		}
	}
	

	
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	public int getpretasknum() {
		return pretasknum;
	}
	public void setpretasknum(int num) {
		this.pretasknum = num;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuaration(int duaration) {
		this.duration = duaration;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public List<Integer> getPredecessorIDs() {
		return predecessorIDs;
	}
	public void setPredecessorIDs(List<Integer> pretaskIDs) {
		this.predecessorIDs = pretaskIDs;
	}
	
	public List<Integer> getresourceIDs( ) {
		return resourceIDs;
	}
	public void setresourceIDs(List<Integer> resIDs) {
		this.resourceIDs = resIDs;
	}
	
	public List<Integer> getsuccessortaskIDS( ) {
		return successorTaskIDS;
	}
	public void setsuccessorTaskIDS(List<Integer> taskids) {
		this.successorTaskIDS = taskids;
	}
	
	
	
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
		this.finishTime = this.startTime + this.duration - 1;
	}
	public int getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}

    public Task clone()throws CloneNotSupportedException {
		//只复制了任务中的值类型变量，如pretasknum、startTime、finishTime
    	//而任务的前继任务数组、后继任务数组、可用资源数组还是引用，在使用时需要注意。
    	Task clone = (Task)super.clone();
   		return clone;     
    }
	public Map<Integer,Double> getCapaleResource() {
		return capaleResource;
	}
	public void setCapaleResource(Map<Integer,Double> capaleResource) {
		this.capaleResource = capaleResource;
	}

	
	
}
