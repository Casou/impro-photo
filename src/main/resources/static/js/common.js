const MONTH_NAMES_SHORT = ["Jan", "Fev", "Mar", "Avr", "Mai", "Jun", "Jul", "Aou", "Sep", "Oct", "Nov", "Dec"];
const MONTH_NAMES_FULL = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Decembre"];
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