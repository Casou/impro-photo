function addCategorie(dto) {
    let idCategorie = newCategorie(dto.id);
    $(`#categories li#${ idCategorie } input.categorie_id`).val(dto.id);
    $(`#categories li#${ idCategorie } input.categorie_nom`).val(dto.nom);
    $(`#categories li#${ idCategorie } select.categorie_type`).val(dto.type);
    $(`#categories li#${ idCategorie } input.categorie_path`).val(dto.pathFolder);

    if (dto.pathInError) {
        $(`#categories li#${ idCategorie } input.categorie_path`)
            .addClass("error")
            .addClass("help")
            .after('<span class="span-icon icon-warning help" title="Ce répertoire n\'existe pas : ' + dto.pathFolder + '"></span>')
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
    if ($('#categories li').size() == 1) {
        alert('Vous ne pouvez pas supprimer toutes les catégories.');
        return;
    }
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
