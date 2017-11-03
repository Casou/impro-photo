class IScreen {
    constructor(nom) {
        this.nom = nom;
    }

    init(status) {
        alert(`Screen ${ this.nom } not defined`);
    }
}