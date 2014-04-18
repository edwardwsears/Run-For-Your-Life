package run_for_your_life.connections.server;

import java.io.IOException;

import run_for_your_life.connections.network.GameRequestObject;
import run_for_your_life.connections.network.GameResponseObject;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer {	
	public static void main (String[] args) {
		Server server = new Server();
		server.start();
		try {
			server.bind(54555, 54777);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof GameRequestObject) {
					GameRequestObject request = (GameRequestObject)object;
					System.out.println(request.text);

					GameResponseObject response = new GameResponseObject();
					response.text = "Thanks";
					connection.sendTCP(response);
				}
			}
		});
	}

}
