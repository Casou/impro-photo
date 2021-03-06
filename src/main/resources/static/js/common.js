const MONTH_NAMES_SHORT = ["Jan", "Fev", "Mar", "Avr", "Mai", "Jun", "Jul", "Aou", "Sep", "Oct", "Nov", "Dec"];
const MONTH_NAMES_FULL = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"];
const DAY_NAMES_SHORT = ["Di", "Lu", "Ma", "Me", "Je", "Ve", "Sa"];
const DAY_NAMES_FULL = ["Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"];


function generateRandom(nbCharacteres) {
    if (nbCharacteres == undefined) {
        nbCharacteres = 7;
    }
    let text = "";
    let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for(let i = 0; i < nbCharacteres; i++) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    }

    return text;
}

jQuery.fn.rotate = function(degrees) {
    $(this).css({'-webkit-transform' : 'rotate('+ degrees +'deg)',
        '-moz-transform' : 'rotate('+ degrees +'deg)',
        '-ms-transform' : 'rotate('+ degrees +'deg)',
        'transform' : 'rotate('+ degrees +'deg)'});
    return $(this);
};

function refresh() {
    location.reload();
}

function showCategorieSelectedPopin(forceShow) {
    if ($("#category_selected").hasClass("fadeInRight") && !forceShow) {
        $("#category_selected").addClass("fadeOutRight").removeClass("fadeInRight");
        $("#category_selected h1, #category_selected #category_selected_name").removeClass("bounceInRight");
    } else {
        $("#category_selected").addClass("fadeInRight").removeClass("fadeOutRight");
        $("#category_selected h1, #category_selected #category_selected_name").addClass("bounceInRight");
    }
}

function getMonthName(monthNumber) {
    return MONTH_NAMES_FULL[monthNumber];
}

function handleAjaxError(resultat, statut, erreur, contextMessage) {
  let message = contextMessage ? contextMessage + "\n\n" : "";
  if (resultat.responseText !== undefined) {
    try {
      let response = JSON.parse(resultat.responseText);
      console.error(response);
      console.error(response.message);
      message += "\n\nException : " + response.message;
    } catch(e) {
      console.error(">> Exception", e);
      message += "\n\n(Impossible de formatter le message d'erreur)";
    }
  }
  alert(message);
}



function showLoading() {
    $("#loading").show();
}

function hideLoading() {
    $("#loading").hide();
}

function getParamKey(param) {
    return param.id.context + "-" + param.id.key;
}

function isNewestVersion(currentVersion, newestVersion) {
    const currentVersionParts = currentVersion.split(".");
    const newestVersionParts = newestVersion.split(".");
    for (let i = 0; i < currentVersionParts.length; i++) {
        const currentVersionPart = parseVersionPart(currentVersionParts[i]);
        const newestVersionPart = parseVersionPart(newestVersionParts[i]);

        if (newestVersionPart !== null && currentVersionPart !== null && newestVersionPart > currentVersionPart) {
            return true;
        }
    }
    return false;
}

function parseVersionPart(versionPart) {
    if (!versionPart) {
        return null;
    }

    if (!isNaN(versionPart)) {
        return parseInt(versionPart);
    }

    const versionParts = versionPart.split("-");
    if (isNaN(versionParts[0])) {
        return null;
    }

    let minus = versionParts[1] === "SNAPSHOT" ? -0.3 : versionParts[1] === "ALPHA" ? -0.2 : versionParts[1] === "BETA" ? -0.1 : 0;
    return parseInt(versionParts[0]) - minus;
}