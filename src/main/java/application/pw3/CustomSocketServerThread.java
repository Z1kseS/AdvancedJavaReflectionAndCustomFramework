package application.pw3;

import java.net.Socket;

public interface CustomSocketServerThread extends Runnable {
	void setSocket(Socket socket);
}