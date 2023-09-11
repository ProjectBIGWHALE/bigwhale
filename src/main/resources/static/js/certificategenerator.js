// document.addEventListener('DOMContentLoaded', function () {
//     const xInput = document.getElementById('x');
//     const yInput = document.getElementById('y');
//     const fontSizeInput = document.getElementById('fontSize');
//     const backgroundImage = document.getElementById('backgroundImage');
//     const textElement = document.getElementById('textElement');
//     const canvas = document.getElementById('canvas');

//     xInput.addEventListener('input', updatePreview);
//     yInput.addEventListener('input', updatePreview);
//     fontSizeInput.addEventListener('input', updatePreview);

//     const imageInput = document.getElementById('image');
//     imageInput.addEventListener('change', function () {
//         const file = imageInput.files[0];
//         const reader = new FileReader();
//         reader.onload = function (event) {
//             backgroundImage.src = event.target.result;
//             updatePreview();
//         };
//         reader.readAsDataURL(file);
//     });

//     function updatePreview() {
//         const x = parseInt(xInput.value);
//         const y = parseInt(yInput.value);
//         const fontSize = parseInt(fontSizeInput.value);

//         textElement.textContent = 'Seu texto aqui';
//         textElement.style.left = x + 'px';
//         textElement.style.top = y + 'px';
//         textElement.style.fontSize = fontSize + 'px';

//         canvas.style.position = 'relative';
//         canvas.style.display = 'inline-block';
//         canvas.style.backgroundImage = `url("${backgroundImage.src}")`;
//         canvas.style.backgroundRepeat = 'no-repeat';
//         canvas.style.backgroundSize = 'contain';
//         canvas.style.width = backgroundImage.width + 'px';
//         canvas.style.height = backgroundImage.height + 'px';

//         textElement.style.position = 'absolute';
//     }
// });
function formatDataBR(data) {
    const dataObj = new Date(data);
    dataObj.setUTCHours(0, 0, 0, 0);
    const dia = String(dataObj.getUTCDate()).padStart(2, '0');
    const mes = String(dataObj.getUTCMonth() + 1).padStart(2, '0');
    const ano = dataObj.getUTCFullYear();
    return `${dia}/${mes}/${ano}`;
}

function updateCertificateText() {
    const participantName = document.getElementById('participant_name').value;
    const textCertificado = document.getElementById('text_certificado');
    const nomeEvento = document.getElementById('event_name').value;
    const cargoOrganizador = document.getElementById('organizing_position').value;
    const nomeOrganizador = document.getElementById('organizing_name').value;
    const cargaHoraria = document.getElementById('workload').value;
    const dataEvento = document.getElementById('event_date').value;
    const dataCertificado = document.getElementById('certificate_date').value;
    const localCertificado = document.getElementById('local_date').value;


    // Crie uma expressão regular com o modificador global (g)
    const nomeCompleto = /\[Nome do Participante\]/g;
    const nome_Evento = /\[Nome do Evento\]/g;
    const cargo_Organizador = /\[Cargo do Organizador\]/g;
    const nome_Organizador = /\[Nome do Organizador\]/g;
    const carga_Horaria = /\[Carga Horária\]/g;
    const data_Evento = /\[Data do Evento\]/g;
    const data_Certificado = /\[Data de Emissão do Certificado\]/g;
    const local_Certificado = /\[Local de Emissão do Certificado\]/g;
    
    // Substitua todas as ocorrências de '[Nome do Participante]'
    let updateText = textCertificado.textContent.replace(nomeCompleto, participantName);
    updateText = updateText.replace(nome_Evento, nomeEvento);
    updateText = updateText.replace(cargo_Organizador, cargoOrganizador);
    updateText = updateText.replace(nome_Organizador, nomeOrganizador);
    updateText = updateText.replace(carga_Horaria, cargaHoraria);
    updateText = updateText.replace(data_Evento, formatDataBR(dataEvento));
    updateText = updateText.replace(data_Certificado, formatDataBR(dataCertificado));
    updateText = updateText.replace(local_Certificado, localCertificado);

    // Atualize o conteúdo do textarea
    textCertificado.value = updateText;
}