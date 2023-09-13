const form = document.getElementById('uploadForm');
const imageInput = document.getElementById('image');
const previewImage = document.getElementById('preview');
const uploadedImage = document.getElementById('uploadedImage');
const imageBase64 = uploadedImage.getAttribute('data-image-base64');

form.addEventListener('dragover', (event) => {
    event.preventDefault();
    form.classList.add('dragover');
});

form.addEventListener('dragleave', (event) => {
    event.preventDefault();
    form.classList.remove('dragover');
});

form.addEventListener('drop', (event) => {
    event.preventDefault();
    form.classList.remove('dragover');

    const droppedFiles = event.dataTransfer.files;
    if (droppedFiles.length > 0) {
        imageInput.files = droppedFiles;
        displayPreviewImage(droppedFiles[0]);
        submitForm();
    }
});

function submitForm() {
    //Oculta o botão de submit
    document.querySelector(".form-action button").style.display = "none";

    //Mostra o loading
    document.querySelector("#loadingOverlay").style.display = "block";

    //Envia o formulário
    form.submit();
}

function displayPreviewImage(imageFile) {
    const reader = new FileReader();

    reader.onload = (e) => {
        previewImage.src = e.target.result;
        if (imageBase64) {
            var img = document.createElement('img');
            img.src = 'data:image/png;base64,' + imageBase64;
            img.style.maxWidth = '500px';
            img.style.width = '100%';
            uploadedImage.parentNode.replaceChild(img, uploadedImage);
        }
    };

    reader.readAsDataURL(imageFile);
}