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
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_spectateur.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_remerciements.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_dates.css" />
	<link rel="shortcut icon" HREF="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>images/GIT-logo.jpg">
	
	<script type="text/javascript" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JS ?>jquery.js"></script>
	<script type="text/javascript" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JS ?>jquery.transit.js"></script>
	<script type="text/javascript" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JS ?>common.js"></script>
	
</head>
<body>
	<input type="hidden" id="currentScreen" />
	<div id="disconnected"><img src="<?= APPLICATION_ABSOLUTE_URL ?>style/images/disconnect.png" /></div>
	<div id="access_debug"><a href="#" onClick="$('#debug').toggle(); return false;"><img src="<?= APPLICATION_ABSOLUTE_URL ?>style/images/debug.png" /></a></div>
	
	<div id="content" class="contentSpectateur"></div>
	
	<div id="debug"></div>

	<div id="watermark">
		<img src="<?= APPLICATION_ABSOLUTE_URL ?>style/images/logo/git_logo_gris.png" />
	</div>


	<script>
		// startWebSocket(host, "SPECTATEUR");

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
					getScreen(STATUS);
				}
			});
		}

		function getScreen(status) {
			console.log("Get screen : " + SCREENS[status.ecran]);
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
					$('#content').html(data);
					$('#currentScreen').val(status.ecran);
				}
			});

			return SCREENS[status.ecran];
		}

		$(document).ready(function() {
			initIndex();
		});

		var popupChild = null;
		$(document).on("keydown", function(event) {
			// Ouverture de la popup Régie via la touche R
			if (event.keyCode == 82) {
				popupChild = window.open('../regie', "popupRegie", "width=1000,height=700,scrollbars=yes");
			}
		});

	</script>
</body>
</html>