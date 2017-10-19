<?php 
$pathToPhpRoot = '../../';

include_once $pathToPhpRoot.'constants.php';
include_once $pathToPhpRoot.'constantsPrivate.php';

?>
<section id="intro">
	<div id="next" class="button nextScreen" onClick="launchcategories();">Catégories >></div>

	<div id="playButton">
		<div id="playVideoPresentateur" class="button" onClick="playVideoPresentateur();">Jouer la vidéo PRESENTATEUR</div>
		<div id="playVideoJoueur" class="button" onClick="playVideoJoueur();">Jouer la vidéo JOUEUR</div>
	</div>
	
	<video muted id="video_presentateur" muted poster="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS_IMAGES ?>poster_noir.jpg" preload="auto">
		<source src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_VIDEO_INTRO ?>presentateur.mp4?<?= generateRandom(18) ?>">
	</video>
	
	<video muted id="video_joueurs" muted poster="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS_IMAGES ?>poster_noir.jpg" preload="auto">
		<source src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_VIDEO_INTRO ?>joueurs.mp4?<?= generateRandom(17) ?>">
	</video>
	
</section>

<script>

	function init(status) {
		$('section').show();
	}
	
	function playVideoJoueur() {
		debug("Play Video Joueur");
		var params = new Object();
		params.action = "playVideoJoueur";
		getWindowOpener().manageAction(params);
		
		$('#video_presentateur').hide();
		$('#video_presentateur')[0].pause();
		
		$('#video_joueurs').show();
		$('#video_joueurs')[0].currentTime = 0;
		$('#video_joueurs')[0].play();
	}
	
	function playVideoPresentateur() {
		debug("Play Video Presentateur");
		var params = new Object();
		params.action = "playVideoPresentateur";
		getWindowOpener().manageAction(params);
		
		$('#video_joueurs').hide();
		$('#video_joueurs')[0].pause();
		
		$('#video_presentateur').show();
		$('#video_presentateur')[0].currentTime = 0;
		$('#video_presentateur')[0].play();
	}
	

	function launchcategories() {
		debug("Launch categories");

		var params = new Object();
		params.action = "changeProperties";
		params.newScreen = "CATEGORIE_LISTE";

		$.ajax({
			type: 'POST', 
			url: "<?= $pathToPhpRoot  ?>controller/mainController.php", 
			dataType : 'json',
			data : {
				action : "changeStatus",
				param : JSON.stringify(params)
			},
			async : false,
			success: function(data, textStatus, jqXHR) {
				console.log(data);
				if (data.status != 'OK') {
					alert("[" + data.status + "] " + data.message);
					console.log(data);
					return;
				} else {
					goToCategories();
				}
			}
		});
	}

	function goToCategories() {
		debug("Go to categories");

		var returnObject = newReturnObject();
		var contentToInsert = null;
		// Récupération du contenu HTML
		var urlScreen = "categorie_liste.php";
		$.ajax({
			type: 'POST', 
			url: urlScreen, 
			dataType : 'html',
			async : false,
			success: function(data, textStatus, jqXHR) {
				contentToInsert = data;
				
				returnObject.status = "OK";
				returnObject.message = "";
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Une erreur est survenue lors de la recuperation de " + urlScreen + " : \n" + jqXHR.responseText);
				console.log(jqXHR);
				returnObject.status = "KO";
				returnObject.message = jqXHR.responseText;
			}
		});

		if (returnObject.status != "OK") {
			return;
		}


		// On envoie l'ordre au parent (spectateur)
		debug("OPENER Launch categories");
		var params = new Object();
		params.action = "launchCategories";
		var resultManage = getWindowOpener().manageAction(params);
		if (resultManage.status != "OK") {
			if (!confirm("Un problème semble être apparu sur le traitement spectateur : " + 
				"[" + resultManage.status + "] " + resultManage.message + ".\n" + 
				"Si l'action a bien été lancée malgré tout, cliquez sur OK. " +
				"Si l'interface spectateur n'a pas reçu l'ordre, cliquez sur Annuler."))
			return;
		}
		
		// On anime le changement d'écran
		$('section').fadeOut(1500);
		setTimeout(function() {
			console.log("Insert");
			$('#content').html(contentToInsert);
			$('#currentScreen').val("CATEGORIE_LISTE");
		}, 2000);

	}

	$(document).ready(function() {
		init(STATUS);
		
		$('video').on('ended',function(){
			$(this).fadeOut();
		});
	});
	

</script>