// Obtém o valor do campo oculto que armazena a imagem base64
var imageBase64 = document.getElementById("imageBase64").value;
// Define a imagem base64 no elemento de visualização da imagem
var viewImage = document.getElementById("viewImage");
if (imageBase64) {
    viewImage.src = "data:image/png;base64," + imageBase64;
}

function copyHexColor(element) {
    var bgColor = element.style.backgroundColor;
    var hexColor = rgbToHex(bgColor);
    navigator.clipboard.writeText(hexColor);
    showNotification();
}

function rgbToHex(rgb) {
    var parts = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
    delete parts[0];
    for (var i = 1; i <= 3; ++i) {
        parts[i] = parseInt(parts[i]).toString(16);
        if (parts[i].length == 1) parts[i] = '0' + parts[i];
    }
    return '#' + parts.join('');
}

function showNotification() {
    var notification = document.getElementById('notification');
    notification.style.display = 'block';
    setTimeout(function () {
        notification.style.display = 'none';
    }, 2000); // Tempo em milissegundos (2 segundos)
}