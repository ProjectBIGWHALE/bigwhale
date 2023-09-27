// "Limits image upload to a maximum of 50MB."
document.addEventListener("DOMContentLoaded", function () {
    const fileInput = document.getElementById("image");
    fileInput.addEventListener("change", function (event) {
        const selectedFile = event.target.files[0];
        const maxSizeInBytes = 50 * 1024 * 1024;
        if (selectedFile.size > maxSizeInBytes) {
            alert("Warning! The file exceeds the maximum allowed size of 50MB.");
            fileInput.value = "";
        }
    });
});

// "Limits image upload to BMP, JPG, JPEG, or GIF formats."
document.addEventListener("DOMContentLoaded", function () {
    const fileInput = document.getElementById("image");
    const formatErrorDiv = document.getElementById("formatError");
    fileInput.addEventListener("change", function (event) {
        const selectedFile = event.target.files[0];
        const allowedFormats = [".bmp", ".jpg", ".jpeg", ".gif"];

        const fileExtension = selectedFile.name.slice(selectedFile.name.lastIndexOf(".")).toLowerCase();
        if (!allowedFormats.includes(fileExtension)) {
            alert("Unsupported file format. Please choose a BMP, JPG, JPEG or GIF file.");
            fileInput.value = "";
        }
    });
});

// Alert to choose to submit the form only after uploading the image
document.addEventListener("DOMContentLoaded", function () {
    const convertButton = document.getElementById("convertButton");

    convertButton.addEventListener("click", function () {
        const imageInput = document.getElementById("image");
        if (imageInput.files.length === 0) {
            alert("You need to choose an image first.");
        } else {
            const imageForm = document.getElementById("imageForm");
            imageForm.submit();
        }
    });
});


//Loading documents
document.getElementById('convertButton').addEventListener('click', function() {
    // Mostra o loading spinner
    document.querySelector('.loadingio-spinner-dual-ball-dbuc2dc03wn').style.display = 'block';

    // Desabilita o botão de download
    this.disabled = true;

    // Submete o formulário
    document.getElementById('imageForm').submit();
});

// Simule o processamento completo aqui
function processamentoCompleto() {
    // Esconde o loading spinner
    document.querySelector('.loadingio-spinner-dual-ball-dbuc2dc03wn').style.display = 'none';

    // Habilita o botão de download
    document.getElementById('convertButton').disabled = false;
}

document.getElementById('convertButton').addEventListener('click', function() {
    // Mostra a overlay de loading
    document.getElementById('loadingOverlay').style.display = 'flex';

    // Desabilita o botão de download
    this.disabled = true;

    // Simule o processamento completo
    setTimeout(processamentoCompleto, 3000);
});

function processamentoCompleto() {
    // Esconde a overlay de loading
    document.getElementById('loadingOverlay').style.display = 'none';

    // Habilita o botão de download
    document.getElementById('convertButton').disabled = false;
}
