
class SalleAttenteScreen extends IScreen {
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);
        this.animationStarted = true;
    }

    init(status) {
        $('#salle_attente').show();
        $('#salle_attente main').show();

        $('#salle_attente main button').click(() => this.sendLaunchImpro());
    }

    goToNextScreen(newStatus) {
        $('#salle_attente').fadeOut(5000, (function() {
            this.nextScreen.init(newStatus);
        }).bind(this));
    }

    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/salle_attente/launchImpro", () => this.launchImpro());
    }

    launchImpro() {
        this.goToNextScreen();
    }

    sendLaunchImpro() {
        $('#salle_attente main button').attr("disabled", true);
        this.wsClient.sendMessage("/app/action/launchImpro", {
            newScreen : this.nextScreen.nom
        });
    }

    toggleAnimation() {
        this.animationStarted = !this.animationStarted;
        if (!this.animationStarted) {
            $('#options button#handleAnimation').html("Start animation");
        } else {
            $('#options button#handleAnimation').html("Stop animation");
        }

        this.wsClient.sendMessage("/app/action/toggleAnimation", {
            action : this.animationStarted
        });
    }

}