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
	
	<div id="selection_ok" class="button" onClick="moveSelection();">Sélection OK</div>
	<?php if ($type != CATEGORY_TYPE_POLAROID) { ?>
	<div id="integralite" class="button" onClick="selectAll();">Intégralité des photos</div>
	
	<div id="photoSound" onClick="photoSound();"><img src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS_IMAGES ?>photo.png" /></div>
	<?php } ?>
	
	<div id="cancel_selection" class="button" onClick="cancelSelection();">Annuler sélection</div>
	
	<div id="retour_noir" class="button" onClick="retourNoir(null);">Retour au noir</div>
	
	<div id="retour_categories" class="button nextScreen" onClick="retourListe();">Retour à la liste</div>
	
	<div id="diapo">
		<img />
		<?php 
		if ($type == CATEGORY_TYPE_POLAROID) {
			for ($i = 1; $i <= 9; $i++) {
		?>
			<div id="mask_<?= $i ?>" class="mask" onClick="revealBlock(<?= $i ?>);">&nbsp;</div>
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

<script>

	var BLOCK_SEND_MESSAGE = false;
	var MAXIMUM_SELECTION = 5;
	var RATIO_DIV_DIAPO = null;

	var AFFICHAGE;
	var DIAPO_LAUNCHED = false;
	var PHOTO_AFFICHEE;
	
	function init(status) {
		console.log("Init categorie.php");
		$('section').fadeIn(2000);

		BLOCK_SEND_MESSAGE = true;
		if (!status.integralite) {
			for (id_element in status.photos_choisies) {
				selectNumber(id_element, true, false); 
			}
		}

		if (status.statut_diapo == 'launched') {
			if (status.integralite) {
				selectAll();
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

					BLOCK_SEND_MESSAGE = false;
				}, 1200);
			}, 1500);
		} else {
			BLOCK_SEND_MESSAGE = false;
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
		} else {
			console.log("Action non définie : " + action);
		}
	}



	var selected_items = {};
	function selectNumber(id) {
		selectNumber(id, false, false, false);
	}

	function selectNumber(id, forceSelect, forceUnselect) {
		if (DIAPO_LAUNCHED) {
			showPicture(id);
			return;
		}
		
		if ($('#' + id).length == 0) {
			return;
		}

		if (selected_items.length >= MAXIMUM_SELECTION) {
			alert('Maximum de sélection : ' + MAXIMUM_SELECTION + ' photos');
			return;
		}
		
		// if ($.inArray(id, selected_items) > 0) {
		if (forceUnselect || (selected_items[id] != undefined && !forceSelect)) {
			if (!BLOCK_SEND_MESSAGE) {
				getWindowOpener().selectNumber(id, false, true);
				
				params = new Object();
				params.action = "changeProperties";
				params.removePhoto = id;
				changeStatusController(params);
			}

			
			$('#' + id).removeClass('selected');
			delete selected_items[id];
		} else {
			if (!BLOCK_SEND_MESSAGE) {
				getWindowOpener().selectNumber(id, true, false);
				
				params = new Object();
				params.action = "changeProperties";
				params.addPhoto = id;
				changeStatusController(params);
			}

			$('#' + id).addClass('selected');
			selected_items[id] = { left : $('#' + id).position().left, top : $('#' + id).position().top };
		}
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


	function selectAll() {
		$('li').each(function() {
			selected_items[$(this).attr('id')] = { left : $(this).position().left, top : $(this).position().top };
		});
		
		if (!BLOCK_SEND_MESSAGE) {
			getWindowOpener().selectAll();

			params = new Object();
			params.action = "changeProperties";
			params.addAllPhoto = Object.keys(selected_items);
			params.statutDiapo = "launched";
			changeStatusController(params);
		}
		$('.selected').removeClass('selected');
		
		$('#selection_ok').hide();
		$('#integralite').hide();
		$('#cancel_selection').show();
		$('#retour_noir').show();
		DIAPO_LAUNCHED = true;
		AFFICHAGE = 'INTEGRALITE';
		$('#diapo').addClass('miniature');
	}
	
	
	
	function photoSound() {
		if (!BLOCK_SEND_MESSAGE) {
			getWindowOpener().photoSound();
		}
	}

	

	function moveSelection() {
		if (!BLOCK_SEND_MESSAGE) {
			getWindowOpener().moveSelection();
			
			params = new Object();
			params.action = "changeProperties";
			params.statutDiapo = "launched";
			changeStatusController(params);
		}
		
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

		$('#selection_ok').hide();
		$('#integralite').hide();
		$('#cancel_selection').show();
		$('#retour_noir').show();
		DIAPO_LAUNCHED = true;
		AFFICHAGE = 'PHOTO';
	}

	function cancelSelection() {
		if (!BLOCK_SEND_MESSAGE) {
			getWindowOpener().cancelSelection();

			var params = new Object();
			params.action = "changeProperties";
			params.cancelSelection = "cancelSelection";
			changeStatusController(params);
		}

		retourNoir(function() { $('#diapo').hide(); } );

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

		
		
		$('#selection_ok').show();
		$('#integralite').show();
		$('#cancel_selection').hide();
		$('#retour_noir').hide();
		DIAPO_LAUNCHED = false;
		AFFICHAGE = null;
		$('#diapo').removeClass('miniature');
	}

	function showPicture(id) {
		if (PHOTO_AFFICHEE == id) {
			return;
		}
		PHOTO_AFFICHEE = id;

		$('.mask').css('opacity', 0.5);

		if (RATIO_DIV_DIAPO == null) {
			RATIO_DIV_DIAPO = $('#diapo').innerWidth() / $('#diapo').innerHeight();
		}
		
		if (!BLOCK_SEND_MESSAGE) {
			getWindowOpener().showPicture(id);
			
			var params = new Object();
			params.action = "changeProperties";
			params.photo_courante = id;
			changeStatusController(params);
		}

		$('#diapo').css('display', 'block');

		$('#diapo').animate({ 'opacity' : 0 }, 500, function() {
			// On met les masques en fonction de la div
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
				$('.mask').css('opacity', 0.5);
				
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
		if (!BLOCK_SEND_MESSAGE) {
			getWindowOpener().revealBlock(id_block);

			setTimeout(function() {
				params = new Object();
				params.action = "changeProperties";
				params.reveal_block = id_block;
				changeStatusController(params);
			}, 200);
		}
		
		$('#mask_' + id_block).animate({ 'opacity' : 0 }, 1000);
	}

	function retourNoir(returnFunction) {
		if (!BLOCK_SEND_MESSAGE && returnFunction == null) {
			getWindowOpener().retourNoir();
			
			params = new Object();
			params.action = "changeProperties";
			params.unset_photo_courante = 1;
			changeStatusController(params);
		}
		
		$('#diapo').animate({ 'opacity' : 0 }, 500, returnFunction);
		$('.selected').removeClass('selected');
		PHOTO_AFFICHEE = null;
	}


	function retourListe() {
		getWindowOpener().retourListe();
		
		setTimeout(function() {
			var params = new Object();
			params.action = "changeProperties";
			params.newScreen = "CATEGORIE_LISTE";
			params.unset_photo_courante = "1";
			params.cancelSelection = "cancelSelection";
			params.statutDiapo = null;
			params.id_categorie = null;
			params.integralite = null;
			changeStatusController(params);

			setTimeout(function() {
				params = new Object();
				params.action = "termine";
				params.id_categorie = <?= $categorie->id ?>;
				actionController("categorie", params);


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
			}, 500);
		}, 500);
	}
	



	$(document).ready(function() {
		console.log("init");
		init(STATUS);
	});
	
</script>