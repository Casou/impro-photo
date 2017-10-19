<?php 
$pathToPhpRoot = '../../';

include_once $pathToPhpRoot.'constants.php';
include_once $pathToPhpRoot.'constantsPrivate.php';

?>
<section id="intro">
	<video id="video_joueurs" poster="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS_IMAGES ?>poster_noir.jpg" preload="auto">
		<source src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_VIDEO_INTRO ?>joueurs.mp4?<?= generateRandom(20) ?>">
	</video>
	<video id="video_presentateur" poster="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS_IMAGES ?>poster_noir.jpg" preload="auto">
		<source src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_VIDEO_INTRO ?>presentateur.mp4?<?= generateRandom(19) ?>">
	</video>
</section>

<script>

	function init(status) {
		$('section').show();
	}
	
	function manageAction(infosJson) {
		var action = infosJson.action;
		console.log("Manage action");
		console.log(action);
		if (action == 'playVideoJoueur') {
			$('#video_presentateur').hide();
			$('#video_presentateur')[0].pause();
			
			$('#video_joueurs').show();
			$('#video_joueurs')[0].currentTime = 0;
			$('#video_joueurs')[0].play();
		} else if (action == 'playVideoPresentateur') {
			$('#video_joueurs').hide();
			$('#video_joueurs')[0].pause();
			
			$('#video_presentateur').show();
			$('#video_presentateur')[0].currentTime = 0;
			$('#video_presentateur')[0].play();
		} else if (action == 'launchCategories') {
			return launchCategories();
		} else {
			var returnObject = newReturnObject();
			returnObject.status = "KO";
			returnObject.message = "Action inconnue : " + action;
			console.log("Action inconnue : " + action);
			return returnObject;
		}
	}


	function launchCategories() {
		var returnObject = newReturnObject();
		var contentToInsert = null;

		$.ajax({
			type: 'POST', 
			url: 'categorie_liste.php', 
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

		$('section').fadeOut(1500);
		
		setTimeout(function() {
			$('#content').html(contentToInsert);
			$('#currentScreen').val("CATEGORIE_LISTE");
		}, 2000);

		return returnObject;
	}



	$(document).ready(function() {
		init(STATUS);
		
		$('video').on('ended',function(){
			$(this).fadeOut();
		});
	});
</script>