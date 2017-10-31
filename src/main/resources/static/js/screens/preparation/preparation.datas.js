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
    $('ul#categoriesList').html("");
    $('ul#datesList').html("");
    $('textarea#remerciements_texte').html("");
    $('span.loading').show();

    let allPromises = [];
    allPromises.push(retrieveCategories());
    allPromises.push(retrieveRemerciements());
    allPromises.push(retrieveDates());

    Promise.all(allPromises).then(function() {
        $('input, select, textarea').change(function() {
            activateButtons();
        })
    }, function(err) {
        console.error(err);
        alert("Erreur lors du All Promise : " + err);
    });
}




function retrieveCategories() {
    return new Promise((resolve, reject) => {
        retrieveDatas("/list/categories", function(categorieDtos) {
            retrieveCategoriesCallback(categorieDtos);
            resolve();
        });
    });
}

function retrieveCategoriesCallback(categorieDtos) {
    $(categorieDtos).each(function(index, categorie) {
        addCategorie(categorie);
    });
    $('#categories span.loading').hide();
    $("ul#categoriesList").sortable();
}




function retrieveDates() {
    return new Promise((resolve, reject) => {
        retrieveDatas("/list/dates", function(dateDtos) {
            retrieveDatesCallback(dateDtos);
            resolve();
        });
    });
}

function retrieveDatesCallback(dateDtos) {
    $(dateDtos).each(function(index, date) {
        addDate(date);
    });
    $('#dates span.loading').hide();
}




function retrieveRemerciements() {
    return new Promise((resolve, reject) => {
        retrieveDatas("/list/remerciements", function(remerciementDtos) {
            retrieveRemerciementsCallback(remerciementDtos);
            resolve();
        });
    });
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







function activateButtons() {
    $('button.submitChanges').attr("disabled", false);
}

function deactivateButtons() {
    $('button.submitChanges').attr("disabled", true);
}







function saveDatas() {
    deactivateButtons();

    let originalText = $('button.submitChanges').first().html();

    $('button.submitChanges').html("Enregistrement en cours...");

    $.ajax({
        url: "/preparation",
        type: 'POST',
        encoding: "UTF-8",
        dataType: 'json',
        data: JSON.stringify(buildForm()),
        contentType: 'application/json'
    })
    .done(function (response) {
        console.log("Response save datas : ", response);
        retrieveAllDatas();
    })
    .fail(function (xhr, ajaxOptions, thrownError) {
        console.error(">> Response save datas");
        console.error(thrownError);

        let message = "Erreur lors de la sauvegarde des données de la préparation de l'impro.";
        if (xhr.responseText != undefined) {
            try {
                let response = JSON.parse(xhr.responseText);
                console.error(response);
                console.error(response.message);
                message += "\n\nException : " + response.message;
            } catch(e) {
                message += "\n\n(Impossible de formatter le message d'erreur)";
            }
        }
        alert(message);
    })
    .always(function () {
        $('button.submitChanges').html(originalText);
        activateButtons();
    });
}

function buildForm() {
    let categories = $.map($('#categories li'), function(categorie, index) {
        return {
            id : $(categorie).find("input.categorie_id").val(),
            nom : $(categorie).find("input.categorie_nom").val(),
            pathFolder : $(categorie).find("input.categorie_path").val(),
            type : $(categorie).find("select.categorie_type").val(),
            ordre : index
        };
    });

    let remerciement = {
        id : $('#remerciements_id').val(),
        texte : $('#remerciements_texte').val()
    }

    let dates = $.map($('ul#datesList li'), function(categorie) {
        return {
            id : $(categorie).find("input.date_id_hidden").val(),
            date : $(categorie).find("input.date_date_input").val(),
            nom : $(categorie).find("input.date_nom_input").val()
        };
    });

    return {
        categories : categories,
        remerciements : remerciement,
        datesImpro : dates
    };
}