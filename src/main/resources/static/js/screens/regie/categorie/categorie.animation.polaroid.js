class CategorieAnimationPolaroid extends AbstractCategorieAnimation {
    
    constructor(wsClient) {
        super(wsClient);
        $("#categorie_title").html("<h1>Choisir 1 photo</h1>");
        $("#backToBlack").css("opacity", 0);
        $("#selectAll").css("opacity", 0);
        $('div#div_one_image').html("");
    }
    
    getMaxPictures() {
        return 1;
    }
    
    validateSelection() {
        $('#validateSelection, #selectAll').css("opacity", "0").attr("disabled", true);
        $('#cancelSelect').css("opacity", "1").attr("disabled", false);
        this.selectionValidated = true;
    
        const pictureIndex = this.selectedIndex[0] - 1;
        $('div#div_one_image').show().css("opacity", 0)
            .html(`<img src="${ this.pictures[pictureIndex] }" />`);
        $('div.imageWrapper').hide();
        
        setTimeout(() => {
            const divOneImage = $("#div_one_image img");
            const borderWidth = parseInt(divOneImage.css("border-width"));
            const img_width = divOneImage[0].width + (borderWidth * 2);
            const img_height = divOneImage[0].height + (borderWidth * 2);
    
            // On met les masques en fonction de l'image
            $('.mask').css('width', (img_width / 3)).css('height', (img_height / 3)).show();
            $('#mask_1').css('top', divOneImage.position().top).css('left', divOneImage.position().left).click(() => this.sendHideMask(1));
            $('#mask_2').css('top', divOneImage.position().top).css('left', divOneImage.position().left + (img_width / 3)).click(() => this.sendHideMask(2));
            $('#mask_3').css('top', divOneImage.position().top).css('left', divOneImage.position().left + (img_width / 3 * 2)).click(() => this.sendHideMask(3));
            $('#mask_4').css('top', divOneImage.position().top + (img_height / 3)).css('left', divOneImage.position().left).click(() => this.sendHideMask(4));
            $('#mask_5').css('top', divOneImage.position().top + (img_height / 3)).css('left', divOneImage.position().left + (img_width / 3)).click(() => this.sendHideMask(5));
            $('#mask_6').css('top', divOneImage.position().top + (img_height / 3)).css('left', divOneImage.position().left + (img_width / 3 * 2)).click(() => this.sendHideMask(6));
            $('#mask_7').css('top', divOneImage.position().top + (img_height / 3 * 2)).css('left', divOneImage.position().left).click(() => this.sendHideMask(7));
            $('#mask_8').css('top', divOneImage.position().top + (img_height / 3 * 2)).css('left', divOneImage.position().left + (img_width / 3)).click(() => this.sendHideMask(8));
            $('#mask_9').css('top', divOneImage.position().top + (img_height / 3 * 2)).css('left', divOneImage.position().left + (img_width / 3 * 2)).click(() => this.sendHideMask(9));
    
            $('div#div_mask').show();
            $('div#div_one_image').css("opacity", 1);
            
        }, 500);
    
    }
    
    cancelSelection() {
        super.cancelSelection();
        $('div#div_mask').fadeOut(1000);
    }
    
    sendHideMask(maskId) {
        this.wsClient.sendMessage("/app/action/polaroid/hideMask", { id: maskId });
    }
    
    hideMask(maskId) {
        $('#mask_' + maskId).fadeOut(1000);
    }
    
}