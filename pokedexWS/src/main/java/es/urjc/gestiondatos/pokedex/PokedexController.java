package es.urjc.gestiondatos.pokedex;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class PokedexController {
	MongoClient con = new MongoClient(new ServerAddress("localhost", 27017));
	MongoDatabase db = con.getDatabase("pokedex");
	MongoCollection<Document> collection = db.getCollection("pokemon");

	public String query(String type, int gen, int ord, int legendary) {
		
		BasicDBObject query = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("type", type));
		obj.add(new BasicDBObject("generation", gen));
		obj.add(new BasicDBObject("is_legendary", legendary));
		query.put("$and", obj);

		//FindIterable<Document> cursor = collection.find(query);
		String cursor = collection.find(query).toString();
		
		return cursor;
	}
}
