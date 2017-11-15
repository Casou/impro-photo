
class SalleAttenteScreen extends IScreen {
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);

        this.animation = new SalleAttenteAnimation();
    }

    init(status) {
        this.animation.initAndStart();
    }

    goToNextScreen(newStatus) {
        $('#salle_attente').fadeOut(ANIMATION_FADE_DURATION, (function() {
            this.nextScreen.init(newStatus);
        }).bind(this));
    }

    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/salle_attente/launchImpro", () => this.launchImpro());
        this.wsClient.subscribe("/topic/salle_attente/toggleAnimation", (response) => this.toggleAnimation(response));
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
