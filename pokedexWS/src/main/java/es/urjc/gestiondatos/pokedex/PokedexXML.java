package es.urjc.gestiondatos.pokedex;

public class PokedexXML {
	String num_serie;
	String usuario;
	String color_cir;
	
	public PokedexXML() {}
	
	public PokedexXML(String num_serie, String usuario, String color_cir) {
		this.num_serie = num_serie;
		this.usuario = usuario;
		this.color_cir = color_cir;
	}

	public String getNum_serie() {
		return num_serie;
	}

	public void setNum_serie(String num_serie) {
		this.num_serie = num_serie;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getColor_cir() {
		return color_cir;
	}

	public void setColor_cir(String color_cir) {
		this.color_cir = color_cir;
	}

	@Override
	public String toString() {
		return "{\"num_serie\":\""+this.num_serie+"\",\"usuario\":\""+this.usuario+"\",\"color_cir\":\""+this.color_cir+"\"}";
	}
	
}

