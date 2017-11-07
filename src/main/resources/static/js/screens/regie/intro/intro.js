class Intro extends IScreen {
    constructor(nom, nextScreen) {
        super(nom, nextScreen);
    }

    init(status) {
        $("#intro").fadeIn(5000);
    }

    goToNextScreen() {
    }

    subscriptions() {
        super.subscriptions();
    }

}
