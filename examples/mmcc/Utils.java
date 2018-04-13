package examples.mmcc;

import java.util.Random;

/**
 * This is a "static" class for library routines. At the moment, it is only used for generating inter-arrival and service times. 
 * Note that when drawing from statistical distributions outside the course Simulation, it is better if you use a library like Apache Math, as it has an entire 
 * development team backing it up, and is thus correct, well-designed, and fast. 
 * 
 * @author Nemanja Milovanovic
 *
 */

public final class Utils {

	private Utils() {
		
	}
	
	/**
	 * @param random	{@link Random} object used to draw pseudo-random numbers
	 * @param lambda	Arrival rate
	 * @param evenIteration	
	 * @return			Returns a realization drawn from an exponential distribution, with rate {@code lambda}, 
	 * 					representing the next inter-arrival time.
	 */
	public static double nextInterArrivalTime(Random random, double lambda, boolean evenIteration) {
		double r;
		if (evenIteration) {
			r = random.nextDouble();
		}
		else {
			r = 1-random.nextDouble();
		}
		return -Math.log(1-r)/lambda;
	}
	
	
	/**
	 * @param random	{@link Random} object used to draw pseudo-random numbers
	 * @param mu	Arrival rate
	 * @return			Returns a realization drawn from an exponential distribution, with rate {@code lambda}, 
	 * 					representing the next inter-arrival time.
	 */
	public static double nextInterArrivalTimeNormal(Random random, double mu, double sigma, boolean evenIteration) {
		double u1;
		double u2;
		boolean outOfRange = true;
		double y1;
		double y2;
		double z;
		double inter = 0;
		
		
		if (evenIteration) {
			while (outOfRange) {
				u1 = random.nextDouble();
				u2 = random.nextDouble();
				y1 = -Math.log(u1);
				y2 = -Math.log(u2);
				
				if (y2 >= Math.pow(y1-1, 2)/2) {
					if (random.nextDouble() <= 0.5) {
						z = y1;
					}
					else {
						z = -y1;
					}
					inter = mu + z * sigma;
					if (inter>0) {
						outOfRange = false;
					}
				}
			}
		}
		else {
			while (outOfRange) {
				u1 = 1- random.nextDouble();
				u2 = 1- random.nextDouble();
				y1 = -Math.log(u1);
				y2 = -Math.log(u2);
				
				if (y2 >= Math.pow(y1-1, 2)/2) {
					if (random.nextDouble() <= 0.5) {
						z = y1;
					}
					else {
						z = -y1;
					}
					inter = mu + z * sigma;
					if (inter>0) {
						outOfRange = false;
					}
				}
			}
		}
		return inter;
	}
	
}
