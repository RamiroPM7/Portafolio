document.addEventListener('DOMContentLoaded', () => {
    // Aquí puedes escribir código JavaScript para agregar interactividad a tu página.
    // Por ejemplo, para un formulario de contacto, podrías agregar un 'event listener'
    // al botón de enviar.
    const form = document.querySelector('form');
    if (form) {
        form.addEventListener('submit', (e) => {
            e.preventDefault(); // Evita que la página se recargue
            alert('Formulario enviado (esta es una simulación). ¡Gracias por contactarme!');
            // Aquí iría el código para enviar el formulario a un servicio de backend,
            // como Formspree, sin necesidad de programar un servidor.
        });
    }

    console.log("¡Tu portafolio está listo para ser personalizado!");
});