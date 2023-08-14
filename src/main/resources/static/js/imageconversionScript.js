// Limita no input o upload de imagem a no máximo 50MB
document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("image").addEventListener("change", function (event) {
      const fileInput = event.target;
      const maxSizeInBytes = 50 * 1024 * 1024; // 50MB em bytes
      if (fileInput.files[0].size > maxSizeInBytes) {
        alert("O arquivo excede o tamanho máximo permitido de 50MB.");
        fileInput.value = ""; // Limpa o valor do input para evitar o envio do arquivo
      }
    });
  });
