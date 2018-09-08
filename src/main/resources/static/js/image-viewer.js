class ImageViewer {
  
  constructor() {
    this.imageList = [];
    this.discriminant = "";
    this.hidePromiseFunction = null;
  
    $(document).ready(() => {
      $("body").append(`
      <div id="imageViewerWrapper">
          <div id="imageViewerClose" onclick="IMAGE_VIEWER.hide();"><img src="/images/icones/cross-white.png" /></div>
          <div id="imageViewer">
              <section id="imageViewerBigPicture"><img src="" /></section>
              <section id="imageViewerImageList"></section>
          </div>
      </div>
      `);

        $("#imageViewerWrapper").click(() => this.hide());
        $("#imageViewer, #imageViewerClose").click(event => event.stopPropagation());
    });
    $(document).keyup(function(e) {
      if (e.keyCode === 27) { // escape key maps to keycode `27`
        IMAGE_VIEWER.hide();
      }
    });
  }
  
  render() {
    const firstImgSrc = this.imageList.length > 0 ? this.imageList[0].source : "";
    const imageListHtml = this.renderDeleteAll() +
      this.imageList.map((image) => this.renderOneImageBox(image)).join("");
    
    $("#imageViewerBigPicture img").attr("src", firstImgSrc);
    $("#imageViewerImageList").html(imageListHtml);
    $("#imageViewerImageList .imageViewerImage").first().addClass("selected");
  }
  
  setImages(imageList, discriminant) {
    this.discriminant = discriminant;
    this.imageList = imageList;
    this.render();
  }
  
  show(hidePromiseFunction, options = { deleteAllAvailable : true }) {
    this.hidePromiseFunction = hidePromiseFunction;
    if (options.deleteAllAvailable) {
        $("#imageViewerImage__deleteAll").show();
    } else {
        $("#imageViewerImage__deleteAll").hide();
    }
    $("#imageViewerWrapper").show();
  }
  
  hide() {
    if (this.hidePromiseFunction) {
      showLoading();
      this.hidePromiseFunction()
      .catch(function() { alert("Erreur lors de la mise à jour du nombre d'images. Rechargez la page.") })
      .then(function() {
        hideLoading();
        $("#imageViewerWrapper").hide();
      });
    } else {
      $("#imageViewerWrapper").hide();
    }
  }
  
  renderOneImageBox(imageDto) {
    const id = generateRandom(10);
    return `
    <div id="imageViewerImage_${ id }" class="imageViewerBox imageViewerImage">
        <img src="${ imageDto.source }"
                imageId="${ id }"
                nom="${ imageDto.nom }"
                path="${ imageDto.path }"
                onClick="IMAGE_VIEWER.showImage('${ id }', '${ imageDto.source }')" />
        <span class="span-icon icon-cross delete" onClick="IMAGE_VIEWER.deleteImage('${ id }')" title="Supprimer la photo"></span>
    </div>
    `;
  }
  
  renderDeleteAll() {
    return `
    <div id="imageViewerImage__deleteAll" class="imageViewerBox" onClick="IMAGE_VIEWER.deleteAllImages();">
        Tout supprimer
    </div>
    `;
  }
  
  showImage(id, imagePath) {
    $('.imageViewerImage').removeClass("selected");
    $('#imageViewerImage_' + id).addClass("selected");
    $('#imageViewerBigPicture img').attr("src", imagePath);
  }

  deleteImage(divId) {
    if (!confirm("Supprimer cette image ?")) {
      return;
    }
    
    $("#imageViewerImage_" + divId + " span.delete").hide();
    const imageToDelete = {
      nom : $("#imageViewerImage_" + divId + " img").attr("nom"),
      path : $("#imageViewerImage_" + divId + " img").attr("path")
    };
    $.ajax({
      type: 'DELETE',
      url: '/preparation/images',
      data: JSON.stringify(imageToDelete),
      dataType: 'json',
      encoding: "UTF-8",
      contentType: 'application/json'
    })
    .done(() => {
      $("#imageViewerImage_" + divId).fadeOut(400, () => {
        if ($("#imageViewerImage_" + divId).hasClass("selected")) {
          let nextPictureDiv = $("#imageViewerImage_" + divId).next();
          if ($(nextPictureDiv).length === 0) {
            nextPictureDiv = $("#imageViewerImage_" + divId).prev();
          }
          if ($(nextPictureDiv).length === 0 || $(nextPictureDiv).attr("id") === "imageViewerImage__deleteAll") {
            $("#imageViewerBigPicture img").attr("src", "");
          } else {
            const nextPicture = $(nextPictureDiv).find("img").first();
            IMAGE_VIEWER.showImage($(nextPicture).attr("imageId"), $(nextPicture).attr("src"));
          }
        }
        $("#imageViewerImage_" + divId).remove();
        this.imageList = this.imageList.find(image => image.source !== imageToDelete.source);
      });
    })
    .fail(function (resultat, statut, erreur) {
      $("#imageViewerImage_" + divId + " span.delete").show();
      handleAjaxError(resultat, statut, erreur);
    })
    .always(function () {
    });
  }
  
  deleteAllImages() {
    if (!confirm("Supprimer toutes les images ?")) {
      return;
    }
  
    const folderToDelete = { nom : this.discriminant, path : "" };
    showLoading();
    $.ajax({
      type: 'DELETE',
      url: '/preparation/images/all',
      data: JSON.stringify(folderToDelete),
      dataType: 'json',
      encoding: "UTF-8",
      contentType: 'application/json'
    })
    .done(() => {
      $(".imageViewerBox").fadeOut(400, () => {
        $("#imageViewerBigPicture img").attr("src", "");
        IMAGE_VIEWER.imageList = [];
        IMAGE_VIEWER.render();
      });
    })
    .fail(function (resultat, statut, erreur) {
      handleAjaxError(resultat, statut, erreur);
    })
    .always(function () {
      hideLoading();
    });
  }
  
}

const IMAGE_VIEWER = new ImageViewer();
$(document).ready(function () {
  IMAGE_VIEWER.render();
});