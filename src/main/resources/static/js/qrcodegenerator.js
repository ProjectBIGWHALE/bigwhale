document.addEventListener("DOMContentLoaded", () => {
  const urlLink = document.querySelector("#link");
  const color = document.querySelector("#get-color");
  const colorSelect = document.querySelector("#color-select");
  const btnDownload = document.querySelector("#download");
  const ulTypes = document.querySelector("#ul-types");
  const types = document.querySelectorAll("input[name='types']");
  const formGroupLink = document.querySelector("#form-group-link");
  const formGroupEmail = document.querySelector("#form-group-email");
  const formGroupWapp = document.querySelector("#form-group-wapp");

  ulTypes.addEventListener("change", () => {
    validationField(isChecked(types), "radio-required");
    enabledBtnDownload();
  });

  color.addEventListener("change", () => {
    onChangeTextColor(color.value, color);
  });
  color.addEventListener("blur", () => {
    validationField(color.value.length, "color-required");
    validationField(!validationColor(color.value, color), 'color-error');
    enabledBtnDownload();
  });

  urlLink.addEventListener("blur", (e) => {
    const urlValue = e.target.value;
    validationField(urlValue.length > 0, "link-required");
    enabledBtnDownload();
  });

  colorSelect.addEventListener("change", (e) => {
    const colorValue = e.target.value;
    color.value = colorValue;
    onChangeTextColor(colorValue, color);
    validationField(colorValue.length > 0, "color-error");
    enabledBtnDownload();
  });

  //função para verficar se os inputs estão vazios
  function isValid() {
    return (
      isChecked(types) && urlLink.value.length > 0 && color.value.length > 0
    );
  }

  //função para habilitar o button download
  function enabledBtnDownload() {
    return isValid()
      ? btnDownload.classList.remove("disabledBtnDownload")
      : btnDownload.classList.add("disabledBtnDownload");
  }

  // btnDownload.addEventListener("click", (e) => {
  //   if (!isValid()) {
  //     e.preventDefault();
  //     validationField(isChecked(types), "radio-required");
  //     validationField(urlLink.value.length > 0, "link-required");
  //     validationField(color.value.length > 0, "color-required");
  //   }
  // });
  
  //Função para adicionar cor ao texto de input Cor
  function onChangeTextColor(colorValue, inputText) {  
    inputText.style.color = colorValue
    const isWhite = (colorValue === "#ffffff" || colorValue === "#fff" || colorValue.toLowerCase() === 'white') ;
    (isWhite) ? inputText.style.backgroundColor = "#000000" : inputText.style.backgroundColor = "" ;
  }

  //função para exibir a mensagem de error
  function showMsgError(spanError) {
    spanError.style.display = "block";
  }
  
  //função para ocultar a mensagem de erro
  function hideMsgError(spanError) {
    spanError.style.display = "none";
  }
  
  // função para validar o campo e exibir a mensagem se houver error
  function validationField(validation, spanId) {
    const error = document.getElementById(spanId);
    return !validation ? showMsgError(error) : hideMsgError(error);
  }
  
  //função para validar cor
  function validationColor(colorValue, inputText){
    inputText.style.color = colorValue;
    if(inputText.style.color){
      return !inputText.style.color;
    }
    else return true;
  }
  
  //função para verificar se algum chekebox foi marcado
  function isChecked(types) {
    const getRadio = document.querySelector("#getRadio");
    const listTitle = document.querySelector("#list-title");
    for (const type of types) {
      if (type.checked) {
        switch(type.id){
          case "link-checked":
            getRadio.value = type.value;
            listTitle.innerText = 'Insira a url'
            enableInput(formGroupLink);
            disableInput(formGroupEmail);
            disableInput(formGroupWapp);
            break;
          case "email-checked":
            getRadio.value = type.value;
            listTitle.innerText = 'Enviar Email'
            enableInput(formGroupEmail);
            disableInput(formGroupLink);
            disableInput(formGroupWapp);
            break;
          case "wapp-cheked":
            getRadio.value = type.value;           
            listTitle.innerText = 'WhatsApp'
            enableInput(formGroupWapp);
            disableInput(formGroupLink);
            disableInput(formGroupEmail);
            break;
        }

        return true;
      }
    }
    return false;
  }

  //função para esconder o tipo não selecionado
  function disableInput( inputId){
    inputId.classList.add("disable__input-choice")
    inputId.classList.remove("enable__input-choice")
  }
  //função para exibir o tipo selecionado
  function enableInput( inputId ){
    inputId.classList.remove("disable__input-choice")
    inputId.classList.add("enable__input-choice")
  }

  //Requisições a API de para pegar as badeiras e os códigos de ligação
  async function getCodeIso(cod) {
    const url = 'https://restcountries.com/v3.1/independent?all&fields=idd,cca2';
    fetch(url)
      .then((res) => res.json())
      .then((data) => {
        for (let country of data) {
          const { idd, cca2 } = country;
          // addOptionsFlagCode(cca2, `${idd.root}${idd.suffixes[0]}`) // essa função é a que vai gerar as flags
          if (cod === `${idd.root}${idd.suffixes}`) {
            if(idd.root+idd.suffixes === '+1')  return getFlagByCodeIsoCode('US', `${idd.root}${idd.suffixes[0]}`);
            return getFlagByCodeIsoCode(cca2, `${idd.root}${idd.suffixes[0]}`);
          }
        }
      })
      .catch((e) => console.error(`Error ao fazer a requisição ${e}`));
  }

  const wapp = document.querySelector("#wapp");
  if(!wapp.value) getCodeIso('+55');

  //função para adicionar o código do país
  wapp.addEventListener('input', (e) => {
    const value = e.target.value.replace(/^([^+])/, '+$1');
    getCodeIso(value);
  });
  
  //função para selecionar a bandeira e seu código a partir da sigla do plaís
  async function getFlagByCodeIsoCode(isoCode, code){
    const countryFlag = document.querySelector("#country-flag");
    countryFlag.src = `https://flagsapi.com/${isoCode}/flat/64.png`;
    countryFlag.alt = `${isoCode}`
    countryFlag.title = `${isoCode}`
    wapp.value = code
  }


  // OBS: ainda falta implementra essa função para gerar a lista de flags

  // const selectFlagCode = document.querySelector("#select-flag-code");
  // const listFlagCode = document.querySelector('#list-flag-code');
  // //função para popular o select
  // selectFlagCode.addEventListener('click', ()=>{
  //   listFlagCode.classList.toggle('disabled-select-flag-code')
  // })
  // function addOptionsFlagCode(flag, code){
  //     listFlagCode.innerHTML += `
  //       <li data-value=${code}>
  //         <img src="https://flagsapi.com/${flag}/flat/64.png" alt="${code}" class="country__flag" />
  //         ${code}
  //       </li>  
  //     `
  // }

});