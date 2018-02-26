const CATEGORIE_ALL_PICTURES_WIDTH = 700;
const CATEGORIE_NB_PICTURES_PER_LINE = 5;
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
        this.selectAllImageOrder = [];
        this.selectionValidated = false;

        $("#categorie_title").html("<h1>Choisir 1 à 5 photos</h1>")
        $("#backToBlack").css("opacity", 1);

        this.wsClient = wsClient;
    }

    getMaxPictures() {
        return 20;
    }

    setPictures(pictures) {
        this.pictures = pictures;

        const nbLines = Math.ceil(this.pictures.length / CATEGORIE_NB_PICTURES_PER_LINE);
        this.selectAllImageOrder = [];
        for (let columnMinusAll = 0; columnMinusAll < CATEGORIE_NB_PICTURES_PER_LINE; columnMinusAll++) {
            for (let line = 1; line <= nbLines; line++) {
                const index = (line * CATEGORIE_NB_PICTURES_PER_LINE) - columnMinusAll;
                if (index <= this.pictures.length) {
                    this.selectAllImageOrder.push(index);
                }
            }
        }
    }

    showPictures() {
        let html = "";
        const divLeft = CATEGORIE_PADDING_LEFT + ($(window).width() - CATEGORIE_PADDING_LEFT - CATEGORIE_ALL_PICTURES_WIDTH) / 2;
        const divTop = CATEGORIE_PADDING_TOP + (($(window).height() - CATEGORIE_ALL_PICTURES_HEIGHT) / 2);

        $('#categorie div#div_images').css("left", divLeft).css("top", divTop);

        this.pictures.forEach((path, index) => {
            const left = CATEGORIE_PICTURE_WIDTH * (index % CATEGORIE_NB_PICTURES_PER_LINE);
            const top = CATEGORIE_PICTURE_HEIGHT * parseInt(index / CATEGORIE_NB_PICTURES_PER_LINE);
            const idPicture = index + 1;
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
        if (!this.selectionValidated) {
            const indexArray = $.inArray(idPicture, this.selectedIndex);
            if (indexArray < 0) {
                if (this.selectedIndex.length >= this.getMaxPictures()) {
                    return;
                }
                this.wsClient.sendMessage("/app/action/category/selectPicture", { id : idPicture });
            } else {
                this.wsClient.sendMessage("/app/action/category/unselectPicture", { id : idPicture });
            }
        } else {
            this.wsClient.sendMessage("/app/action/category/showPicture", { id : idPicture });
        }
    }

    selectPicture(idPicture) {
        $("#picture_" + idPicture).addClass('selected');
        this.selectedIndex.push(idPicture);
        $("#validateSelection").css("opacity", 1).attr("disabled", false);
    }

    unselectPicture(idPicture) {
        $("#picture_" + idPicture).removeClass('selected');
        const indexArray = $.inArray(idPicture, this.selectedIndex);
        this.selectedIndex.splice(indexArray, 1);
        $("#validateSelection").css("opacity", (this.selectedIndex.length == 0) ? 0.5 : 1)
            .attr("disabled", (this.selectedIndex.length == 0));
    }

    validateSelection() {
        $('#validateSelection, #selectAll').css("opacity", "0").attr("disabled", true);
        $('#cancelSelect').css("opacity", "1").attr("disabled", false);
        this.selectionValidated = true;
        $('div#div_one_image').html("").fadeIn(500);

        $('div.imageWrapper:not(.selected)').hide();
        
        const sectionHeight = $(window).height() - CATEGORIE_HEADER_HEIGHT;
        const pictureWidth = $('#picture_1 div.pictureNumber').width();
        const leftPosition = $(window).width()
            - $("#div_images").offset().left
            - pictureWidth
            - CATEGORIE_SELECTED_ZONE_PADDING_RIGHT;
        const oneElementHeight = (sectionHeight / this.selectedIndex.length) - 20;
        const divImagesTop = $("div#div_images").offset().top;

        this.selectedIndex.forEach((idPicture, idx) => {
            $('#picture_' + idPicture).show().removeClass('selected');

            let topPosition = CATEGORIE_HEADER_HEIGHT
                + oneElementHeight * idx
                + ((oneElementHeight - $('#picture_' + idPicture + ' .pictureOverview').height()) / 2);
            topPosition -= divImagesTop;
            $('#picture_' + idPicture).css({
                'top' : topPosition + "px",
                'left' : leftPosition + 'px',
            });
        });
    }

    cancelSelection() {
        $('#validateSelection').css("opacity", "0.5").attr("disabled", true);
        $('#selectAll').css("opacity", "1").attr("disabled", false);
        $('#cancelSelect').css("opacity", "0").attr("disabled", true);
        this.selectionValidated = false;
        $('div#div_one_image, div#div_one_image_small').fadeOut(500);

        this.selectedIndex.forEach((idPicture) => {
            const picture = $('#picture_' + idPicture);
            $(picture).removeClass('selected');

            $(picture).css({
                'top' : picture.attr("originalTop") + "px",
                'left' : picture.attr("originalLeft") + 'px',
            });
        });
  
        $('div.imageWrapper').show().css({ 'opacity' : 1 });
        this.selectedIndex = [];
    }

    selectAll(animation) {
        $('#validateSelection, #selectAll').css("opacity", "0").attr("disabled", true);
        $('#cancelSelect').css("opacity", "1").attr("disabled", false);
        this.selectionValidated = true;
        this.selectedIndex = Object.keys(this.pictures).map(id => parseInt(id) + 1);

        if (animation === undefined || animation) {
            this.selectAllAnimation();
        } else {
            $('div#div_one_image_small').fadeIn(1000);
        }
    }

    selectAllAnimation() {
        $('div.imageWrapper').addClass("selected");

        setTimeout(() => {
            $('div.imageWrapper').removeClass("selected");
            $('div#div_one_image_small').fadeIn(1000);
        }, 10000);
    }

    showPicture(id) {
        $('div.imageWrapper').removeClass("selected");
        $("#picture_" + id).addClass('selected');
        const index = id - 1;
        const isAllSelected = this.selectedIndex.length === this.pictures.length;
        const divSelector = isAllSelected ? 'div#div_one_image_small' : 'div#div_one_image';
        if ($(`${ divSelector } img`).length > 0) {
            $(`${ divSelector } img`).fadeOut(500, () => {
                this.addAndShowPicture(divSelector, index);
            });
        } else {
            this.addAndShowPicture(divSelector, index);
        }
    }

    addAndShowPicture(divSelector, index) {
        $(`${ divSelector }`).html(`<img src="${ this.pictures[index] }" style="display : none" />`);
        $(`${ divSelector } img`).fadeIn(500);
    }

    backToBlack() {
        const isAllSelected = this.selectedIndex.length === this.pictures.length;
        const divSelector = isAllSelected ? 'div#div_one_image_small' : 'div#div_one_image';
        $(`${ divSelector } img`).fadeOut(500, () => $(`${ divSelector }`).html(""));
    }

}