// Selecionar o elemento com a classe 'carrossel'
const carrossel = document.querySelector(".carrossel");

const carrossel_documento = document.querySelector(".carrossel-documento");

const carrossel_seguranca = document.querySelector(".carrossel-seguranca");

// Selecionar todas os cards dentro do carrossel
const images = carrossel.querySelectorAll(".card-designer");

const documento = carrossel_documento.querySelectorAll(".card-documento");

const seguranca = carrossel_seguranca.querySelectorAll(".card-seguranca");

// Variável para acompanhar o índice do card
let currentIndex = 0;
let currentIndex2 = 0;
let currentIndex3 = 0;

// Função para mostrar o card anterior
function showPreviousImage(carouselClass) {
  if (carouselClass === 'carrossel') {
    currentIndex = (currentIndex - 1 + images.length) % images.length;
    updateCarousel(carrossel, images, currentIndex);

  } else if (carouselClass === 'carrossel-documento') {
    currentIndex2 = (currentIndex2 - 1 + documento.length) % documento.length;
    updateCarousel(carrossel_documento, documento, currentIndex2);

  } else if (carouselClass === 'carrossel-seguranca') {
    currentIndex3 = (currentIndex3 - 1 + seguranca.length) % seguranca.length;
    updateCarousel(carrossel_seguranca, seguranca, currentIndex3);
  }
}

// Função para mostrar a próximo card
function showNextImage(carouselClass) {
  if (carouselClass === 'carrossel') {
    currentIndex = (currentIndex + 1) % images.length;
    updateCarousel(carrossel, images, currentIndex);

  } else if (carouselClass === 'carrossel-documento') {
    currentIndex2 = (currentIndex2 + 1) % documento.length;
    updateCarousel(carrossel_documento, documento, currentIndex2);

  } else if (carouselClass === 'carrossel-seguranca') {
    currentIndex3 = (currentIndex3 + 1) % seguranca.length;
    updateCarousel(carrossel_seguranca, seguranca, currentIndex3);
  }
}

let imagesToShow = 0;
// Chame a função para ajustar o número de imagens assim que a página for carregada
ajustarNumeroDeImagens();

// Variável para definir a quantidade de cards a serem exibidas no carrossel
// Altere este valor para exibir uma quantidade diferente de cards

// Função para atualizar o carrossel com os tres cards 
function updateCarousel(carouselElement, imagesArray, currentIndex) {
  // Clonar todos os cards para garantir que elas sejam independentes
  const clonedImages = Array.from(imagesArray).map(div => div.cloneNode(true));


  const orderedImages = [];
  for (let i = 0; i < imagesToShow; i++) {
    const indexToShow = (currentIndex + i) % imagesArray.length;
    orderedImages.push(clonedImages[indexToShow]);
  }

  while (carouselElement.firstChild) {
    carouselElement.removeChild(carouselElement.firstChild);
  }

  orderedImages.forEach(div => {
    carouselElement.appendChild(div);
  });
}

ajustarNumeroDeImagens();
window.addEventListener('resize', ajustarNumeroDeImagens);

// Função para ajustar o número de imagens com base no tamanho da tela
function ajustarNumeroDeImagens() {
  // Obtenha o tamanho da tela
  var larguraDaTela = window.innerWidth;

  var larguraDaTelaSmartphone = window.screen.availWidth;

  // Verifique o tamanho da tela e ajuste o número de imagens conforme necessário
  if (larguraDaTela > 1850) { // Se a largura for menor que 1024 pixels
    imagesToShow = 4; // Defina um valor intermediário
  } else if (larguraDaTela > 1300) { // Por exemplo, se a largura for menor que 768 pixels
    imagesToShow = 3; // Defina um valor menor para exibir menos imagens
  } else if (larguraDaTela < 850 || larguraDaTelaSmartphone < 850) {
    imagesToShow = 1;
  }

  else if (larguraDaTela > 850) { // Se a largura for menor que 1024 pixels
    imagesToShow = 2; // Defina um valor intermediário

  }


  // Inicializar os carrosseis com as tres primeiras imagens
  updateCarousel(carrossel, images, currentIndex);
  updateCarousel(carrossel_documento, documento, currentIndex2);
  updateCarousel(carrossel_seguranca, seguranca, currentIndex3);

  // Agora você pode usar a variável numImagensExibidas para controlar o número de imagens exibidas no seu site.
}