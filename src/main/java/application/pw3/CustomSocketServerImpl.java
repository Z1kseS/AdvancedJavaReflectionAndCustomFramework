package application.pw3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import framework.core.annotations.Autowiring;
import framework.core.annotations.Component;
import framework.core.factory.BeanFactory;

@Component("customSocketServer")
public class CustomSocketServerImpl implements CustomSocketServer {

	@Autowiring
	private BeanFactory beanFactory;

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	private static final int PORT = 8080;
	private ServerSocket serverSocket;

	@Override
	public void start() throws IOException {
		System.out.println(beanFactory);
		if (serverSocket == null) {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server registered at port " + PORT);

			while (true) {
				try {
					Socket socket = serverSocket.accept();
					System.out.println(beanFactory);
					CustomSocketServerThread customSocketServerThread = (CustomSocketServerThread) beanFactory
							.getBean("customSocketServerThread");
					customSocketServerThread.setSocket(socket);
					new Thread(customSocketServerThread).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Server is already registered.");
		}
	}

	@Override
	public void stop() throws IOException {
		if (serverSocket != null) {
			System.out.println("Server isn't started.");
		} else {
			serverSocket.close();
		}

	}
}
