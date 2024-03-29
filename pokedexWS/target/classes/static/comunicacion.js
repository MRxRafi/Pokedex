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
	    		document.getElementById("uname").innerHTML = "Nombre de usuario: " + msg.pokedexXML.usuario;
	            document.getElementById("n_serie").innerHTML = "Pokedex numero: " + msg.pokedexXML.num_serie;
	            break;
	    	case "RESULT":
	    		//recibimos los pokemon en forma de json (String)
	    		pokemonJSON = msg.result;
	    		result = JSON.parse(pokemonJSON);
	    		console.log(result)
	    		displayResult();
	    		break;
	    	case "PHOTO_RESULT":
	    		var data = JSON.parse(msg.data)
	    		console.log(data)
	    		images = [];
	    		imageIndex = 0;
	    		for(img in data){
	    			//console.log(data[img])
	    			images.push(data[img])
	    		}
	    		document.getElementById("ItemPreview").src = "data:image/png;base64," + images[imageIndex];
	    		//console.log(data.image0)
	    		break;
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

 //Adri
function queryWS(type1, gen, ord, leg){
	connection.data.type = "QUERY"
	connection.data.type1 = type1
	connection.data.gen = gen
	connection.data.ord =ord
	connection.data.leg = leg
	connection.send(JSON.stringify(connection.data));
}

function queryPhotosWS(idx){
	connection.data.type = "PHOTO";
	connection.data.idx = idx;
	connection.send(JSON.stringify(connection.data));
}

function deleteWS(id){
	connection.data.type = "DELETE"
	connection.data.id = id
	console.log(JSON.stringify(connection.data))
	connection.send(JSON.stringify(connection.data))
}

function closeConnection(){
	connection.data.type = "ENDING";
	connection.data.actualPlayer = DLabyrinth.player;
	connection.send(JSON.stringify(connection.data));
}