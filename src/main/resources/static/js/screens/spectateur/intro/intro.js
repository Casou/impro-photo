class Intro extends IScreen {
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);
        this.videoHandler = new IntroVideosHandler();
    }

    init(status) {
        $("#intro").fadeIn(5000);
        $("html").addClass("black");
    }

    goToNextScreen(responseJson) {
        let categoryList = JSON.parse(responseJson.body).categoryList;
        $('#intro').fadeOut(5000, (function() {
            $("html").removeClass("black");
            this.nextScreen.init(null, categoryList);
        }).bind(this));
    }

    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/intro/playVideo", (responseJson) => this.videoHandler.playVideo(JSON.parse(responseJson.body).label));
        this.wsClient.subscribe("/topic/intro/pauseVideo", (responseJson) => this.videoHandler.pauseVideo(JSON.parse(responseJson.body).label));
        this.wsClient.subscribe("/topic/intro/stopVideo", (responseJson) => this.videoHandler.stopVideo(JSON.parse(responseJson.body).label));
        this.wsClient.subscribe("/topic/intro/goCategories", (responseJson) => this.goToNextScreen(responseJson));
    }

}

