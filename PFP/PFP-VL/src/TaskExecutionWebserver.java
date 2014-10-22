import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class TaskExecutionWebserver {
	
	private static final int NTHREADS = 100;
	private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
	
	public static void main(String[] args) {
		
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(80);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			final Socket connection;
			try {
				connection = socket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Runnable task = new Runnable() {
				public void run() {
					handleRequest(connection);
				}

				private void handleRequest(Socket connection) {
					// TODO Auto-generated method stub
					
				}
			};
			exec.execute(task);	// bis zu NTREADS fuehren Anfragen aus!
		}
	}

}
