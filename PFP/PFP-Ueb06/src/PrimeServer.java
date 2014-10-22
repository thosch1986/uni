
public class PrimeServer implements Runnable {

	
	/**
	 * Creates a list of prime numbers (separated by ", ") between the numbers a and b.
	 * Both boundaries are included in the list if appropriate.
	 * @param a
	 * @param b
	 * @return the list as string
	 */
	public String primeList(long a, long b) {
       //TODO
		return null;
    }

	/**
	 * Simple primality test.
	 * @param p
	 * @return <code>true</code>, if p is prime, else return <code>false</code>
	 */
    public boolean isPrime(long p) {
       //TODO
    	return false;
    }


	@Override
	public void run() {
		//TODO
	}
	
	public int threads;
	public int port;
	public long up;
	public long low;
	
	/**
	 * Creates a new server instance
	 * @param threads
	 * number of threads used in the server
	 * @param port
	 * port used by the server
	 * @param up
	 * upper bound of the prime list
	 * @param low
	 * lower bound of the prime list
	 */
	public PrimeServerEx(int threads, int port, long up, long low){
		this.threads = threads;
		this.port = port;
		this.up = up;
		this.low = low;
	}
	
	public static void main(String[] args) {
        new Thread(new PrimeServer(10, 12345, 10, 200)).start();
    }


}
