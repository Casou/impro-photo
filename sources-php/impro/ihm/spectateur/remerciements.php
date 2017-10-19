<?php
$pathToPhpRoot = '../../';

include_once($pathToPhpRoot."constants.php");
include_once($pathToPhpRoot."constantsPrivate.php");
require_once $pathToPhpRoot."metier/metierRemerciements.php";

$remerciementDo = MetierRemerciements::getRemerciements();

?>

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
				<img id="joueur_img_<?= $nbPhoto ?>" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JOUEURS.$entry ?>" />
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

</section>

<script>

	function init(status) {
		$('section').fadeIn(2000);

		for (id_element in status.photos_choisies) {
			selectJoueur(id_element); 
		}
	}

	function selectJoueur(id_joueur) {
		$('li#joueur_' + id_joueur).animate({ 'opacity' : 1 }, 1000);
	}

	function unselectJoueur(id_joueur) {
		$('li#joueur_' + id_joueur).animate({ 'opacity' : 0 }, 1000);
	}

	function show_remerciements() {
		$('#remerciement_text').animate({ 'opacity' : 1 }, 1000);
	}

	function hide_remerciements() {
		$('#remerciement_text').animate({ 'opacity' : 0 }, 1000);
	}

	function goDates() {
		var returnObject = newReturnObject();
		var contentToInsert = null;

		$.ajax({
			type: 'POST', 
			url: 'dates.php', 
			dataType : 'html',
			async : false,
			success: function(data, textStatus, jqXHR) {
				contentToInsert = data;

				returnObject.status = "OK";
				returnObject.message = "";
			},
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR);
				returnObject.status = "KO";
				returnObject.message = jqXHR.responseText;
			}
		});

		$('section').fadeOut(1500);
		
		setTimeout(function() {
			$('#content').html(contentToInsert);
			$('#currentScreen').val("REMERCIEMENTS");
		}, 2000);

		return returnObject;
	}

	$(document).ready(function() {
		init(STATUS);
	});

</script>