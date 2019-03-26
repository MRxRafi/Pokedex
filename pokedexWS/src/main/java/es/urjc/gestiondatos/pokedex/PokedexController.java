package es.urjc.gestiondatos.pokedex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import org.basex.core.*;
import org.basex.core.cmd.*;
import static com.mongodb.client.model.Sorts.*;

public class PokedexController {
	// Adri
	MongoClient con = new MongoClient(new ServerAddress("localhost", 27017));
	MongoDatabase db = con.getDatabase("pokedex");
	MongoCollection<Document> collection = db.getCollection("pokemon");

	// Rafa
	PokedexXML pokXML = new PokedexXML();

	// Adri
	@SuppressWarnings("deprecation")
	public String query(String type1, int gen, int ord, int legendary) {
		type1 = transformarTipo(type1);

		BasicDBObject query = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		if(!type1.equals("all")) {
		obj.add(new BasicDBObject("type1", type1));
		}
		if(gen != 0) {
		obj.add(new BasicDBObject("generation", gen));
		}
		obj.add(new BasicDBObject("is_legendary", legendary));
		query.put("$and", obj);
		
		Bson sort;
		FindIterable<Document> cursor;
		
		switch (ord) {
		case 1:
			sort = ascending("name");
			cursor = collection.find(query).sort(sort);
			System.out.println("ascending");
			break;
		case -1:
			sort = descending("name");
			cursor = collection.find(query).sort(sort);
			System.out.println("Descending");
			break;
		default:
			cursor = collection.find(query);
		}

		/*
		 * String result = "["; List<Document> listDoc = new ArrayList<>();
		 * 
		 * for (Document doc : cursor) { listDoc.add(doc); } result =
		 * result.replace(result.substring(result.length()-1), ""); result += "]";
		 */

		return JSON.serialize(cursor);
	}

	public void closeMongoSession() {
		con.close();
	}

	// Rafa
	// Cosas del XML
	public PokedexXML getPokedexXML() {
		// Leemos el XML y lo guardamos en pokXML
		// XQuery, etc
		Context context = new Context();
		String separator = System.getProperty("file.separator");
		String collectionPath = System.getProperty("user.dir") + separator + "src" + separator + "main" + separator
				+ "resources" + separator + "static" + separator;
		String xmlCatalog = "pokedex.xml";

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
		} catch (Exception e) {
			System.out.println("[POKEDEXML] No se ha podido realizar la consulta " + e.getMessage());
		}

		context.close();
		// Después, lo devolvemos
		return pokXML;
	}

	public String transformarTipo(String t) {
		switch (t) {
		case "Todos":
			return "all";
		case "Normal":
			return "normal";
		case "Fuego":
			return "fire";
		case "Agua":
			return "water";
		case "Planta":
			return "grass";
		case "Tierra":
			return "ground";
		case "Roca":
			return "rock";
		case "Electrico":
			return "electric";
		case "Veneno":
			return "poison";
		case "Bicho":
			return "bug";
		case "Volador":
			return "flying";
		case "Lucha":
			return "fight";
		case "Fantasma":
			return "ghost";
		case "Dragon":
			return "dragon";
		case "Psiquico":
			return "psychic";
		case "Hielo":
			return "ice";
		case "Hada":
			return "fairy";
		case "Acero":
			return "steel";
		case "Siniestro":
			return "dark";
		default:
			return "FALLO";
		}
	}
}
