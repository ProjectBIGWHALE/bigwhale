document.addEventListener("DOMContentLoaded", function () {
    const h1Element = document.querySelector("h1");
    const passwordElement = document.querySelector("label[for='pwd']");
    const arquivoElement = document.querySelector("#arquivo");
    const criptografarRadio = document.getElementById("criptografar");
    const pwd = document.getElementById("pwd");
    const chk = document.getElementById("chk");
    const showPasswordIcon = document.querySelector('.show-password');
    const hidePasswordIcon = document.querySelector('.hide-password');

    // Set initial visibility state
    pwd.type = 'password';
    hidePasswordIcon.style.display = 'none';

    showPasswordIcon.addEventListener('click', () => {
        pwd.type = 'text';
        showPasswordIcon.style.display = 'none';
        hidePasswordIcon.style.display = 'inline-block';
    });

    hidePasswordIcon.addEventListener('click', () => {
        pwd.type = 'password';
        hidePasswordIcon.style.display = 'none';
        showPasswordIcon.style.display = 'inline-block';
    });

    criptografarRadio.addEventListener("change", function () {
        if (this.checked) {
            h1Element.textContent = "Criptografando Arquivos";
            arquivoElement.textContent = "Escolha um arquivo compactado:";
            passwordElement.textContent = "Escolha a senha: ";
        }
    });

    const descriptografarRadio = document.getElementById("descriptografar");

    descriptografarRadio.addEventListener("change", function () {
        if (this.checked) {
            h1Element.textContent = "Descriptografando Arquivos";
            arquivoElement.textContent = "Escolha um arquivo criptografado:";
            passwordElement.textContent = "Digite a senha: ";
        }
    });

    //file-name é um span para adicionar o nome do arquivo
    const fileName = document.getElementById('file-name');
    
    //Função para exibir o nome do arquivo selecionado
    arquivoElement.addEventListener("change", () =>{
        const name = arquivoElement.files[0].name;
        return fileName.innerText = `Arquivo: ${name}`;
    })
});
