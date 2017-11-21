class Categorie extends IScreen {

    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);

        this.categorie = null;
        this.pictures = [];
        this.animation = null;
    }

    init(status, categorie) {
        $('#categorie').fadeIn(ANIMATION_FADE_DURATION);

        if (categorie != undefined) {
            this.categorie = categorie;
            this.loadCategorie();
            this.showCategoriePopin();
        } else {
            this.retrieveCategorie(status);
        }
    }

    goToNextScreen() {
    }

    subscriptions() {
        super.subscriptions();
    }

    retrieveCategorie(status) {
        $.ajax({
            url: '/categories/search/by-id?id=' + status.idCategorie,
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            contentType: 'application/json',
            context: this
        })
        .done(function (category) {
            this.categorie = category;
            let loadCategoriesPromise = this.loadCategorie();
            this.showCategoriePopin();

            loadCategoriesPromise.then(() => this.selectPictures(status.photosChoisies));
        })
        .fail(function (resultat, statut, erreur) {
            console.log(resultat, statut, erreur);
            alert("Erreur lors de la récuparation de la catégorie : " + id);
        })
        .always(function () {
        });
    }

    showCategoriePopin() {
        $('#category_selected_name').html(this.categorie.nom);
        toggleCategorie();
    }

    loadCategorie() {
        if (this.categorie.type == "PHOTO") {
            this.animation = new CategorieAnimationNormal(this.wsClient);
        }

        let promise = new Promise((resolve, reject) => {
            this.retrieveCategoriePictures(this.categorie.pathFolder, resolve, reject);
        });
        promise.then(() => this.animation.showPictures());
        return promise;
    }

    retrieveCategoriePictures(path, resolve, reject) {
        this.pictures = [];
        $.ajax({
            url: '/categories/getPictures',
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            data: "pathFolder=" + path,
            contentType: 'application/json',
            context: this
        })
        .done(function (picturePaths) {
            this.animation.setPictures(picturePaths);
            this.animation.showPictures();
            resolve();
        })
        .fail(function (resultat, statut, erreur) {
            console.log(resultat, statut, erreur);
            alert("Erreur lors de la récuparation de la catégorie : " + id);
            reject(erreur);
        })
        .always(function () {
        });
    }

    selectPictures(pictureList) {
        console.log("selectPictures", pictureList);
        pictureList.forEach(function(pictureId) {
            this.animation.selectPicture(pictureId);
        }.bind(this));
    }

}