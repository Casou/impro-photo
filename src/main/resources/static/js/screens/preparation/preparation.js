$(document).ready(function() {
    calcRemainingChars();
    $('#remerciements_texte').on("keyup", calcRemainingChars);

    retrieveAllDatas();
    retrieveMusiques();

    $('section#categorie_tab_main button.submitChanges').click(function() {
        saveDatas();
    });
    
    $("menu nav").click(function() {
        showSection($(this).attr("id"));
    });
    const firstMenu = $("menu nav").first();
    showSection($(firstMenu).attr("id"));
});

function showSection(id) {
    $("section").hide();
    $("section#" + id + "_main").show();
    $("nav").removeClass("selected");
    $("nav#" + id).addClass("selected");
}