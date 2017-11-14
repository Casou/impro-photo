class IScreen {
    constructor(nom, nextScreen, wsClient) {
        this.nom = nom;
        this.nextScreen = nextScreen;
        this.wsClient = wsClient;

        this.subscriptions();
    }

    init(status) {
        alert(`Init screen not defined for : ${ this.nom }`);
    }

    goToNextScreen() {
        alert(`Go to next screen not defined for : ${ this.nom }`);
    }

    subscriptions() {

    }
}