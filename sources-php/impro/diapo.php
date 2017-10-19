<?php 
$NB_PHOTOS = 150;
$NB_LIGNES = 6;
$HEIGHT_DIV = 92;
$WIDTH_DIV = 52;
?>

<div id="tableau_photo" 
	style="width : <?= round($NB_PHOTOS / $NB_LIGNES) * $WIDTH_DIV ?>px; margin-left : -<?= round($NB_PHOTOS / $NB_LIGNES) * $WIDTH_DIV / 2 ?>px; height = <?= $NB_LIGNES * $HEIGHT_DIV ?>px; margin-top : -<?= $NB_LIGNES * $HEIGHT_DIV /2 ?>px">
<?php 

$folder = "photos/impro/";
$myDirectory = opendir($folder) or die('Erreur');
$allImages = array();
while(false !== ($entry = @readdir($myDirectory))) {
	if($entry != '.' && $entry != '..') {
		$allImages[] = $folder.$entry;
	}
}

$firstIndex = null;
$index = -1;
$indexAlreadyUsed = array();
for ($i = 0; $i < $NB_PHOTOS; $i++) {
	if (count($indexAlreadyUsed) == count($allImages)) {
		break;
	}
	
	$cpt = 0;
	$exit = false;
	while ($index == -1 || array_key_exists($index, $indexAlreadyUsed)) {
		if (count($allImages) == count($indexAlreadyUsed)) {
			$exit = true;
			break;
		}
		$index = rand(0, count($allImages) - 1);
	}
	
	if ($exit) {
		break;
	}
?>
	<div class="div_photo">
		<img id="cadre_image_<?= $i ?>" src="style/images/question_mark.png" class="question_mark" />
		<img id="input_cadre_image_<?= $i ?>" src="<?= $allImages[$index] ?>" style="display : none;" />
	</div>
<?php
	$indexAlreadyUsed[$index] = true;
	if ($firstIndex == null) $firstIndex = $index;
}
?>

</div>

<div id="diapo_grand">
	<div id="diapo_image">
		<span class="helper"></span>
		<span class="content">
			<img src="<?= $allImages[$firstIndex] ?>" />
		</span>
	</div>
</div>


<script>
	var INDEX = 0;
	var DOCUMENT_READY = false;
	
	function playDiapo() {
		if (!DOCUMENT_READY) {
			console.log("DOCUMENT_READY " + DOCUMENT_READY);
			return "Impro launched";
		}
		showDiapoGrand(INDEX);
		return "Diapo launched";
	};

	function showDivPhoto(divPhoto) {
		$(divPhoto).fadeIn();
		if ($(divPhoto).next('.div_photo').length > 0) {
			setTimeout(function() {
				return showDivPhoto($(divPhoto).next('.div_photo'));
			}, 30);
		} else {
			DOCUMENT_READY = true;
		}
	}

	function showDiapoGrand(idPhoto) {
		console.log($('#diapo_grand').is(':visible'));
		if (!$('#diapo_grand').is(':visible')) {
			$('#diapo_grand img').attr('src', $('#input_cadre_image_' + idPhoto).attr('src'));
			$('#diapo_grand').css("opacity", "1");
			$('#diapo_grand').fadeIn();
		} else {
			$('#diapo_grand img').animate({ opacity : 0 }, 500, function() {
				$('#diapo_grand img').attr('src', $('#input_cadre_image_' + idPhoto).attr('src'));
				$('#diapo_grand img').animate({ opacity : 1 }, 500 );
			});
		}
		$('#cadre_image_' + idPhoto).animate({ opacity : 0 }, 500, function() {
			$('#cadre_image_' + idPhoto).attr('src', $('#input_cadre_image_' + idPhoto).attr('src'));
			$('#cadre_image_' + idPhoto).animate({ opacity : 1 }, 500 );
		});
		INDEX++;
	}

	$(document).ready(function() {
		showDivPhoto($('.div_photo').first());
		var popupHeight = $(window).height() * 0.8;
		$('#diapo_grand .content').css('height', popupHeight + "px");
	});

	$(document).keydown(function(e) {
	    if (e.keyCode == 27) { // Esc
	    	if ($('#diapo_grand').is(':visible')) {
				$('#diapo_grand').animate({ opacity : 0 }, 500, function() {
					$('#diapo_grand').hide();
					console.log($('#diapo_grand').is(':visible'));
				});
			}
	    }
	});

</script>