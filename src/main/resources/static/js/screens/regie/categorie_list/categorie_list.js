class CategorieList extends IScreen {
    constructor(nom, nextScreen, wsClient) {
        super(nom, nextScreen, wsClient);

        this.categories = [];
    }

    init(status, categoryList) {
        $("#categorie_list").fadeIn(ANIMATION_FADE_DURATION);
        if (categoryList != undefined) {
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

    subscriptions() {
        super.subscriptions();
        this.wsClient.subscribe("/topic/category_list/launchCategorie", (response) => this.goToNextScreen(JSON.parse(response.body)));
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
        this.categories = [];
        let html = "";
        categoryList.forEach(function(category, index) {
            let cssClass = category.termine ? "termine" : "";
            html += `
            <li id="categorie_${index}" index="${index}" class="transparent" style="animation-delay : ${ 3 + (index / 10) }s;">
                <a href="#" class="${ cssClass }" onClick="categories.launchCategorie(${ category.id }); return false;">
                    ${ category.nom }
                </a>            
            </li>
            `;
            this.categories.push(`categorie_${index}`);
        }.bind(this));
        $("ul#categoryList").html(html);
    }

    showNextCategory() {
        let nextCategory = $(".transparent").first();
        if (nextCategory != null && nextCategory.length > 0) {
            $(nextCategory).removeClass("transparent");
        }
        this.wsClient.sendMessage("/app/action/showNextCategory", { });
    }

    showAllCategories() {
        $("li.transparent").each(function(index, li) {
            setTimeout(() => { $(li).removeClass("transparent"); }, index * 0.2 * 1000);
        });
        this.wsClient.sendMessage("/app/action/showAllCategories", { });
    }

    launchCategorie(id) {
        this.wsClient.sendMessage("/app/action/launchCategorie", { id : id });
    }

}