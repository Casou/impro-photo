let VOLUME_PLAYLIST = 10;
let PLAYLIST_SONGS = [];
let PLAYLIST_REMAINING_SONGS = [];
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
            <button class="metal radial" onClick="loadRandom();"><span class="fa fa-step-forward" aria-hidden="true"></span></button>
            <div id="volume">
                <span id="volume_value">${ VOLUME_PLAYLIST }</span>
                <div id="volume_buttons">
                    <button class="metal radial mini" onClick="plusVolume();"><span class="fa fa-plus"  aria-hidden="true"></span></button>
                    <button class="metal radial mini" onClick="minusVolume();"><span class="fa fa-minus" aria-hidden="true"></span></button>
                </div>
            </div>
            
            <audio id="playlist" preload="true"></audio>
        </div>
        <div id="jingles">
            <h2>Jingles</h2>
            <ul>
                <li>Loading...</li>            
            </ul>
            <audio id="jingle" preload="true"></audio>
        </div>
        <div id="controlActionButton">
            <button id="refresh" class="metal radial" onClick="refreshImpro();"><img src="/images/refresh.png" /></button>
            <button id="restart" class="metal radial" onClick="restartImpro();"><img src="/images/restart.png" /></button>
        </div>
    </div>
    `);

    initPlaylist();
    initJingles();
});


/* ************************************
 *************  PLAYLIST  *************
 ************************************ */

function plusVolume() {
    if (VOLUME_PLAYLIST < 10) {
        VOLUME_PLAYLIST++;
        $("#volume_value").html(VOLUME_PLAYLIST);
        updateVolume();
    }
}

function minusVolume() {
    if (VOLUME_PLAYLIST > 0) {
        VOLUME_PLAYLIST--;
        $("#volume_value").html(VOLUME_PLAYLIST);
        updateVolume();
    }
}

function updateVolume() {
    $("#playlist audio#playlist").prop("volume", VOLUME_PLAYLIST / 10);
}



function initPlaylist() {
    let allSongsPromise = getAllSongs()
    allSongsPromise.then((allSongs) => {
        fillAudio(allSongs);
        updateVolume();
        $('#playlist audio#playlist').on("ended", loadRandom);
    });
}

function getAllSongs() {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: '/list/playlistSongs',
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

function fillAudio(allSongs) {
    if (allSongs.length === 0) {
        throw new RangeError("Songs are empty");
    }

    PLAYLIST_SONGS = allSongs;
    PLAYLIST_REMAINING_SONGS = PLAYLIST_SONGS.slice(0);
    loadRandom();
}

function getStatusChar() {
    if (IS_PLAYING) {
        return "►";
    }
    return "❚❚";
}

function loadRandom() {
    if (PLAYLIST_REMAINING_SONGS.length == 0) {
        PLAYLIST_REMAINING_SONGS = PLAYLIST_SONGS.slice(0);
    }

    let randomIndex = parseInt(Math.random() * PLAYLIST_REMAINING_SONGS.length);
    let song = PLAYLIST_REMAINING_SONGS[randomIndex];
    if (song == undefined) {
        console.log(randomIndex, PLAYLIST_REMAINING_SONGS.length, PLAYLIST_REMAINING_SONGS);
        loadRandom();
        return;
    }
    loadSong(song);
    PLAYLIST_REMAINING_SONGS.splice(randomIndex, 1);

    if (IS_PLAYING) {
        playPlaylist();
    }
}

function loadSong(song) {
    CURRENT_SONG = song;
    updatePlaylistScreen();
    $("#playlist audio#playlist").attr("src", song.path);
}

function updatePlaylistScreen() {
    $("#playlist_screen").html(getStatusChar() + " " + CURRENT_SONG.nom);
}

function togglePlaylist() {
    if (IS_PLAYING) {
        pausePlaylist();
    } else {
        playPlaylist();
    }
}

function playPlaylist() {
    stopJingle();
    $("#playlist audio#playlist")[0].play();
    IS_PLAYING = true;
    updatePlaylistScreen();
    $('#togglePlaylist span').removeClass("fa-play").addClass("fa-pause");
}

function pausePlaylist() {
    $("#playlist audio#playlist")[0].pause();
    IS_PLAYING = false;
    updatePlaylistScreen();
    $('#togglePlaylist span').removeClass("fa-pause").addClass("fa-play");
}






/* ***********************************
 *************  JINGLES  *************
 *********************************** */

function initJingles() {
    let allJinglesPromise = getAllJingles()
    allJinglesPromise.then((allJingles) => {
        fillJingleList(allJingles);
    });
    $('#jingles audio#jingle').on("ended", stopJingle);
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

function fillJingleList(allJingles) {
    $('#jingles ul').html("");
    $(allJingles).each((index, jingle) => {
        let html = `<li>
            <button class="metal radial mini jingle" onClick="toggleJingle('${ jingle.path }', this);"><span class="fa fa-play" aria-hidden="true"></span></button>
            ${ jingle.nom }
        </li>`;
        $('#jingles ul').append(html);
    });
}

function toggleJingle(path, button) {
    pausePlaylist();
    let span = $(button).find('span');
    let isCurrentlyPlaying = span.hasClass("fa-stop");
    if (isCurrentlyPlaying) {
        stopJingle();
    } else {
        playJingle(path, span);
    }
}

function playJingle(path, span) {
    $('button.jingle span').removeClass("fa-stop").addClass("fa-play");
    $(span).removeClass("fa-play").addClass("fa-stop");
    $("#jingles audio#jingle").attr("src", path);
    $("#jingles audio#jingle")[0].play();
}

function stopJingle() {
    $('button.jingle span').removeClass("fa-stop").addClass("fa-play");
    $("#jingles audio#jingle")[0].pause();
    $("#jingles audio#jingle")[0].currentTime = 0;
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