$(document).ready(() => {
    $("body").append(`
    <div id="dialogHelpImageWrapper">
        <div id="dialogHelpImageClose" onclick="$('#dialogHelpImageWrapper').hide();"><img src="/images/icones/cross-white.png" /></div>
        <div id="dialogHelpImage">
            <img />
        </div>    
    </div>`);

    $("#dialogHelpImageWrapper").click(() => $('#dialogHelpImageWrapper').hide());
    $("#dialogHelpImage, #dialogHelpImageClose").click(event => event.stopPropagation());
    $(document).keyup(function(e) {
        if (e.keyCode === 27) { // escape key maps to keycode `27`
            $('#dialogHelpImageWrapper').hide();
        }
    });

    $("span.helpImage").click((event) => displayHelpImage($(event.target).attr("image")));
});

function displayHelpImage(image) {
    $('#dialogHelpImageWrapper #dialogHelpImage img').attr("src", "/images/help/" + image + ".png");
    $('#dialogHelpImageWrapper').show();
}