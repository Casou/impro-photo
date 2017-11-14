class Categories extends IScreen {
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);
    }

    init(status, categoryList) {
        $("#categories").fadeIn(5000);
        if (categoryList != undefined) {
            this.renderCategoryList(categoryList);
        } else {
            this.retrieveCategoryList();
        }
    }

    goToNextScreen() {
    }

    subscriptions() {
        super.subscriptions();
    }

    retrieveCategoryList() {
        $.ajax({
            url: '/list/categories',
            type: 'GET',
            encoding: "UTF-8",
            dataType: 'json',
            contentType: 'application/json',
            context: this
        })
        .done(function (categoryList) {
            this.renderCategoryList(categoryList);
        })
        .fail(function (resultat, statut, erreur) {
            console.log(resultat, statut, erreur);
            alert("Erreur lors de la récuparation des categories : " + erreur);
        })
        .always(function () {
        });
    }

    renderCategoryList(categoryList) {
        let html = "";
        $(categoryList).each(function(index, category) {
            let cssClass = category.termine ? "termine" : "";
            html += `
            <li>
                <a href="#" class="${ cssClass }" onClick="launchCategorie(${ category.id }); return false;">
                    ${ category.nom }
                </a>            
            </li>
            `;
        });
        $("ul#categoryList").html(html);
    }

}