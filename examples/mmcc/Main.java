package examples.mmcc;

import general.Replication;
import general.Simulation;
import general.automagic.AutoReplication;

public class Main {

	public static void main(String[] args) {
		// parameters
		
		double timeHorizon = 179;
		long n = 2000;
		long seed = 0;
		int[][] weights = {{11, 15, 18, 20, 19, 15, 12, 11, 13, 8},
							{8, 9, 11, 12, 14, 15, 16, 18, 20, 8}, 
							{1, 5, 8, 10, 11, 12, 13, 15, 20, 8}};
		int[] seats = {20,20,20,20,20,20,20,20,20,Integer.MAX_VALUE};
		int[] revs = {1000, 900, 850, 750, 700, 650, 600, 500, 350, 0};
		double[] mu = {0.8, 0.4, 0.6};
		double[] sigma = {0.2, 0.1, 0.15};
		String question = "e";
		
		
		
		MMCCState state = new MMCCState(timeHorizon, seed, weights, seats, revs, mu, sigma, question);
		Replication<MMCCState> replication = new AutoReplication<>(state);
		
		Simulation<MMCCState> simulation = new Simulation<>(replication);
		simulation.run(n);
		simulation.printEstimates();
	}
}
