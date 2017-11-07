let VOLUME_PLAYLIST = 10;

$(document).ready(function () {

    $("body").append(`<div id="controlPanel" class="">
        <h1>Panneau de contrôle</h1>
        <div id="playlist">
            <h2>Playlist</h2>
            <button class="metal radial"><span class="fa fa-play" aria-hidden="true"></span></button>
            <button class="metal radial"><span class="fa fa-pause" aria-hidden="true"></span></button>
            <div id="volume">
                <span id="volume_value">${ VOLUME_PLAYLIST }</span>
                <div id="volume_buttons">
                    <button class="metal radial mini" onClick="plusVolume();"><span class="fa fa-plus"  aria-hidden="true"></span></button>
                    <button class="metal radial mini" onClick="minusVolume();"><span class="fa fa-minus" aria-hidden="true"></span></button>
                </div>
            </div>
        </div>
    </div>
    `);

});

function plusVolume() {
    if (VOLUME_PLAYLIST < 10) {
        VOLUME_PLAYLIST++;
        $("#volume_value").html(VOLUME_PLAYLIST);
    }
}

function minusVolume() {
    if (VOLUME_PLAYLIST > 0) {
        VOLUME_PLAYLIST--;
        $("#volume_value").html(VOLUME_PLAYLIST);
    }
}