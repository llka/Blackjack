/* Toggle between adding and removing the "responsive" class to header when the user clicks on the icon */
function responsiveNav() {
    var x = document.getElementById("topnav");
    if (x.className === "header") {
        x.className += " responsive";
    } else {
        x.className = "header";
    }
}

function refreshProtection(event) {
     if (event.key === "F5"
        || event.keyCode === 116) {
         event.preventDefault();
         event.stopPropagation();
    }
}
window.addEventListener("keydown", refreshProtection);