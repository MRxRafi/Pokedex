package es.urjc.gestiondatos.pokedex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import org.basex.core.BaseXException;
import org.basex.core.*;
import org.basex.core.cmd.*;

public class PokedexController {
	//Adri
	MongoClient con = new MongoClient(new ServerAddress("localhost", 27017));
	MongoDatabase db = con.getDatabase("pokedex");
	MongoCollection<Document> collection = db.getCollection("pokemon");
	
	//Rafa
	PokedexXML pokXML = new PokedexXML();
	
	//Adri
	@SuppressWarnings("deprecation")
	public String query(String type1, int gen, int ord, int legendary) {
		
		BasicDBObject query = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("type1", type1));
		obj.add(new BasicDBObject("generation", gen));
		obj.add(new BasicDBObject("is_legendary", legendary));
		query.put("$and", obj);

		FindIterable<Document> cursor = collection.find(query);
		/*
		String result = "[";
	    List<Document> listDoc = new ArrayList<>();

	    for (Document doc : cursor) {
	    	listDoc.add(doc);
	    }
	    result = result.replace(result.substring(result.length()-1), "");
	    result += "]";
	    */
	    
		return JSON.serialize(cursor);
	}
	
	public void closeMongoSession() {
		con.close();
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
