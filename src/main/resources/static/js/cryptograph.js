document.addEventListener("DOMContentLoaded", function () {
    const h1Element = document.querySelector("h1");
    const arquivoElement = document.querySelector("label[for='arquivo']");
    const passwordElement = document.querySelector("label[for='senha']");
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
});
