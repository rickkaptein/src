package examples.mmcc;

import java.util.Arrays;
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
		
	private Random random;
	private final int[][] weights;
	private final int[] seats;
	private final int[] revs;
	private final int products = 10;
	private final double [] mu;
	private final double [] sigma;
	private final Counter[] soldProducts = new Counter[products];
	private final Counter[] soldOutProducts = new Counter[products];
	private final Counter[] arrivalsArray = new Counter[3];
	private final String question;
	private long prevSeed;
	private boolean evenIteration;
	
	
	
	@AutoCounter("Total number of rejected arrivals")
	private Counter rejected;
	
	@AutoCounter("Number of rejected business arrivals")
	private Counter rejectedB;
	
	@AutoCounter("Number of rejected leisure arrivals")
	private Counter rejectedL;
	
	@AutoCounter("Number of rejected economy arrivals")
	private Counter rejectedE;
	
	
	@AutoCounter("Total arrivals")
	private Counter arrivals;
	
	@AutoCounter("Total arrivals business")
	private Counter arrivalsB;
	
	@AutoCounter("Total arrivals leisure")
	private Counter arrivalsL;
	
	@AutoCounter("Total arrivals economy")
	private Counter arrivalsE;
	
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

	@AutoCounter("Sold out product A")
	private Counter soldOutA;

	@AutoCounter("Sold out product B")
	private Counter soldOutB;

	@AutoCounter("Sold out product C")
	private Counter soldOutC;

	@AutoCounter("Sold out product D")
	private Counter soldOutD;

	@AutoCounter("Sold out product E")
	private Counter soldOutE;

	@AutoCounter("Sold out product F")
	private Counter soldOutF;

	@AutoCounter("Sold out product G")
	private Counter soldOutG;

	@AutoCounter("Sold out product H")
	private Counter soldOutH;

	@AutoCounter("Sold out product I")
	private Counter soldOutI;
	
	@AutoCounter("Sold out all products")
	private Counter soldOutAll;
	
	// Example of annotation with initialization value
	@AutoCounter(value="Cumulated time all servers busy", initialValue=0d)
	private Counter busyTime;
	
	public MMCCState(
			double timeHorizon, 
			long seed,
			int[][] weights,
			int[] seats,
			int[] revs,
			double[] mu,
			double[] sigma,
			String question) {
		super(timeHorizon, seed);
		this.weights = weights;
		this.seats = seats;
		this.revs = revs;
		this.mu = mu;
		this.sigma = sigma;
		this.question = question;
		this.prevSeed = seed;
		random = new Random(seed);
		evenIteration = false;
		
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
		
		soldOutProducts[0] = soldOutA;
		soldOutProducts[1] = soldOutB;
		soldOutProducts[2] = soldOutC;
		soldOutProducts[3] = soldOutD;
		soldOutProducts[4] = soldOutE;
		soldOutProducts[5] = soldOutF;
		soldOutProducts[6] = soldOutG;
		soldOutProducts[7] = soldOutH;
		soldOutProducts[8] = soldOutI;
		
		arrivalsArray[0] = arrivalsB;
		arrivalsArray[1] = arrivalsL;
		arrivalsArray[2] = arrivalsE;
		
	}
	
	@Initialize
	public void initReplication() {
		double lambdaBusiness = 1.2* Math.sin((Math.PI*179) / 180);
		double lambdaLeisure = (0.6*179)/179;
		double lambdaEconomy = 0.8*(1-(Math.sin((Math.PI*179) / 180)));		
		
		double nextArrivalTimesBusiness = Utils.nextArrivalTime(random, 0, 0);
		addEvent(nextArrivalTimesBusiness, this::doArrivalBusiness);
		
		double nextArrivalTimesLeisure = Utils.nextArrivalTime(random, 0, 1);
		addEvent(nextArrivalTimesLeisure, this::doArrivalLeisure);
		
		double nextArrivalTimesEconomy = Utils.nextArrivalTime(random, 0, 2);
		addEvent(nextArrivalTimesEconomy, this::doArrivalEconomy);
		
		/*
		if (question == "a" || question == "b" || question == "c" || question == "d") {
			double nextArrivalTimesBusiness = Utils.nextInterArrivalTime(random, lambdaBusiness, true);
			addEvent(nextArrivalTimesBusiness, this::doArrivalBusiness);
			System.out.println(nextArrivalTimesBusiness);
			
			double nextArrivalTimesLeisure = Utils.nextInterArrivalTime(random, lambdaLeisure, true);
			addEvent(nextArrivalTimesLeisure, this::doArrivalLeisure);
			System.out.println(nextArrivalTimesLeisure);
			
			double nextArrivalTimesEconomy = Utils.nextInterArrivalTime(random, lambdaEconomy, true);
			addEvent(nextArrivalTimesEconomy, this::doArrivalEconomy);
			System.out.println(nextArrivalTimesEconomy);
		}
		else {
			double nextArrivalTimesBusiness = Utils.nextInterArrivalTimeNormal(random, mu[0], sigma[0], true);
			addEvent(nextArrivalTimesBusiness, this::doArrivalBusiness);
			
			double nextArrivalTimesLeisure = Utils.nextInterArrivalTimeNormal(random, mu[1], sigma[1], true);
			addEvent(nextArrivalTimesLeisure, this::doArrivalLeisure);
			
			double nextArrivalTimesEconomy = Utils.nextInterArrivalTimeNormal(random, mu[2], sigma[2], true);
			addEvent(nextArrivalTimesEconomy, this::doArrivalEconomy);
		}
		*/
		
		
		
	}
	
	@StopCriterium
	public boolean allProductsSold() {
		return soldOutAll.getValue()>0;
	}
	
	
	// call arrival method with passenger type
	public void doArrivalBusiness(double eventTime) {
		doArrival(eventTime, 0, mu[0], sigma[0]);
	}
	public void doArrivalLeisure(double eventTime) {
		doArrival(eventTime, 1, mu[1], sigma[1]);
	}
	public void doArrivalEconomy(double eventTime) {
		doArrival(eventTime, 2, mu[2], sigma[2]);
	}
	
	
	
	public void doArrival(double eventTime, int passenger, double muArrival, double sigmaArrival) {
		double newTime = eventTime;
		int availability[] = new int[products];
		double probs[] = new double[products];
				
		// update counter for total nr of arrivals
		arrivals.increment();
		arrivalsArray[passenger].increment();
					

		// check available products
		if (question == "a" || question == "b" || question == "c") {
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
		}
		//Question d and e
		else {
			boolean stillChecking = true;
			availability[9] = 1;
			int i = products -2;
			
			while (stillChecking) {
				if (i>0) {
					if (soldProducts[i].getValue() < seats[i]) {
						availability[i] = 1;
						stillChecking = false;
						
						// check time restriction of products D and H
						if (newTime > 179-21 && (i==3||i==7)) {	
							availability[i] = 0;
							i--;
						}
					}
					else {
						availability[i] = 0;
						i--;
					}
				}
				else {
					stillChecking = false;
				}
			}
		}
	
		
		
		// Calculate probabilities
		int sum = 0;
		for (int j=0; j<products; j++) {
			sum += availability[j] * weights[passenger][j];
		}
		
		
		for (int i=0; i<products; i++) {
			// set probability for unavailable items at 0
			if (availability[i] == 0) {
				probs[i] = 0;
			}
			else {
				probs[i] = weights[passenger][i] / ((double)sum);
			}
			// add previous probability
			if (i>0) {
				probs[i] += probs[i-1];
			}
			
		}
		
		// Set probabilities for unavailable products at 0
		for (int i=0; i<products; i++) {
			if (availability[i] == 0) {
				probs[i] = 0;
			}
		}
		
	
		// choose product
		double r;
		if (question == "a" || question == "b") {
			r = random.nextDouble();
		}
		else {
			if (evenIteration) {
				r = random.nextDouble();
			}
			else {
				r = 1-random.nextDouble();
			}
		}
		boolean stillChoosing = true;
		int iteration = 0;
		while (stillChoosing) {
			boolean rejection = false;

			if (r < probs[iteration]) {
				
				if (question == "a" || question == "b" || question == "c" || question == "d") {
					soldProducts[iteration].increment();
					revenue.incrementBy(revs[iteration]);
				}
				
				//For question e
				else if (iteration != 9){
					double rand;
					int increment = 0;
					
					//Generate new random number
					if (evenIteration) {
						rand = random.nextDouble();
					}
					else {
						rand = 1-random.nextDouble();
					}
					
					//Choose amount of products
					if (rand < 0.55) {
						increment = 1;
					}
					else if (rand < 0.85) {
						if (seats[iteration] - soldProducts[iteration].getValue()  >= 2) {
							increment = 2;
						}
						else {
							rejection = true;
						}
					}
					else if (rand < 0.95) {
						if (seats[iteration] - soldProducts[iteration].getValue()  >= 3) {
							increment = 3;
						}
						else {
							rejection = true;
						}
					}
					else {
						if (seats[iteration] - soldProducts[iteration].getValue()  >= 4) {
							increment = 4;
						}
						else {
							rejection = true;
						}
					}
					soldProducts[iteration].incrementBy(increment);
					if (rejection) {
						soldProducts[9].increment();
					}
					revenue.incrementBy(increment*revs[iteration]);
				}
				else {
					soldProducts[9].increment();
				}
				
				
				//Increment rejection counter per class if no product is chosen
				if (iteration == 9 || rejection) {
					if (passenger == 0) {
						rejectedB.increment();
					}
					else if (passenger == 1) {
						rejectedL.increment();
					}
					else {
						rejectedE.increment();
					}
				}
				
				stillChoosing = false;
			}
			else {
				iteration++;
			}
		}
		
		// Update counters for sold out products
		int soldOutCounter = 0;
		for (int i=0; i<products-1; i++) {
			if (soldProducts[i].getValue() == seats[i]) {
				soldOutCounter++;
				soldOutProducts[i].increment();
			}
		}
		if (soldOutCounter == products-1) {
			soldOutAll.increment();
		}
		
		// generate next arrival
		double currentTime = eventTime;
		
		// calculate lambda
		double lambda;
		if (passenger == 0) {
			lambda = 1.2* Math.sin((Math.PI*(179-newTime)) / 180);
		}
		else if (passenger == 1) {
			lambda = (0.6*(179-newTime))/179;
		}
		else {
			lambda = 0.8*(1-(Math.sin((Math.PI*(179-newTime)) / 180)));
		}
		
		
		double nextArrivalTime;
		
		//Question a and b
		if (question == "a" || question == "b") {
			nextArrivalTime= currentTime + Utils.nextInterArrivalTime(random, lambda, true);
			//nextArrivalTime = Utils.nextArrivalTime(random, newTime, passenger);
		}
		//Question c and d
		else if (question == "c" || question == "d") {
			nextArrivalTime= currentTime + Utils.nextInterArrivalTime(random, lambda, evenIteration);
		}	
		//Question e
		else {
			nextArrivalTime = currentTime + Utils.nextInterArrivalTimeNormal(random, muArrival, sigmaArrival, evenIteration);
		}
		
		// call next arrival method
		if (passenger == 0) {
			addEvent(nextArrivalTime, this::doArrivalBusiness);
		}
		else if (passenger == 1) {
			addEvent(nextArrivalTime, this::doArrivalLeisure);
		}
		else {
			addEvent(nextArrivalTime, this::doArrivalEconomy);
		}
	}
	
	
	@AutoMeasure("Rejection probability")
	public double getRejectionProbability() {
		return rejected.getValue()/arrivals.getValue();
	}
	@AutoMeasure("Rejection probability business")
	public double getRejectionBProbability() {
		double result = 0;
		if (arrivalsArray[0].getValue() > 0) {
			result = rejectedB.getValue()/arrivalsArray[0].getValue();
		}
		return result;
	}
	@AutoMeasure("Rejection probability leisure")
	public double getRejectionLProbability() {
		return rejectedL.getValue()/arrivalsArray[1].getValue();
	}
	@AutoMeasure("Rejection probability economy")
	public double getRejectionEProbability() {
		return rejectedE.getValue()/arrivalsArray[2].getValue();
	}
	
	@AutoMeasure("Total revenue")
	public double getTotalRevenue() {
		return revenue.getValue();
	}
	
	@AutoMeasure("Sold out product A")
	public double getSoldOutA() {
		double result = 0;
		if (soldOutA.getValue() > 0) {
			result = soldOutA.getValue() / soldOutA.getValue();
		}
		return result;
	}
	
	@AutoMeasure("Sold out product B")
	public double getSoldOutB() {
		double result = 0;
		if (soldOutB.getValue() > 0) {
			result = soldOutB.getValue() / soldOutB.getValue();
		}
		return result;
	}
	
	@AutoMeasure("Sold out product C")
	public double getSoldOutC() {
		double result = 0;
		if (soldOutC.getValue() > 0) {
			result = soldOutC.getValue() / soldOutC.getValue();
		}
		return result;
	}
	
	@AutoMeasure("Sold out product D")
	public double getSoldOutD() {
		double result = 0;
		if (soldOutD.getValue() > 0) {
			result = soldOutD.getValue() / soldOutD.getValue();
		}
		return result;	}
	
	@AutoMeasure("Sold out product E")
	public double getSoldOutE() {
		double result = 0;
		if (soldOutE.getValue() > 0) {
			result = soldOutE.getValue() / soldOutE.getValue();
		}
		return result;
	}
	
	@AutoMeasure("Sold out product F")
	public double getSoldOutF() {
		double result = 0;
		if (soldOutF.getValue() > 0) {
			result = soldOutF.getValue() / soldOutF.getValue();
		}
		return result;
	}
	
	@AutoMeasure("Sold out product G")
	public double getSoldOutG() {
		double result = 0;
		if (soldOutG.getValue() > 0) {
			result = soldOutG.getValue() / soldOutG.getValue();
		}
		return result;
	}
	
	@AutoMeasure("Sold out product H")
	public double getSoldOutH() {
		double result = 0;
		if (soldOutH.getValue() > 0) {
			result = soldOutH.getValue() / soldOutH.getValue();
		}
		return result;
	}
	
	@AutoMeasure("Sold out product I")
	public double getSoldOutI() {
		double result = 0;
		if (soldOutI.getValue() > 0) {
			result = soldOutI.getValue() / soldOutI.getValue();
		}
		return result;
	}
	
	@AutoMeasure("All products sold out")
	public double getSoldOutAll() {
		double result = 0;
		if (soldOutAll.getValue() > 0) {
			result = soldOutAll.getValue() / soldOutAll.getValue();
		}
		return result;
	}
	

	@Override
	public void reset() {
				
		if (question != "a" || question != "b") {
			evenIteration = !evenIteration;

			// Store seed value from even iterations
			if (evenIteration) {
				prevSeed = random.nextLong();
			}

			// reuse seed value
			random = new Random(prevSeed);
		}
	}
}
