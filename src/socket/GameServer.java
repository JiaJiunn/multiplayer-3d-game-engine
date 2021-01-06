package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class GameServer {
	
	private ServerSocket serverSocket;
    private Socket clientSocket;
    
    private HashMap<Integer, Integer[]> client_input;
    
    private HashMap<Integer, Integer[]> pos_database = new HashMap<Integer, Integer[]>();

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		GameServer server = new GameServer();
        server.start(8888);
	}
	
	public void start(int port) throws IOException, ClassNotFoundException {
		serverSocket = new ServerSocket(port);
		while (true) {
			clientSocket = serverSocket.accept();
			
			ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
			Object object = objectInput.readObject();
			
			client_input = (HashMap<Integer, Integer[]>) object;
            for (Integer key : client_input.keySet()) {
            	pos_database.put(key, client_input.get(key));
            }
			ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutput.writeObject(pos_database);
		}
    }

}
