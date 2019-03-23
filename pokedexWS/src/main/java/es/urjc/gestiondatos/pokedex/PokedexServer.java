package es.urjc.gestiondatos.pokedex;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.MongoDatabase;

public class PokedexServer {
	public static void main(String[] args) {
		// ¿Se podria abrir el server?

		// Parte del cliente
		MongoClient con = new MongoClient(new ServerAddress("localhost", 27017));
		MongoDatabase db = con.getDatabase("pokedex");
		MongoCollection<Document> collection = db.getCollection("pokemon");

		BasicDBObject query = new BasicDBObject();
		query.put("type1", "electric");

		FindIterable<Document> cursor = collection.find(query);
	     Iterator<Document> it = cursor.iterator();

		con.close();
		while(it.hasNext()) { System.out.println(it.next()); }
	}
}
