package examples.mmcc;

import java.util.Random;

import general.Counter;
import general.SystemState;
import general.annotations.AutoCounter;
import general.annotations.AutoMeasure;
import general.annotations.Initialize;

/**
 * This class models the system state for a typical M/M/c/c queuing system.
 * 
 * @author Nemanja Milovanovic
 *
 */

public class MMCCState extends SystemState<MMCCState> {
		
	private final Random random;
	private final int[][] weights;
	private final int[] seats;
	private final int[] revs;
	private final int products = 10;
	private final Counter[] soldProducts = new Counter[products];
	
	
	@AutoCounter("Number of rejected arrivals")
	private Counter rejected;
	
	@AutoCounter("Total arrivals")
	private Counter arrivals;
	
	@AutoCounter("Total revenue")
	private Counter revenue;
	
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
			double timeHorizon, 
			long seed,
			int[][] weights,
			int[] seats,
			int[] revs) {
		super(timeHorizon, seed);
		this.weights = weights;
		this.seats = seats;
		this.revs = revs;
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
		soldProducts[9] = rejected;
	}
	
	@Initialize
	public void initReplication() {
		double lambdaBusiness = 1.2* Math.sin((Math.PI*179) / 180);
		double lambdaLeisure = (0.6*179)/179;
		double lambdaEconomy = 0.8*(1-(Math.sin((Math.PI*179) / 180)));
		
		
		double nextArrivalTimesBusiness = Utils.nextInterArrivalTime(random, lambdaBusiness);
		addEvent(nextArrivalTimesBusiness, this::doArrivalBusiness);
		
		double nextArrivalTimesLeisure = Utils.nextInterArrivalTime(random, lambdaLeisure);
		addEvent(nextArrivalTimesLeisure, this::doArrivalLeisure);
		
		double nextArrivalTimesEconomy = Utils.nextInterArrivalTime(random, lambdaEconomy);
		addEvent(nextArrivalTimesEconomy, this::doArrivalEconomy);
		
	}
	
	
	// call arrival method with passenger type
	public void doArrivalBusiness(double eventTime) {
		doArrival(eventTime, 0);
	}
	public void doArrivalLeisure(double eventTime) {
		doArrival(eventTime, 1);
	}
	public void doArrivalEconomy(double eventTime) {
		doArrival(eventTime, 2);
	}
	
	
	
	public void doArrival(double eventTime, int passenger) {
		double newTime = eventTime;
		int availability[] = new int[products];
		double probs[] = new double[products];
		
				
		// update counter for total nr of arrivals
		arrivals.increment();
						
		
		for (int i=0; i<products; i++) {
			// check availability seats
			if (soldProducts[i].getValue() < seats[i]) {
				availability[i] = 1;
			}
			else {
				availability[i] = 0;
			}
		}
			
		// check time restriction
		if (newTime > 179-21) {	
			availability[3] = 0;
			availability[7] = 0;
		}
		
		
		// Calculate probabilities
		for (int i=0; i<products; i++) {
			int sum = 0;
			double prev = 0;
			
			if (availability[i] == 0) {
				probs[i] = 1;
			}
			else {
				for (int j=0; j<products; j++) {
					sum += availability[j] * weights[passenger][j];
				}
				if (i>0) {
					prev = probs[i-1];
				}
				probs[i] = weights[passenger][i] / (sum+.0) + prev;
			}
		}
		
		
		// choose product
		double r = random.nextDouble();
		boolean stillChoosing = true;
		
		while (stillChoosing) {
			int i = 0;
				if (r < probs[i]) {
					
					soldProducts[i].increment();
					revenue.incrementBy(revs[i]);
					
					stillChoosing = false;
				}
				else {
					i++;
				}
		}

		
		// generate next arrival
		double lambda;
		if (passenger == 0) {
			lambda = 1.2* Math.sin((Math.PI*179-newTime) / 180);
		}
		else if (passenger == 1) {
			lambda = (0.6*(179-newTime))/179;
		}
		else {
			lambda = 0.8*(1-(Math.sin((Math.PI*(179-newTime)) / 180)));
		}
		double currentTime = eventTime;
		double nextInterArrivalTime = Utils.nextInterArrivalTime(random, lambda);
		double nextArrivalTime = currentTime + nextInterArrivalTime;
		addEvent(nextArrivalTime, this::doArrivalBusiness);
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
