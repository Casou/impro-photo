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
		var returnObject = newReturnObject();
		var contentToInsert = null;

		$.ajax({
			type: 'POST', 
			url: 'salle_attente.php', 
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
			$('#currentScreen').val("OUTRO");
		}, 2000);

		return returnObject;
	}

	$(document).ready(function() {
		init(STATUS);
	});
	
</script>
