class Categorie extends IScreen {

    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);

        this.categorie = null;
        this.pictures = [];
        this.animation = null;
    
        this.wsClient.subscribe("/topic/category/selectPicture", (response) => this.animation.selectPicture(JSON.parse(response.body).id));
        this.wsClient.subscribe("/topic/category/unselectPicture", (response) => this.animation.unselectPicture(JSON.parse(response.body).id));
        this.wsClient.subscribe("/topic/category/validateSelection", () => this.animation.validateSelection());
        this.wsClient.subscribe("/topic/category/cancelSelection", () => this.animation.cancelSelection());
        this.wsClient.subscribe("/topic/category/selectAll", () => this.animation.selectAll());
        this.wsClient.subscribe("/topic/category/showPicture", (response) => this.animation.showPicture(JSON.parse(response.body).id));
        this.wsClient.subscribe("/topic/category/backToBlack", () => this.animation.backToBlack());
        
        this.wsClient.subscribe("/topic/polaroid/hideMask", (response) => this.animation.hideMask(JSON.parse(response.body).id));

        this.wsClient.subscribe("/topic/playAppareilPhoto", () => $("#photoSoundAudio")[0].play());
    }

    init(status, categorie) {
        $('#categorie').fadeIn(ANIMATION_FADE_DURATION);

        if (categorie !== undefined) {
            this.categorie = categorie;
            this.loadCategorie();
            this.showCategoriePopin();
        } else {
            this.retrieveCategorie(status, true)
                .then(() => {
                    if (status.integralite) {
                        this.animation.selectAll(false);
                    } else {
                        this.selectPictures(status.photosChoisies);
                        if (status.photosChoisies.length > 0 && status.statutDiapo == "launched") {
                            this.animation.validateSelection(false);
                        }
                    }
                    if (status.photoCourante != null) {
                        this.animation.showPicture(status.photoCourante);
                    }
                    if (status.blockMasques && status.blockMasques.length > 0) {
                        status.blockMasques.forEach((maskId) => this.animation.hideMask(maskId));
                    }
                });
        }
    }

    goToNextScreen() {
        this.animation.returnToList();
        $('#categorie').fadeOut(ANIMATION_FADE_DURATION, function() {
            this.nextScreen.init(null);
        }.bind(this));
    }

    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/category/returnToList", () => this.goToNextScreen());
    }

    retrieveCategorie(status, noDelay) {
        return new Promise((resolve) => {
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
                this.loadCategorie(noDelay).then(() => {
                    resolve();
                });
                this.showCategoriePopin();
            })
            .fail(function (resultat, statut, erreur) {
                console.log(resultat, statut, erreur);
                alert("Erreur lors de la récuparation de la catégorie : " + id);
            })
            .always(function () {
            });
        });
    }

    showCategoriePopin() {
        $('#category_selected_name').html(this.categorie.nom);
        showCategorieSelectedPopin(true);
    }

    loadCategorie(noDelay) {
        if (this.categorie.type === "POLAROID") {
            this.animation = new CategorieAnimationPolaroid(this.wsClient);
        } else {
            this.animation = new CategorieAnimationNormal(this.wsClient);
        }

        let promise = new Promise((resolve, reject) => {
            this.retrieveCategoriePictures(this.categorie.pathFolder, resolve, reject);
        });
        promise.then(() => this.animation.showPictures(noDelay));
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
        pictureList.forEach(function(pictureId) {
            this.animation.selectPicture(pictureId);
        }.bind(this));
    }

}