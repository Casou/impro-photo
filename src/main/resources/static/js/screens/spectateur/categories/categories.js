class Categories extends IScreen {
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);
    }

    init(status, categoryList) {
        $("#categories").fadeIn(ANIMATION_FADE_DURATION);
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
        this.wsClient.subscribe("/topic/category_list/showNextCategory", () => this.showNextCategory());
        this.wsClient.subscribe("/topic/category_list/showAllCategories", () => this.showAllCategories());
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
            <li id="categorie_${index}" class="hidden">
                <a href="#" class="${ cssClass }" onClick="launchCategorie(${ category.id }); return false;">
                    ${ category.nom }
                </a>            
            </li>
            `;
        });
        $("ul#categoryList").html(html);
    }

    showNextCategory() {
        $(".hidden").first().removeClass("hidden").addClass("animated flipInY");
    }

    showAllCategories() {
        $("li.hidden").each(function(index, li) {
            $(li).removeClass("hidden").addClass("animated flipInY").css("animation-delay", (index * 0.2) + "s");
        });
    }

}