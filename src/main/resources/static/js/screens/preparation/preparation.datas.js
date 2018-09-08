function fetchDatas(url, callback, callbackError) {
    $.ajax({
        url: url,
        type: 'GET',
        encoding: "UTF-8",
        dataType: 'json',
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
            if (callbackError) {
                callbackError();
            }
        })
        .always(function () {
        });
}


function retrieveCategories() {
    return new Promise((resolve, reject) => {
        fetchDatas("/list/categoriesWithCompletion",
            (categorieDtos) => {
                retrieveCategoriesCallback(categorieDtos);
                resolve();
            },
            reject);
    });
}

function retrieveCategoriesCallback(categorieDtos) {
    $("#categories ul#categoriesList").html("");
    $(categorieDtos).each(function (index, categorie) {
        addCategorie(categorie);
    });
    $('#categories span.loading').hide();
    $("ul#categoriesList").sortable();
}

function addPreviewDate(dateDto) {
    const date = new Date(dateDto.date);
    $("#dates_preview_calendar").append(
        `<li class="date">
            <figure>
                <header>
                    ${ getMonthName(date.getMonth()) }
                </header>
                <div class="dateNumber">
                    ${ date.getDate() }
                </div>
            </figure>
            <label>
                ${ dateDto.nom }
            </label>
        </li>`
    )
}


function retrieveRemerciements() {
    return new Promise((resolve, reject) => {
        fetchDatas("/list/remerciements", remerciementDto => {
            retrieveRemerciementsCallback(remerciementDto);
            resolve();
        }, reject);
    });
}

function retrieveRemerciementsCallback(remerciementDto) {
    $('#remerciements_div textarea#remerciements_texte').html(remerciementDto.texte);
    calcRemainingChars();
}

function postRemerciements() {
    const remerciement = $("#remerciements_texte").val();
    $.ajax({
        url: "/remerciements",
        type: 'POST',
        encoding: "UTF-8",
        data: JSON.stringify({texte: remerciement}),
        dataType: 'json',
        contentType: 'application/json'
    })
        .done(() => {
        })
        .fail(function (resultat, statut, erreur) {
            console.error(url);
            console.error(resultat, statut, erreur);
            alert("Erreur lors de la l'enregistrement des remerciements : \n" +
                "Erreur : " + erreur + "\n" +
                "Statut : " + statut + "\n" +
                "Résultat : " + resultat);
            if (callbackError) {
                callbackError();
            }
        });
}


function activateButtons() {
    $('section#categorie_tab_main button.submitChanges').attr("disabled", false);
}

function deactivateButtons() {
    $('section#categorie_tab_main button.submitChanges').attr("disabled", true);
}


function saveCategories() {
    showLoading();

    const categories = $.map($('#categories li'), function (categorie, index) {
        return {
            id: $(categorie).find("input.categorie_id").val(),
            nom: $(categorie).find("input.categorie_nom").val(),
            pathFolder: $(categorie).find("input.categorie_path").val(),
            type: $(categorie).find("select.categorie_type").val(),
            ordre: index
        };
    });

    $.ajax({
        url: "/preparation",
        type: 'POST',
        encoding: "UTF-8",
        dataType: 'json',
        data: JSON.stringify({categories}),
        contentType: 'application/json'
    })
        .done(function () {
            retrieveCategories().then(() => hideLoading());
        })
        .fail(function (xhr, ajaxOptions, thrownError) {
            console.error(">> Response save datas");
            console.error(thrownError);

            let message = "Erreur lors de la sauvegarde des données de la préparation de l'impro.";
            if (xhr.responseText) {
                try {
                    let response = JSON.parse(xhr.responseText);
                    console.error(response);
                    console.error(response.message);
                    message += "\n\nException : " + response.message;
                } catch (e) {
                    console.error(">> Exception", e);
                    message += "\n\n(Impossible de formatter le message d'erreur)";
                }
            }
            alert(message);
        });
}

function saveDates() {
    showLoading();

    const datesImpro = $.map($('ul#datesList li'), function (categorie) {
        return {
            id: $(categorie).find("input.date_id_hidden").val(),
            date: $(categorie).find("input.date_date_input").val(),
            nom: $(categorie).find("input.date_nom_input").val()
        };
    });

    $.ajax({
        url: "/preparation",
        type: 'POST',
        encoding: "UTF-8",
        dataType: 'json',
        data: JSON.stringify({datesImpro}),
        contentType: 'application/json'
    })
        .done(function () {
            retrieveDates().then(() => hideLoading());
        })
        .fail(function (xhr, ajaxOptions, thrownError) {
            console.error(">> Response save datas");
            console.error(thrownError);

            let message = "Erreur lors de la sauvegarde des données de la préparation de l'impro.";
            if (xhr.responseText) {
                try {
                    let response = JSON.parse(xhr.responseText);
                    console.error(response);
                    console.error(response.message);
                    message += "\n\nException : " + response.message;
                } catch (e) {
                    console.error(">> Exception", e);
                    message += "\n\n(Impossible de formatter le message d'erreur)";
                }
            }
            alert(message);
        });
}


function retrieveMusiques() {
    return new Promise((resolve, reject) => {
        fetchDatas("/list/playlistSongs", function (musiquesDtos) {
            retrieveMusiquesCallback(musiquesDtos);
            resolve();
        }, reject);
    });
}


function retrieveMusiquesCallback(musiquesDtos) {
    $("section#musiques_tab_main table tbody").html("");
    $(musiquesDtos).each(function (index, musiqueDto) {
        $("section#musiques_tab_main table tbody").append(`
            <tr class="song_${ index }">
                <td><input type="checkbox" name="musiquesToDelete[]" value="${ musiqueDto.nom }" class="actionCheck" /></td>
                <td>
                    <span class="fa fa-play" 
                          aria-hidden="true" 
                          onClick="playMusique('${ musiqueDto.path }')"
                    ></span>
                    ${ musiqueDto.nom }
                </td>
                <td><span class="deleteSong" onClick="deleteSong('${ musiqueDto.nom }', ${ index });">[X]</span></td>
            </tr>
        `);
    });
}

function playMusique(path) {
    $('#musiques_tab_main audio').attr("src", path);
}


let INTRO_PICTURES = [];
let DATES_PICTURES = [];
function retrievePicturesTabDatas() {
    const allPromises = [];
    allPromises.push(retrieveIntroPictures());

    return new Promise((resolve, reject) => {
        Promise.all(allPromises).then(function () {
            resolve();
        }, function (err) {
            console.error(err);
            alert("Erreur lors du All Promise (Divers tab) : " + err);
            reject();
        });
    });
}

function retrieveIntroPictures() {
    return new Promise((resolve, reject) => {
        fetchDatas("/list/pictures/intro", (pictures) => {
            INTRO_PICTURES = pictures;
            $("#divers_intro_nb_pictures").html(pictures.length);
            resolve();
        }, reject);
    });
}


function retrieveJoueurs() {
    return new Promise((resolve, reject) => {
        fetchDatas("/list/pictures/joueurs", (pictures) => {
            $("#remerciements_tab_main ul").html("");
            pictures.forEach(picture => {
                $("#remerciements_tab_main ul").append(
                    `<li><img src="${ picture.source }" /></li>`
                );
            });

            resolve();
        }, reject);
    });
}



function retrieveDates() {
    const datePromises = [];
    datePromises.push(new Promise((resolve, reject) => {
        fetchDatas("/list/dates", function (dateDtos) {
            retrieveDatesCallback(dateDtos);
            resolve();
        }, reject);
    }));

    datePromises.push(retrieveDatesPictures());

    return Promise.all(datePromises);
}

function retrieveDatesCallback(dateDtos) {
    $("#datesList").html("");
    $("#dates_preview_calendar").html("");
    $(dateDtos).each(function (index, date) {
        addDate(date);
        addPreviewDate(date);
    });
    $('#datesNext span.loading').hide();
}

function retrieveDatesPictures() {
    return new Promise((resolve, reject) => {
        fetchDatas("/list/pictures/dates", (pictures) => {
            $('#dates_preview_photos').html("");
            pictures.forEach((picture, index)  => {
                const randRotate = Math.round(Math.random() * 40) - 20;
                $('#dates_preview_photos').append(
                    `<img src="${ picture.source }" style="top : ${ (index * 125) - 15 }px;  transform: rotate(${ randRotate }deg);" />`
                );
            });

            DATES_PICTURES = pictures;
            $("#divers_dates_nb_pictures").html(pictures.length);
            resolve();
        }, reject);
    });
}

let CURRENT_VERSION = null;
function retrieveUpdateDatas() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/applicationInfo',
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            contentType: 'application/json'
        })
        .done(dto => {
            $('#currentBuild').html(dto.applicationVersion);
            CURRENT_VERSION = dto.applicationVersion;
            $('#currentBuildDate').html(dto.applicationTimestamp);
            resolve();
        })
        .fail(reject);
    });
}

function asyncRetrieveNewVersion() {
    return new Promise((resolve, reject) => {
        if (!PARAMETRES["GENERAL-UPDATE_URL"]) {
            $("#versionTable").hide();
            reject();
        }

        $.ajax({
            url: PARAMETRES["GENERAL-UPDATE_URL"],
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            contentType: 'application/json'
        })
        .done(versioningDto => {
            $("#versionTable tbody").html(
                versioningDto.versions.map(version => {
                    const icon = CURRENT_VERSION === version.buildNumber ?
                        `<span class="span-icon icon-accept"></span>` :
                            isNewestVersion(version.buildNumber, CURRENT_VERSION) ?
                                `<span class="span-icon icon-accept olderVersion"></span>`
                            : "";

                    return `
                        <tr>
                            <td>${ version.buildNumber }</td>
                            <td>${ version.buildDate ? new Date(version.buildDate).toLocaleDateString() : "-" }</td>
                            <td>${ version.buildDescription }</td>
                            <td>${ icon }</td>
                        </tr>
                    `}).join("")
            );
            resolve();
        })
        .fail(() => {
            $("#versionTable").hide();
            reject();
        });
    });
}



function retrieveParametres() {
    return new Promise((resolve, reject) => {
        fetchDatas("/list/parametres",
            (parametres) => {
                retrieveParametresCallback(parametres);
                resolve();
            },
            reject);
    });
}

let PARAMETRES = null;
function retrieveParametresCallback(parametres) {
    $("#param_tab tbody").html("");

    PARAMETRES = [];
    parametres.forEach(param => PARAMETRES[getParamKey(param)] = param.valueType === 'BOOLEAN' ? param.value === 'true' : param.value);

    parametres.forEach(param => {
        $("#param_tab tbody").append(`
        <tr>
            <td>${ param.id.context }</td>
            <td>${ param.id.key }</td>
            <td>${ param.description }</td>
            <td>${ renderInputParam(param) }</td>
        </tr>
        `);
    });
}

function renderInputParam(param) {
    if (param.valueType === 'BOOLEAN') {
        return `
        <select onChange="updateParam('${ param.id.context }', '${ param.id.key }', this)">
            <option value="true" ${ param.value === "true" && "selected" }>Vrai</option>
            <option value="false" ${ param.value === "false" && "selected" }>Faux</option>
        </select>
        `;
    } else if (param.valueType === 'INTEGER') {
        return `
        <input type="number" value="${ param.value }" onBlur="updateParam('${ param.id.context }', '${ param.id.key }', this)" />
        `;
    } else if (param.valueType === 'STRING') {
        return `
        <input type="text" value="${ param.value }" onBlur="updateParam('${ param.id.context }', '${ param.id.key }', this)" />
        `;
    }

    return param.value;
}