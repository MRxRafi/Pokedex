var connection;

function openWSConnection(){
	connection = new WebSocket('ws://' + window.location.host + '/pokedex');
	connection.data = { pokedexXML: undefined };
	
	connection.onopen = function (event) {
        console.log('[DEBUG-WS] Se ha establecido conexion con el servidor.');
	}
	connection.onerror = function (error) {
	    console.log('[DEBUG-WS] Ha ocurrido un error: ' + error)
	}
	
	// Un monitor de eventos que atiende una llamada cuando la conexión del
	// WebSocket cambia a un estado CERRADO (CLOSED). El monitor recibe un
	// CloseEvent llamado "cerrado".
	connection.onclose = function (event) {
	        console.log('[DEBUG-WS] Se ha cerrado la conexion.')
	}
	connection.onmessage = function (message) {
	    //console.log('[DEBUG-WS] Se ha recibido un mensaje: ' + message.data);
	    var msg = JSON.parse(message.data);

	    console.log('INFO RECIBIDA ' + msg.type);

	    switch (msg.type) {
	    	case "CONEX_ESTAB":
	    		connection.data.pokedexXML = msg.pokedexXML;
	    		document.getElementById("uname").innerHTML = "Nombre de usuario: " + msg.pokedexXML.usuario;
	            document.getElementById("n_serie").innerHTML = "Pokedex numero: " + msg.pokedexXML.num_serie;
	    	case "CONEX_CERR":
	        	//Cerrar Conexión
	        	connection.close();
	    }
	}
	waitForSocketConnection(connection, createPokedexWS);
	
}

function createPokedexWS(){
	connection.data.type = 'JOIN'
	connection.send(JSON.stringify(connection.data));
}

function waitForSocketConnection(socket, callback){
    setTimeout(
        function () {
            if (socket.readyState === 1) {
                console.log("Connection is made")
                if(callback != null){
                    callback();
                }
                return;

            } else {
                console.log("wait for connection...")
                waitForSocketConnection(socket, callback);
            }

        }, 20); // wait 5 milisecond for the connection...
}
/*
function updateStateWS(){
	connection.data.type = 'UPDATE';
	connection.data.actualPlayer = DLabyrinth.player;
	connection.data.sendItems = sendItems;
	connection.data.bala = DLabyrinth.bala;
	connection.send(JSON.stringify(connection.data));
	
	
}

function closeConnection(){
	connection.data.type = "ENDING";
	connection.data.actualPlayer = DLabyrinth.player;
	connection.send(JSON.stringify(connection.data));
}*/