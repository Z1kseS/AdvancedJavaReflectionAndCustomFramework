package application.lab3;

import java.io.IOException;

public interface CustomSocketServer {
	void start() throws IOException;
	
	void stop() throws IOException;
}
