class CategorieAnimationTriptyque extends AbstractCategorieAnimation {
    
    constructor(wsClient) {
        super(wsClient);
        $("#categorie_title").html("<h1>Choisir 3 photos</h1>")
    }
    
    getMaxPictures() {
        return 3;
    }
    
    validateSelection() {
        $('#validateSelection, #selectAll').css("opacity", "0").attr("disabled", true);
        $('#cancelSelect').css("opacity", "1").attr("disabled", false);
        this.selectionValidated = true;
        $('div#div_one_image').html("").fadeIn(500);
        
        $('div.imageWrapper:not(.selected)').css({'opacity': 0});
        $('div.imageWrapper:not(.selected)').hide();
        
        const firstLeftPosition = $("#div_images").width() * 0.1;
        const firstTopPosition = $("#div_images").height() * 0.6;
        const firstPictureIndex = this.selectedIndex[0];
        $('#picture_' + firstPictureIndex).css({
            'top': firstTopPosition + "px",
            'left': firstLeftPosition + 'px'
        }).removeClass('selected');
        
        const secondLeftPosition = $("#div_images").width() * 0.4;
        const secondTopPosition = $("#div_images").height() * 0.1;
        const secondPictureIndex = this.selectedIndex[1];
        $('#picture_' + secondPictureIndex).css({
            'top': secondTopPosition + "px",
            'left': secondLeftPosition + 'px'
        }).removeClass('selected');
        
        const thirdLeftPosition = $("#div_images").width() * 0.7;
        const thirdTopPosition = firstTopPosition;
        const thirdPictureIndex = this.selectedIndex[2];
        $('#picture_' + thirdPictureIndex).css({
            'top': thirdTopPosition + "px",
            'left': thirdLeftPosition + 'px'
        }).removeClass('selected');
        
    }
    
}