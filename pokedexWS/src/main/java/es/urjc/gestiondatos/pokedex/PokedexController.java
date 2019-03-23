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
	//Adri
	MongoClient con = new MongoClient(new ServerAddress("localhost", 27017));
	MongoDatabase db = con.getDatabase("pokedex");
	MongoCollection<Document> collection = db.getCollection("pokemon");
	
	//Rafa
	PokedexXML pokXML = new PokedexXML();
	
	//Adri
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
	
	//Rafa
	//Cosas del XML
	public PokedexXML getPokedexXML() {
		//Leemos el XML y lo guardamos en pokXML
		//XQuery, etc
		Context context = new Context();
		String separator = System.getProperty("file.separator");
		String collectionPath = System.getProperty("user.dir")+ separator+"src"+separator+"main"+separator
				+"resources"+separator+"static"+separator;
	    String xmlCatalog ="pokedex.xml";
		try {
			new CreateDB("pokedex").execute(context);
			new Add(xmlCatalog, collectionPath).execute(context);
			
			XQuery usuario = new XQuery("/pokedex/usuario");
			XQuery num_serie = new XQuery("/pokedex/num_serie");
			XQuery color_cir = new XQuery("/pokedex/color_cir");
			
			pokXML.setUsuario((String) usuario.execute(context).split(">|<")[2]);
			pokXML.setNum_serie((String) num_serie.execute(context).split(">|<")[2]);
			pokXML.setColor_cir((String) color_cir.execute(context).split(">|<")[2]);
			
			System.out.println("[POKEDEXML] Satisfactorio");
		} catch(Exception e) {
			System.out.println("[POKEDEXML] No se ha podido realizar la consulta " + e.getMessage());
		}
		
		context.close();
		//Después, lo devolvemos
		return pokXML;
	}
}
