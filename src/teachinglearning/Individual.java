package teachinglearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Individual {
	// 每个个体的目标函数个数
		static final int objNum = 2;
		// 个体中任务
		private List<ITask> taskslist = new ArrayList<ITask>(); 
		// 个体中资源
		private List<Resource> Reslist = new ArrayList<Resource>(); 
		// 目标函数
		private double[] obj = new double[objNum];
		// 解
		private List<List<Integer>> scheduling = new ArrayList<List<Integer>>();
		private Case project;
		private int non_dominatedRank;
		public Individual(Case project,int taskInitialMethod) {
			this.project = project;
			settaskslist(project);
			setReslist(project);
			//初始化任务序列、资源序列
			initialization(project,taskInitialMethod);
			
			//计算个体的目标函数值，输出计算了起停时间的任务对象list
			objCompute(project);

			
		}
		public Individual(List<List<Integer>> scheduling,Case project) {
			// 创建个体的染色体
			this.project = project;
			settaskslist(project);	
			setReslist(project);
			this.scheduling = scheduling;
			//计算个体的目标函数值，输出计算了起停时间的任务对象list
			objCompute(project);		
		}
		
		/**
		 * 将随机初始化解，表示任务序列、资源序列
		 *
		 * @return 返回由任务执行序列和资源分配序列组成的集合
		 */
		/*先注释
		public void initialization(Case project,int taskInitialMethod) {
			
			List<Integer> taskList = new ArrayList<Integer>();
			List<Integer> resourceList = new ArrayList<Integer>();
			// 可执行任务集合
			List<Integer> executableTaskIDS = new ArrayList<Integer>();	
			List<Task> tasks = project.getTasks();
            int numTasks=project.getN();
			// 求taskList任务执行序列和resourceList资源分配序列
			for (int i = 0; i < numTasks; i++) {  
				
				executableTaskIDS.clear();
				double rand = Math.random();	
				for (int k = 0; k < numTasks; k++) {
					if (taskslist.get(k).pretasknum == 0){
						executableTaskIDS.add(tasks.get(k).getTaskID());
					}
				}
				if (executableTaskIDS.size() == 0){
					break;
				}
				
				///三种方法random(默认）,taskduration,successors
				int A = (int) ( rand * executableTaskIDS.size());
				int  currentTaskID = executableTaskIDS.get(A);
				
			    if(taskInitialMethod==2){
					///taskdurationBased
			    	currentTaskID=taskdurationBasedRule(executableTaskIDS);
				}
                if(taskInitialMethod==3){
					///taskSuccessorsBased
                	currentTaskID=successorsBasedRule(executableTaskIDS);
				}
				
				taskList.add(currentTaskID);
				taskslist.get(currentTaskID -1).pretasknum = -1;   //当前任务已经被使用，做上标记以防止下次被选用
				
				//处理后续任务
				for (int k = 0; k < tasks.size(); k++) {
					//把所有以任务j为前置任务的前置任务数减1；
					if (tasks.get(k).getPredecessorIDs().contains(currentTaskID)){
						taskslist.get(k).pretasknum--;	
					}
				}
				
				// 求对应的资源分配序列resourceList
				// 可执行该任务的资源集合
				
				List<Integer> capableReslist = tasks.get(currentTaskID -1).getresourceIDs();//Case里设置了resourceIDs
				
				List<Integer> minloadRes=minWorkloadRes(capableReslist);
				int currentResID= minSalaryRes(minloadRes);
				resourceList.add(currentResID);
				//更新资源的负担,workload-duration
				int preworkload= Reslist.get(currentResID-1).getresourceWorkload( );
				int currentTaskDuaration=taskslist.get(currentTaskID -1).getDuration();
				Reslist.get(currentResID-1).setresourceWorkload(preworkload+currentTaskDuaration);
				
			}
		
			this.scheduling.add(taskList);
			this.scheduling.add(resourceList);

			return ;
		}
		*/
public void initialization(Case project,int taskInitialMethod) {
			
			List<Integer> taskList = new ArrayList<Integer>();
			List<Integer> resourceList = new ArrayList<Integer>();
			// 可执行任务集合
			List<Integer> executableTaskIDS = new ArrayList<Integer>();	
			List<Task> tasks = project.getTasks();
            int numTasks=project.getN();
			// 求taskList任务执行序列和resourceList资源分配序列
			while(taskList.size()<numTasks) {  
				
				executableTaskIDS.clear();
				double rand = Math.random();	
				for (int k = 0; k < numTasks; k++) {
					if (taskslist.get(k).pretasknum == 0){
						executableTaskIDS.add(tasks.get(k).getTaskID());
					}
				}
				if (executableTaskIDS.size() == 0){
					break;
				}
				while(executableTaskIDS.size()!=0) {
				///三种方法random(默认）,taskduration,successors
				int A = (int) ( rand * executableTaskIDS.size());
				int  currentTaskID = executableTaskIDS.get(A);
				
			    if(taskInitialMethod==2){
					///taskdurationBased
			    	currentTaskID=taskdurationBasedRule(executableTaskIDS);
				}
                if(taskInitialMethod==3){
					///taskSuccessorsBased
                	currentTaskID=successorsBasedRule(executableTaskIDS);
				}
				
				taskList.add(currentTaskID);
				taskslist.get(currentTaskID -1).pretasknum = -1;   //当前任务已经被使用，做上标记以防止下次被选用
				
				//处理后续任务
				for (int k = 0; k < tasks.size(); k++) {
					//把所有以任务j为前置任务的前置任务数减1；
					if (tasks.get(k).getPredecessorIDs().contains(currentTaskID)){
						taskslist.get(k).pretasknum--;	
					}
				}
				
				// 求对应的资源分配序列resourceList
				// 可执行该任务的资源集合
				
				List<Integer> capableReslist = tasks.get(currentTaskID -1).getresourceIDs();//Case里设置了resourceIDs
				int currentResID=minWorkloadAndSalaryRes(capableReslist);
				resourceList.add(currentResID);
				//更新资源的负担,workload-duration
				int preworkload= Reslist.get(currentResID-1).getresourceWorkload( );
				int currentTaskDuaration=taskslist.get(currentTaskID -1).getDuration();
				Reslist.get(currentResID-1).setresourceWorkload(preworkload+currentTaskDuaration);
				Iterator <Integer> it = executableTaskIDS.iterator();  
				while(it.hasNext())  
				{  
				    if(it.next() == currentTaskID)  
				    {  
				        it.remove();  
				    }  
				} 
			}
			}
			this.scheduling.add(taskList);
			this.scheduling.add(resourceList);

			return ;
		}
		//根据任务工期选择当前执行任务,descending降序
		public int taskdurationBasedRule(List<Integer> executableTaskIDS) {
			List<Integer> executabletask=executableTaskIDS;
			Collections.sort(executabletask, new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					int flag = 0;
					
					if (taskslist.get(o1-1).getDuration() <taskslist.get(o2-1).getDuration()) {
						flag = 1;
					}
					if (taskslist.get(o1-1).getDuration() >taskslist.get(o2-1).getDuration()) {
						flag = -1;
					}
					return flag;
				}

			});
			
			return executabletask.get(0);
		}
		///根据继任者successors选择当前执行任务，ascending
		public int successorsBasedRule(List<Integer> executableTaskIDS) {
			List<Integer> executabletask=executableTaskIDS;
			List<Task> tasks=project.getTasks();
			Collections.sort(executabletask, new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					int flag = 0;
					
					if (tasks.get(o1-1).getsuccessortaskIDS().size() >tasks.get(o2-1).getsuccessortaskIDS().size()) {
						flag = 1;
					}
					if (tasks.get(o1-1).getsuccessortaskIDS().size()<tasks.get(o2-1).getsuccessortaskIDS().size()) {
						flag = -1;
					}
					return flag;
				}

			});
			
			return executabletask.get(0);
		}
		//寻找最小负担（工期和）的资源集,同样负担则选较小薪水的
		public Integer minWorkloadAndSalaryRes(List<Integer> capableRes){
			int minloadAndSalaryRes;
			Collections.sort(capableRes, new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					int flag = 0;
					
					if (Reslist.get(o1-1).getresourceWorkload() >Reslist.get(o2-1).getresourceWorkload()) {
						flag = 1;
					}
					if (Reslist.get(o1-1).getresourceWorkload() <Reslist.get(o2-1).getresourceWorkload()) {
						flag = -1;
					}
					if (Reslist.get(o1-1).getresourceWorkload() ==Reslist.get(o2-1).getresourceWorkload()) {
						if (Reslist.get(o1-1).getSalary() >Reslist.get(o2-1).getSalary()) {
							flag = 1;
						}
						if (Reslist.get(o1-1).getSalary() <Reslist.get(o2-1).getSalary()) {
							flag = -1;
						}
					}
					return flag;
				}

			});

			minloadAndSalaryRes=capableRes.get(0);
			return minloadAndSalaryRes;
		}
		
		//在同样负担的资源集里寻找最小薪水（工期和）的资源，多个取排列第一个//已舍弃
		public int minSalaryRes(List<Integer> sameloadRes){
			
			Collections.sort(sameloadRes, new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					int flag = 0;
					
					if (Reslist.get(o1-1).getSalary() >Reslist.get(o2-1).getSalary()) {
						flag = 1;
					}
					if (Reslist.get(o1-1).getSalary() <Reslist.get(o2-1).getSalary()) {
						flag = -1;
					}
					return flag;
				}

			});
			int minsalaryRes=sameloadRes.get(0);
			return minsalaryRes;
		}
		public void objCompute(Case project) {
			List<Task> tasks = project.getTasks();
			List<Resource> resourses = project.getResources();
			List<Integer> pretaskids;
			int maxtime = 0;
			double cost = 0.0;
			
			int[] endtime_res = new int[project.getM()];
			for (int i = 0; i < endtime_res.length ; i++) {
				endtime_res[i] = 0;
			}
			
			for (int i = 0; i < project.getN(); i++){
				int endtime = 0;
				Task curtask = tasks.get(scheduling.get(0).get(i)-1);
				//得到所有前置任务,循环每一前置任务，取最晚结束时间
				pretaskids = curtask.getPredecessorIDs();
				for (int j = 0; j < pretaskids.size();j++){
					if (endtime < tasks.get(pretaskids.get(j)-1).getFinishTime()){
						endtime = tasks.get(pretaskids.get(j)-1).getFinishTime();
					}
				}
				//当前任务所对应的资源最晚时间
				if (endtime < endtime_res[scheduling.get(1).get(i)-1]){
					endtime = endtime_res[scheduling.get(1).get(i)-1];
				}
				//设置当前任务的开始时间及完成时间
				taskslist.get(scheduling.get(0).get(i)-1).setstarttime(endtime +1);
				//更新当前任务资源的最后完工时间
				endtime_res[scheduling.get(1).get(i)-1] = taskslist.get(scheduling.get(0).get(i)-1).finishTime;
	            //当前个体最后的完成时间
				if (maxtime < endtime_res[scheduling.get(1).get(i)-1]){
	            	maxtime = endtime_res[scheduling.get(1).get(i)-1];
	            }
		     
				// 计算成本			
				int duration = tasks.get(scheduling.get(0).get(i) - 1).getDuration();
				double salary = resourses.get(scheduling.get(1).get(i) - 1).getSalary();
				cost += duration * salary;			
				
			}
			this.obj[0] =  (double)maxtime;
			this.obj[1] = cost;
		}
		//交配生子,两个交叉点
		public Individual Mating(Individual husband, int crosspoint1,int crosspoint2) {
			List<List<Integer>> newstudent_scheduling= new ArrayList<List<Integer>>();
			List<Integer> _tasks = new ArrayList<>();
			List<Integer> _tmptasks = new ArrayList<>();
			List<Integer> _resources = new ArrayList<>();
			int point1=crosspoint1;
			int numTasks=this.scheduling.get(0).size();
			for (int i = 0; i <=crosspoint1;i++){
				_tasks.add(i,this.scheduling.get(0).get(i));
				_resources.add(i,this.scheduling.get(1).get(i));
				_tmptasks.add(i,this.scheduling.get(0).get(i));
			}
			for (int i =crosspoint2+1,j=crosspoint1; i<numTasks;i++){
				_tmptasks.add(++j,this.scheduling.get(0).get(i));
			}		
			for (int i = 0; i<numTasks;i++){
				if (!_tmptasks.contains(husband.scheduling.get(0).get(i))){
					point1++;
					_tasks.add(point1,husband.scheduling.get(0).get(i));
					_resources.add(point1,husband.scheduling.get(1).get(i));
					
				}
			}
			for (int i =crosspoint2+1; i<numTasks;i++){
				_tasks.add(i,this.scheduling.get(0).get(i));
				_resources.add(i,this.scheduling.get(1).get(i));
			}	
			newstudent_scheduling.add(_tasks);
			newstudent_scheduling.add(_resources);
			Individual newstudent = new Individual(newstudent_scheduling,project);	
			return newstudent;
			
			
		}
		//三个individual个体选出更好的
		public Individual updatebetter(Individual newstudent1,Individual newstudent2) {
			//copy 调用的对象-个体
			Individual betterStudent=new Individual(this.scheduling,project);
			//betterStudent和newstudent1相比，工期first,成本second
			if(Population.Dominated(betterStudent, newstudent1, project)==2) {
				betterStudent=newstudent1;
			}
			if(Population.Dominated(betterStudent, newstudent2, project)==2) {
				betterStudent=newstudent2;
			}
			return betterStudent;
		}
		//两个个体相比，选出更好的
		public Individual updatebetter(Individual individual) {
			//copy 调用的对象-个体
			Individual betterIndividual=new Individual(this.scheduling,project);
			if(Population.Dominated(betterIndividual, individual, project)==2) {
				betterIndividual=individual;
			}
			return betterIndividual;
		}
	    //reinforce individual teacher
		public Individual reforceTeacher() {
			//the permutation-based local search PBLS 基于策略的本地搜索
			//传递的对象copy
			List<List<Integer>> scheduling=this.scheduling;
			Individual teacher=new Individual(scheduling,project);
			Individual teacher_PBLS=teacher.permutationBasedLocalSearch ();
			Individual betterteacher=teacher_PBLS.updatebetter(teacher);
			//resource-based local search RBLS 基于资源的本地搜索
			Individual teacher_RBLS=betterteacher.updateResourceList(TeachLearn.probR);
			betterteacher=teacher_RBLS.updatebetter(betterteacher);
			return betterteacher;
		}
		public Individual permutationBasedLocalSearch() {
			List<Task> tasks=project.getTasks();
			List<List<Integer>> updatescheduling=new LinkedList<List<Integer>>();
			List<Integer> _tasks = new ArrayList<>();
			List<Integer> _resources = new ArrayList<>();
			for(int i=0;i<project.getN();i++) {
				_tasks.add(this.scheduling.get(0).get(i));
				_resources.add(this.scheduling.get(1).get(i));
			}
			updatescheduling.add(_tasks);
			updatescheduling.add(_resources);
			int numTasks=tasks.size();
			for(int k=0;k<numTasks-1;k++){
				double _probP=Math.random();
				double probP=0.2;//exits one project belong to task100 but lack of some
				if(tasks.size()==100) probP=TeachLearn.probP_task100;
				else if(tasks.size()==200) probP=TeachLearn.probP_task200;
				if(_probP<=probP){
				int taskID1 =updatescheduling.get(0).get(k);
				int taskID2 =updatescheduling.get(0).get(k+1);
				int resID1 =updatescheduling.get(1).get(k);
				int resID2 =updatescheduling.get(1).get(k+1);
				List<Integer> preIds=tasks.get(taskID2-1).getPredecessorIDs();
				if(!preIds.contains(taskID1)) {
					updatescheduling.get(0).set(k, taskID2);
					updatescheduling.get(0).set(k+1, taskID1);
					updatescheduling.get(1).set(k, resID2);
					updatescheduling.get(1).set(k+1, resID1);
				}
				
				}
			}
			Individual individual= new Individual(updatescheduling,project);
			return individual;
		}
		//更新个体的资源表-随机 resource-based local search RBLS 基于资源的本地搜索
		public Individual updateResourceList(double probR) {
			
			List<Task> tasks=project.getTasks();
			List<List<Integer>> updatescheduling=new LinkedList<List<Integer>>();
			List<Integer> _tasks = new ArrayList<>();
			List<Integer> _resources = new ArrayList<>();
			List<Integer> capapleResIDS=new ArrayList<Integer>();
			int numTasks=project.getN();
			int taskId, resId,randomResId;
			double _probR;
			for(int k=0;k<numTasks;k++) {
			    taskId=this.scheduling.get(0).get(k);
			    resId=this.scheduling.get(1).get(k);
				randomResId=resId;
				capapleResIDS=tasks.get(taskId-1).getresourceIDs();
				_probR=Math.random();
			    if(_probR<=probR) {
			    	
				    int randomindex=(int)Math.random()*(capapleResIDS.size()-1);
				    randomResId=capapleResIDS.get(randomindex);
			    	
			    }
			    _tasks.add(k,taskId);
			    _resources.add(k,randomResId);
			}
			updatescheduling.add(_tasks);
			updatescheduling.add(_resources);
			Individual student=new Individual(updatescheduling,project);
			return student;
		}
		private void settaskslist(Case project){
			for (int i = 0; i < project.getTasks().size();i++){
				ITask itask = new ITask(project.getTasks().get(i));
				taskslist.add(itask);
			}
		}
		public List<ITask> getTaskslist() {
			return taskslist;
		}
		private void setReslist(Case project){
			for (int i = 0; i < project.getResources().size();i++){
				Resource res = project.getResources().get(i);
				Reslist.add(res);
			}
		}
		public List<Resource> getReslist() {
			return Reslist;
		}
		// 获取该个体的染色体结构(taskid)
		public List<List<Integer>> getScheduling() {
			return this.scheduling;
		}
		// 获取该个体的目标函数值
		public double[] getObj() {
			return this.obj;
		}
		
		public void setObj(double[] obj) {
			this.obj = obj;
		}
		// non_dominatedRank的setter方法
		public void setNon_dominatedRank(int non_dominatedRank) {
			this.non_dominatedRank = non_dominatedRank;
		}

}
