const CATEGORIE_ALL_PICTURES_WIDTH = 700;
const CATEGORIE_PICTURE_WIDTH = 140;
const CATEGORIE_PADDING_LEFT = 150;
const CATEGORIE_PADDING_TOP = 30;
const CATEGORIE_ALL_PICTURES_HEIGHT = 530;
const CATEGORIE_PICTURE_HEIGHT = 140;

const CATEGORIE_HEADER_HEIGHT = 100;
const CATEGORIE_SELECTED_ZONE_PADDING_RIGHT = 35;

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
        this.wsClient.subscribe("/topic/category/validateSelection", () => this.validateSelection());
        this.wsClient.subscribe("/topic/category/cancelSelection", () => this.cancelSelection());
    }

    getMaxPictures() {
        return 20;
    }

    setPictures(pictures) {
        this.pictures = pictures;
    }

    showPictures() {
        let html = "";
        let divLeft = CATEGORIE_PADDING_LEFT + ($(window).width() - CATEGORIE_PADDING_LEFT - CATEGORIE_ALL_PICTURES_WIDTH) / 2;
        let divTop = CATEGORIE_PADDING_TOP + (($(window).height() - CATEGORIE_ALL_PICTURES_HEIGHT) / 2);

        $('#categorie div#div_images').css("left", divLeft).css("top", divTop);

        this.pictures.forEach((path, index) => {
            let left = CATEGORIE_PICTURE_WIDTH * (index % 5);
            let top = CATEGORIE_PICTURE_HEIGHT * parseInt(index / 5);
            let idPicture = index + 1;
            html += `
            <div id="picture_${ idPicture }" class="imageWrapper"
                    originalLeft="${ left }" originalTop="${ top }" 
                    style="left : ${ left }px; top : ${ top }px;" onClick="categorie.animation.togglePicture(${ idPicture });">
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
            if (this.selectedIndex.length >= this.getMaxPictures()) {
                return;
            }
            this.wsClient.sendMessage("/app/action/category/selectPicture", { id : idPicture });
        } else {
            this.wsClient.sendMessage("/app/action/category/unselectPicture", { id : idPicture });
        }
    }

    selectPicture(idPicture) {
        $("#picture_" + idPicture).addClass('selected');
        this.selectedIndex.push(idPicture);
        $("#validateSelection").css("opacity", 1);
    }

    unselectPicture(idPicture) {
        $("#picture_" + idPicture).removeClass('selected');
        let indexArray = $.inArray(idPicture, this.selectedIndex);
        this.selectedIndex.splice(indexArray, 1);
        $("#validateSelection").css("opacity", (this.selectedIndex.length == 0) ? 0.5 : 1);
    }

    validateSelection() {
        $('#validateSelection, #selectAll').css("opacity", "0");
        $('#cancelSelect').css("opacity", "1");

        let opacityPromise = new Promise((resolve, reject) => {
            $('div.imageWrapper:not(.selected)').animate({ 'opacity' : 0 }, CATEGORY_ANIMATION_OPACITY_DURATION, () => {
                $('div.imageWrapper:not(.selected)').hide();
                resolve();
            });
        });

        let sectionHeight = $(window).height() - CATEGORIE_HEADER_HEIGHT;
        let leftPosition = $(window).width()
            - $("#div_images").offset().left
            - $('#picture_1 div.pictureNumber').width()
            - CATEGORIE_SELECTED_ZONE_PADDING_RIGHT;
        let oneElementHeight = (sectionHeight / this.selectedIndex.length) - 20;
        let divImagesTop = $("#div_images").offset().top;

        opacityPromise.then(() => {
            this.selectedIndex.forEach((idPicture, idx) => {
                $('#picture_' + idPicture).removeClass('selected');

                let topPosition = CATEGORIE_HEADER_HEIGHT
                    + oneElementHeight * idx
                    + ((oneElementHeight - $('#picture_' + idPicture + ' .pictureOverview').height()) / 2);
                topPosition -= divImagesTop;
                $('#picture_' + idPicture).animate({
                    'top' : topPosition + "px",
                    'left' : leftPosition + 'px',
                }, CATEGORY_ANIMATION_VALIDATE_MOVE_DURATION);
            });
        });
    }

    cancelSelection() {
        $('#validateSelection, #selectAll').css("opacity", "1");
        $('#cancelSelect').css("opacity", "0");

        this.selectedIndex.forEach((idPicture) => {
            let picture = $('#picture_' + idPicture);
            $(picture).removeClass('selected');

            $(picture).animate({
                'top' : picture.attr("originalTop") + "px",
                'left' : picture.attr("originalLeft") + 'px',
            }, CATEGORY_ANIMATION_VALIDATE_MOVE_DURATION);
        });

        setTimeout(() => {
            $('div.imageWrapper').show().animate({ 'opacity' : 1 }, CATEGORY_ANIMATION_OPACITY_DURATION);
        }, CATEGORY_ANIMATION_VALIDATE_MOVE_DURATION);

        this.selectedIndex = [];
    }

}