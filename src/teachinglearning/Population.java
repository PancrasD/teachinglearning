package teachinglearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Population {

	private int populationsize;
	private Individual[] population;
	

	private Case project;
	private List<Individual> teachers;
	private List<Individual> students;

	public Population(int populationSize, Case project) {
		this.populationsize = populationSize;
		this.project = project;
		this.population = new Individual[populationSize];
	}
	
	public Population(int populationSize, Case project,boolean initial) {
		this.populationsize = populationSize;
		this.project = project;
		this.population = new Individual[populationSize];
		if (initial) {
			for (int i = 0; i < populationSize-2; i++) {
				
				Individual individual = new Individual(project,TeachLearn.randomTask);
				this.population[i] = individual;
			}
			Individual individual_duration = new Individual(project,TeachLearn.durationBasedTask);
			this.population[populationSize-2] = individual_duration;
			Individual individual_successors = new Individual(project,TeachLearn.successorsBasedTask);
			this.population[populationSize-1] = individual_successors;	
		}
		TeachersAndStudents(this,project);
		
		
}
	//algorithm
	public Population getLearning()
	{
		//非支配排序 非支配层  支配层
		//非支配层教学
		teachStudent();
		//支配层学习
		studentselfLearning();
		//新种群
		//update teachers students
		TeachersAndStudents(this,project);
		int reinIntensity=50;
		if(project.getN()==100) reinIntensity=TeachLearn.reinIntensity_task100;
		if(project.getN()==200) reinIntensity=TeachLearn.reinIntensity_task200;
		for(int k=0;k<reinIntensity;k++)
			reforcementTeacher();
		//更新population
		List<Individual> indList=new ArrayList<Individual>();
		int tSize=this.teachers.size();
		int sSize=this.students.size();
		indList.addAll(this.teachers);
		indList.addAll(this.students);
		Individual[] newIndividuals=new Individual[indList.size()];
		for(int i=0; i<indList.size();i++) {
			newIndividuals[i]=indList.get(i);
		}
		this.setPopulation(newIndividuals);
		return  this;
	}
	public void TeachersAndStudents(Population pop,Case project) {
		Individual[] individuals =pop.getPopulation();
		List<Individual> teachers=new ArrayList<Individual>();
		List<Individual> students=new ArrayList<Individual>();
		List<List<Integer>> dominatedIndex=non_Dominated_Sort(pop,0,project);
		int teacherRanks=1;
		for(int i=0;i<teacherRanks;i++) {
		List<Integer> teachersIndex=dominatedIndex.get(i);
		for(int j=0;j<teachersIndex.size();j++) {
			teachers.add(individuals[teachersIndex.get(j)]);
		}
		}
		for(int j=teacherRanks;j<dominatedIndex.size();j++) {
			List<Integer> studentsIndex=dominatedIndex.get(j);
			for(int i=0;i<studentsIndex.size();i++) {
				students.add(individuals[studentsIndex.get(i)]);	
			}
		}
		this.setTeachers(teachers);
		this.setStudents(students);
	}
	//select teacher  update teacher
	 public  Population TeacherAndStu(Case project) {
	    	int populationSize = populationsize;
			Individual[] individuals =this.population;
			List<Individual> teacherAndStudent=new ArrayList<Individual>();
			for(int k=0;k<populationSize;k++)
			{
				teacherAndStudent.add(individuals[k]);
			}
			Collections.sort(teacherAndStudent,new Comparator<Individual>() {
				@Override
				public int compare(Individual o1,Individual o2) {
					int flag=0;
					if(o1.getObj()[0]>o2.getObj()[0]) {flag=1;}
					if(o1.getObj()[0]<o2.getObj()[0]) {flag=-1;}
					if(o1.getObj()[0]==o2.getObj()[0]) {
						if(o1.getObj()[1]>o2.getObj()[1]) {flag=1;}
						if(o1.getObj()[1]<o2.getObj()[1]) {flag=-1;}
					}
					return flag;
				}
			});
			Population pop=new Population(populationSize,project);
			for (int i =0; i <pop.size();i++){
				pop.setIndividual(i, teacherAndStudent.get(i));
			}
			return pop;
	    }

	 
	 /**
	  * 对种群进行非支配排序，将解分成不同的非支配级 此处为了便于处理，用个体在种群中的序列号进行一系列计算
	  * @param population
	  *            种群
	  *        level  计算非支配排序的最大层级，0，计算所有层，1，计算第1层。
	  * @return 返回种群分成不同非支配等级后，每个个体在对应种群中序列号集合
	  */
	 public static List<List<Integer>> non_Dominated_Sort(Population population,int level,Case project) {
			int populationSize = population.size();
			Individual[] individuals = population.getPopulation();

			List<List<Integer>> indivIndexRank = new ArrayList<List<Integer>>(); // 按非支配等级排序后，各个体在种群中对应的序列号的集合
			// 每个解都可以分成两个实体部分:1、支配该解的其他解的数量np;2、被该解支配的解集合Sp
			List<List<Integer>> spList = new ArrayList<List<Integer>>();// 存储的是个体在种群中的序列号
			for (int i = 0; i < populationSize; i++) {
				spList.add(new ArrayList<Integer>());
			}
			int[] np = new int[populationSize];
			for (int i = 0; i < populationSize; i++) {
				for (int j = i+1; j < populationSize; j++) {

					int flag=Dominated(individuals[i], individuals[j],project);
					if (flag == 1) { // 前者支配后者
						spList.get(i).add(j); // 将个体j加入个体i的支配个体队列
						np[j]++;  // 支配个体j的个体数+1
					}
					if (flag == 2) { // 后者支配前者
						spList.get(j).add(i);
						np[i]++;
					}
				}
			}
			// 定义一个集合，用来存储前面已经排好等级的个体在种群的序列号
			int num = 0;
			int Rank =0;
			while (num < populationSize) {
				List<Integer> FRank = new ArrayList<Integer>(); // FRank是种群中，非支配的不同等级,如F1，F2...
				for (int i = 0; i < populationSize; i++) {
					if (np[i] == 0) {
						FRank.add(i); //将所有没有被任何其他个体支配的个体加入到层级
						individuals[i].setNon_dominatedRank(Rank);
						np[i] = -1; //标记个体已处理
						num ++;  //已处理的个体数量计数，当已处理个体个数达到种群人数上线即可终止处理
					}
				}
				//被分层的个体所支配的个体的被支配个体数量减1
				for (int i = 0; i < FRank.size(); i++) {
					for (int j = 0; j < spList.get(FRank.get(i)).size(); j++) {
						np[spList.get(FRank.get(i)).get(j)]--;
					}
				}
				
				indivIndexRank.add(FRank);
				
				Rank ++;
				if ((level != 0)&&(Rank >= level)){
					break;
				}
				
			}
			return indivIndexRank;
		}
	 /**
	  * 两个个体之间是否存在支配关系
	  * flag=1代表individual1支配individual2；flag=2表示individual2支配individual1；flag=0表示两者之间没有支配关系
	  * 
	  * @param individual1
      * @param individual2
	  * @return
      */
	 public static int Dominated(Individual individual1, Individual individual2,Case project) {
			int flag, n, k;
			flag =  n = k = 0;

			double[] obj1=individual1.getObj();
			double[] obj2=individual2.getObj();
			
			for (int i = 0; i < obj1.length; i++) {
				if (obj1[i] < obj2[i]) {
					n++;
				} else if (obj1[i] > obj2[i]) {
					k++;
				}
			}
			if (k == 0 && n > 0) {
				flag = 1;
			}
			if (n == 0 && k > 0) {
				flag = 2;
			}
			return flag;
		}
	//teacher phase
	public void teachStudent() {
		//老师教学后的student-种群
		List<Individual> newStudents = new ArrayList<Individual>();
		Individual teacher =teachers.get((int)Math.random()*(teachers.size()));
		int numTasks= project.getN();
		for (int i = 0; i < students.size(); i++) {
			Individual student =students.get(i);
			//crosspoint 取值0-(size()-1)
			int swapPoint1 = (int) (Math.random() *( numTasks));
			int swapPoint2=(int) (Math.random() *( numTasks));
			while(swapPoint1==swapPoint2) swapPoint2=(int) (Math.random() *(numTasks));
			if(swapPoint1>swapPoint2) {
				int crosspoint=swapPoint2;
				swapPoint2=swapPoint1;
				swapPoint1=crosspoint;
			}	
			//long startTime2 = System.currentTimeMillis();
			Individual newstudent1 = student.Mating(teacher, swapPoint1,swapPoint2);
			Individual newstudent2 = teacher.Mating(student, swapPoint1,swapPoint2);
			//long endTime2 = System.currentTimeMillis();
		    //System.out.println("Mating"+(endTime2-startTime2));
			Individual betterstudent=student.updatebetter(newstudent1,newstudent2);
			newStudents.add(betterstudent);
		}
		//non_Dominated_Sort(Population population,int level,Case project)
		this.setStudents(newStudents);
	}
	//student自学习student phase
	public void studentselfLearning() {
		//学生自学后的student-种群
        List<Individual> newStudents = new ArrayList<Individual>();
        int studentSize=this.students.size();
        int numTasks=this.project.getN();
		for (int i = 0; i <studentSize ; i++) {
			//adjust tasklist
			int currentStudentIndex=i;
			//crosspoint取值0-(size()-1)
			int randomStudentIndex=(int) (Math.random() *(studentSize));
			while(randomStudentIndex==currentStudentIndex){
				randomStudentIndex=(int) (Math.random() *(studentSize));
			}
			
			int swapPoint1 = (int) (Math.random() *(numTasks-1));
			int swapPoint2=(int) (Math.random() *(numTasks-1));
			while(swapPoint1==swapPoint2) swapPoint2=(int) (Math.random() *(numTasks-1));
			if(swapPoint1>swapPoint2) {
				int crosspoint=swapPoint2;
				swapPoint2=swapPoint1;
				swapPoint1=crosspoint;
			}
			Individual currentStudent =this.students.get(i);
			Individual randomStudent=this.students.get(randomStudentIndex);
			Individual newstudent1 = currentStudent.Mating(randomStudent, swapPoint1,swapPoint2);
			Individual newstudent2 = randomStudent.Mating(currentStudent, swapPoint1,swapPoint2);
			Individual betterstudent=currentStudent.updatebetter(newstudent1,newstudent2);
			//adjust resourcelist  resource-based local search RBLS 基于资源的本地搜索
		    Individual newindividual=betterstudent.updateResourceList(TeachLearn.probR);
		    Individual betterstu=newindividual.updatebetter(betterstudent);
			newStudents.add(betterstu);
		}
		this.setStudents(newStudents);
	}
	//Reforcement teacher
	public void reforcementTeacher() {
		//更新teacher,和student重组种群
        List<Individual> newTeachers=new ArrayList<Individual>();
        for(int i=0;i<this.teachers.size();i++) {
		Individual teacher =this.teachers.get(i);
		Individual newteacher=teacher.reforceTeacher();
		Individual betterteacher=newteacher.updatebetter(teacher);
		newTeachers.add(betterteacher);
        }
        this.setTeachers(newTeachers);
		
	}
	// 获取种群的个体成员
		public Individual[] getPopulation() {
			return this.population;
		}
		// 获取种群大小
		public int size() {
			return this.populationsize;
		}
		// 设置种群中的个体
		public Individual setIndividual(int offset, Individual individual) {
			return population[offset] = individual;
		}
		
		
		public int getPopulationsize() {
			return populationsize;
		}

		public List<Individual> getTeachers() {
			return teachers;
		}

		public void setTeachers(List<Individual> teachers) {
			this.teachers = teachers;
		}

		public List<Individual> getStudents() {
			return students;
		}

		public void setStudents(List<Individual> students) {
			this.students = students;
		}

		public void setPopulationsize(int populationsize) {
			this.populationsize = populationsize;
		}
		public void setPopulation(Individual[] population) {
			this.population = population;
		}
}