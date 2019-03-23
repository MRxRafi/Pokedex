package es.urjc.gestiondatos.pokedex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

//import es.urjc.ramstudios.dlabyrinth.WebsocketGameHandler;

@SpringBootApplication
@EnableWebSocket


public class App implements WebSocketConfigurer {
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(pokedexHandler(), "/pokedex").setAllowedOrigins("*");
	}

	@Bean
	public WebsocketPokedexHandler pokedexHandler() {
		return new WebsocketPokedexHandler();
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
