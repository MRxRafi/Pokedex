package es.urjc.gestiondatos.pokedex;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
//import com.mongodb.client.MongoDatabase;

public class PokedexServer {
	public static void main(String[] args) {
		// ¿Se podria abrir el server?

		// Parte del cliente
		MongoClient con = new MongoClient(new ServerAddress("localhost", 27017));
		DB db = con.getDB("pokedex");
		DBCollection collection = db.getCollection("pokemon");

		BasicDBObject query = new BasicDBObject();
		query.put("type1", "electric");

		DBObject cursor = collection.findOne(query);
		System.out.println(cursor);

		con.close();
		/*
		 * while(cursor.hasNext()) { System.out.println(cursor.next()); }
		 */
	}
}
