package teachinglearning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class TeachingLearning {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length==1){
            File ff = new File("case_def");
            String[] fl = ff.list(); 
            if (fl==null){
            	System.out.print("没有在case_def目录下找到算例文件");
            	return;           	
            }
    		if (args[0].trim().toLowerCase().equals("t")){
    			PrintStream out = System.out;
                for(int i = 0; i<fl.length; i++){
                	 String _fn =  "case_def/" + fl[i];
                	 String _fo = "data/TeachingLearning_"+fl[i]+".txt";
                	 TeachingLearning_algorithm(_fn,_fo);
                }
                System.setOut(out);
                System.out.println(fl.length +"个案例TeachingLearning_algorithm算法计算完成"); 
                return;
                
    		}
    		
        }else{
        	System.out.print("请输入参数：'t'、自学习算法");
        	return;
        }

	}
	public static void TeachingLearning_algorithm(String casefile,String datafile){
		//记录开始计算的时间，用于统计本算法的总时间
		long startTime = System.currentTimeMillis();
		// 创建案例类对象
		Case project = new Case(casefile);
		// 初始化种群
		int populationSize=100;
		if(project.getN()==100)
			 populationSize=TeachLearn.populationSize_task100;
		if(project.getN()==200)
			 populationSize=TeachLearn.populationSize_task200;
		long startTime1 = System.currentTimeMillis();
	    Population population = new Population(populationSize,project,true);
	    //第一个是Teacher,其他是Student
	    long endTime1 = System.currentTimeMillis();
	    System.out.println("new population"+(endTime1-startTime1));
	    //Population pop=population.TeacherAndStu(project);
	    List<Individual> bestsolutions=new ArrayList<Individual>();
        //循环迭代 算法指定的次数
	    int runtimes=0;
	    while(runtimes<1)
	    {
	    int generationCount = 0;	
		while (generationCount < TeachLearn.maxGenerations ) {
			long startTime2 = System.currentTimeMillis();
			population=population.getLearning();
			long endTime2 = System.currentTimeMillis();
		    //System.out.println("getLearning"+(endTime2-startTime2));
			generationCount++;
		}
	
		//从最后得到种群中获取最优解集
		for(int k=0;k<population.getTeachers().size();k++)
		bestsolutions.add(population.getTeachers().get(k));
		
		runtimes++;
	    }
		
		 File f = new File(datafile);
		 PrintStream ps = null;
		 try {
		   if (f.exists()) f.delete();
		   f.createNewFile();
		   FileOutputStream fos = new FileOutputStream(f);
		   ps = new PrintStream(fos);
		   System.setOut(ps);
		   //输出最优解集
		   Tool.printsolution(bestsolutions,startTime);			   
		 } catch (IOException e) {
			e.printStackTrace();
		 }  finally {
	        if(ps != null) 	ps.close();
	     }
		 
	}
   
}
