
$(document).ready(function() {
    $.ajax({
        url: '/statutPreparation',
        type: 'GET',
        encoding: "UTF-8",
        dataType: 'json',
//            data: JSON.stringify(inputDto),
        contentType: 'application/json'
    })
        .done(function (statusDto) {
            updateStatut(statusDto);
        })
        .fail(function (resultat, statut, erreur) {
            console.log(resultat, statut, erreur);
            alert("Erreur lors de la récuparation du statut de la préparation : " + erreur);
        })
        .always(function () {
        });
});



function updateStatut(statusDto) {
    console.log(statusDto);

    let allStatus = [];
    allStatus.push(updateStatutCategories(statusDto));
    allStatus.push(updateStatutVideo(statusDto));
    allStatus.push(updateStatutPhotosJoueurs(statusDto));
    allStatus.push(updateStatutRemerciements(statusDto));
    allStatus.push(updatePhotosPresentationDates(statusDto));

    let generalStatus = "ok";
    $(allStatus).each(function(index, status) {
        if (status == "ko") {
            generalStatus = "ko";
        } else if (status == "warning" && generalStatus == "ok") {
            generalStatus = "warning";
        }
    });
    $('#check_impro .icon-general-state').addClass("status_" + generalStatus);
}


function updateStatutCategories(statusDto) {
    let status = "ok";
    if (statusDto.categories) {
        $("span#status_categories").addClass("ok").removeClass("ko").removeClass("loading").html("OK");
    } else {
        $("span#status_categories").addClass("ko").removeClass("ok").removeClass("loading").html("KO");
        status = "ko";
    }
    $('li#status_categories_li span.span-icon').addClass("status_" + status);
    return status;
}

function updateStatutVideo(statusDto) {
    let etatVideo = "";
    let status = "ok";
    if (!statusDto.videoPresentationPresentateur) {
        etatVideo += '<span class="warning">Vidéo présentateur [KO]</span>';
        status = "warning";
    } else {
        etatVideo += '<span class="ok">Vidéo présentateur [OK]</span>';
    }
    if (!statusDto.videoPresentationJoueurs) {
        etatVideo += '<span class="warning">Vidéo joueur [KO]</span>';
        status = "warning";
    } else {
        etatVideo += '<span class="ok">Vidéo joueur [OK]</span>';
    }
    $("span#status_video").removeClass("loading").html(etatVideo);
    $('li#status_video_li span.span-icon').addClass("status_" + status);
    return status;
}

function updateStatutPhotosJoueurs(statusDto) {
    let status = "ok";
    $("span#status_photos_joueurs").removeClass("loading").html(statusDto.photosJoueurs);
    if (statusDto.photosJoueurs == 0) {
        $("span#status_photos_joueurs").addClass("warning");
        status = "warning";
    } else {
        $("span#status_photos_joueurs").addClass("ok");
    }
    $('li#status_photos_joueurs_li span.span-icon').addClass("status_" + status);
    return status;
}

function updateStatutRemerciements(statusDto) {
    if (statusDto.remerciements != null && statusDto.remerciements.texte != undefined && statusDto.remerciements.texte != "") {
        $("span#status_remerciements").removeClass("loading").html("Défini").addClass("ok").prop("title", statusDto.remerciements);
    } else {
        $("span#status_remerciements").removeClass("loading").html("Aucun").addClass("neutre");
    }
    $('li#status_remerciements_li span.span-icon').addClass("status_ok");
    return "ok";
}

function updatePhotosPresentationDates(statusDto) {
    let status = "ok";
    if (statusDto.photosPresentationDates != 5) {
        $("span#status_photos_dates").removeClass("loading").addClass("warning").html(statusDto.photosPresentationDates
            + " photo(s) dans le dossier (Attendus : 5)");
        status = "warning";
    } else {
        $("span#status_photos_dates").removeClass("loading").addClass("ok").html("OK");
    }
    $('li#status_photos_dates_li span.span-icon').addClass("status_" + status);
    return status;
}
