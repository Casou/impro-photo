function getWindowOpener() {
    if (window.opener == null) {
        alert("La fenetre spectateur n'a pas pu être trouvée. Si la communication est rompue, essayez d'appuyer sur R sur l'interface spectateur sans fermer l'interface régie.\n" +
            "Sinon fermez cette fenêtre et appuyer sur R l'interface spectateur.");
        return null;
    }

    return window.opener;
}


function generateRandom(nbCharacteres) {
	if (nbCharacteres == undefined) {
		nbCharacteres = 7; 
	}
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < nbCharacteres; i++ ) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    }

    return text;
}

$.ajaxSetup({
    error: function (resultat, statut, erreur) {
    	handleAjaxError(resultat, statut, erreur);
    }
});

function handleAjaxError(resultat, statut, erreur) {
	// let errorMessage = erreur;

    console.log(resultat);
    console.log(erreur);

    if (resultat != undefined && resultat.responseText != undefined) {
		alert('Erreur lors de l\'appel AJAX : ' + resultat.responseText);
	} else {
		alert('Erreur lors de l\'appel AJAX : ' + resultat);
	}
}

function newReturnObject() {
    let returnObject = new Object();
    returnObject.status = "KO";
    returnObject.message = "notInitialized";

    return returnObject;
}

function debug(message) {
    console.log("DEBUG " + message);
    $('#debugContent').append(getTimeFormatted()).append(message).append("<br/>");
}

function getTimeFormatted() {
    var date = new Date();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    hours = hours < 10 ? '0'+ hours : hours;
    minutes = minutes < 10 ? '0'+ minutes : minutes;
    seconds = seconds < 10 ? '0'+ seconds : seconds;
    return '[' + hours + ':' + minutes + ':' + seconds + '] ';
}


function dump(obj) {
    var out = obj;
    for (var i in obj) {
        out += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;" + i + ": " + obj[i];
    }
    return out;
}

jQuery.fn.rotate = function(degrees) {
    $(this).css({'-webkit-transform' : 'rotate('+ degrees +'deg)',
                 '-moz-transform' : 'rotate('+ degrees +'deg)',
                 '-ms-transform' : 'rotate('+ degrees +'deg)',
                 'transform' : 'rotate('+ degrees +'deg)'});
    return $(this);
};