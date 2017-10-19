<?php 
$pathToPhpRoot = '../../';

include_once $pathToPhpRoot.'constants.php';
include_once $pathToPhpRoot.'constantsPrivate.php';

?>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Le GIT - Impro Photo</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_intro.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_salle_attente.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_diapo.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_categories.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_regie.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_remerciements.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_dates.css" />
	<link rel="shortcut icon" HREF="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>images/GIT-logo.jpg">
	
	<script type="text/javascript" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JS ?>jquery.js"></script>
	<script type="text/javascript" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JS ?>jquery.transit.js"></script>
	<script type="text/javascript" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JS ?>common.js"></script>
	
</head>
<body>
	<input type="hidden" id="currentScreen" />
	<div id="disconnected">
		<div class="disconnected_text">
			<h1>Déconnecté du spectateur</h1>
			<p>Fermez cette fenêtre et réouvrez la à partir de la page Spectateur (touche R)</p>
		</div>
	</div>
	
	<div id="access_debug"><a href="#" onClick="$('#debug').toggle(); return false;"><img src="<?= APPLICATION_ABSOLUTE_URL ?>style/images/debug.png" /></a></div>

	<div id="restartButton"><a href="#" onClick="resetAll();"><img src="<?= APPLICATION_ABSOLUTE_URL ?>style/images/restart.png" /></a></div>
	<div id="refreshButton"><a href="#" onClick="refreshStatus();"><img src="<?= APPLICATION_ABSOLUTE_URL ?>style/images/refresh.png" /></a></div>
	
	<div id="regie">Régie</div>
	
	<div id="content" class="contentRegie"></div>
	
	<div id="debug">
		<a class="clear" href="#" onclick="$('#debugContent').html(''); return false;">Clear</a>
		<div id="debugContent"></div>
	</div>


	<script>
		// startWebSocket(host, "REGIE");

		var SCREENS = {
			'SALLE_ATTENTE' : 'salle_attente.php',	
			'INTRO' : 'intro.php',	
			'CATEGORIE_LISTE' : 'categorie_liste.php',
			'CATEGORIE' : 'categorie.php',
			'REMERCIEMENTS' : 'remerciements.php',
			'DATES' : 'dates.php',
			'OUTRO' : 'salle_attente.php',
		};

		var STATUS;
		function initIndex() {
			debug("Get Status");
			$.ajax({
				type: 'POST', 
				url: "<?= $pathToPhpRoot  ?>controller/mainController.php", 
				dataType : 'json',
				data : {
					action : 'status'
				},
				async : false,
				success: function(data, textStatus, jqXHR) {
					if (data.status != 'OK') {
						alert("[" + data.status + "] " + data.message);
						console.log(data);
						return;
					}

					STATUS = data.message;
					debug(dump(STATUS));
					getScreen(STATUS, false);
				}
			});
		}

		function getScreen(status, transmitParent) {
			debug("Get Screen : " + SCREENS[status.ecran]);
			$.ajax({
				type: 'POST', 
				url: SCREENS[status.ecran], 
				dataType : 'html',
				data : {
					id_categorie : status.id_categorie,
					type_ecran : status.type_ecran,
					photos_choisies : status.photos_choisies,
					integralite : status.integralite,
					photo_courante : status.photo_courante,
					statut_diapo : status.statut_diapo,
					block_masques : status.block_masques
				},
				async : false,
				success: function(data, textStatus, jqXHR) {
					if (transmitParent) {
						debug("OPENER Get Screen : " + SCREENS[status.ecran]);
						var openerScreen = getWindowOpener().getScreen(status);
						debug(">> " + openerScreen);
					}
					$('#content').html(data);
					$('#currentScreen').val(status.ecran);
				}
			});
		}

		var CHECK_PARENT_INTERVAL = null;
		$(document).ready(function() {
			initIndex();

			CHECK_PARENT_INTERVAL = setInterval(checkParent, 2000);
		});

		function checkParent() {
			if (getWindowOpener() == null || getWindowOpener() == self) {
				clearInterval(CHECK_PARENT_INTERVAL);
				$('#disconnected').show();
			}
		}

		function resetAll() {
			if (!confirm("Voulez-vous remettre à zéro le logiciel (retourne à l'écran d'accueil, efface la progression, comme si la représentation n'avait pas commencée) ?")) {
				return;
			}

			debug("Reset All");

			$.ajax({
				type: 'POST', 
				url: "<?= $pathToPhpRoot  ?>controller/mainController.php", 
				dataType : 'json',
				data : {
					action : 'resetStatus'
				},
				async : false,
				success: function(data, textStatus, jqXHR) {
					if (data.status != 'OK') {
						alert("[" + data.status + "] " + data.message);
						console.log(data);
						return;
					}

					STATUS = data.message;
					getScreen(STATUS, true);
				}
			});
		}

		function refreshStatus() {
			if (!confirm("Voulez-vous rafraichir les 2 écrans (régie et spectateur).\nATTENTION : Le rafraichissement est instantané (pas d'animation).")) {
				return;
			}

			getWindowOpener().initIndex();
			initIndex();
		}
		
	</script>
</body>
</html>