
class SalleAttenteScreen extends IScreen {
    constructor(nom, nextScreen) {
        super(nom, nextScreen);
        this.animationStarted = true;
    }

    init(status) {
        $('#salle_attente').show();
        $('#salle_attente main').show();

        $('#salle_attente main button').click(() => this.sendLaunchImpro());
    }

    goToNextScreen(newStatus) {
        $('#salle_attente').fadeOut(5000, (function() {
            SCREENS[this.nextScreen].init(newStatus);
        }).bind(this));
    }

    subscriptions() {
        super.subscriptions();
        WEBSOCKET_CLIENT.subscribe("/topic/salle_attente/launchImpro", () => this.launchImpro());
    }

    launchImpro() {
        this.goToNextScreen();
    }

    sendLaunchImpro() {
        $('#salle_attente main button').attr("disabled", true);
        WEBSOCKET_CLIENT.sendMessage("/app/action/launchImpro", {});
    }

    toggleAnimation() {
        this.animationStarted = !this.animationStarted;
        if (!this.animationStarted) {
            $('#options button#handleAnimation').html("Start animation");
        } else {
            $('#options button#handleAnimation').html("Stop animation");
        }

        WEBSOCKET_CLIENT.sendMessage("/app/action/toggleAnimation", {
            action : this.animationStarted
        });
    }

}