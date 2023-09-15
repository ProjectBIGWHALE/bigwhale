document.addEventListener("DOMContentLoaded", () => {
  const urlLink = document.querySelector("#link");
  const color = document.querySelector("#get-color");
  const colorSelect = document.querySelector("#color-select");
  const btnDownload = document.querySelector("#download");
  const ulTypes = document.querySelector("#ul-types");
  const types = document.querySelectorAll("input[name='types']");

  ulTypes.addEventListener("change", () => {
    validationField(isChecked(types), "checkbox-error");
    enabledBtnDownload();
  });

  color.addEventListener("blur", (e) => {
    const colorValue = e.target.value;
    validationField(colorValue.length > 0, "color-error");
    enabledBtnDownload();
  });

  urlLink.addEventListener("blur", (e) => {
    const urlValue = e.target.value;
    validationField(urlValue.length > 0, "link-error");
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

  btnDownload.addEventListener("click", (e) => {
    if (!isValid()) {
      e.preventDefault();
      validationField(isChecked(types), "checkbox-error");
      validationField(urlLink.value.length > 0, "link-error");
      validationField(color.value.length > 0, "color-error");
    }
  });

  //Função para adicionar cor ao texto de input Cor
  function onChangeTextColor(color, text) {
    if (color === "#ffffff") return (text.style.color = "#000000");
    return (text.style.color = color);
  }

  //função para exibir a mensagem de error
  function showMsgError(spanError) {
    spanError.style.visibility = "visible";
  }

  //função para ocultar a mensagem de erro
  function hideMsgError(spanError) {
    spanError.style.visibility = "hidden";
  }

  // função para validar o campo e exibir a mensagem se houver error
  function validationField(validation, spanId) {
    const error = document.getElementById(spanId);
    return !validation ? showMsgError(error) : hideMsgError(error);
  }

  //função para verificar se algum chekebox foi marcado
  function isChecked(types) {
    for (const type of types) {
      if (type.checked === true) {
        return true
      }
    }
    return false;
  }
});
