package es.urjc.gestiondatos.pokedex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WebsocketPokedexHandler extends TextWebSocketHandler {
	private static Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<WebSocketSession>());
	ObjectMapper mapper = new ObjectMapper();
	boolean debug = true;
	PokedexController pokedexController = new PokedexController();

	// Invoked after WebSocket negotiation has succeeded and the WebSocket
	// connection is opened and ready for use.
	// @Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	// Invoked after the WebSocket connection has been closed by either side, or
	// after a transport error has occurred. Although the session may technically
	// still be open, depending on the underlying implementation, sending messages
	// at this point is discouraged and most likely will not succeed.
	// @Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
	}

	// Invoked when a new WebSocket message arrives.
	// @Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		synchronized (sessions) {
			// obj recibido
			JsonNode node = mapper.readTree(message.getPayload());
			// Obj a mandar
			ObjectNode json = mapper.createObjectNode();

			switch (node.get("type").asText()) {
			case "JOIN":
				json.put("type", "CONEX_ESTAB");
				json.putPOJO("pokedexXML", pokedexController.getPokedexXML());
				session.sendMessage(new TextMessage(json.toString()));
				pokedexController.insertImages();
				break;
				
			case "QUERY":
				String type = node.get("type1").asText();
				int gen = node.get("gen").asInt();
				int ord = node.get("ord").asInt();
				int leg = node.get("leg").asInt();
				json.put("type", "RESULT");
				json.put("result", pokedexController.query(type, gen, ord, leg));
				TextMessage m = new TextMessage(json.toString());
				session.sendMessage(m);
				
				break;
				
			case "PHOTO":
				String idx = node.get("idx").asText();
				json.put("type", "PHOTO_RESULT");
				pokedexController.writeImages(idx);
				//json.put("data", pokedexController.readImages().toString());
				//session.sendMessage(new TextMessage(json.toString()));
				byte[] byteArray = pokedexController.readImages();
				//session.sendMessage(byteArray, 0, byteArray.length);
				System.out.println(byteArray);
				break;
				
			case "DELETE":
				pokedexController.delete(node.get("id").asText());
				json.put("type", "DELETE_COMPLETE");
				break;
				
			case "CREATE":
				break;
			}
		}
	}
}
