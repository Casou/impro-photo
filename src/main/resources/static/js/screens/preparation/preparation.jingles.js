function retrieveJingles() {
    return new Promise((resolve, reject) => {
        fetchDatas("/jingles", function (categoryJingleDtos) {
            retrieveJinglesCallback(categoryJingleDtos);
            resolve();
        }, reject);
    });
}

let JINGLES_CATEGORIES = [];
let JINGLES_CATEGORY_SELECTED = null;
function retrieveJinglesCallback(categoryJingleDtos) {
    $('#jingleList aside ul, #jingleList main table tbody').html("");
    JINGLES_CATEGORIES = [];
    categoryJingleDtos.forEach(category => JINGLES_CATEGORIES[category.id] = category);

    JINGLES_CATEGORIES.forEach(category => {
        $('#jingleList aside ul').append(`
            <li id="jingle_category_${ category.id }" onClick="selectJingleCategory(${ category.id })">
                ${ category.name }
            </li>
        `);
    });
    if (JINGLES_CATEGORY_SELECTED) {
        selectJingleCategory(JINGLES_CATEGORY_SELECTED.id);
    }
}

function selectJingleCategory(id) {
    $('#jingleList aside li').removeClass("selected");
    $('#jingle_category_' + id).addClass("selected");
    JINGLES_CATEGORY_SELECTED = JINGLES_CATEGORIES[id];
    renderJingleMainCategory();
}

function renderJingleMainCategory() {
    if (!JINGLES_CATEGORY_SELECTED) {
        $('#jingleList main').html("");
        return;
    }

    const allRowsRendered = JINGLES_CATEGORY_SELECTED.jingles.map(jingle => `
        <tr id="jingle_${ jingle.id }">
            <td>
                <span class="fa fa-play" aria-hidden="true" onClick="playJingle('${ jingle.path }')"></span>
                ${ jingle.name }
            </td>
            <td class="actionCell">
                <span class="delete" onClick="deleteJingle('${ jingle.id }')">[X]</span>
            </td>
        </tr>`).join("");

    $('#jingleList main').html(`
    <header>
        <button class="submitChanges deleteSongs" onClick="deleteCategoryJingle(${ JINGLES_CATEGORY_SELECTED.id })">Supprimer la catégorie</button>
        <audio autoplay="false" controls="true"></audio>
        
        <form id="importJinglesIntoCategory" class="importForm" onsubmit="return false;">
            Archive ZIP / fichiers audio contentant les <span class="instructionImportante">jingles</span> à importer dans ce dossier : 
            <input id="inputImportJingleIntoCategory" type="file" name="file" accept=".mp3,.wav,.ogg,.zip" multiple="multiple"/>
            <span class="fa fa-question-circle helpImage" image="FlatMusiqueZip" onclick="displayHelpImage('FlatMusiqueZip')"></span>
            <button class="submitChanges" onClick="uploadJingleCategory(${ JINGLES_CATEGORY_SELECTED.id }); return false;">Envoyer</button>
        </form>
    </header>
    <div id="jingleTable">
        <table>
            <thead>
            <tr>
                <th>Titre</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
                ${ allRowsRendered }
            </tbody>
        </table>
    </div>
    `);
}

function playJingle(path) {
    $('#jingleList audio').attr("src", path);
}

function deleteJingle(id) {
    $.ajax({
        url: '/jingle/' + id,
        type: 'DELETE',
        encoding: "UTF-8",
        dataType: 'json',
        contentType: 'application/json'
    })
    .done(function () {
        $("#jingle_" + id).remove();
        JINGLES_CATEGORY_SELECTED.jingles = JINGLES_CATEGORY_SELECTED.jingles.filter(j => j.id !== id);
        JINGLES_CATEGORIES.filter(cat => cat.id === JINGLES_CATEGORY_SELECTED.id)[0].jingles = JINGLES_CATEGORY_SELECTED.jingles;
    })
    .fail(function (xhr, ajaxOptions, thrownError) {
        console.error(">> Response delete song");
        console.error(thrownError);

        let message = "Erreur lors de la suppression d'un jingle (" + nom + ")";
        if (xhr.responseText != undefined) {
            try {
                let response = JSON.parse(xhr.responseText);
                console.error(response);
                console.error(response.message);
                message += "\n\nException : " + response.message;
            } catch(e) {
                console.error(">> Exception", e);
                message += "\n\n(Impossible de formatter le message d'erreur)";
            }
        }
        alert(message);
    });
}

function deleteCategoryJingle(id) {
    if (!confirm("Voulez-vous supprimer tous les jingles de cette catégorie ?")) {
        return;
    }

    $.ajax({
        url: '/jingleCategory/' + id,
        type: 'DELETE',
        encoding: "UTF-8",
        dataType: 'json',
        contentType: 'application/json'
    })
    .done(function () {
        $("#jingle_category_" + id).remove();
        JINGLES_CATEGORY_SELECTED = null;
        renderJingleMainCategory();
    })
    .fail(function (xhr, ajaxOptions, thrownError) {
        console.error(">> Response delete song");
        console.error(thrownError);

        let message = "Erreur lors de la suppression d'un jingle (" + nom + ")";
        if (xhr.responseText != undefined) {
            try {
                let response = JSON.parse(xhr.responseText);
                console.error(response);
                console.error(response.message);
                message += "\n\nException : " + response.message;
            } catch(e) {
                console.error(">> Exception", e);
                message += "\n\n(Impossible de formatter le message d'erreur)";
            }
        }
        alert(message);
    });
}

function uploadJingles() {
    uploadZip("/jingles", "importJingles", () => {
        $("#inputImportJingle").val("");
        retrieveJingles().then(() => hideLoading());
    });
}

function uploadJingleCategory(categoryId) {
    uploadZip("/jingleCategory/" + categoryId, "importJinglesIntoCategory", () => {
        $("#inputImportJingleIntoCategory").val("");
        retrieveJingles().then(() => hideLoading());
    });
}
