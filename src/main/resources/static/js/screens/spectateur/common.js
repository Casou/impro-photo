WEBSOCKET_CLIENT.subscribe("/topic/general/refresh", () => refresh());

let VOLUME_PLAYLIST = 10;
let PLAYLIST_SONGS = [];
let PLAYLIST_REMAINING_SONGS = [];
let CURRENT_SONG;
let IS_PLAYING = false;
$(document).ready(function () {
    $("body").append(`
        <div id="playlist" style="display : none;">
            <audio id="playlist" preload="true"></audio>
            <audio id="jingle" preload="true"></audio>
        </div>
    `);
    
    $('#playlist audio#jingle').on("ended", jingleStopped);
    initPlaylist();
});

function initPlayer() {
    IS_PLAYING = STATUS.isPlaylistPlaying;
    if (STATUS.currentSong) {
        loadSong(STATUS.currentSong);
    } else {
        loadRandom();
    }
    VOLUME_PLAYLIST = STATUS.playlistVolume;
    updateVolume();
}

function initPlaylist() {
    const allSongsPromise = getAllSongs();
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
            url: '/songs',
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
}

WEBSOCKET_CLIENT.subscribe("/topic/general/setVolume", (responseJson) => updateVolume(JSON.parse(responseJson.body)));
function updateVolume(volumeDto) {
    if (volumeDto) {
        VOLUME_PLAYLIST = volumeDto.volume;
    }
    $("#playlist audio#playlist").prop("volume", VOLUME_PLAYLIST / 10);
    $("#playlist audio#jingle").prop("volume", VOLUME_PLAYLIST / 10);
}

WEBSOCKET_CLIENT.subscribe("/topic/general/nextSong", () => loadRandom());
function loadRandom() {
    if (PLAYLIST_REMAINING_SONGS.length === 0) {
        if (PLAYLIST_SONGS.length === 0) {
            return;
        }
        PLAYLIST_REMAINING_SONGS = PLAYLIST_SONGS.slice(0);
    }
    
    const randomIndex = parseInt(Math.random() * PLAYLIST_REMAINING_SONGS.length);
    const song = PLAYLIST_REMAINING_SONGS[randomIndex];
    if (!song) {
        loadRandom();
        return;
    }
    loadSong(song);
    PLAYLIST_REMAINING_SONGS.splice(randomIndex, 1);
}

function loadSong(song) {
    CURRENT_SONG = song;
    $("#playlist audio#playlist").attr("src", song.path);
    
    WEBSOCKET_CLIENT.sendMessage("/app/action/updateSong", song);
    
    if (IS_PLAYING) {
        playPlaylist();
    }
}

WEBSOCKET_CLIENT.subscribe("/topic/general/playPlaylist", () => playPlaylist());
function playPlaylist() {
    stopJingle();
    $("#playlist audio#playlist")[0].play();
    IS_PLAYING = true;
    
    WEBSOCKET_CLIENT.sendMessage("/app/action/playlistPlaying", CURRENT_SONG);
}

WEBSOCKET_CLIENT.subscribe("/topic/general/pausePlaylist", () => pausePlaylist());
function pausePlaylist() {
    $("#playlist audio#playlist")[0].pause();
    IS_PLAYING = false;
    
    WEBSOCKET_CLIENT.sendMessage("/app/action/playlistPaused", {});
}




WEBSOCKET_CLIENT.subscribe("/topic/general/playJingle", (responseJson) => playJingle(JSON.parse(responseJson.body).path));
function playJingle(path) {
    pausePlaylist();
    $("#playlist audio#jingle").attr("src", path);
    $("#playlist audio#jingle")[0].play();
}

WEBSOCKET_CLIENT.subscribe("/topic/general/stopJingle", () => stopJingle());
function stopJingle() {
    $("#playlist audio#jingle")[0].pause();
    $("#playlist audio#jingle")[0].currentTime = 0;
}

function jingleStopped() {
    stopJingle();
    WEBSOCKET_CLIENT.sendMessage("/app/action/jingleStopped", {});
}