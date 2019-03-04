package teachinglearning;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tool {

	public static void printsolution(List<Individual> solution,long startTime) {
	
		       List<Individual> solutions=solution;
		       Collections.sort(solutions,new Comparator<Individual>() {
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
		       int size=solutions.size();
		     
		       for(int i=0;i<size;i++)
		       {
			    double[] obj = solutions.get(i).getObj();
				System.out.println("项目工期为：" + obj[0]/8.0 + "；项目成本为：" + obj[1]);
		       }
		       
				long endTime = System.currentTimeMillis();
				System.out.println("共计用时：" + (endTime - startTime) / 1000 + "秒");
			}
	
}
