const CATEGORIE_ALL_PICTURES_WIDTH = 700;
const CATEGORIE_NB_PICTURES_PER_LINE = 5;
const CATEGORIE_PICTURE_WIDTH = 140;
const CATEGORIE_PADDING_TOP = 30;
const CATEGORIE_ALL_PICTURES_HEIGHT = 530;
const CATEGORIE_PICTURE_HEIGHT = 140;

const CATEGORIE_ANIMATIONS = ["bounceIn", "fadeInDown", "zoomInLeft", "flipInY"];
const CATEGORIE_HEADER_HEIGHT = 100;
const CATEGORIE_SELECTED_ZONE_PADDING_RIGHT = 35;

class AbstractCategorieAnimation {

    constructor() {
        $('#categorie div#div_images')
            .css("width", CATEGORIE_ALL_PICTURES_WIDTH)
            .css("height", CATEGORIE_ALL_PICTURES_HEIGHT);

        let heightOneImage = $(window).height() * 0.8;
        $('div#div_one_image')
            .css("height", heightOneImage + "px")
            .css("line-height", heightOneImage + "px");

        this.selectedIndex = [];
        this.selectAllImageOrder = [];
        this.animationAllComplexe = true;
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

    showPictures(noDelay) {
        let html = "";
        const divLeft = ($(window).width() - CATEGORIE_ALL_PICTURES_WIDTH) / 2;
        const divTop = CATEGORIE_PADDING_TOP + (($(window).height() - CATEGORIE_ALL_PICTURES_HEIGHT) / 2);

        $('#categorie div#div_images').css("left", divLeft).css("top", divTop).show();

        const animation = CATEGORIE_ANIMATIONS[parseInt(Math.random() * CATEGORIE_ANIMATIONS.length)];

        this.pictures.forEach((path, index) => {
            const left = CATEGORIE_PICTURE_WIDTH * (index % 5);
            const top = CATEGORIE_PICTURE_HEIGHT * parseInt(index / 5);
            const idPicture = index + 1;
            const delay = noDelay ? 0 : 2 + index / 8;
            let cssClass = noDelay ? `animated fadeIn ` : `animated ${ animation } `;

            html += `
            <div id="picture_${ idPicture }"  class="imageWrapper ${ cssClass }"
                originalLeft="${ left }" originalTop="${ top }"
                style="left : ${ left }px; top : ${ top }px; animation-delay : ${ delay }s;">
                <div class="pictureNumber">
                    ${ idPicture }
                </div>
            </div>
            `;
        });

        $('#categorie div#div_images').html(html);
        $('#categorie div#div_images .pictureNumber').each((index, pictureNumber) => {
            $(pictureNumber).attr("originalWidth", $(pictureNumber).width() + "px")
                .attr("originalHeight", $(pictureNumber).height() + "px")
                .attr("originalFontSize", $(pictureNumber).css("font-size"))
                .attr("originalLineHeight", $(pictureNumber).css("line-height"));
        });
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

    validateSelection(animation) {
        const leftPosition = $(window).width()
            - $("#div_images").offset().left
            - $('#picture_1 div.pictureNumber').width()
            - CATEGORIE_SELECTED_ZONE_PADDING_RIGHT;
  
        const sectionHeight = $(window).height() - CATEGORIE_HEADER_HEIGHT;
        const oneElementHeight = (sectionHeight / this.selectedIndex.length) - 20;
        const divImagesTop = $("#div_images").offset().top;
      
        if (animation === false) {
            $('div#div_one_image').html("").show();
            $('div.imageWrapper:not(.selected) .pictureNumber').css('opacity', 0);
  
            this.selectedIndex.forEach((idPicture, idx) => {
                $('#picture_' + idPicture).removeClass('selected');
  
                const topPosition = this.getTopPosition(idx, idPicture, oneElementHeight, divImagesTop);
                $('#picture_' + idPicture).css({
                  'top' : topPosition + "px",
                  'left' : leftPosition + 'px',
                });
            });
          
            return;
        }
        
        $('div#div_one_image').html("").fadeIn(500);

        let opacityPromise = new Promise((resolve) => {
            $('div.imageWrapper:not(.selected) .pictureNumber').animate({ 'opacity' : 0 }, CATEGORY_ANIMATION_OPACITY_DURATION, () => resolve());
        });

        opacityPromise.then(() => {
            this.selectedIndex.forEach((idPicture, idx) => {
                $('#picture_' + idPicture).removeClass('selected');

                const topPosition = this.getTopPosition(idx, idPicture, oneElementHeight, divImagesTop);
                $('#picture_' + idPicture).animate({
                    'top' : topPosition + "px",
                    'left' : leftPosition + 'px',
                }, CATEGORY_ANIMATION_VALIDATE_MOVE_DURATION);
            });
        });
    }
    
    getTopPosition(idx, idPicture, oneElementHeight, divImagesTop) {
        let topPosition = CATEGORIE_HEADER_HEIGHT
            + oneElementHeight * idx
            + ((oneElementHeight - $('#picture_' + idPicture + ' .pictureOverview').height()) / 2);
        topPosition -= divImagesTop;
        return topPosition;
    }

    returnToList() {
        this.selectedIndex.forEach((idPicture) => {
            let picture = $('#picture_' + idPicture);
            $(picture).removeClass('selected');
        
            $(picture).css({
                'top' : picture.attr("originalTop") + "px",
                'left' : picture.attr("originalLeft") + 'px',
            });
        });
        this.selectedIndex = [];
        $('div#div_one_image').fadeOut(CATEGORY_ANIMATION_OPACITY_DURATION, () => $('div#div_one_image').html(""));
    }
    
    cancelSelection() {
        const isAllSelected = this.selectedIndex.length === this.pictures.length;

        $('div#div_one_image').fadeOut(500);
        $("div.imageWrapper .pictureNumber").each((index, pictureNumber) => {
            $(pictureNumber).css({
                "width": $(pictureNumber).attr("originalWidth"),
                "height": $(pictureNumber).attr("originalHeight"),
                "font-size" : $(pictureNumber).attr("originalFontSize"),
                "line-height" : $(pictureNumber).attr("originalLineHeight"),
            });

        });

        const delay = isAllSelected ? 0 : CATEGORY_ANIMATION_VALIDATE_MOVE_DURATION;
        if (!isAllSelected) {
            this.selectedIndex.forEach((idPicture) => {
                let picture = $('#picture_' + idPicture);
                $(picture).removeClass('selected');

                $(picture).animate({
                    'top' : picture.attr("originalTop") + "px",
                    'left' : picture.attr("originalLeft") + 'px',
                }, delay);
            });
        } else {
            this.selectedIndex.forEach((idPicture) => {
                let picture = $('#picture_' + idPicture);
                $(picture).removeClass('selected');
        
                $(picture).css({
                    'top' : picture.attr("originalTop") + "px",
                    'left' : picture.attr("originalLeft") + 'px',
                });
            });
        }

        setTimeout(() => {
            $('div.imageWrapper').show().animate({ 'opacity' : 1 }, CATEGORY_ANIMATION_OPACITY_DURATION);
            $('div.imageWrapper .pictureNumber').show().animate({ 'opacity' : 1 }, CATEGORY_ANIMATION_OPACITY_DURATION);
        }, delay);

        this.selectedIndex = [];
    }

    selectAll(animation) {
        $('div.imageWrapper').addClass("selected");
        this.selectedIndex = Object.keys(this.pictures).map(id => parseInt(id) + 1);

        if (animation === undefined || animation) {
            if (this.animationAllComplexe) {
                this.selectAllAnimationComplexe();
            } else {
                this.selectAllAnimationSimple();
            }
        } else {
            $('div#div_one_image').show();
            $('div.imageWrapper').hide().removeClass("selected");
        }
    }

    selectAllAnimationSimple() {
        setTimeout(function() {
            $('div.imageWrapper')
                .fadeOut(1000, () => {
                    $('div.imageWrapper').removeClass("selected");
                    $('div#div_one_image').html("").fadeIn(500);
                });
        }, 500);
    }

    selectAllAnimationComplexe() {
        const promiseLogo = new Promise((resolve) => {
            $('div#div_selectAllTarget')
                .delay(CATEGORY_ANIMATION_VALIDATE_MOVE_DURATION)
                .animate({'right': '-45px'}, CATEGORY_ANIMATION_VALIDATE_MOVE_DURATION, () => resolve())
        });

        const divAllTargetWidth = $('div#div_selectAllTarget').width();
        const divImagesTop = $("div#div_images").offset().top;
        const topValue = $("div#div_selectAllTarget").offset().top
            - divImagesTop
            + $("div#div_selectAllTarget").height() * 0.28;
        promiseLogo.then(() => {
            $(this.selectAllImageOrder).each(function(index, imageIndex) {
                setTimeout(() => {
                    this.movePictureNumberToSelectAllTarget(imageIndex, topValue);
                }, index * 200);
            }.bind(this));
            setTimeout(() => {
                $('div#div_selectAllTarget')
                    .animate({'right': `-${divAllTargetWidth}px`}, CATEGORY_ANIMATION_VALIDATE_MOVE_DURATION, () => {
                        $('div.imageWrapper .pictureNumber').css("opacity", 0);
                    });
                $('div#div_one_image').html("").fadeIn(500);
            }, this.selectAllImageOrder.length * 200 + 2000 + 500);
        });
    }

    movePictureNumberToSelectAllTarget(imageIndex, topValue) {
        const leftValue = $(window).width();
        $('div.imageWrapper#picture_' + imageIndex)
            .delay(500)
            .animate({
                'top' : topValue + 'px'
            }, 1000, function() {
                $('div.imageWrapper#picture_' + imageIndex)
                    .animate({
                        'left' : leftValue + 'px'
                    }, 1000);
            });


        $('div.imageWrapper#picture_' + imageIndex + ' .pictureNumber')
            .animate({
                'height': '50px',
                'width': '50px',
                'font-size' : '30px',
                'line-height' : '50px',
            }, 1500);
    }

    showPicture(id) {
        $('div.imageWrapper').removeClass("selected");
        $("#picture_" + id).addClass('selected');
        let index = id - 1;
        if ($('div#div_one_image img').length > 0) {
            $('div#div_one_image img').fadeOut(500, () => {
                this.addAndShowPicture(index);
            });
        } else {
            this.addAndShowPicture(index);
        }
    }

    addAndShowPicture(index) {
        $('div#div_one_image').html(`<img src="${ this.pictures[index] }" style="display : none" />`);
        $('div#div_one_image img').fadeIn(500);
    }

    backToBlack() {
        $('div#div_one_image img').fadeOut(500, () => $('div#div_one_image').html(""));
    }

}