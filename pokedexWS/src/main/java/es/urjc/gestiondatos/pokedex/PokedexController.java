package es.urjc.gestiondatos.pokedex;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.XQuery;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;

@SuppressWarnings("deprecation")
public class PokedexController {
	MongoClient con = new MongoClient(new ServerAddress("localhost", 27017));
	MongoDatabase db = con.getDatabase("pokedex");
	MongoCollection<Document> collection = db.getCollection("pokemon");

	GridFS gfsPhoto = new GridFS(con.getDB("pokedex"), "photo");

	PokedexXML pokXML = new PokedexXML();

	// Devuelve true si existe la coleccion con el nombre indicado
	private boolean collectionExists(final String collectionName) {
		MongoIterable<String> collectionNames = db.listCollectionNames();
		for (final String name : collectionNames) {
			if (name.equalsIgnoreCase(collectionName + ".files") || name.equalsIgnoreCase(collectionName + ".chunks")) {
				return true;
			}
		}
		return false;
	}

	// Inserta las imagenes en la base de datos si no lo ha hecho ya
	public void insertImages() {
		if (!collectionExists("photo")) {

			File[] images = getImagesFromFolder(System.getProperty("user.dir") + "/src/main/resources/static/pokemon");
			for (File file : images) {
				GridFSInputFile gfsFile;
				try {
					gfsFile = gfsPhoto.createFile(file);
					gfsFile.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			System.out.println("Collection exists");
		}
	}

	// borra las imagenes de la carpeta
	private void eraseImages() {
		File[] files = getImagesFromFolder(System.getProperty("user.dir") + "/src/main/resources/static/selectedImage");
		for (File file : files) {
			file.delete();
		}
	}

	// Busca las imágenes que coinciden con el indice y las escribe en la carpeta
	public void writeImages(String idx) {
		eraseImages();
		DBObject clause1 = new BasicDBObject();
		clause1.put("name", idx + "\".png\"");
		DBObject clause2 = new BasicDBObject();
		clause2.put("name", "/.*" + idx + "-.*/");
		List<DBObject> or = new ArrayList<DBObject>();
		or.add(clause1);
		or.add(clause2);
		DBObject query = new BasicDBObject("$or", or);
		
		String s = idx + ".png"; // "/.*" + idx + ".*/";
		List<GridFSDBFile> files = gfsPhoto.find(s);
		for (GridFSDBFile file : files) {
			try {
				System.out.println(file);
				OutputStream out = new FileOutputStream(new File(System.getProperty("user.dir")
						+ "/src/main/resources/static/selectedImage/" + file.getFilename()));
				file.writeTo(out);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String readImages() {
		try {
			File[] images = getImagesFromFolder(
					System.getProperty("user.dir") + "/src/main/resources/static/selectedImage");
			BufferedImage[] imagesBuffer = new BufferedImage[images.length];
			for (int i = 0; i < imagesBuffer.length; i++) {
				imagesBuffer[i] = ImageIO.read(images[i]);
			}

			ByteArrayOutputStream[] baos = new ByteArrayOutputStream[imagesBuffer.length];

			for (int i = 0; i < baos.length; i++) {
				baos[i] = new ByteArrayOutputStream();
				ImageIO.write(imagesBuffer[i], "png", baos[i]);
			}

			byte[][] byteArray = new byte[baos.length][];

			for (int i = 0; i < byteArray.length; i++) {
				byteArray[i] = baos[i].toByteArray();
			}

			// El array de bytes de la imagen hay que pasarlo a base 64 para que lo lea
			// javascript correctamente

			String json = "{";

			for (int i = 0; i < byteArray.length; i++) {
				json += "\"image" + i + "\":\"";
				json += DatatypeConverter.printBase64Binary(byteArray[i]) + "\"";
				if (i < byteArray.length - 1)
					json += ",";
			}

			json += "}";

			return json;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	// Devuelve la ruta de todas las imágenes de los pokemon
	public File[] getImagesFromFolder(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		/*
		 * String[] paths = new String[files.length]; for(int i = 0; i < files.length;
		 * i++) { paths[i] = files[i].getPath(); }
		 */
		return files;
	}

	public String query(String type1, int gen, int ord, int legendary) {
		type1 = transformarTipo(type1);

		BasicDBObject query = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		if (!type1.equals("all")) {
			obj.add(new BasicDBObject("type1", type1));
		}
		if (gen != 0) {
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

	public void delete(String id) {
		// BasicDBObject query = new BasicDBObject("_id",
		// ObjectId("563237a41a4d68582c2509da"));
		// List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		// obj.add(new BasicDBObject("id", id));
		BasicDBObject del = new BasicDBObject("_id", new ObjectId(id));
		System.out.println(del);
		System.out.println(collection.deleteOne(del));
	}

	public void closeMongoSession() {
		con.close();
	}

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
			return "fighting";
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
