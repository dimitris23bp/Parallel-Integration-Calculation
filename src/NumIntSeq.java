import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NumIntSeq {

	final static int CORES = Runtime.getRuntime().availableProcessors();	
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{

		long numSteps = 0;
		double sum = 0.0;
		int tasks = 5;


		/* parse command line */
		if (args.length != 1) {
			System.out.println("arguments:  number_of_steps");
			System.exit(1);
		}
		
		try {
			numSteps = Long.parseLong(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("argument " + args[0] + " must be long int");
			System.exit(1);
		}
		
		//Pool with as much threads as the number of available cores
		ExecutorService pool = Executors.newFixedThreadPool(CORES);
		
		//Variable that holds the results one by one
		Future<Double> result;		
		//Loops the task is going to take care of
		long loops = numSteps / tasks;
		//First and last values of loops per task
		long start = 0, finish = 0;
		

		
		/* start timing */
		long startTime = System.currentTimeMillis();

		double step = 1.0 / (double) numSteps;
		for(int i = 0; i < tasks; i++) {
			
			
			finish += loops;
			result = pool.submit(new ThreadStuff(step, start, finish));
			start += loops;
						
			sum += result.get();
		}

		//My pi
		double pi = sum * step;

		/* end timing and print result */
		long endTime = System.currentTimeMillis();
		
		pool.shutdown();
		
		System.out.printf("sequential program results with %d steps\n", numSteps);
		System.out.printf("computed pi = %22.20f\n", pi);
		System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
		System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
	}
}
