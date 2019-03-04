package teachinglearning;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Case {

	private int N;// 案例包含的任务数量
	private int M;// 案例包含的资源数量
	private int PR;// 案例包含的紧前关系数量
	private int K;// 案例包含的技能类型数量
   
	private List<Task> tasks = new ArrayList<>();
	private List<Resource> resources = new ArrayList<>();
	private List<Integer> characteristics = new ArrayList<>();

	public Case() {
		caseDefinition();
	}

	public Case(String defFile) {
		readCaseDef(defFile);
		this.N = characteristics.get(0);
		this.M = characteristics.get(1);
		this.PR = characteristics.get(2);
		this.K = characteristics.get(3);
		setCapapleResource();
		countsuccessor();
		setTaskCapapleResource();
		setResourceWorkload();
	}

	// 定义一个读取案例文件的方法，获取案例的相关信息
	public void readCaseDef(String defFile) {
		try (FileInputStream fis = new FileInputStream(defFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
			String line = null;
			int row = 0;
			int a = 0;
			int b = 0;
			int n = 0;
			int m = 0;
			while ((line = br.readLine()) != null) {
				row++;

				if (row < 3) {
					continue;
				}
				if (3 <= row && row <= 6) {// 读取案例的一般特征信息：任务数量，资源数量，紧前任务数量，技能种类
					String[] strArr = line.split(":");
					characteristics.add(Integer.valueOf(strArr[1]));
					continue;
				}
				if (line.startsWith("ResourceID     Salary     Skills")) {
					a = row;
					m = characteristics.get(1);
					continue;
				}

				if (a < row && row <= (a + m)) {
					String[] resource = line.trim().split("     ", 3);// 5个空格符为分隔符,并限定数组长度为3
					Resource r = new Resource(Integer.valueOf(resource[0]), Double.valueOf(resource[1]), resource[2]);
					resources.add(r);
					continue;
				}
				if (line.startsWith("TaskID     Duration     Skill     Predecessor IDs")) {
					b = row;
					n = characteristics.get(0);
					continue;
				}
				if (b < row && row <= (b + n)) {
					String[] task = line.trim().split("     ", 4);// 5个空格符为分割符，限定数组长度为4,无紧前任务的，数组长度为3
					if (task.length == 3) {
						Task t = new Task(Integer.valueOf(task[0]), Integer.valueOf(task[1]), task[2]);
						tasks.add(t);
					} else {
						Task t = new Task(Integer.valueOf(task[0]), Integer.valueOf(task[1]), task[2], task[3]);
						tasks.add(t);
					}
					continue;
				}

			}
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	public void caseDefinition() {

		this.N = 10;
		this.M = 3;
		this.PR = 4;
		this.K = 3;

		String[] resource1 = { "1", "56.0", "Q1:0 Q2:1" }; // 这里暂时模仿读取文件时，形成的字符串数组
		Resource r1 = new Resource(Integer.valueOf(resource1[0]), Double.valueOf(resource1[1]), resource1[2]);
		String[] resource2 = { "2", "53.6", "Q2:2 Q0:1" };
		Resource r2 = new Resource(Integer.valueOf(resource2[0]), Double.valueOf(resource2[1]), resource2[2]);
		String[] resource3 = { "3", "28.9", "Q0:1 Q1:0" };
		Resource r3 = new Resource(Integer.valueOf(resource3[0]), Double.valueOf(resource3[1]), resource3[2]);

		resources.add(r1);
		resources.add(r2);
		resources.add(r3);

		String[] task1 = { "1", "37", "Q2:1" };
		Task t1 = new Task(Integer.valueOf(task1[0]), Integer.valueOf(task1[1]), task1[2]);

		String[] task2 = { "2", "36", "Q2:2" };
		Task t2 = new Task(Integer.valueOf(task2[0]), Integer.valueOf(task2[1]), task2[2]);

		String[] task3 = { "3", "21", "Q0:1" };
		Task t3 = new Task(Integer.valueOf(task3[0]), Integer.valueOf(task3[1]), task3[2]);

		String[] task4 = { "4", "23", "Q1:0" };
		Task t4 = new Task(Integer.valueOf(task4[0]), Integer.valueOf(task4[1]), task4[2]);

		String[] task5 = { "5", "36", "Q0:1" };
		Task t5 = new Task(Integer.valueOf(task5[0]), Integer.valueOf(task5[1]), task5[2]);

		String[] task6 = { "6", "13", "Q2:1" };
		Task t6 = new Task(Integer.valueOf(task6[0]), Integer.valueOf(task6[1]), task6[2]);

		String[] task7 = { "7", "13", "Q1:0", "4 5" };
		Task t7 = new Task(Integer.valueOf(task7[0]), Integer.valueOf(task7[1]), task7[2], task7[3]);

		String[] task8 = { "8", "37", "Q0:1" };
		Task t8 = new Task(Integer.valueOf(task8[0]), Integer.valueOf(task8[1]), task8[2]);

		String[] task9 = { "9", "36", "Q2:1", "7" };
		Task t9 = new Task(Integer.valueOf(task9[0]), Integer.valueOf(task9[1]), task9[2], task9[3]);

		String[] task10 = { "10", "19", "Q1:0", "3" };
		Task t10 = new Task(Integer.valueOf(task10[0]), Integer.valueOf(task10[1]), task10[2], task10[3]);

		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);
		tasks.add(t4);
		tasks.add(t5);
		tasks.add(t6);
		tasks.add(t7);
		tasks.add(t8);
		tasks.add(t9);
		tasks.add(t10);
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public int getM() {
		return M;
	}

	public void setM(int m) {
		M = m;
	}

	public int getPR() {
		return PR;
	}

	public void setPR(int pR) {
		PR = pR;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	


	public void setCapapleResource() {

			// 任务i的可供选择的资源集合

		// 每一种资源拥有的技能集合
		List<Map<String, Integer>> staffSkillList = new ArrayList<>();
		for (int i = 0; i < resources.size(); i++) {
			// 资源i拥有的技能-技能水平
			Map<String, Integer> resourceSkill = new HashMap<>();
			String[] skills = resources.get(i).getSkils().trim().split(" ");
			for (String skill : skills) {
				String[] str = skill.split(":");
				resourceSkill.put(str[0], Integer.valueOf(str[1]));

			}
			staffSkillList.add(resourceSkill);
		}

		// 遍历任务集
		for (int i = 0; i < tasks.size(); i++) {
			List<Integer> capapleResourceIDS = new ArrayList<Integer>();
			// 任务i的所需技能类型和水平
			String[] skill_level = tasks.get(i).getSkill().trim().split(":");
			// 遍历每一种资源拥有的技能集合
			for (int j = 0; j < staffSkillList.size(); j++) {
				Map<String, Integer> map = staffSkillList.get(j);
				if (map.keySet().contains(skill_level[0])
						&& map.get(skill_level[0]) >= Integer.valueOf(skill_level[1])) {
					capapleResourceIDS.add(resources.get(j).getResourceID());
				}
			}
			
			tasks.get(i).setresourceIDs(capapleResourceIDS);
		}
		return;
	}
	
	public void countsuccessor() {
		//循环每一列，

		for (int i = 0; i< tasks.size();i++){
			List<Integer> successorIDS = new ArrayList<>();
			for (int j = i+1; j < tasks.size(); j++) {
				int num =  tasks.get(j).getpretasknum();
			   if (num > 0) {
					List<Integer> pre_IDs = tasks.get(j).getPredecessorIDs();
					for (int k=0;k<num;k++) {
						if (pre_IDs.get(k)==i+1){
							successorIDS.add(tasks.get(j).getTaskID());
						}
					}
				}
			}
			tasks.get(i).setsuccessorTaskIDS(successorIDS);
		}
		return;
	}
	
	/**
	 * 判断任务执行链表中相邻两个任务之间是否存在紧前关系约束。 如果task1是task2的紧前任务，则返回true
	 * 分两种情况：1.task2没有紧前任务,返回false; 2.task2有紧前任务: 紧前任务包含task1; 紧前任务不包含task1
	 * 
	 * @param task1
	 *            任务1
	 * @param task2
	 *            任务2
	 * @return
	 */
	public boolean isPredecessor(Task task1, Task task2) {
		boolean flag = false;

		// task1的ID
		int task1_ID = task1.getTaskID();
		if (task2.getPredecessorIDs().contains(task1_ID)) {
			flag = true;
		}
		return flag;
	}

	public void setTaskCapapleResource() {
		for (int i = 0; i < tasks.size(); i++) {
			Map<Integer, Double> r_possibility = new HashMap<>();
			List<Integer> resurceid = tasks.get(i).getresourceIDs();
			for (int j = 0; j < resurceid.size(); j++) {
				r_possibility.put(resurceid.get(j), ((double) 1) / resurceid.size());
			}
			tasks.get(i).setCapaleResource(r_possibility);
		}

	}
	public void setResourceWorkload() {
		
		for (int i = 0; i < resources.size(); i++) {
			resources.get(i).setresourceWorkload(0);
		}
	}

}
