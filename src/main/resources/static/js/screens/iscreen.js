class IScreen {
    constructor(nom, nextScreen) {
        this.nom = nom;
        this.nextScreen = nextScreen;

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