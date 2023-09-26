document.addEventListener("DOMContentLoaded", () => {
  //campo de rádios
  const ulTypes = document.querySelector("#ul-types");
  const types = document.querySelectorAll("input[type='radio']");
  const dataType = document.querySelector("#dataType");

  // campo de cores
  const color = document.querySelector("#get-color");
  const colorSelect = document.querySelector("#color-select");

  // campo de inputs para exibição
  const formGroupLink = document.querySelector("#form-group-link");
  const formGroupEmail = document.querySelector("#form-group-email");
  const formGroupWapp = document.querySelector("#form-group-wapp");
  const listTitle = document.querySelector("#list-title");
  
  ulTypes.addEventListener("change", () => {
    isChecked(types)
  });

  color.addEventListener("input", () => {
    onChangeTextColor(color.value, color);
  });

  colorSelect.addEventListener("change", (e) => {
    const colorValue = e.target.value;
    color.value = colorValue;
    onChangeTextColor(colorValue, color);
  });
  
  //Função para adicionar cor ao texto de input Cor
  function onChangeTextColor(colorValue, inputText) {  
    inputText.style.color = colorValue
    const isWhite = (colorValue === "#ffffff" || colorValue === "#fff" || colorValue.toLowerCase() === 'white') ;
    (isWhite) ? inputText.style.backgroundColor = "#000000" : inputText.style.backgroundColor = "" ;
  }

  //função para verificar se algum chekebox foi marcado
  function isChecked(types) {
    for (const type of types) {
      if (type.checked) {
        switch(type.id){
          case "link-checked":
            dataType.value = type.value;
            listTitle.innerText = 'Insira a url'
            enableInput(formGroupLink);
            disableInput(formGroupEmail);
            disableInput(formGroupWapp);
            break;
          case "email-checked":
            dataType.value = type.value;
            listTitle.innerText = 'Enviar Email'
            enableInput(formGroupEmail);
            disableInput(formGroupLink);
            disableInput(formGroupWapp);
            break;
          case "wapp-cheked":
            dataType.value = type.value;           
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

  isChecked(types);

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

  wapp.addEventListener('click', e => e.target.value = '+55');
  //função para adicionar o código do país
  wapp.addEventListener('input', (e) => {
    let value = e.target.value.replace(/^([^+])/, '+$1');
    getCodeIso(value);
  });


  const btnSubmit = document.getElementById('btnSubmit');
  //Função pare remover espaços, parêtese e hífens
  btnSubmit.addEventListener('click', () =>{
    wapp.value = wapp.value.replace(/[()\s-]/g, '');
  })
  
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