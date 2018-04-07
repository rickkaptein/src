package examples.mmcc;

import general.Replication;
import general.Simulation;
import general.automagic.AutoReplication;

public class Main {

	public static void main(String[] args) {
		// parameters
		int nServers = 3;
		double lambda = 5;
		
		double timeHorizon = 179;
		long n = 1000;
		long seed = 0;
		
		MMCCState state = new MMCCState(lambda, timeHorizon, seed);
		Replication<MMCCState> replication = new AutoReplication<>(state);
		
		Simulation<MMCCState> simulation = new Simulation<>(replication);
		simulation.run(n);
		simulation.printEstimates();
	}
}
