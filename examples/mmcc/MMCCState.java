package examples.mmcc;

import java.util.Random;

import general.Counter;
import general.SystemState;
import general.annotations.AutoCounter;
import general.annotations.AutoMeasure;
import general.annotations.Initialize;
import general.annotations.StopCriterium;

/**
 * This class models the system state for a typical M/M/c/c queuing system.
 * 
 * @author Nemanja Milovanovic
 *
 */

public class MMCCState extends SystemState<MMCCState> {
		
	private final Random random;
	private final double lambda;	
	
	@AutoCounter("Number of rejected arrivals")
	private Counter rejected;
	
	@AutoCounter("Total arrivals")
	private Counter arrivals;
	
	// Example of annotation with initializion value
	@AutoCounter(value="Cumulated time all servers busy", initialValue=0d)
	private Counter busyTime;
	
	public MMCCState(
			double lambda, 
			double timeHorizon, 
			long seed) {
		super(timeHorizon, seed);
		this.lambda = lambda;
		random = new Random(seed);
		reset();
	}
	
	@Initialize
	public void initReplication() {
		double nextArrivalTime = Utils.nextInterArrivalTime(random, lambda);
		addEvent(nextArrivalTime, this::doArrival);
	}
	
	// This serves as an example, the method can be removed...
	@StopCriterium
	public boolean veryDangerousCheck() {
		// A complicated way to write false...
		return random.nextDouble() > Double.POSITIVE_INFINITY;
	}
	
	public void doArrival(double eventTime) {
		double prevTime = getCurrentTime();
		double newTime = eventTime;
		
		
		// update counter for total nr of arrivals
		arrivals.increment();
		
		//TODO choose product
		
		// generate next arrival
		double currentTime = eventTime;
		double nextInterArrivalTime = Utils.nextInterArrivalTime(random, lambda);
		double nextArrivalTime = currentTime + nextInterArrivalTime;
		addEvent(nextArrivalTime, this::doArrival);
	}
	
	
	
	@AutoMeasure("Rejection probability")
	public double getRejectionProbability() {
		
		
		return rejected.getValue()/arrivals.getValue();
	}
	

	@Override
	public void reset() {
		
		// TODO: do not forget to edit this method if you ever change this class!
	}
}
