package examples.mmcc;

import general.Replication;
import general.Simulation;
import general.automagic.AutoReplication;

public class Main {

	public static void main(String[] args) {
		// parameters
		int nServers = 3;
		double lambda = 50;
		double mu = 2;
		
		double timeHorizon = 100;
		long n = 100;
		long seed = 0;
		
		MMCCState state = new MMCCState(nServers, lambda, mu, timeHorizon, seed);
		Replication<MMCCState> replication = new AutoReplication<>(state);
		
		Simulation<MMCCState> simulation = new Simulation<>(replication);
		simulation.run(n);
		simulation.printEstimates();
	}
}
