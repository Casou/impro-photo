class SalleAttenteAnimation {
    constructor() {
        this.images = [];

        this.intervalPointer = null;
        this.intervalNewPhoto = 2500;
        this.indexLoop = 0;
        this.nbDivImages = 10;
        this.imageFadeDuration = 2000;

        this.isAnimationActive = true;
    }

    initAndStart() {
        this.retrieveAllPhotos().then((images) => {
            this.images = images;
            this.initPhotosFlottantes();
            this.startAnimation();

            $("#salle_attente").fadeIn(2000);
        }, function(err) {
            console.error(err);
            alert("Erreur lors de la récupération des photos : " + err);
        });
    }

    retrieveAllPhotos() {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: '/list/allPhotos',
                type: 'GET',
                encoding: "UTF-8",
                dataType: 'json',
                contentType: 'application/json'
            })
            .done(function (allPhotoPaths) {
                let images = [];
                allPhotoPaths.forEach(function(photoPath) {
                    images.push(photoPath);
                });
                resolve(images);
            })
            .fail(function (xhr, ajaxOptions, thrownError) {
                console.error(">> Response save datas");
                console.error(thrownError);

                if (xhr.responseText != undefined) {
                    try {
                        let response = JSON.parse(xhr.responseText);
                        console.error(response);
                        console.error(response.message);
                    } catch(e) {
                        console.error(">> Exception", e);
                    }
                }
                reject();
            })
            .always(function () {
            });
        });
    }


    initPhotosFlottantes() {
        for (let i = 0; i < this.nbDivImages; i++) {
            $('#photosFlottantes').append("<div id=\"photo_" + i + "\" class=\"photo\"><img /></div>");
        }
    }

    startAnimation() {
        this.intervalPointer = setInterval(() => { this.showImage(); }, this.intervalNewPhoto);
    }

    stopAnimation() {
        clearInterval(this.intervalPointer);
        this.intervalPointer = null;
    }

    isAnimationRunning() {
        return this.intervalPointer != null;
    }


    showImage() {
        let randIndex = Math.min(Math.round(Math.random() * this.images.length), this.images.length - 1);
        this.createImage(randIndex, this.indexLoop);
        this.indexLoop = (this.indexLoop + 1) % this.nbDivImages;
    }

    createImage(indexImage, indexDiv) {
        let randTopPercent = Math.round(Math.random() * 80) - 5;
        let randLeftPercent = Math.round(Math.random() * 90) - 10;
        let randRotate = Math.round(Math.random() * 80) - 40;
        let randWidth = 150 + Math.round(Math.random() * 450);

        $('#photo_' + indexDiv + " img").attr('src', this.images[indexImage]).css('width', randWidth).css("height", "auto");
        $('#photo_' + indexDiv).css('top', randTopPercent + "%").css('left', randLeftPercent + "%");
        $('#photo_' + indexDiv).rotate(randRotate).fadeIn(this.imageFadeDuration);
        setTimeout(function() {
            $('#photo_' + indexDiv).fadeOut(this.imageFadeDuration);
        }, 6000);
    }

    loopImages() {
        let randomTimeout = Math.round(Math.random() * this.intervalNewPhoto) + 500;
        let randIndexPhoto = Math.min(Math.round(Math.random() * this.images.length), this.images.length - 1);
        if (this.isAnimationActive) {
            setTimeout(function() {
                this.createImage(randIndexPhoto);
                this.loopImages();
            }, randomTimeout);
        }
    }

}
