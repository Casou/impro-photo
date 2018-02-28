class Dates extends IScreen {
    
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);
    }
    
    init(status) {
        $("#dates").fadeIn(ANIMATION_FADE_DURATION);
        this.retrieveAndAddDates();
        this.retrieveAndAddPhotosDates();
    }
    
    sendGoToNextScreen() {
        this.wsClient.sendMessage("/app/action/dates/goIntro", {
            newScreen : this.nextScreen.nom
        });
    }
    
    goToNextScreen() {
        $('#dates').fadeOut(ANIMATION_FADE_DURATION, function() {
            this.nextScreen.init(null);
        }.bind(this));
    }
    
    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/dates/goIntro", (response) => this.goToNextScreen(JSON.parse(response.body)));
    }
    
    retrieveAndAddDates() {
        $.ajax({
            url: '/list/dates',
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            contentType: 'application/json',
            context: this
        })
        .done(function (listDatesDto) {
            this.renderDates(listDatesDto);
        })
        .fail(function (resultat, statut, erreur) {
            console.log(resultat, statut, erreur);
            alert("Erreur lors de la récuparation des infos de remerciements : " + erreur);
        })
        .always(function () {
        });
    }
    
    renderDates(listDatesDto) {
        const div = $("#div_dates");
        listDatesDto.forEach((dateDto) => {
            const date = new Date(dateDto.date);
            div.append(
                `<div class="date">
                    <figure>
                        <header>
                            ${ getMonthName(date.getMonth()) }
                        </header>
                        <section>
                            ${ date.getDate() }
                        </section>
                    </figure>
                    <label>
                        ${ dateDto.nom }
                    </label>
                </div>
                `);
        });
    }
    
    retrieveAndAddPhotosDates() {
        $.ajax({
            url: '/list/photosDates',
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            contentType: 'application/json',
            context: this
        })
        .done(function (listPhotos) {
            this.renderPhotos(listPhotos);
        })
        .fail(function (resultat, statut, erreur) {
            console.log(resultat, statut, erreur);
            alert("Erreur lors de la récuparation des infos de remerciements : " + erreur);
        })
        .always(function () {
        });
    }
    
    renderPhotos(listPhotos) {
        const div = $("#img_impros");
        listPhotos.forEach((photoPath, index) => {
            const randRotate = Math.round(Math.random() * 40) - 20;
            div.append(
                `<img src="${ photoPath }" style="top : ${ (index * 170) - 50 }px;  transform: rotate(${ randRotate }deg);" />`
            );
        });
    }
    
}