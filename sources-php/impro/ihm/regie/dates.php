<?php
$pathToPhpRoot = '../../';

include_once($pathToPhpRoot."constants.php");
include_once($pathToPhpRoot."constantsPrivate.php");
require_once $pathToPhpRoot."metier/metierDates.php";

$dates = MetierDates::getFutureDates();

?>

<section id="dates">
	<h1>Dates</h1>


	<div id="div_dates">
		<ul>
		<?php
			if ($dates != null) {
				$indexDate = 0;
				foreach ($dates as $date) {
		?>
			<li id="date_<?= $indexDate ?>">
				<?= formatDateToDisplay($date->date) ?> <span class="event_nom"><?= $date->nom ?></span>
			</li>
		<?php
					$indexDate++;
				}
			}
		?>
		</ul>
	</div>


	<div id="img_impros">
		<?php 
			$MAX_IMAGES = 8;
			$nbPhotos = 0;
			$top = -50;

			$folder = $pathToPhpRoot.FOLDER_PHOTO_DATES;
			$myDirectory = opendir($folder) or die('Erreur');
			while(false !== ($entry = @readdir($myDirectory))) {
				if ($MAX_IMAGES == $nbPhotos) {
					break;
				}
				if($entry != '.' && $entry != '..') {
			?>
				<img src="<?= $pathToPhpRoot.FOLDER_PHOTO_INTRO.$entry ?>" style="top : <?= $top ?>px" />
			<?php
					$nbPhotos++;
					$top += 170;
				}
			}
		?>
	</div>


	<div id="goOutro" class="button nextScreen" onClick="goOutro();">Retour à l'intro</div>

</section>

<script>

	function init(status) {
		$('section').fadeIn(2000);
		rotateImages();
	}

	function rotateImages() {
		$("#img_impros img").each(function() {
			var randRotate = Math.round(Math.random() * 40) - 20;
			$(this).rotate(randRotate);
		});
		
	}

	function goOutro() {
		debug("goOutro");

		var returnObject = newReturnObject();
		var contentToInsert = null;
		// Récupération du contenu HTML
		var urlScreen = "salle_attente.php";
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
		debug("OPENER goOutro");
		var resultManage = getWindowOpener().goOutro();
		if (resultManage == undefined || resultManage.status != "OK") {
			if (!confirm("Un problème semble être apparu sur le traitement spectateur : " + 
				"[" + resultManage.status + "] " + resultManage.message + ".\n" + 
				"Si l'action a bien été lancée malgré tout, cliquez sur OK. " +
				"Si l'interface spectateur n'a pas reçu l'ordre, cliquez sur Annuler."))
			return;
		}

		var params = new Object();
		params.action = "changeProperties";
		params.newScreen = "OUTRO";
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
			$('#currentScreen').val("OUTRO");
		}, 2000);
	}


	$(document).ready(function() {
		init(STATUS);
	});

</script>