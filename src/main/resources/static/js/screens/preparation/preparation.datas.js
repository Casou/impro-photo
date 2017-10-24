function retrieveDatas(url, callback) {
    $.ajax({
        url: url,
        type: 'GET',
        encoding: "UTF-8",
        dataType: 'json',
//            data: JSON.stringify(inputDto),
        contentType: 'application/json'
    })
    .done(function (dtos) {
        callback(dtos);
    })
    .fail(function (resultat, statut, erreur) {
        console.error(url);
        console.error(resultat, statut, erreur);
        alert("Erreur lors de la récuparation des données de la préparation de l'impro : \n" +
            "Erreur : " + erreur + "\n" +
            "Statut : " + statut + "\n" +
            "Résultat : " + resultat);
    })
    .always(function () {
    });
}


function retrieveAllDatas() {
    retrieveCategorieTypes(function() {
        retrieveCategories();
        retrieveRemerciements();
        retrieveDates();
    });
}


function retrieveCategorieTypes(callback) {
    retrieveDatas("/list/categories/types", function(types) {
        retrieveCategorieTypesCallback(types);
        callback();
    });
}

function retrieveCategorieTypesCallback(types) {
    $(types).each(function(index, type) {
        $("#hiddenClones span.type select").append(`<option value="${type.code}">
            ${type.label}
        </option>`);
    });
}




function retrieveCategories() {
    retrieveDatas("/list/categories", retrieveCategoriesCallback);
}

function retrieveCategoriesCallback(categorieDtos) {
    $(categorieDtos).each(function(index, categorie) {
        addCategorie(categorie);
    });
    $('#categories span.loading').remove();
    $("ul#categoriesList").sortable();
}




function retrieveDates() {
    retrieveDatas("/list/dates", retrieveDatesCallback);
}

function retrieveDatesCallback(dateDtos) {
    $(dateDtos).each(function(index, date) {
        addDate(date);
    });
    $('#dates span.loading').remove();
}




function retrieveRemerciements() {
    retrieveDatas("/list/remerciements", retrieveRemerciementsCallback);
}

function retrieveRemerciementsCallback(remerciementDtos) {
    if (remerciementDtos.length == 0) {
        $('#remerciements textarea#remerciements_texte').html("");
        $('#remerciements input#remerciements_id').val("");
    } else {
        let remerciement = remerciementDtos[0];
        $('#remerciements textarea#remerciements_texte').html(remerciement.texte);
        $('#remerciements input#remerciements_id').val(remerciement.id);
    }
    calcRemainingChars();
}