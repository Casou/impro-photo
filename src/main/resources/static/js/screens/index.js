$(document).ready(function() {
    getVersion();
    getEtatPreparation();

    $("#restart_raspberry").click(restartRaspberry);
    $("#shutdown_raspberry").click(shutdownRaspberry);
});

function getVersion() {
    $.ajax({
        url: '/applicationInfo',
        type: 'GET',
        encoding: "UTF-8",
        dataType: 'json',
        contentType: 'application/json'
    })
    .done(dto => {
        // console.log(dto);
        if (dto.applicationVersion) {
            let ip = "";
            if (dto.ip && dto.ip.indexOf(":") > 0) {
                const ipAndPort = dto.ip.split(":");
                const ipParts = ipAndPort[0].split(".");
                ip += ipParts.join('<span class="separator">.</span>');
                ip += '<span class="separator">:</span>' + ipAndPort[1];
            }
            $('#ip').html(ip);

            let version = "Version ?";
            if (dto.applicationVersion.indexOf(".") > 0) {
                const versionParts = dto.applicationVersion.split(/\.|\-/);
                version = "Version " + versionParts.join('<span class="separator">.</span>');
            }
            $('#version').html(version);
            $('aside').show();
        } else {
            $('aside').hide();
        }
        if (dto.isRaspberry) {
            $(".raspberry_only").show();
        }
    });
}

function getEtatPreparation() {
    $.ajax({
        url: '/statutPreparation',
        type: 'GET',
        encoding: "UTF-8",
        dataType: 'json',
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
}


function updateStatut(statusDto) {
    let allStatus = [];
    allStatus.push(updateStatutImpro(statusDto));
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


function updateStatutImpro(statusDto) {
    let status = "ok";
    if (statusDto.improLaunched) {
        $("span#status_impro").addClass("ko").removeClass("ok").removeClass("loading").html("Déjà démarrée");
        status = "ko";
    } else {
        $("span#status_impro").addClass("ok").removeClass("ko").removeClass("loading").html("Au début");
    }
    $('li#status_impro_li span.span-icon').addClass("status_" + status);
    return status;
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
        etatVideo += '<span class="warning">Présentateur [KO]</span>';
        status = "warning";
    } else {
        etatVideo += '<span class="ok">Présentateur [OK]</span>';
    }
    if (!statusDto.videoPresentationJoueurs) {
        etatVideo += '<span class="warning">Joueur [KO]</span>';
        status = "warning";
    } else {
        etatVideo += '<span class="ok">Joueur [OK]</span>';
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
    if (statusDto.remerciements) {
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

function restartRaspberry() {
    if (!alert("Êtes-vous sûr de vouloir redémarrer le Rasperry ?")) {
        return;
    }
    $.ajax({
        url: '/restart',
        type: 'POST',
        encoding: "UTF-8",
        dataType: 'json',
        contentType: 'application/json'
    });
}

function shutdownRaspberry() {
    if (!alert("Êtes-vous sûr de vouloir éteindre le Rasperry ?")) {
        return;
    }
    $.ajax({
        url: '/shutdown',
        type: 'POST',
        encoding: "UTF-8",
        dataType: 'json',
        contentType: 'application/json'
    });
}