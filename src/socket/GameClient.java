package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class GameClient {
	
	private static Socket clientSocket;
	
	public void startConnection(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
    }

    public HashMap<Integer, Integer[]> sendMessage(Integer id, Integer[] controls) throws IOException, ClassNotFoundException {
    	HashMap<Integer, Integer[]> titleList = new HashMap<Integer, Integer[]>();
		ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
		
//		HashMap<Integer, Integer[]> capitalCities = new HashMap<Integer, Integer[]>();
//		Integer[] my_info = {1, -1, 1};
//		capitalCities.put(0, my_info);
		
		HashMap<Integer, Integer[]> controlsUpdate = new HashMap<Integer, Integer[]>();
		controlsUpdate.put(id, controls);
		objectOutput.writeObject(controlsUpdate);
		
		ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
		Object object = objectInput.readObject();
		HashMap<Integer, Integer[]> all_pos =  (HashMap<Integer, Integer[]>) object;
        return all_pos;
    }

    public static void stopConnection() throws IOException {
    	clientSocket.close();
    }
    
    public static void main(String[] args) throws ClassNotFoundException, IOException {
    	for (int i = 0; i < 5; i++) {
    		GameClient client = new GameClient();
    	    client.startConnection("127.0.0.1", 8888);
    	    Integer[] controlsUpdate = {0, 1, i};
    	    HashMap<Integer, Integer[]> ret = client.sendMessage(0, controlsUpdate);
    	    System.out.println(ret.get(0)[0]);
    	    System.out.println(ret.get(0)[1]);
    	    System.out.println(ret.get(0)[2]);
    	    System.out.println("---");
    	    stopConnection();
    	}
	}

}
