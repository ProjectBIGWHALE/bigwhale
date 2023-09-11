document.addEventListener("DOMContentLoaded", function () {
  const h1Element = document.querySelector("h1");
  const passwordElement = document.querySelector("label[for='pwd']");
  const arquivoElement = document.querySelector("#arquivo");
  const criptografarRadio = document.getElementById("criptografar");
  const descriptografarRadio = document.getElementById("descriptografar");

  const pwd = document.getElementById("pwd");

  const btnToggleIcon = document.getElementById("btn-toggle-icon");
  const showPasswordIcon = document.querySelector(".show-password");
  const hidePasswordIcon = document.querySelector(".hide-password");

  const btnDownload = document.querySelector("#download");

  function isValid() {
    return (criptografarRadio.checked || descriptografarRadio.checked) &&
    arquivoElement.files.length > 0 && pwd.value.length > 0
  }

  //função para habilitar o button download
  function enabledBtnDownload() {
    return (isValid()) 
    ? btnDownload.classList.remove("disabledBtnDownload")
    : btnDownload.classList.add("disabledBtnDownload")  ;
      
  }
  
  // ler os eventos de entrada do input password
  pwd.addEventListener('input', ()=>{
    validationField( pwd.value.length > 0, 'pwd-error');
    enabledBtnDownload()
  })

  btnToggleIcon.addEventListener("click", () => {
    pwd.type = (pwd.type === "text") ? "password" : "text";
    showPasswordIcon.classList.toggle('disabledCheckboxIcon');
    hidePasswordIcon.classList.toggle('disabledCheckboxIcon');
  });


  criptografarRadio.addEventListener("change", function () {
    enabledBtnDownload();
    if (this.checked) {
      h1Element.textContent = "Criptografando Arquivos";
      arquivoElement.textContent = "Escolha um arquivo compactado:";
      passwordElement.textContent = "Escolha a senha: ";
    }
    validationField( criptografarRadio.checked, 'radio-error');
  });

  descriptografarRadio.addEventListener("change", function () {
    enabledBtnDownload();
    if (this.checked) {
      h1Element.textContent = "Descriptografando Arquivos";
      arquivoElement.textContent = "Escolha um arquivo criptografado:";
      passwordElement.textContent = "Digite a senha: ";
    }
    validationField(descriptografarRadio.checked, 'radio-error');
  });

  //file-name é um span para adicionar o nome do arquivo
  const fileName = document.getElementById("file-name");

  //evento para exibir os errors caso o botão download é clicado
  btnDownload.addEventListener('click', (e) => {
    if(!isValid()) {
      e.preventDefault();
      validationField(arquivoElement.files.length, 'file-error');
      validationField( pwd.value.length > 0, 'pwd-error');
      validationField( criptografarRadio.checked || descriptografarRadio.checked, 'radio-error');
    };
  })

  //Evento para exibir o nome do arquivo selecionado
  arquivoElement.addEventListener("change", () => {
    const name = arquivoElement.files[0].name;
    validationField(arquivoElement.files.length, 'file-error');
    enabledBtnDownload();
    return (fileName.innerText = `Arquivo: ${name}`);
  });
});

//função para exibir a mensagem de error
function showMsgError( spanError ){
  spanError.style.visibility = 'visible';
}

//função para ocultar a mensagem de erro
function hideMsgError( spanError){
  spanError.style.visibility = 'hidden';
}

// função para validar o campo e exibir a mensagem se houver error
function validationField(validation, spanId){
  const error = document.getElementById(spanId);
  return (!validation) ? showMsgError(error) :  hideMsgError(error) ;
  

}
