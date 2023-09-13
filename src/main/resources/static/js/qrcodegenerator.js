document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector("#form");
    const urlLink = document.querySelector("#link");


    urlLink.addEventListener('change', (e) =>{
        if(e.target.value){
            fetch('/') //end point que a imagem foi armazenada
            .then(res => res.json())
            .then(data => {
                console.log(data);
            })
            .catch(e => console.error(`Error: ${e}`))
        }
    });

})