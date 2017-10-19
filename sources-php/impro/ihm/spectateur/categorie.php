<?php 

$pathToPhpRoot = '../../';
include_once $pathToPhpRoot.'constants.php';
include_once $pathToPhpRoot.'constantsPrivate.php';
require_once $pathToPhpRoot."metier/metierCategorie.php";

$categorie = MetierCategorie::getCategorieById($_POST['id_categorie']);

$type = $categorie->type;
?>

<section id="categorie">
	<h1><?= $categorie->nom ?></h1>
	
	<div id="diapo">
		<img />
		
		<?php 
		if ($type == CATEGORY_TYPE_POLAROID) {
			for ($i = 1; $i <= 9; $i++) {
		?>
			<div id="mask_<?= $i ?>" class="mask">&nbsp;</div>
		<?php 
			}
		} 
		?>
	</div>
	
	<div id="div_images">
		<ul>
		<?php 
			$nbPhoto=1;
			$folder = utf8_decode($pathToPhpRoot.FOLDER_PHOTO_IMPRO.$categorie->path_folder."/");
			$myDirectory = opendir($folder) or die('Erreur');
			while(false !== ($entry = @readdir($myDirectory))) {
				if($entry != '.' && $entry != '..') {
					$id = md5($entry);
		?>
			<li id="<?= $id ?>" onClick="selectNumber('<?= $id ?>');">
				<?= $nbPhoto ?>
				<input type="hidden" id="img_<?= $id ?>" value="<?= APPLICATION_ABSOLUTE_URL.FOLDER_PHOTO_IMPRO.$categorie->path_folder."/".$entry ?>" />
			</li>
		<?php
				$nbPhoto++;
				}
			}
		?>
		</ul>
	</div>
</section>

<audio id="photo">
	<source src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_RESSOURCES ?>appareil_photo_long.ogg" type="audio/ogg" />
	<source src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_RESSOURCES ?>appareil_photo_long.mp3" type="audio/mpeg" />
</audio>


<audio id="snapshot">
	<source src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_RESSOURCES ?>appareil_photo.ogg" type="audio/ogg" />
	<source src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_RESSOURCES ?>appareil_photo.mp3" type="audio/mpeg" />
</audio>

<script>

	var MAXIMUM_SELECTION = 5;
	var RATIO_DIV_DIAPO = null;
	

	function init(status) {
		console.log("Init categorie.php");
		$('section').fadeIn(2000);

		if (!status.integralite) {
			for (id_element in status.photos_choisies) {
				selectNumber(id_element, true, false, true); 
			}
		}

		if (status.statut_diapo == 'launched') {
			if (status.integralite) {
				selectAllNoAnimation();
			} else {
				moveSelection();
			}
		}

		if (status.photo_courante != null) {
			setTimeout(function() {
				showPicture(status.photo_courante);
				setTimeout(function() {
					for (id_block in status.block_masques) {
						$('#mask_' + id_block).css('opacity', 0);
					}
				}, 1200);
			}, 1500);
		}

	}
	
	function manageAction(infosJson) {
		var action = infosJson.action;
		console.log("Manage action");
		console.log(action);
		if (action == 'selectNumber') {
			selectNumber(infosJson.id_element, true, false);
		} else if (action == 'unselectNumber') {
			selectNumber(infosJson.id_element, false, true);
		} else if (action == 'moveSelection') {
			moveSelection();
		} else if (action == 'cancelSelection') {
			cancelSelection();
		} else if (action == 'showPicture') {
			showPicture(infosJson.id_element);
		} else if (action == 'retourNoir') {
			retourNoir();
		} else if (action == 'selectAll') {
			selectAll();
		} else if (action == 'goCategories') {
			retourListe();
		} else if (action == 'revealBlock') {
			revealBlock(infosJson.block);
		} else if (action == 'photoSound') {
			photoSound();
		} else {
			console.log("Action non définie : " + action);
		}
	}



	var selected_items = {};
	function selectNumber(id) {
		selectNumber(id, false, false);
	}
	
	function selectNumber(id, forceSelect, forceUnselect) {
		if ($('#' + id).length == 0) {
			return;
		}

		if (selected_items.length >= MAXIMUM_SELECTION) {
			// alert('Maximum de sélection : ' + MAXIMUM_SELECTION + ' photos');
			return;
		}
		
		// if ($.inArray(id, selected_items) > 0) {
		if (forceUnselect || (selected_items[id] != undefined && !forceSelect)) {
			$('#' + id).removeClass('selected');
			delete selected_items[id];
			/*
			selected_items = jQuery.grep(selected_items, function( n, i ) {
				return n !== id;
			});
			*/
		} else {
			$('#' + id).addClass('selected');
			selected_items[id] = { left : $('#' + id).position().left, top : $('#' + id).position().top };
		}
	}


	function selectAll() {
		$('li').addClass('selected');
		$('#diapo').addClass('selectAll');

		setTimeout(function() {
			$('div#div_images ul').fadeOut('1500');
		}, 1500);
	}
	
	function selectAllNoAnimation() {
		$('li').each(function() {
			selected_items[$(this).attr('id')] = { left : $(this).position().left, top : $(this).position().top };
		});

		$.each(selected_items, function(id, position) {
			$('#' + id).css({ 
				'position' : 'absolute',
				'top' : '-200px',
				'left' : '-200px',
			}).removeClass('selected');
		});

		$('#diapo').addClass('selectAll');
	}

	function moveSelection() {
		$('li:not(.selected)').animate({ 'opacity' : 0 }, 1000);
		
		setTimeout(function() {
			$('li:not(.selected)').css('display', 'none');
			
			var idx = 0;
			var top_position;
			var margin;
			$.each(selected_items, function(id_element, position) {
				$('#' + id_element).css({ 
					'position' : 'absolute',
					'top' : position.top,
					'left' : position.left,
				}).removeClass('selected');

				margin = $('#' + id_element).css('margin-bottom').replace("px", "");
				margin = margin * 2;
				top_position = (idx * ($('#' + id_element).height() + margin)) + 100;
				$('#' + id_element).animate({ 
					'top' : top_position + "px",
					'left' : '10px',
				}, 1000);
				idx++;
			});

		}, 1000);
	}

	function cancelSelection() {
		retourNoir(function() { $('#diapo').hide(); $('div#div_images ul').fadeIn('1500'); } );

		$('li:not(.selected)').css('display', 'block');
		
		$.each(selected_items, function(id_element, position) {
			$('#' + id_element).animate({ 
				'top' : position.top,
				'left' : position.left,
			}, 1000);
			
		});

		setTimeout(function() {
			$.each(selected_items, function(id_element, position) {
				$('#' + id_element).css({
					'position' : 'relative',
					'top' : 'auto',
					'left' : 'auto'
				}).removeClass('selected');
			});

			$('li:not(.selected)').animate({ 'opacity' : 1 }, 1000);

			$.each(selected_items, function(id_element, position) {
				$('#' + id_element).css({
					'top' : 'auto',
					'left' : 'auto'
				});
			});
			selected_items = {};
		}, 1200);

		$('#diapo').removeClass('selectAll');
	}

	

	function showPicture(id) {

		$('.mask').animate({ 'opacity' : 1 }, 500);
		$('#diapo').css('display', 'block');

		if (RATIO_DIV_DIAPO == null) {
			RATIO_DIV_DIAPO = $('#diapo').innerWidth() / $('#diapo').innerHeight();
		}

		$('#diapo').animate({ 'opacity' : 0 }, 500, function() {
			var div_width = $('#diapo').width();
			var div_height = $('#diapo').height();
			$('.mask').css('width', (div_width / 3)).css('height', (div_height / 3));
			$('#mask_1').css('top', 0).css('left', 0);
			$('#mask_2').css('top', 0).css('left', (div_width / 3));
			$('#mask_3').css('top', 0).css('left', (div_width / 3 * 2));
			$('#mask_4').css('top', (div_height / 3)).css('left', 0);
			$('#mask_5').css('top', (div_height / 3)).css('left', (div_width / 3));
			$('#mask_6').css('top', (div_height / 3)).css('left', (div_width / 3 * 2));
			$('#mask_7').css('top', (div_height / 3 * 2)).css('left', 0);
			$('#mask_8').css('top', (div_height / 3 * 2)).css('left', (div_width / 3));
			$('#mask_9').css('top', (div_height / 3 * 2)).css('left', (div_width / 3 * 2));

			$('.selected').removeClass('selected');
			$('#' + id).addClass('selected');
			
			var img_src = $('#img_' + id).val();
			
			$('#diapo img').attr('src', img_src);
			$('#diapo img').one("load", function() {
				$('#diapo img').css('width', 'auto');
				$('#diapo img').css('height', 'auto');
				
				var ratio_img = $('#diapo img')[0].clientWidth / $('#diapo img')[0].clientHeight;
				if (ratio_img > RATIO_DIV_DIAPO) { // La largeur est proportionnellement plus grande
					$('#diapo img').css('width', '100%');
					$('#diapo img').css('height', 'auto');
				} else { // La hauteur est proportionnellement plus grande
					$('#diapo img').css('height', '100%');
					$('#diapo img').css('width', 'auto');
				}

				// On fait l'action 2 fois car il y a certains blocks qui ne le font pas la première (je ne sais pas pourquoi...)
				$('.mask').css('opacity', 1);

				$('#diapo').animate({ 'opacity' : 1 }, 500, function() {

					var img_width = $('#diapo img')[0].clientWidth;
					var img_height = $('#diapo img')[0].clientHeight;
					
					// On met les masques en fonction de l'image
					$('.mask').css('width', (img_width / 3)).css('height', (img_height / 3));
					$('#mask_1').css('top', $('#diapo img').position().top).css('left', $('#diapo img').position().left);
					$('#mask_2').css('top', $('#diapo img').position().top).css('left', $('#diapo img').position().left + (img_width / 3));
					$('#mask_3').css('top', $('#diapo img').position().top).css('left', $('#diapo img').position().left + (img_width / 3 * 2));
					$('#mask_4').css('top', $('#diapo img').position().top + (img_height / 3)).css('left', $('#diapo img').position().left);
					$('#mask_5').css('top', $('#diapo img').position().top + (img_height / 3)).css('left', $('#diapo img').position().left + (img_width / 3));
					$('#mask_6').css('top', $('#diapo img').position().top + (img_height / 3)).css('left', $('#diapo img').position().left + (img_width / 3 * 2));
					$('#mask_7').css('top', $('#diapo img').position().top + (img_height / 3 * 2)).css('left', $('#diapo img').position().left);
					$('#mask_8').css('top', $('#diapo img').position().top + (img_height / 3 * 2)).css('left', $('#diapo img').position().left + (img_width / 3));
					$('#mask_9').css('top', $('#diapo img').position().top + (img_height / 3 * 2)).css('left', $('#diapo img').position().left + (img_width / 3 * 2));
					
				});
			});
			
		});
	}
		
	function revealBlock(id_block) {
		var audio = $('#snapshot')[0];
		audio.currentTime = 0;
		audio.play();
		$('#mask_' + id_block).animate({ 'opacity' : 0 }, 1000, function() {

		});
	}

	function photoSound() {
		var audio = $('#photo')[0];
		audio.currentTime = 0;
		audio.play();
	}
	
	function retourNoir(returnFunction) {
		$('#diapo').animate({ 'opacity' : 0 }, 500, returnFunction);
		$('.selected').removeClass('selected');
	}

	function retourListe() {
		$('section').fadeOut(3000, function() {
			$.ajax({
				type: 'POST', 
				url: 'categorie_liste.php?show_all=true', 
				dataType : 'html',
				async : false,
				success: function(data, textStatus, jqXHR) {
					$('#content').html(data);
					$('#currentScreen').val("CATEGORIE_LISTE");
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Une erreur est survenue : \n" + jqXHR.responseText);
				}
			});
		});
	}


	

	$(document).ready(function() {
		init(STATUS);
	});
	
</script>