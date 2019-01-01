class CategorieList extends IScreen {
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);
        this.categoryScreen = null;
    }

    init(status, categoryList) {
        $("#categorie_list").fadeIn(ANIMATION_FADE_DURATION);
        if (categoryList) {
            this.renderCategoryList(categoryList);
        } else {
            this.retrieveCategoryList();
        }
    }

    goToNextScreen(categorie) {
        $('#categorie_list').fadeOut(ANIMATION_FADE_DURATION, function() {
            this.nextScreen.init(null, categorie);
        }.bind(this));
    }
    
    launchCategorieScreen(categorie) {
        $('#categorie_list').fadeOut(ANIMATION_FADE_DURATION, function() {
            this.categoryScreen.init(null, categorie);
        }.bind(this));
    }

    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/category_list/showNextCategory", (dto) => this.showNextCategory(dto));
        this.wsClient.subscribe("/topic/category_list/showAllCategories", () => this.showAllCategories());
        this.wsClient.subscribe("/topic/category_list/launchCategorie", (response) => this.launchCategorieScreen(JSON.parse(response.body)));
        this.wsClient.subscribe("/topic/category_list/goRemerciements", (response) => this.goToNextScreen(JSON.parse(response.body)));
    }

    retrieveCategoryList() {
        $.ajax({
            url: '/categories',
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
            alert("Erreur lors de la récuparation des categorie_list : " + erreur);
        })
        .always(function () {
        });
    }

    renderCategoryList(categoryList) {
        let html = "";
        $(categoryList).each(function(index, category) {
            let cssClass = category.termine ? "termine" : "";
            html += `
            <li id="categorie_${index}" class="categorie ${ !STATUS.categoriesShown && "hidden" }">
                <a href="#" class="${ cssClass }">
                    ${ category.nom }
                </a>            
            </li>
            `;
        });
        $("ul#categoryList").html(html);
    }

    showNextCategory(dto) {
        $("li.categorie.hidden").first().removeClass("hidden").addClass("animated flipInY");
        STATUS.categoriesShown = STATUS.categoriesShown || dto.lastCategorie;
    }

    showAllCategories() {
        $("li.categorie.hidden").each(function(index, li) {
            $(li).removeClass("hidden").addClass("animated flipInY").css("animation-delay", (index * 0.2) + "s");
        });
        STATUS.categoriesShown = true;
    }

}