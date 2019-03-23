// Hides or shows the content of the display
function bot_func(id_elem){
    document.getElementById(id_elem).classList.toggle("show");
}
// Close the dropdown if the user clicks outside of it
window.onclick = function(e) {
    if (!e.target.matches('.dropbtn')) {
        var myDropdown = document.getElementsByClassName('dropdown-content');
        for(i = 0; i < myDropdown.length; i++){
            if (myDropdown[i].classList.contains('show')) {
                myDropdown[i].classList.remove('show');
            }
        }
    }
    //Si hacemos click en un elemento de la lista, lo detecta
    //Cambiamos el texto del botón por el del elemento de la lista
    if(e.target.matches('a')){
        var texto = e.target.innerText;
        e.path[2].childNodes[1].firstChild.data = texto;
    }

}

//Cargar XML
function loadXML(path, callback){
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", path, true);
    rawFile.onreadystatechange = function () {
        if (rawFile.readyState === 4) {
            if (rawFile.status === 200 || rawFile.status == 0) {
                var xmlDoc = rawFile.responseXML;
                callback(xmlDoc);
            }
        }
    }
    rawFile.send(null);
}

//Cargar XML al HTML (se ejecuta al abrir la pokedex)
function getUname() {
    loadXML("pokedex.xml", function(xmlDoc){
        var username = xmlDoc.getElementsByTagName("usuario")[0].childNodes[0].data;
        var num_serie = xmlDoc.getElementsByTagName("num_serie")[0].childNodes[0].data;
        document.getElementById("uname").innerHTML = "Nombre de usuario: " + username;
        document.getElementById("n_serie").innerHTML = "Pokédex nº: " + num_serie;
    });
}
