const CATEGORIE_ALL_PICTURES_WIDTH = 750;
const CATEGORIE_PICTURE_WIDTH = 140;
const CATEGORIE_PADDING_TOP = 30;
const CATEGORIE_ALL_PICTURES_HEIGHT = 530;
const CATEGORIE_PICTURE_HEIGHT = 140;

const CATEGORIE_ANIMATIONS = ["bounceIn", "fadeInDown", "zoomInLeft", "flipInY"];

class AbstractCategorieAnimation {

    constructor(wsClient) {
        $('#categorie div#div_images')
            .css("width", CATEGORIE_ALL_PICTURES_WIDTH)
            .css("height", CATEGORIE_ALL_PICTURES_HEIGHT);

        this.selectedIndex = [];

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

        let animation = CATEGORIE_ANIMATIONS[parseInt(Math.random() * CATEGORIE_ANIMATIONS.length)];

        this.pictures.forEach((path, index) => {
            let left = CATEGORIE_PICTURE_WIDTH * (index % 5);
            let top = CATEGORIE_PICTURE_HEIGHT * parseInt(index / 5);
            let idPicture = index + 1;
            let cssClass=`animated ${ animation } `;
            html += `
            <div id="picture_${ idPicture }"  class="imageWrapper ${ cssClass }" style="left : ${ left }px; top : ${ top }px; animation-delay : ${ 2 + index / 8 }s;">
                <div class="pictureNumber">
                    ${ idPicture }
                </div>
            </div>
            `;
        });

        $('#categorie div#div_images').html(html);
    }


    selectPicture(idPicture) {
        $("#picture_" + idPicture).addClass('selected');
    }

    unselectPicture(idPicture) {
        $("#picture_" + idPicture).removeClass('selected');
    }

}