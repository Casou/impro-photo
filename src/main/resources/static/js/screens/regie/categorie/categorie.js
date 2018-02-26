class Categorie extends IScreen {

    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);

        this.categorie = null;
        this.pictures = [];
        this.animation = null;

        $("#category_selected").removeClass("animated");
        $("#category_selected .animated").removeClass("animated");
    
        this.wsClient.subscribe("/topic/category/selectPicture", (response) => this.animation.selectPicture(JSON.parse(response.body).id));
        this.wsClient.subscribe("/topic/category/unselectPicture", (response) => this.animation.unselectPicture(JSON.parse(response.body).id));
        this.wsClient.subscribe("/topic/category/validateSelection", () => this.animation.validateSelection());
        this.wsClient.subscribe("/topic/category/cancelSelection", () => this.animation.cancelSelection());
        this.wsClient.subscribe("/topic/category/selectAll", () => this.animation.selectAll());
        this.wsClient.subscribe("/topic/category/showPicture", (response) => this.animation.showPicture(JSON.parse(response.body).id));
        this.wsClient.subscribe("/topic/category/backToBlack", () => this.animation.backToBlack());
    
        this.wsClient.subscribe("/topic/polaroid/hideMask", (response) => this.animation.hideMask(JSON.parse(response.body).id));
    }

    init(status, categorie) {
        $('#categorie').fadeIn(ANIMATION_FADE_DURATION);

        if (categorie != undefined) {
            this.categorie = categorie;
            this.loadCategorie();
            this.showCategoriePopin();
        } else {
            this.retrieveCategorie(status)
                .then(() => {
                    if (status.integralite) {
                        this.animation.selectAll(false);
                    } else {
                        this.selectPictures(status.photosChoisies);
                        if (status.photosChoisies.length > 0 && status.statutDiapo == "launched") {
                            this.animation.validateSelection();
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
    
    returnToCategorieList() {
        this.animation.cancelSelection();
        this.wsClient.sendMessage("/app/action/category/returnToList", {});
    }

    goToNextScreen() {
        $('#categorie').fadeOut(ANIMATION_FADE_DURATION, function() {
            this.nextScreen.init(null);
        }.bind(this));
    }

    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/category/returnToList", () => this.goToNextScreen());
    }

    retrieveCategorie(status) {
        return new Promise((resolve, reject) => {
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
                this.loadCategorie().then(() => {
                    resolve();
                });
                this.showCategoriePopin();
            })
            .fail(function (resultat, statut, erreur) {
                console.log(resultat, statut, erreur);
                alert("Erreur lors de la récuparation de la catégorie : " + id);
                reject(erreur);
            })
            .always(function () {
            });
        });
    }

    showCategoriePopin() {
        $('#category_selected_name').html(this.categorie.nom);
        showCategorieSelectedPopin(true);
    }

    loadCategorie() {
        if (this.categorie.type === "POLAROID") {
            this.animation = new CategorieAnimationPolaroid(this.wsClient);
        } else if (this.categorie.type === "TRIPTYQUE") {
            this.animation = new CategorieAnimationTriptyque(this.wsClient);
        } else {
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

    validateSelection() {
        this.wsClient.sendMessage("/app/action/category/validateSelection", {});
    }
    cancelSelection() {
        this.wsClient.sendMessage("/app/action/category/cancelSelection", {});
    }
    selectAll() {
        this.wsClient.sendMessage("/app/action/category/selectAll", {});
    }

    backToBlack() {
        this.wsClient.sendMessage("/app/action/category/backToBlack", {});
    }

}