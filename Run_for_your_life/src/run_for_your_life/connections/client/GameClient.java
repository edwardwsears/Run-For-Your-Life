package run_for_your_life.connections.client;

import java.io.IOException;

import run_for_your_life.connections.network.GameRequestObject;
import run_for_your_life.connections.network.GameResponseObject;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class GameClient {
	public static void main(String[] args) {
		Client client = new Client();
	    client.start();
	    
	    try {
			client.connect(5000, "192.168.0.4", 54555, 54777);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    GameRequestObject request = new GameRequestObject();
	    request.text = "Here is the request";
	    client.sendTCP(request);
		
		client.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof GameResponseObject) {
					GameResponseObject response = (GameResponseObject)object;
					System.out.println(response.text);
				}
			}
		});
	}
}
