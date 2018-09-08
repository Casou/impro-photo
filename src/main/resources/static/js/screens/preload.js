let __LOADING_HIDDEN_DIV__;
let __LOADED_MAX_IMAGE__ = 0;
let __LOADED_IMAGES__ = 0;

$(document).ready(function () {
    __LOADING_HIDDEN_DIV__ = document.getElementById("loadingHiddenDiv");
    loadParameters().then(() => {
        if (PARAMETRES["GENERAL-MODAL_LOADING"]) {
            showLoading();
        }
        preloadAllImages().then(hideLoading);
    });
});


function preloadAllImages() {
    return new Promise((resolve, reject)  => {
        $.ajax({
            url: '/list/allImages',
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            contentType: 'application/json'
        })
        .done(imageDtoList => {
            preloadAllImages_callback(imageDtoList)
                .then(resolve);
        })
        .fail((resultat, statut, erreur) => {
            console.log(resultat, statut, erreur);
            alert("Erreur lors de la récuparation du statut de la préparation : " + erreur);
            $("#loading").hide();
            reject();
        });
    });
}

function preloadAllImages_callback(imageDtoList) {
    __LOADED_MAX_IMAGE__ = imageDtoList.length;
    const allPromises = [];

    imageDtoList.forEach(imageDto => {
        allPromises.push(preloadImage(imageDto.source)
            .then(() => {
                __LOADED_IMAGES__++;
                const perc = parseInt((__LOADED_IMAGES__ / __LOADED_MAX_IMAGE__) * 100);
                $("#loadingPercentage").html(perc);
            }));
    });

    return Promise.all(allPromises);
}

function preloadImage(url) {
    // img.src = url;
    // var img = new Image();

    return new Promise((resolve, reject)  => {
        const img = document.createElement("IMG");
        img.src = url;
        img.onload = resolve;

        __LOADING_HIDDEN_DIV__.appendChild(img);
    });

}


function loadParameters() {
    return new Promise((resolve, reject)  => {
        $.ajax({
            url: '/list/parametres',
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            contentType: 'application/json'
        })
        .done(params => {
            params.forEach(param => PARAMETRES[getParamKey(param)] = param.valueType === 'BOOLEAN' ? param.value === 'true' : param.value);
            resolve();
        })
        .fail((resultat, statut, erreur) => {
            console.log(resultat, statut, erreur);
            alert("Erreur lors de la récuparation du statut de la préparation : " + erreur);
            hideLoading();
            reject();
        });
    });
}