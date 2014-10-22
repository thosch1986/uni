import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * created 07.05.2008
 */

/**
 * Small Demo-Server that answers each request with the current date
 * 
 * @author Marc Woerlein<woerlein@informatik.uni-erlangen.de>
 * 
 */
public class TimeServer {

	public static void main(String[] args) throws IOException {
		final int port = 12345;

		ServerSocket s = new ServerSocket(port);
		while (!s.isClosed()) {
			// accept connection
			final Socket sock = s.accept();

			// send output
			final PrintStream os = new PrintStream(sock.getOutputStream());
			os.println("<html><body>");
			os.println(new Date());
			os.println("</body></html>");
			os.flush();

			// end connection
			sock.close();
		}
	}
}
