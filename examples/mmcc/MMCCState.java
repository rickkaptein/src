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
	private final int[][] weights;
	private final int[] seats;
	private final int products = 9;
	private final Counter[] soldProducts = new Counter[products];
	
	@AutoCounter("Number of rejected arrivals")
	private Counter rejected;
	
	@AutoCounter("Total arrivals")
	private Counter arrivals;
	
	@AutoCounter("Sold products A")
	private Counter soldA;
	
	@AutoCounter("Sold products B")
	private Counter soldB;
	
	@AutoCounter("Sold products C")
	private Counter soldC;
	
	@AutoCounter("Sold products D")
	private Counter soldD;
	
	@AutoCounter("Sold products E")
	private Counter soldE;
	
	@AutoCounter("Sold products F")
	private Counter soldF;
	
	@AutoCounter("Sold products G")
	private Counter soldG;
	
	@AutoCounter("Sold products H")
	private Counter soldH;
	
	@AutoCounter("Sold products I")
	private Counter soldI;
	
	
	
	// Example of annotation with initializion value
	@AutoCounter(value="Cumulated time all servers busy", initialValue=0d)
	private Counter busyTime;
	
	public MMCCState(
			double lambda, 
			double timeHorizon, 
			long seed,
			int[][] weights,
			int[] seats) {
		super(timeHorizon, seed);
		this.lambda = lambda;
		this.weights = weights;
		this.seats = seats;
		random = new Random(seed);
		reset();
		
		soldProducts[0] = soldA;
		soldProducts[1] = soldB;
		soldProducts[2] = soldC;
		soldProducts[3] = soldD;
		soldProducts[4] = soldE;
		soldProducts[5] = soldF;
		soldProducts[6] = soldG;
		soldProducts[7] = soldH;
		soldProducts[8] = soldI;
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
		double r = random.nextDouble();
		
		
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
