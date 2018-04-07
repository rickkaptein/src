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
	
	private int serversBusy;
	
	private final Random random;
	private final double lambda;
	private final double mu;
	private final int nServers;
	
	@AutoCounter("Server utilization cumulative")
	private Counter utilization;
	
	@AutoCounter("Number of rejected arrivals")
	private Counter rejected;
	
	@AutoCounter("Total arrivals")
	private Counter arrivals;
	
	// Example of annotation with initializion value
	@AutoCounter(value="Cumulated time all servers busy", initialValue=0d)
	private Counter busyTime;
	
	public MMCCState(
			int nServers, 
			double lambda, 
			double mu, 
			double timeHorizon, 
			long seed) {
		super(timeHorizon, seed);
		this.nServers = nServers;
		this.lambda = lambda;
		this.mu = mu;
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
		
		// update counter for server utilization
		utilization.incrementBy((newTime - prevTime)*serversBusy);
		
		// update counter for total nr of arrivals
		arrivals.increment();
		
		if (serversBusy == nServers) {
			// update counter for rejection of arrival
			rejected.increment();
			
			// update counter for all servers busy, only if all servers busy
			busyTime.incrementBy(newTime - prevTime);
		} else {
			// immediately get serviced
			// update system state
			serversBusy++;

			// generate next departure
			double currentTime = eventTime;
			double serviceDuration = Utils.nextServiceTime(random, mu);
			double departureTime = currentTime + serviceDuration;
			addEvent(departureTime, this::doDeparture);
		}
		
		// generate next arrival
		double currentTime = eventTime;
		double nextInterArrivalTime = Utils.nextInterArrivalTime(random, lambda);
		double nextArrivalTime = currentTime + nextInterArrivalTime;
		addEvent(nextArrivalTime, this::doArrival);
	}
	
	public void doDeparture(double eventTime) {
		double prevTime = getCurrentTime();
		double newTime = eventTime;
		
		// update counter for server utilization
		utilization.incrementBy((newTime - prevTime)*serversBusy);
		
		// update counter for all servers busy, only if all servers busy
		if (serversBusy == nServers) {
			busyTime.incrementBy(newTime - prevTime);
		}

		// decrease number of busy servers by 1
		serversBusy--;
	}
	
	@AutoMeasure("Busy fraction")
	public Double getBusyFraction() {
		//TODO: add the actual computation
		return 0d;
	}
	
	@AutoMeasure("Rejection probability")
	public double getRejectionProbability() {
		//TODO: add the actual computation
		return 0d;
	}
	
	@AutoMeasure("Server utilization")
	public Double getServerUtilization() {
		//TODO: add the actual computation
		return 0d;
	}

	@Override
	public void reset() {
		serversBusy = 0;
		// TODO: do not forget to edit this method if you ever change this class!
	}
}
