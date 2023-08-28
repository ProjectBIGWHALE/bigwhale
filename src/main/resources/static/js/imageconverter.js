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