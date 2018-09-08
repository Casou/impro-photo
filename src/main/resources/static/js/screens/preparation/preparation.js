$(document).ready(function() {
    calcRemainingChars();
    $('#categories h2 button').click(deleteAllCategories);
    $('#remerciements_texte').on("keyup", calcRemainingChars);

    const allPromises = [];
    allPromises.push(retrieveCategories());
    allPromises.push(retrieveDates());
    allPromises.push(retrieveMusiques());
    allPromises.push(retrieveJingles());
    allPromises.push(retrievePicturesTabDatas());
    allPromises.push(retrieveJoueurs());
    allPromises.push(retrieveRemerciements());
    allPromises.push(retrieveUpdateDatas());
    allPromises.push(retrieveParametres());

    Promise.all(allPromises).then(function() {
        $('input, select, textarea').change(activateButtons);
        asyncRetrieveNewVersion().catch(() => {});
    }, function(err) {
        console.error(err);
        alert("Erreur lors du All Promise : " + err);
    });
    
    $("#videos_tab_main video source").each(function () {
        $(this).attr("src", $(this).attr("originalSrc") + "?" + generateRandom(10));
    });
    $("#videos_tab_main video")[0].load();

    $('section#categorie_tab_main button.sendCategoriesForm').click(saveCategories);
    $('section#dates_tab_main button.sendDatesForm').click(saveDates);

    $("menu nav").click(function() {
        showSection($(this).attr("id"));
    });

    selectDefaultTab();
});

function selectDefaultTab() {
    const hash = window.location.hash.substr(1);
    const selectedId = hash && hash + "_tab";
    const firstMenu = $("menu nav").first();
    showSection(selectedId || $(firstMenu).attr("id"));
}

function showSection(id) {
    $("section.tab").hide();
    $("section#" + id + "_main").show();
    $("nav").removeClass("selected");
    $("nav#" + id).addClass("selected");
}


function deleteAllCategories() {
    if (!confirm("Voulez-supprimer toutes les catégories (et leurs dossiers associés) ?")) {
        return;
    }
    showLoading();
    $.ajax({
        url: '/categories',
        type: 'DELETE',
        encoding: "UTF-8",
        dataType: 'json',
        contentType: 'application/json'
    })
    .done(() => {
        $("ul#categoriesList").html("");
    })
    .always(() => hideLoading());
}

function showDiversPictures(imageList, discriminant) {
    IMAGE_VIEWER.setImages(imageList, discriminant);
    IMAGE_VIEWER.show(retrievePicturesTabDatas);
}

function uploadDiversImageCallback(discriminant) {
    let promise;
    switch (discriminant) {
      case("intro") :
          promise = retrieveIntroPictures();
          break;
      case("dates") :
          promise = retrieveDatesPictures();
          break;
      default :
          alert("Type inconnu : " + discriminant);
    }
    
    promise.then(() => {
        $("#inputImages" + discriminant).val("");
        hideLoading();
    });
}


function resetImpro() {
    showLoading();
    $.ajax({
        url: '/preparation/resetImpro',
        type: 'PUT',
        encoding: "UTF-8",
        dataType: 'json',
        contentType: 'application/json'
    })
    .done(() => {})
    .always(() => hideLoading());
}