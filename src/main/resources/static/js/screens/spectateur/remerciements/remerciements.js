class Remerciements extends IScreen {
    
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);
    }

    init(status,) {
        $("#remerciements").fadeIn(ANIMATION_FADE_DURATION);
        this.retrieveAndAddRemerciements();
    }

    goToNextScreen() {
        $('#remerciements').fadeOut(ANIMATION_FADE_DURATION, function() {
            this.nextScreen.init(null);
        }.bind(this));
    }

    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/remerciements/goDates", (response) => this.goToNextScreen(JSON.parse(response.body)));
        this.wsClient.subscribe("/topic/remerciements/showPicture", (response) => this.showJoueurPicture(JSON.parse(response.body).id));
        this.wsClient.subscribe("/topic/remerciements/showText", () => this.showTexte());
    }
    
    retrieveAndAddRemerciements() {
        $.ajax({
            url: '/remerciementsInfo',
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            contentType: 'application/json',
            context: this
        })
        .done(function (remerciementsDto) {
            this.renderRemerciements(remerciementsDto);
        })
        .fail(function (resultat, statut, erreur) {
            console.log(resultat, statut, erreur);
            alert("Erreur lors de la récuparation des infos de remerciements : " + erreur);
        })
        .always(function () {
        });
    }
    
    renderRemerciements(remerciementsDto) {
        const ul = $("#div_joueurs ul");
        $(ul).html("");
        remerciementsDto.photosJoueurs.forEach((pathPicture, index) => {
            ul.append(
                `<li id="joueursPicture_${ index }">
                    <img src="${ pathPicture }" />
                </li>`);
            $("#joueursPicture_" + index).click(() => this.sendShowJoueurPicture(index));
        });
    
        $("#remerciement_text p").html(remerciementsDto.texte);
    }
    
    showJoueurPicture(index) {
        $("#joueursPicture_" + index).animate({ opacity : 1 }, ANIMATION_FADE_DURATION);
    }
    
    showTexte() {
        $("#remerciement_text").animate({ opacity : 1 }, ANIMATION_FADE_DURATION);
    }
    
}