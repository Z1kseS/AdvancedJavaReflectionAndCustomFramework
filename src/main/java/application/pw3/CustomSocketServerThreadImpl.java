package application.pw3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import framework.core.annotations.Component;
import framework.core.annotations.Prototype;

@Component("customSocketServerThread")
@Prototype
public class CustomSocketServerThreadImpl implements CustomSocketServerThread {

	private Socket socket;

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;

		// Print out details of this connection
		System.out.println("Thread started with name: " + Thread.currentThread().getName());
		System.out.println("Accepted Client Address - " + socket.getInetAddress().getHostName());

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			// At this point, we can read for input and reply with
			// appropriate output.

			// read incoming stream
			String clientCommand = in.readLine();
			System.out.println("Client Says :" + clientCommand);

			// Process it
			out.println("Server Says : " + clientCommand.toUpperCase());
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Clean up
			try {
				in.close();
				out.close();
				socket.close();
				System.out.println("...Stopped");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

}
