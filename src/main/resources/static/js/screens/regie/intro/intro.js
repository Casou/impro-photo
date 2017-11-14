class Intro extends IScreen {
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);
        this.videoHandler = new IntroVideosHandler(wsClient);
    }

    init(status) {
        $("#intro").fadeIn(5000);
    }

    sendGoToNextScreen() {
        this.wsClient.sendMessage("/app/action/intro/goCategories", {
            newScreen : this.nextScreen.nom
        });
    }

    goToNextScreen(responseJson) {
        let categoryList = JSON.parse(responseJson.body).categoryList;
        $('#intro').fadeOut(5000, (function() {
            this.nextScreen.init(null, categoryList);
        }).bind(this));
    }

    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/intro/goCategories", (responseJson) => this.goToNextScreen(responseJson));
    }

}

