<?php
$pathToPhpRoot = '../../';

include_once($pathToPhpRoot."constants.php");
include_once($pathToPhpRoot."constantsPrivate.php");
require_once $pathToPhpRoot."metier/metierRemerciements.php";

$remerciementDo = MetierRemerciements::getRemerciements();

?>

<?php if ($remerciementDo != null) { ?>
<button id="show_remerciements" onClick="show_remerciements();">Afficher remerciement</button>
<?php } ?>

<section id="remerciements">
	<h1>Remerciements</h1>

	<div id="div_joueurs">
		<ul>
		<?php 
			$nbPhoto=1;
			$myDirectory = opendir($pathToPhpRoot.FOLDER_JOUEURS) or die('Erreur');
			while(false !== ($entry = @readdir($myDirectory))) {
				$ext = strtoupper(pathinfo($entry, PATHINFO_EXTENSION));
				if($entry != '.' && $entry != '..' 
					&& ($ext == "JPG" || $ext == "JPEG" || $ext == "PNG" || $ext == " GIF")) {
		?>
			<li id="joueur_<?= $nbPhoto ?>">
				<a href="#" onClick="selectJoueur(<?= $nbPhoto ?>); return false;">
					<img id="joueur_img_<?= $nbPhoto ?>" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JOUEURS.$entry ?>" />
				</a>
			</li>
		<?php
				$nbPhoto++;
				}
			}
		?>
		</ul>
	</div>

	<?php if ($remerciementDo != null) { ?>
	<div id="remerciement_text">
		<h2>Un grand merci :</h2>
		<p><?= $remerciementDo[0]->texte ?></p>
	</div>
	<?php } ?>

	<div id="goDates" class="button nextScreen" onClick="goDates();">Aller aux dates</div>

</section>

<script>

	
	function init(status) {
		$('section').fadeIn(2000);

		for (id_element in status.photos_choisies) {
			selectJoueur(id_element);
		}
	}


	var JOUEURS_SELECTED = {};
	function selectJoueur(id_joueur) {
		// Le joueur est déjà sélectionné => on le déselectionne
		if (JOUEURS_SELECTED[id_joueur] != undefined) {
			var params = new Object();
			params.action = "changeProperties";
			params.removePhoto = id_joueur;
			changeStatusController(params);

			JOUEURS_SELECTED[id_joueur] = undefined;

			getWindowOpener().unselectJoueur(id_joueur);
			$('li#joueur_' + id_joueur).animate({ 'opacity' : 0.4 }, 1000);
			return;
		}

		var params = new Object();
		params.action = "changeProperties";
		params.addPhoto = id_joueur;
		changeStatusController(params);

		getWindowOpener().selectJoueur(id_joueur);
		$('li#joueur_' + id_joueur).animate({ 'opacity' : 1 }, 1000);

		JOUEURS_SELECTED[id_joueur] = 1;
	}



	function show_remerciements() {
		console.log("show_remerciements");
		if ($('#remerciement_text').css('opacity') == 1) {
			getWindowOpener().hide_remerciements();
			$('#remerciement_text').animate({ 'opacity' : 0.4 }, 1000);
			return;
		}
		getWindowOpener().show_remerciements();
		$('#remerciement_text').animate({ 'opacity' : 1 }, 1000);
	}


	function actionController(action, params) {
		$.ajax({
			type: 'POST', 
			url: "<?= $pathToPhpRoot  ?>controller/mainController.php", 
			dataType : 'json',
			data : {
				action : action,
				param : JSON.stringify(params)
			},
			async : false,
			success: function(data, textStatus, jqXHR) {
				console.log(data);
				if (data.status != 'OK') {
					alert("[" + data.status + "] " + data.message);
					console.log(data);
					return;
				}
			}
		});
	}

	function changeStatusController(params) {
		actionController("changeStatus", params);
	}

	function goDates() {
		debug("goDates");

		var returnObject = newReturnObject();
		var contentToInsert = null;
		// Récupération du contenu HTML
		var urlScreen = "dates.php";
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
		debug("OPENER goDates");
		var resultManage = getWindowOpener().goDates();
		if (resultManage == undefined || resultManage.status != "OK") {
			if (!confirm("Un problème semble être apparu sur le traitement spectateur : " + 
				"[" + resultManage.status + "] " + resultManage.message + ".\n" + 
				"Si l'action a bien été lancée malgré tout, cliquez sur OK. " +
				"Si l'interface spectateur n'a pas reçu l'ordre, cliquez sur Annuler."))
			return;
		}

		var params = new Object();
		params.action = "changeProperties";
		params.newScreen = "DATES";
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
				}
			}
		});
		
		// On anime le changement d'écran
		$('section').fadeOut(1500);
		setTimeout(function() {
			$('#content').html(contentToInsert);
			$('#currentScreen').val("DATES");
		}, 2000);
	}


	$(document).ready(function() {
		init(STATUS);
	});

</script>