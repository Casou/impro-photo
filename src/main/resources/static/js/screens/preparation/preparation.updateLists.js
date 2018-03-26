function addCategorie(dto) {
    let idCategorie = newCategorie(dto.id);
    $(`#categories li#${ idCategorie } input.categorie_id`).val(dto.id);
    $(`#categories li#${ idCategorie } input.categorie_nom`).val(dto.nom);
    $(`#categories li#${ idCategorie } select.categorie_type`).val(dto.type);
    $(`#categories li#${ idCategorie } input.categorie_path`).val(dto.pathFolder);
    $(`#categories li#${ idCategorie } span.pictures span.nb-picture`).html(dto.nbPictures);
    if (dto.tooManyPictures) {
        $(`#categories li#${ idCategorie } span.pictures span.icon-picture`).removeClass("icon-picture").addClass("icon-picture_error");
    }

    if (dto.pathInError) {
        $(`#categories li#${ idCategorie } input.categorie_path`)
            .addClass("error")
            .addClass("help")
            .after('<span class="span-icon icon-warning help" title="Ce répertoire n\'existe pas : ' + dto.pathFolder + '"></span>')
            ;
    }
    if (!dto.existsInDatabase) {
        $(`#categories li#${ idCategorie } input.categorie_path`)
            .after('<span class="span-icon icon-exclamation help no-name" title="Ce répertoire n\'a pas de nom ni de type."></span>')
        ;
    }
}

function newCategorie(id) {
    let newId = 'cat_' + ((id == undefined) ? generateRandom(7) : id);
    $('#categoriesList').append($('#hiddenClones #cat_clone').first().clone().attr('id', newId));
    $('#categories li').last().find('input').val('');
    $('#categories li').last().find('select').val('PHOTO');
    $('#categories li').last().find('.id_tech').html(id);
    return newId;
}

function removeCategorie(li) {
    $(li).remove();
}


function addDate(dto) {
    let idDate = newDate(dto.id);
    $(`#datesList li#${ idDate } input.date_id_hidden`).val(dto.id);
    $(`#datesList li#${ idDate } input.date_date_input`).attr("id", idDate + "_date")
        .datepicker("setDate", new Date(dto.date));
    $(`#datesList li#${ idDate } input.date_nom_input`).val(dto.nom);
}

function datePickerize(selector) {
    return $(selector).datepicker({
        dateFormat: 'dd/mm/yy',
        firstDay : 1,
        dayNames : DAY_NAMES_FULL,
        dayNamesMin : DAY_NAMES_SHORT,
        dayNamesShort : DAY_NAMES_SHORT,
        monthNames : MONTH_NAMES_FULL,
        monthNamesShort : MONTH_NAMES_SHORT})
    ;
}

function newDate(id) {
    let newId = 'date_' + ((id == undefined) ? generateRandom(7) : id);
    $('#datesList').append($('#hiddenClones #date_clone').first().clone().attr('id', newId));
    datePickerize(`#datesList li#${ newId } input.date_date_input`);
    return newId;
}

function removeDate(li) {
    $(li).remove();
}

let TEXTEAREA_MAX_CHARS;
$(document).ready(function() {
    TEXTEAREA_MAX_CHARS = $('#remerciements_texte').attr('maxlength');
    $('#nb_max_chars').html(TEXTEAREA_MAX_CHARS);
});
function calcRemainingChars() {
    $('#nb_remaining_chars').html(TEXTEAREA_MAX_CHARS - $('#remerciements_texte').val().length);
}





function deleteSong(nom, index) {
    const songToDelete = { nom : nom };
    $.ajax({
        url: "/song",
        type: 'DELETE',
        encoding: "UTF-8",
        dataType: 'json',
        data: JSON.stringify(songToDelete),
        contentType: 'application/json'
    })
    .done(function (response) {
        $("tr#song_" + index).remove();
    })
    .fail(function (xhr, ajaxOptions, thrownError) {
        console.error(">> Response delete song");
        console.error(thrownError);
        
        let message = "Erreur lors de la suppression d'une musique.";
        if (xhr.responseText != undefined) {
            try {
                let response = JSON.parse(xhr.responseText);
                console.error(response);
                console.error(response.message);
                message += "\n\nException : " + response.message;
            } catch(e) {
                console.error(">> Exception", e);
                message += "\n\n(Impossible de formatter le message d'erreur)";
            }
        }
        alert(message);
    });
}

function deleteSelectedSongs() {
    const checked = $("section#musiques_tab_main table tbody td input[type=checkbox]:checked");
    if ($(checked).length == 0) {
        alert("Aucune musique choisie");
        return;
    }
    
    const songsToDelete = [];
    $(checked).each(function(index, input) {
        songsToDelete.push({ nom : input.value })
    });
    
    $.ajax({
        url: "/songs",
        type: 'DELETE',
        encoding: "UTF-8",
        dataType: 'json',
        data: JSON.stringify(songsToDelete),
        contentType: 'application/json'
    })
    .done(function () {
        $(checked).each(function(index) {
            $("tr#song_" + index).remove();
        });
        $("section#musiques_tab_main table thead th input[type=checkbox]").prop("checked", false);
    })
    .fail(function (xhr, ajaxOptions, thrownError) {
        console.error(">> Response delete songs");
        console.error(thrownError);
        
        let message = "Erreur lors de la suppression des musiques.";
        if (xhr.responseText != undefined) {
            try {
                let response = JSON.parse(xhr.responseText);
                console.error(response);
                console.error(response.message);
                message += "\n\nException : " + response.message;
            } catch(e) {
                console.error(">> Exception", e);
                message += "\n\n(Impossible de formatter le message d'erreur)";
            }
        }
        alert(message);
    });
}

function uploadSongs() {
    showLoading();
    $.ajax({
        url: "/songs",
        type: 'POST',
        data: new FormData(document.getElementById("importMusique")),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false
    })
    .done(function () {
        $("#inputImportPlaylist").val("");
        retrieveMusiques().then(() => hideLoading());
    })
    .fail(function (xhr, ajaxOptions, thrownError) {
        console.error(">> Response import songs");
        console.error(thrownError);
        
        let message = "Erreur lors de l'import des musiques.";
        if (xhr.responseText != undefined) {
            try {
                let response = JSON.parse(xhr.responseText);
                console.error(response);
                console.error(response.message);
                message += "\n\nException : " + response.message;
            } catch(e) {
                console.error(">> Exception", e);
                message += "\n\n(Impossible de formatter le message d'erreur)";
            }
        }
        alert(message);
        hideLoading();
    });
}


function uploadCategories() {
    showLoading();
    $.ajax({
        url: "/categories",
        type: 'POST',
        data: new FormData(document.getElementById("importCategories")),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false
    })
    .done(function () {
        $("#inputImportCategorie").val("");
        retrieveCategories().then(() => hideLoading());
    })
    .fail(function (xhr, ajaxOptions, thrownError) {
        console.error(">> Response import categories");
        console.error(thrownError);
        
        let message = "Erreur lors de l'import des catégories.";
        if (xhr.responseText != undefined) {
            try {
                let response = JSON.parse(xhr.responseText);
                console.error(response);
                console.error(response.message);
                message += "\n\nException : " + response.message;
            } catch(e) {
                console.error(">> Exception", e);
                message += "\n\n(Impossible de formatter le message d'erreur)";
            }
        }
        alert(message);
        hideLoading();
    });
}