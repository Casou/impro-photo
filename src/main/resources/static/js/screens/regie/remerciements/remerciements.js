class Remerciements extends IScreen {
    
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);
    }

    init(status, categoryList) {
        $("#remerciements").fadeIn(ANIMATION_FADE_DURATION);
        this.retrieveAndAddRemerciements();
    }
    
    sendGoToNextScreen() {
        this.wsClient.sendMessage("/app/action/remerciements/goDates", {
            newScreen : this.nextScreen.nom
        });
    }
    
    goToNextScreen(newStatus) {
        $('#remerciements').fadeOut(ANIMATION_FADE_DURATION, (function() {
            this.nextScreen.init(newStatus);
        }).bind(this));
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
        remerciementsDto.photosJoueurs.forEach((pathPicture, index) => {
            ul.append(
                `<li id="joueursPicture_${ index }">
                    <img src="${ pathPicture }" />
                </li>`);
            $("#joueursPicture_" + index).click(() => this.sendShowJoueurPicture(index));
        });
    
        if (remerciementsDto.texte) {
            $("#remerciement_text p").html(remerciementsDto.texte);
            $("#remerciement_text").click(() => this.sendShowText());
        } else {
            $("#remerciement_text").hide();
        }
        
    }
    
    sendShowJoueurPicture(index) {
        this.wsClient.sendMessage("/app/action/remerciements/showPicture", {
            id : index
        });
    }
    
    sendShowText(index) {
        this.wsClient.sendMessage("/app/action/remerciements/showText", {
            id : index
        });
    }
    
    showJoueurPicture(index) {
        $("#joueursPicture_" + index).css("opacity", 1);
    }
    
    showTexte() {
        $("#remerciement_text").css("opacity", 1);
    }

}