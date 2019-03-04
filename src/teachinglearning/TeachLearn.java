package teachinglearning;

public class TeachLearn {

	// 种群大小
		public static final int populationSize_task100 = 300;//50
		public static final int populationSize_task200 = 300;//150
		
		// 项目案例
		public static final double maxGenerations = 500;
		public static final int randomTask=1;
		public static final int durationBasedTask=2;//descending
		public static final int successorsBasedTask=3;//ascending
		public static final double probR=0.05;//调整资源概率0.01,0.05,0.1,0.2
		public static final double probP_task100=0.2;//调整任务概率0.05,0.1,0.2,0.5
		public static final double probP_task200=0.5;//调整任务概率
		public static final int reinIntensity_task100= 50;
		public static final int reinIntensity_task200 = 20;//reinforcement teacher tensity
}
