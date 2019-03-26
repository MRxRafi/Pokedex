//criterios de busqueda
var type = "fire"
var gen = 1
var ord = 1
var leg = 0

//Aqui guardamos los pokemon resultantes
var result;

// Hides or shows the content of the display
function bot_func(id_elem) {
	document.getElementById(id_elem).classList.toggle("show");
}
// Close the dropdown if the user clicks outside of it
window.onclick = function(e) {
	if (!e.target.matches('.dropbtn')) {
		var myDropdown = document.getElementsByClassName('dropdown-content');
		for (i = 0; i < myDropdown.length; i++) {
			if (myDropdown[i].classList.contains('show')) {
				myDropdown[i].classList.remove('show');
			}
		}
	}
	// Si hacemos click en un elemento de la lista, lo detecta
	// Cambiamos el texto del botón por el del elemento de la lista
	if (e.target.matches('a')) {
		var texto = e.target.innerText;
		e.path[2].childNodes[1].firstChild.data = texto;
		console.log(texto)
		//console.log(e.target)
		updateCriterios(e.target.parentNode, texto)
	}

}

// Search Button

function search() {
	queryWS(type, gen, ord, leg);
}

function updateCriterios(e, texto) {
	console.log(e.id)
	switch (e.id) {
	case "tipodrop":
		type = texto
		break
	case "ordenardrop":
		switch (texto) {
		case "Ascendente":
			ord = 1
			break
		case "Descendente":
			ord = -1
			break
		case "Ninguno":
			ord = 0
		}
		break
	case "generaciondrop":
		switch(texto){
		case "Todas":
			gen = 0;
			break;
		case "1ª generación":
			gen = 1
			break
		case "2ª generación":
			gen = 2
			break
		case "3ª generación":
			gen = 3
			break;
		case "4ª generación":
			gen =4
			break
		case "5ª generación":
			gen = 5
			break
		case "6ª generación":
			gen = 6
			break
		}
	}
	console.log(type + " " + gen + " " + ord)
}

function displayResult() {
	resultDiv = document.getElementById("result");
	while (resultDiv.firstChild) {
		resultDiv.removeChild(resultDiv.firstChild);
	}
	result.forEach(function(element, index) {
		var node = document.createElement("LI")     
		var textnode = document.createTextNode(element.name)
		node.appendChild(textnode)
		node.id = index
		node.onclick = function(){showDetails(this.id)}
		document.getElementById("result").appendChild(node)
	})
	 
}
function showDetails(idx){
	descriptionDiv = document.getElementById("description")
	
	while (descriptionDiv.firstChild) {
		descriptionDiv.removeChild(descriptionDiv.firstChild)
	}
	//var textnode = document.createTextNode(result[idx].name)
	//descriptionDiv.appendChild(textnode)
	
	Object.getOwnPropertyNames(result[idx]).forEach(function(key){
		var textnode = document.createTextNode(key + ": " + result[idx][key])
		var brnode = document.createElement("BR");
		descriptionDiv.appendChild(textnode)
		descriptionDiv.appendChild(brnode)
	})
	
}
