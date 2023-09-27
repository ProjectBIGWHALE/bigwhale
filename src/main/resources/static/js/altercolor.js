//      FUNÇÃO PARA INCLUIR A IMAGEM NA TELA
function previewImage(event) {
    var input = event.target;
    var preview = document.getElementById('preview');

    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            preview.innerHTML = '<img src="' + e.target.result + '" alt="Pré-visualização da imagem">';
            enableColorPicker();
        };
        reader.readAsDataURL(input.files[0]);
    }
}


//       FUNÇÃO QUE ATIVA O SELETOR DE CORES
function enableColorPicker() {
    var imagePreview = document.querySelector('#preview img');
    imagePreview.addEventListener('click', function(event) {
        pickColor(event, 'dropper');
    });
}

//      FUNÇÃO QUE PEGA A COR CLICADA NO CIRCULO
function pickColorCircle() {
    var circleColor = document.querySelector('.img_cromatico');
    circleColor.addEventListener('click', function(event) {
        pickColor(event, 'alter_color');
    });
}

//      FUNÇÃO PARA PEGAR A COR NO CLICK DA IMAGEM
function pickColor(event, inputId) {
    var image = event.target;
    var canvas = document.createElement('canvas');
    var context = canvas.getContext('2d');
    
    canvas.width = image.width;
    canvas.height = image.height;
    context.drawImage(image, 0, 0, image.width, image.height);

    var rect = image.getBoundingClientRect();
    var x = event.clientX - rect.left;
    var y = event.clientY - rect.top;

    var pixelData = context.getImageData(x, y, 1, 1).data;
    var color = rgbToHex(pixelData[0], pixelData[1], pixelData[2]);

    var colorInput = document.getElementById(inputId);
    if (colorInput) {
        colorInput.value = color;
        
    }
}


//      FUNÇÃO PRA RETORNAR O CLICK DO INPUT EM RGB
function rgbToHex(r, g, b) {
    return '#' + ((r << 16) | (g << 8) | b).toString(16).padStart(6, '0');
}


//      MOSTRA/ESCONDE O CIRCULO CROMATICO DE CORES
function imageCircleColor() {
    var imgCromatico = document.querySelector('.img_cromatico');
    if (imgCromatico.style.display === 'none') {
        imgCromatico.style.display = 'block';
    } else {
        imgCromatico.style.display = 'none';
    }
}

function pickColorWheel(event) {
    var colorPicker = document.querySelector('.color-picker2');
    var colorWheel = document.querySelector('.color-wheel');
    var corValor = document.getElementById('cor-valor2');
    var dropper = document.getElementById('dropper');

    var rect = colorWheel.getBoundingClientRect();
    var x = event.clientX - rect.left;
    var y = event.clientY - rect.top;
    var centerX = rect.width / 2;
    var centerY = rect.height / 2;
    var angle = Math.atan2(y - centerY, x - centerX);
    var degrees = angle * (180 / Math.PI);
    var hue = (degrees + 360) % 360;

    var color = hslToHex(hue, 1, 0.5);
    corValor.textContent = color;
    dropper.value = color;

    colorPicker.style.left = x + 'px';
    colorPicker.style.top = y + 'px';
}

function hslToHex(h, s, l) {
    h /= 360;
    var r, g, b;
    if (s === 0) {
        r = g = b = l;
    } else {
        function hue2rgb(p, q, t) {
            if (t < 0) t += 1;
            if (t > 1) t -= 1;
            if (t < 1 / 6) return p + (q - p) * 6 * t;
            if (t < 1 / 2) return q;
            if (t < 2 / 3) return p + (q - p) * (2 / 3 - t) * 6;
            return p;
        }

        var q = l < 0.5 ? l * (1 + s) : l + s - l * s;
        var p = 2 * l - q;

        r = Math.round(hue2rgb(p, q, h + 1 / 3) * 255);
        g = Math.round(hue2rgb(p, q, h) * 255);
        b = Math.round(hue2rgb(p, q, h - 1 / 3) * 255);
    }

    return '#' + ((r << 16) | (g << 8) | b).toString(16).padStart(6, '0');
}

// FUNÇÃO PRA APAGAR OS INPUTS
function btnClear() {
    var dropper = document.getElementById('dropper');
    var alterColorInput = document.getElementById('alter_color');

    dropper.value = '';
    alterColorInput.value = '';
}

// function downloadAlteredImage() {
//     var alteredImage = document.querySelector('#altered-image img');
//     var canvas = document.createElement('canvas');
//     var context = canvas.getContext('2d');

//     canvas.width = alteredImage.width;
//     canvas.height = alteredImage.height;
//     context.drawImage(alteredImage, 0, 0, alteredImage.width, alteredImage.height);

//     // Converter o canvas para um blob de dados
//     canvas.toBlob(function (blob) {
//         // Criar um URL temporário para o blob
//         var url = URL.createObjectURL(blob);

//         // Criar um elemento de link para fazer o download
//         var a = document.createElement('a');
//         a.href = url;
//         a.download = 'imagem_alterada.png'; // Nome do arquivo de download
//         a.style.display = 'none';

//         // Anexar o elemento de link ao corpo do documento e disparar o clique
//         document.body.appendChild(a);
//         a.click();

//         // Remover o elemento de link e liberar o URL
//         document.body.removeChild(a);
//         URL.revokeObjectURL(url);
//     });
// }