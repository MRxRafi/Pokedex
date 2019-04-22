//criterios de busqueda
var type = "fire"
var gen = 1
var ord = 1
var leg = 0

// Aqui guardamos los pokemon resultantes
var result;

// Pokemon seleccionado actualmente (undefined si no hay ninguno)
var selected;

//Imagen/es del pokemon seleccionado
var images = [];
//Indice de la imagen que se esta mostrando(si que hay alguna)
var imageIndex = 0;

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
		// console.log(e.target)
		updateCriterios(e.target.parentNode, texto)
	}

	if (e.target.name === "legendario") {
		if (e.target.checked === true) {
			leg = 1;
		} else {
			leg = 0;
		}
	}

}

// Search Button

function search() {
	queryWS(type, gen, ord, leg);
}

function deleteSelected() {
	if (selected != undefined) {
		deleteWS(selected._id.$oid)
		var i = result.indexOf(selected)
		result.splice(i, 1)
		displayResult()
		selected = undefined;
	}
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
		switch (texto) {
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
			gen = 4
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
		node.onclick = function() {
			showDetails(this.id)
		}
		document.getElementById("result").appendChild(node)
	})

}
function showDetails(idx) {

	selected = result[idx]

	descriptionDiv = document.getElementById("description")

	while (descriptionDiv.firstChild) {
		descriptionDiv.removeChild(descriptionDiv.firstChild)
	}
	// var textnode = document.createTextNode(result[idx].name)
	// descriptionDiv.appendChild(textnode)

	Object.getOwnPropertyNames(result[idx]).forEach(function(key) {
		var textnode;
		/*
		 * if (key == "_id") { var idObj = JSON.parse(result[idx][key]) textnode =
		 * document.createTextNode(key +": " + idObj.$oid)
		 * console.log(result[idx][key]) } else {
		 */
		textnode = document.createTextNode(key + ": " + result[idx][key])
		// }
		var brnode = document.createElement("BR");
		descriptionDiv.appendChild(textnode)
		descriptionDiv.appendChild(brnode)
	})
	
	//search photo
	queryPhotosWS(result[idx].pokedex_number)
}

function nextImage(){
	if(imageIndex == images.length-1){
		imageIndex = 0
	}else{
		imageIndex += 1
	}
	document.getElementById("ItemPreview").src = "data:image/png;base64," + images[imageIndex];
	
}
