const ANIMATION_FADE_OUT = 4000;

class SalleAttenteScreen extends IScreen {
    constructor(nom, nextScreen) {
        super(nom, nextScreen);

        this.animation = new SalleAttenteAnimation();
    }

    init(status) {
        this.animation.initAndStart();
    }

    goToNextScreen(newStatus) {
        $('#salle_attente').fadeOut(5000, (function() {
            SCREENS[this.nextScreen].init(newStatus);
        }).bind(this));
    }

    subscriptions() {
        super.subscriptions();
        WEBSOCKET_CLIENT.subscribe("/topic/salle_attente/launchImpro", () => this.launchImpro());
        WEBSOCKET_CLIENT.subscribe("/topic/salle_attente/toggleAnimation", (response) => this.toggleAnimation(response));
    }

    launchImpro() {
        this.goToNextScreen();
    }

    toggleAnimation(responseJson) {
        let response = JSON.parse(responseJson.body);
        if (response.message === "false") {
            this.animation.stopAnimation();
        } else {
            this.animation.startAnimation();
        }
    }
}
