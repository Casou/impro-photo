$(document).ready(function() {
    calcRemainingChars();
    $('#remerciements_texte').on("keyup", calcRemainingChars);

    retrieveAllDatas();
    retrieveMusiques();
    retrieveJingles();
    
    $("#videos_tab_main video source").each(function () {
        $(this).attr("src", $(this).attr("originalSrc") + "?" + generateRandom(10));
    });
    $("#videos_tab_main video")[0].load();

    $('section#categorie_tab_main button.sendCategoriesForm').click(function() {
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

function showLoading() {
    $("#loading").show();
}

function hideLoading() {
    $("#loading").hide();
}
