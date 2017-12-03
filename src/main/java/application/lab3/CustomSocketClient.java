package application.lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import framework.core.annotations.Component;

@Component("customSocketClient")
public class CustomSocketClient {
	public void start(String host, int port) {
		Socket socket = null;

		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException uhe) {
			System.out.println("Unknown Host :" + host);
			socket = null;
		} catch (IOException ioe) {
			System.out.println("Cant connect to server at " + port + ". Make sure it is running.");
			socket = null;
		}

		if (socket == null)
			System.exit(-1);

		BufferedReader in = null;
		PrintWriter out = null;

		try {
			// Create the streams to send and receive information
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			out.println("ahahaha");
			out.flush();

			System.out.println("Retrieved message from server: " + in.readLine());

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
