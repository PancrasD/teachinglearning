package teachinglearning;


public class ITask {
	public int taskid;
	public int pretasknum;
	public int startTime;
	public int finishTime;
	private int duration;
	
	public ITask(Task task){
		taskid = task.getTaskID();
		pretasknum=task.getpretasknum();
		duration = task.getDuration();
	}
	
	public void setstarttime(int starttime){
		startTime = starttime;
		finishTime = startTime + duration - 1;
	}
    public int getDuration() {
    	return this.duration;
    }
}
