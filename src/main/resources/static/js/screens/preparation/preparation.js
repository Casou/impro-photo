$(document).ready(function() {
    calcRemainingChars();
    $('#remerciements_texte').on("keyup", calcRemainingChars);

    retrieveAllDatas();

    $('button.submitChanges').click(function() {
        saveDatas();
    })
});