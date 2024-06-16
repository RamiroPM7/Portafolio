document.addEventListener("DOMContentLoaded", function() {
  var toggleButton = document.getElementById("toggleButton");
  var passwordField = document.getElementById("contrasena");
  var eyeIcon = document.getElementById("eyeIcon");

  var inputEditar = document.getElementById("inputEditar");
  var botonEditar = document.getElementById("botonEditar");

  toggleButton.addEventListener("click", function() {
    if (passwordField.type === "password") {
      passwordField.type = "text";
      eyeIcon.src = "../assets/img/hidden.png"; // Cambia el icono a uno que indique "ocultar"
    } else {
      passwordField.type = "password";
      eyeIcon.src = "../assets/img/eye.png"; // Cambia el icono a uno que indique "mostrar"
    }
  });


  botonEditar.addEventListener("click", function() {
    inputEditar.removeAttribute("readonly");
    inputEditar.focus(); // Poner el foco en el input después de hacer clic en el botón
  });


  inputEditar.addEventListener("blur", function() {
    inputEditar.setAttribute("readonly", true);
  });

  botonEditar2.addEventListener("click", function() {
    inputEditar2.removeAttribute("readonly");
    inputEditar2.focus(); // Poner el foco en el input después de hacer clic en el botón
  });


  inputEditar2.addEventListener("blur", function() {
    inputEditar2.setAttribute("readonly", true);
  });

  botonEditar3.addEventListener("click", function() {
    contrasena.removeAttribute("readonly");
    contrasena.focus(); // Poner el foco en el input después de hacer clic en el botón
  });


  contrasena.addEventListener("blur", function() {
    contrasena.setAttribute("readonly", true);
  });


});
