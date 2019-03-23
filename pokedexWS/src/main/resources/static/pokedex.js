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
