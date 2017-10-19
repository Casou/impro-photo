<?php 
$pathToPhpRoot = '../../';

include_once $pathToPhpRoot.'constants.php';
include_once $pathToPhpRoot.'constantsPrivate.php';

?>
<section id="salle_attente">
	<header>
		<img src="<?= APPLICATION_ABSOLUTE_URL ?>style/images/Git_logo_large.png" />
	</header>
	
	<main>
		<button onClick="launchImpro()">Lancer l'impro</button>
	</main>

	<div id="options">
		<button id="handleAnimation" onClick="handleAnimationOpener();">Stop animation</button>
	</div>

	<div id="photosFlottantes">	
	</div>
</section>

	<script>
		var ANIMATION_FADE_OUT = 4000;
		function init(status) {
			$('section').fadeIn(2000);

			if (getWindowOpener() != null && !getWindowOpener().isAnimationRunning()) {
				$('#handleAnimation').html('Start animation');
			}
		}

		function launchImpro() {
			// Changement de statut dans la bdd
			params = new Object();
			params.action = "changeProperties";
			params.newScreen = "INTRO";

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
						goToIntro();
					}
				}
			});

			// return "Animating";
		}

		function goToIntro() {
			debug("Go intro");
			// On récupère le contenu de l'écran à afficher.
			var returnObject = newReturnObject();
			var contentToInsert = null;

			var urlScreen = "intro.php";
			$.ajax({
				type: 'POST', 
				url: urlScreen, 
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
					alert("Une erreur est survenue lors de la recuperation de " + urlScreen + " : \n" + jqXHR.responseText);
					console.log(jqXHR);
					debug(dump(jqXHR));
					returnObject.status = "KO";
					returnObject.message = jqXHR.responseText;
				}
			});

			if (returnObject.status != "OK") {
				return;
			}

			// On envoie l'ordre au parent (spectateur)
			var params = new Object();
			params.action = "launchImpro";
			debug("OPENER Go intro");
			var resultManage = getWindowOpener().manageAction(params);
			if (resultManage.status != "OK") {
				if (!confirm("Un problème semble être apparu sur le traitement spectateur : " + 
					"[" + resultManage.status + "] " + resultManage.message + ".\n" + 
					"Si l'action a bien été lancée malgré tout, cliquez sur OK. " +
					"Si l'interface spectateur n'a pas reçu l'ordre, cliquez sur Annuler."))
				return;
			}
			
			// On anime le changement d'écran
			// $('section').addClass('animate_intro');
			$('main button').hide(500);
			$('section').fadeOut(ANIMATION_FADE_OUT);

			setTimeout(function() {
				$('#content').html(contentToInsert);
				$('#currentScreen').val("INTRO");
			}, ANIMATION_FADE_OUT + 500);
		}

		function handleAnimationOpener() {
			if ($('#handleAnimation').html() == "Stop animation") {
				getWindowOpener().stopAnimation();
				$('#handleAnimation').html("Start animation");
			} else {
				getWindowOpener().startAnimation();
				$('#handleAnimation').html("Stop animation");
			}
		}
		

		
		$(document).ready(function() {
			// loopImages();
			// CLEAR_IMAGE_INTERVAL = setInterval(function() { clearImages(); }, 3000);

			init(STATUS);
		});

	</script>

