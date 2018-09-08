class CategorieAnimationPolaroid extends CategorieAnimationNormal {
    
    constructor() {
        super();
        $('div#div_one_image').html("");
    }
    
    validateSelection() {
        const pictureIndex = this.selectedIndex[0] - 1;
        $('div#div_one_image').show().css("opacity", 0);
        $('div#div_one_image').html(`<img src="${ this.pictures[pictureIndex] }" />`);
    
        $('div#div_images').fadeOut(1000, function() {
            const divOneImage = $('#div_one_image img');
            const borderWidth = parseInt(divOneImage.css("border-width"));
            const img_width = divOneImage[0].width + (borderWidth * 2) + 5;
            const img_height = divOneImage[0].height + (borderWidth * 2) + 5;
            
            // On met les masques en fonction de l'image
            $('.mask').css('width', (img_width / 3)).css('height', (img_height / 3)).show();
            $('#mask_1').css('top', divOneImage.position().top).css('left', divOneImage.position().left);
            $('#mask_2').css('top', divOneImage.position().top).css('left', divOneImage.position().left + (img_width / 3));
            $('#mask_3').css('top', divOneImage.position().top).css('left', divOneImage.position().left + (img_width / 3 * 2));
            $('#mask_4').css('top', divOneImage.position().top + (img_height / 3)).css('left', divOneImage.position().left);
            $('#mask_5').css('top', divOneImage.position().top + (img_height / 3)).css('left', divOneImage.position().left + (img_width / 3));
            $('#mask_6').css('top', divOneImage.position().top + (img_height / 3)).css('left', divOneImage.position().left + (img_width / 3 * 2));
            $('#mask_7').css('top', divOneImage.position().top + (img_height / 3 * 2)).css('left', divOneImage.position().left);
            $('#mask_8').css('top', divOneImage.position().top + (img_height / 3 * 2)).css('left', divOneImage.position().left + (img_width / 3));
            $('#mask_9').css('top', divOneImage.position().top + (img_height / 3 * 2)).css('left', divOneImage.position().left + (img_width / 3 * 2));
            
            $('div#div_mask').fadeIn(1000, () => {
                $('div#div_one_image').animate({ opacity : 1 }, 1000);
            });
            
        });
    }
    
    returnToList() {
        super.returnToList();
        $('div#div_one_image').html("");
        $('div#div_mask').fadeOut(1000);
    }
    
    cancelSelection() {
        super.cancelSelection();
        $('div#div_one_image').html("");
        $('div#div_mask').fadeOut(1000);
        $('div#div_images').fadeIn(1000);
    }
    
    hideMask(maskId) {
        $('#mask_' + maskId).fadeOut(1000);
        $('#polaroidSoundAudio')[0].play();
    }
    
}