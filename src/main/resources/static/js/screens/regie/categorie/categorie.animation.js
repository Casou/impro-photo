const CATEGORIE_ALL_PICTURES_WIDTH = 750;
const CATEGORIE_PICTURE_WIDTH = 140;
const CATEGORIE_PADDING_TOP = 30;
const CATEGORIE_ALL_PICTURES_HEIGHT = 530;
const CATEGORIE_PICTURE_HEIGHT = 140;

class AbstractCategorieAnimation {

    constructor(wsClient) {
        $('#categorie div#div_images')
            .css("width", CATEGORIE_ALL_PICTURES_WIDTH)
            .css("height", CATEGORIE_ALL_PICTURES_HEIGHT);

        this.selectedIndex = [];

        $("#categorie_title").html("<h1>Choisir 1 à 5 photos</h1>")

        this.wsClient = wsClient;
        this.wsClient.subscribe("/topic/category/selectPicture", (response) => this.selectPicture(JSON.parse(response.body).id));
        this.wsClient.subscribe("/topic/category/unselectPicture", (response) => this.unselectPicture(JSON.parse(response.body).id));
    }

    setPictures(pictures) {
        this.pictures = pictures;
    }

    showPictures() {
        let html = "";
        let divLeft = ($(window).width() - CATEGORIE_ALL_PICTURES_WIDTH) / 2;
        let divTop = CATEGORIE_PADDING_TOP + (($(window).height() - CATEGORIE_ALL_PICTURES_HEIGHT) / 2);

        $('#categorie div#div_images').css("left", divLeft).css("top", divTop);

        this.pictures.forEach((path, index) => {
            let left = CATEGORIE_PICTURE_WIDTH * (index % 5);
            let top = CATEGORIE_PICTURE_HEIGHT * parseInt(index / 5);
            let idPicture = index + 1;
            html += `
            <div id="picture_${ idPicture }" class="imageWrapper" style="left : ${ left }px; top : ${ top }px;" onClick="categorie.animation.togglePicture(${ idPicture });">
                <div class="pictureOverview">
                     <img src="${ path }" />
                </div>
                <div class="pictureNumber">
                    ${ idPicture }
                </div>
                <input type="hidden" value="${ path }" id="img_${ idPicture }">
            </div>
            `;
        });

        $('#categorie div#div_images').html(html);
    }

    togglePicture(idPicture) {
        let indexArray = $.inArray(idPicture, this.selectedIndex);
        if (indexArray < 0) {
            this.wsClient.sendMessage("/app/action/category/selectPicture", { id : idPicture });
        } else {
            this.wsClient.sendMessage("/app/action/category/unselectPicture", { id : idPicture });
        }
    }

    selectPicture(idPicture) {
        $("#picture_" + idPicture).addClass('selected');
        this.selectedIndex.push(idPicture);
    }

    unselectPicture(idPicture) {
        $("#picture_" + idPicture).removeClass('selected');
        let indexArray = $.inArray(idPicture, this.selectedIndex);
        this.selectedIndex.splice(indexArray, 1);
    }

}