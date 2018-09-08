let VOLUME_PLAYLIST = 10;
let CURRENT_SONG;
let IS_PLAYING = false;

$(document).ready(function () {

    $("body").append(`
    <div id="controlPanelTab" class="collapsed metal-bg" onClick="$('#controlPanelTab, #controlPanel').toggleClass('collapsed');">Panneau de contrôle</div>
    <div id="controlPanel" class="collapsed metal-bg">
        <h1>Panneau de contrôle</h1>
        <div id="playlist">
            <h2>Playlist</h2>
            <div id="playlist_screen">
                Loading...
            </div>
            <button id="togglePlaylist" class="metal radial" onClick="togglePlaylist();"><span class="fa fa-play" aria-hidden="true"></span></button>
            <button class="metal radial" onClick="nextSong();"><span class="fa fa-step-forward" aria-hidden="true"></span></button>
            <div id="volume">
                <span id="volume_value">${ VOLUME_PLAYLIST }</span>
                <div id="volume_buttons">
                    <button onClick="plusVolume();"><span class="fa fa-plus"  aria-hidden="true"></span></button>
                    <button onClick="minusVolume();"><span class="fa fa-minus" aria-hidden="true"></span></button>
                </div>
            </div>
        </div>
        <div id="jingles">
            <ul id="jingles_categories">
                <li>Loading...</li>            
            </ul>
            <ul id="jingles_files">
                <li>Loading...</li>            
            </ul>
        </div>
        <div id="controlActionButton">
            <button id="refresh" class="metal radial" onClick="refreshImpro();"><img src="/images/refresh.png" /></button>
            <button id="restart" class="metal radial" onClick="restartImpro();"><img src="/images/restart.png" /></button>
        </div>
        <footer>Version <span id="applicationVersion"></span><span id="applicationTimestamp"></span></footer>
    </div>
    `);
    
    initJingles();
    getInfos();
});

function initPlayer() {
    IS_PLAYING = STATUS.isPlaylistPlaying;
    CURRENT_SONG = STATUS.currentSong;
    VOLUME_PLAYLIST = STATUS.playlistVolume;
    updatePlaylistScreen();
    updateVolume();
}

/* ************************************
 *************  PLAYLIST  *************
 ************************************ */

function plusVolume() {
    if (VOLUME_PLAYLIST < 10) {
        sendUpdateVolume(VOLUME_PLAYLIST + 1);
        $("#volume_buttons button").attr("disabled", true);
    }
}

function minusVolume() {
    if (VOLUME_PLAYLIST > 0) {
        sendUpdateVolume(VOLUME_PLAYLIST - 1);
        $("#volume_buttons button").attr("disabled", true);
    }
}

function sendUpdateVolume(volumeToSet) {
    WEBSOCKET_CLIENT.sendMessage("/app/action/setVolume", { volume : volumeToSet });
}

WEBSOCKET_CLIENT.subscribe("/topic/general/setVolume", (responseJson) => updateVolume(JSON.parse(responseJson.body)));
function updateVolume(volumeDto) {
    if (volumeDto) {
        VOLUME_PLAYLIST = volumeDto.volume;
    }
    $("#volume_value").html(VOLUME_PLAYLIST);
    $("#volume_buttons button").attr("disabled", false);
    // $("#playlist audio#playlist").prop("volume", VOLUME_PLAYLIST / 10);
}


function getStatusChar() {
    if (IS_PLAYING) {
        return "►";
    }
    return "❚❚";
}

function updatePlaylistScreen() {
    if (CURRENT_SONG) {
        $("#playlist_screen").html("<span class='status_char'>" + getStatusChar() + "</span> " + CURRENT_SONG.nom);
    }
    if (IS_PLAYING) {
        $('#togglePlaylist span').removeClass("fa-play").addClass("fa-pause");
    } else {
        $('#togglePlaylist span').removeClass("fa-pause").addClass("fa-play");
    }
}

function togglePlaylist() {
    if (IS_PLAYING) {
        WEBSOCKET_CLIENT.sendMessage("/app/action/pausePlaylist", {});
    } else {
        WEBSOCKET_CLIENT.sendMessage("/app/action/playPlaylist", {});
    }
}

function nextSong() {
    WEBSOCKET_CLIENT.sendMessage("/app/action/nextSong", {});
}

WEBSOCKET_CLIENT.subscribe("/topic/general/playlistPlaying", (responseJson) => playPlaylist(JSON.parse(responseJson.body)));
function playPlaylist(songDto) {
    CURRENT_SONG = songDto;
    IS_PLAYING = true;
    updatePlaylistScreen();
    $('button.jingle span').removeClass("fa-stop").addClass("fa-play");
}

WEBSOCKET_CLIENT.subscribe("/topic/general/playlistPaused", () => pausePlaylist());
function pausePlaylist() {
    IS_PLAYING = false;
    updatePlaylistScreen();
}

WEBSOCKET_CLIENT.subscribe("/topic/general/updateSong", (responseJson) => updateSong(JSON.parse(responseJson.body)));
function updateSong(songDto) {
    CURRENT_SONG = songDto;
    updatePlaylistScreen();
}





/* ***********************************
 *************  JINGLES  *************
 *********************************** */
let JINGLE_CATEGORIES = [];
function initJingles() {
    getAllJingles().then((allJingleCategories) => {
        JINGLE_CATEGORIES = allJingleCategories;
        renderJingleList();
    });
}


function getAllJingles() {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: '/list/jingles',
            dataType: 'json',
            encoding: "UTF-8",
            contentType: 'application/json'
        })
        .done(function (allSongs) {
            resolve(allSongs);
        })
        .fail(function (resultat, statut, erreur) {
            handleAjaxError(resultat, statut, erreur);
            reject();
        })
        .always(function () {
        });
    });
}

function renderJingleList() {
    $('#jingles ul').html("");
    $('#jingles_categories').html(JINGLE_CATEGORIES.map(category => `
        <li onClick="selectJingleCategory('${ category.nom }')">
            ${ category.nom }
        </li>`).join(""));
}

function selectJingleCategory(nom) {
    const category = JINGLE_CATEGORIES.filter(c => c.nom = nom)[0];
    $('#jingles_files')
        .html(`<li class="back" onClick="unSelectJingleCategory()">Retour</li>`)
        .append(
            category.jingles.map(jingle =>
                `<li>
                    <button class="jingle" onClick="toggleJingle('${ jingle.path }', this);"><span class="fa fa-play" aria-hidden="true"></span></button>
                    ${ jingle.nom }
                </li>`
            ).join("")
    );
    $('#jingles_categories').addClass("selectedJingleCategory");
}

function unSelectJingleCategory() {
    $('#jingles_categories').removeClass("selectedJingleCategory");
}


function toggleJingle(path, button) {
    // pausePlaylist();
    const span = $(button).find('span');
    const isCurrentlyPlaying = span.hasClass("fa-stop");
    if (isCurrentlyPlaying) {
        stopJingle();
    } else {
        playJingle(path, span);
    }
}

function playJingle(path, span) {
    WEBSOCKET_CLIENT.sendMessage("/app/action/playJingle", { path : path });
    
    $('button.jingle span').removeClass("fa-stop").addClass("fa-play");
    $(span).removeClass("fa-play").addClass("fa-stop");
}

function stopJingle() {
    $('button.jingle span').removeClass("fa-stop").addClass("fa-play");
    WEBSOCKET_CLIENT.sendMessage("/app/action/stopJingle", {});
}

WEBSOCKET_CLIENT.subscribe("/topic/general/jingleStopped", () => jingleStopped());
function jingleStopped() {
    $('button.jingle span').removeClass("fa-stop").addClass("fa-play");
}







/* ***********************************
 *************  ACTIONS  *************
 *********************************** */

WEBSOCKET_CLIENT.subscribe("/topic/general/refresh", () => refresh());
function restartImpro() {
    if (confirm("Voulez-vous redémarrer l'improvisation ? Cela remettra tous les statuts à zéro et rafraichira la régie ET le spectateur.")) {
        WEBSOCKET_CLIENT.sendMessage("/app/action/resetImpro", {});
    }
}
function refreshImpro() {
    if (confirm("Voulez-vous rafraichir l'affichage de la régie ET du spectateur ?")) {
        WEBSOCKET_CLIENT.sendMessage("/app/action/refresh", {});
    }
}





/* ************************************
 *************  GET INFO  *************
 ************************************ */

function getInfos() {
  $.ajax({
    type: 'GET',
    url: '/applicationInfo',
    dataType: 'json',
    encoding: "UTF-8",
    contentType: 'application/json'
  })
  .done(function (infoDto) {
    $("#applicationVersion").html(infoDto.applicationVersion);
    $("#applicationTimestamp").html(infoDto.applicationTimestamp);
  })
  .fail(function (resultat, statut, erreur) {
    handleAjaxError(resultat, statut, erreur);
  })
  .always(function () {
  });
}