<?php 
$pathToPhpRoot = '../../';

include_once $pathToPhpRoot.'constants.php';
include_once $pathToPhpRoot.'constantsPrivate.php';

?>
<section id="salle_attente">
	<header>
		<img src="<?= APPLICATION_ABSOLUTE_URL ?>style/images/Git_logo_large.png" />
	</header>
	
	<main style="display : none">
		<button>Impro photo</button>
	</main>

	<div id="photosFlottantes">	
	</div>
</section>
	
	<script>
		var ANIMATION_FADE_OUT = 4000;
		function init(status) {
			$('section').fadeIn(2000);
		}


		function manageAction(infosJson) {
			var action = infosJson.action;
			console.log("Manage action");
			console.log(action);
			if (action == 'launchImpro') {
				return launchImpro();
			} else {
				var returnObject = newReturnObject();
				returnObject.status = "KO";
				returnObject.message = "Action inconnue : " + action;
				console.log("Action inconnue : " + action);
				return returnObject;
			}
		}

		var NB_DIV_IMAGES = 10;
		var INTERVAL_NEW_PHOTO = 1500;
		var INTERVAL_POINTER;
		var IMAGES = [
			<?php 
				$folder = $pathToPhpRoot.FOLDER_PHOTO_INTRO;
				$myDirectory = opendir($folder) or die('Erreur');
				while(false !== ($entry = @readdir($myDirectory))) {
					if($entry != '.' && $entry != '..') {
				?>
				"<?= $pathToPhpRoot.FOLDER_PHOTO_INTRO.$entry ?>",
				<?php
					}
				}
			?>
		];

		function initPhotosFlottantes() {
			for (var i = 0; i < NB_DIV_IMAGES; i++) {
				$('#photosFlottantes').append("<div id=\"photo_" + i + "\" class=\"photo\"><img /></div>");
			}
		}

		function startAnimation() {
			INTERVAL_POINTER = setInterval(function() { showImage(); }, INTERVAL_NEW_PHOTO);
		}

		function stopAnimation() {
			clearInterval(INTERVAL_POINTER);
			INTERVAL_POINTER = null;
		}

		function isAnimationRunning() {
			return INTERVAL_POINTER != null;
		}
		

		var INDEX_LOOP = 0;
		function showImage() {
		    var randIndex = Math.min(Math.round(Math.random() * IMAGES.length), IMAGES.length - 1);
			createImage(randIndex, INDEX_LOOP);
			INDEX_LOOP = (INDEX_LOOP + 1) % NB_DIV_IMAGES;
		}

		function createImage(indexImage, indexDiv) {
			var randTopPercent = Math.round(Math.random() * 80) - 5;
			var randLeftPercent = Math.round(Math.random() * 90) - 10;
			while (randLeftPercent > 20 && randLeftPercent < 60 && randTopPercent < 45) {
				randTopPercent = Math.round(Math.random() * 80) - 5;
				randLeftPercent = Math.round(Math.random() * 90) - 10;
			}
			var randRotate = Math.round(Math.random() * 80) - 40;
			var randWidth = 150 + Math.round(Math.random() * 450);

			$('#photo_' + indexDiv + " img").attr('src', IMAGES[indexImage]).css('width', randWidth).css("height", "auto");
			$('#photo_' + indexDiv).css('top', randTopPercent + "%").css('left', randLeftPercent + "%");
			$('#photo_' + indexDiv).rotate(randRotate).fadeIn(500);
			setTimeout(function() {
				$('#photo_' + indexDiv).fadeOut(500);
		    }, 4000);
		}

		function loopImages() {
		    var rand = Math.round(Math.random() * INTERVAL) + 500;
		    var randIndex = Math.min(Math.round(Math.random() * IMAGES.length), IMAGES.length - 1);
		    if (LOOP_CREATE_IMAGE) {
			    setTimeout(function() {
					createImage(randIndex);
					loopImages();  
			    }, rand);
		    }
		}

		

		function launchImpro() {
			var returnObject = newReturnObject();
			var contentToInsert = null;

			$.ajax({
				type: 'POST', 
				url: 'intro.php', 
				dataType : 'html',
				async : false,
				success: function(data, textStatus, jqXHR) {
					// clearInterval(CLEAR_IMAGE_INTERVAL);
					// LOOP_CREATE_IMAGE = false;
					contentToInsert = data;
					
					returnObject.status = "OK";
					returnObject.message = "";
				},
				error: function(jqXHR, textStatus, errorThrown) {
					// alert("Une erreur est survenue : \n" + jqXHR.responseText);
					console.log(jqXHR);
					returnObject.status = "KO";
					returnObject.message = jqXHR.responseText;
				}
			});

			// $('section').addClass('animate_intro');
			$('section').fadeOut(ANIMATION_FADE_OUT);
			
			setTimeout(function() {
				stopAnimation();
				$('#content').html(contentToInsert);
				$('#currentScreen').val("INTRO");
			}, ANIMATION_FADE_OUT + 500);

			return returnObject;
		}

		$(document).ready(function() {
			initPhotosFlottantes();
			startAnimation();
			
			init(STATUS);
			$('#watermark').hide();
		});

	</script>

