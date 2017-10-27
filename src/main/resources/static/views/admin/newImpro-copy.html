<?php 
$pathToPhpRoot = '../../';

include_once $pathToPhpRoot.'constants.php';
include_once $pathToPhpRoot.'constantsPrivate.php';
include_once $pathToPhpRoot.'metier/metierStatus.php';
include_once $pathToPhpRoot.'metier/metierCategorie.php';
include_once $pathToPhpRoot.'metier/metierRemerciements.php';
include_once $pathToPhpRoot.'metier/metierDates.php';

$post = "";
if (!isset($_POST['action'])) {
	MetierStatus::reinitStatus();
} else {
	MetierCategorie::manageCategories($_POST);
	MetierRemerciements::setRemerciement($_POST);
	MetierDates::manageDates($_POST);
}

$categories = MetierCategorie::getAllCategorie();
$remerciements = MetierRemerciements::getRemerciements();
$dates = MetierDates::getFutureDates();
?>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Le GIT - Impro Photo</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>style_admin.css" />
	<link rel="stylesheet" href="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>jquery-ui/jquery-ui.css" />
	
	<script type="text/javascript" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JS ?>jquery.js"></script>
	<script type="text/javascript" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_CSS ?>jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="<?= APPLICATION_ABSOLUTE_URL.FOLDER_JS ?>common.js"></script>
</head>
<body id="newImpro" class="admin">
	<h1>Préparation d'une impro</h1>

	<form action="newImpro.php" method="post">
		<h2>Liste des catégories <button onClick="addCategorie(); return false;">Ajouter</button></h2>
		<input type="hidden" name="action" value="update" />
		
		<ul id="categories">
			<?php
			foreach($categories as $categorie) {
				$class = $categorie->termine ? "termine" : "";
			?>
			<li class="ui-state-default" id="cat_<?= $categorie->id ?>">
				<span class="ui-icon ui-icon-arrowthick-2-n-s"></span>
				
				<span class="help id_tech" title="Id technique"><?= $categorie->id ?></span>
				<input type="hidden" name="categorie_id[]" value="<?= $categorie->id ?>" />
				<input type="text" name="categorie_nom[]" value="<?= $categorie->nom ?>" />
				
				<span class="type">
					Type : 
					<select name="categorie_type[]">
						<?php foreach($ALL_TYPES as $type => $label) { ?>
							<option value="<?= $type ?>" <?= $categorie->type == $type ? "selected" : "" ?>><?= $label ?></option>
						<?php } ?>
					</select>
				</span>
				
				<?php 
				$title = "";
				$class = "";
				$folder_path = $pathToPhpRoot.FOLDER_PHOTO_IMPRO.$categorie->path_folder;
				if (!file_exists(utf8_decode($folder_path))) {
					$title = "Ce répertoire n'existe pas : $folder_path";
					$class = "error help";
				} ?>
				<span class="chemin">
					<span class="<?= $class ?>" title="<?= $title ?>">
					Chemin :
					</span>
					<input type="text" name="categorie_path[]" value="<?= $categorie->path_folder ?>" /> 
				</span> 
				
				<a class="delete" href="#" onClick="removeCategorie($(this).parents('li')); return false;">
					[X]
				</a>
			</li>
			<?php } ?>
		</ul>
		
		<h2>Texte de remerciements (70 caractères max)</h2>
		<h3>"Un grand merci : "</h3>
		<textarea id="remerciements_texte" name="remerciements_texte" cols="60" rows="1" maxlength="70"><?= $remerciements != null ? $remerciements[0]->texte : "" ?></textarea>
		<p id="textarea_remaining_chars"><span id="nb_remaining_chars"></span> caractères restants.</p>

		<h2>Prochaines dates <button onClick="addDate(); return false;">Ajouter</button></h2>
		<ul id="dates">
			<?php
			foreach($dates as $date) {
			?>
			<li>
				<input type="hidden" class="date_id" name="date_id[]" maxlength="10" placeholder="Date" value="<?= $date->id ?>" />
				<input type="date" class="date_date" name="date_date[]" maxlength="10" placeholder="Date" value="<?= formatDate($date->date) ?>" />
				<input type="text" class="date_nom" name="date_nom[]" maxlength="40" placeholder="Nom de l'évènement" value="<?= $date->nom ?>" />
				<a class="delete" href="#" onClick="removeDate($(this).parents('li')); return false;">
					[X]
				</a>
			</li>
			<?php
			}
			?>
		</ul>

		<input id="submitChanges" type="submit" value="Enregistrer les modifications" />

	</form>


	<div id="hiddenClones" style="display : none;">
		<ul>
			<li class="ui-state-default" id="cat_clone">
				<span class="ui-icon ui-icon-arrowthick-2-n-s"></span>
				
				<span class="help id_tech" title="Id technique"></span>
				<input type="hidden" name="categorie_id[]" value="" />
				<input type="text" name="categorie_nom[]" value="" />
				
				<span class="type">
					Type : 
					<select name="categorie_type[]">
						<?php foreach($ALL_TYPES as $type => $label) { ?>
							<option value="<?= $type ?>" <?= $categorie->type == $type ? "selected" : "" ?>><?= $label ?></option>
						<?php } ?>
					</select>
				</span>
				<span class="chemin">
					<span>
					Chemin :
					</span>
					<input type="text" name="categorie_path[]" value="" /> 
				</span> 
				
				<a class="delete" href="#" onClick="removeCategorie($(this).parents('li')); return false;">
					[X]
				</a>
			</li>
			<li id="date_clone">
				<input type="hidden" class="date_date" name="date_id[]" maxlength="10" placeholder="Date" value="" />
				<input type="date" class="date_date" name="date_date[]" maxlength="10" placeholder="Date" value="" />
				<input type="text" class="date_nom" name="date_nom[]" maxlength="40" placeholder="Nom de l'évènement" value="" />
				<a class="delete" href="#" onClick="removeDate($(this).parents('li')); return false;">
					[X]
				</a>
			</li>
		</ul>
	</div>
	
	
	<div id="back">
		<a href="<?= $pathToPhpRoot ?>"><< Retour à l'accueil</a>
	</div>
	
	<script>
		var TEXTEAREA_MAX_CHARS = $('#remerciements_texte').attr('maxlength');

		$(document).ready(function() {
			$("ul#categories").sortable();

			calcRemainingChars();
			$('#remerciements_texte').on("keyup", calcRemainingChars);
		});

		function addCategorie() {
			$('#categories').append($('#hiddenClones #cat_clone').first().clone().attr('id', 'cat_' + generateRandom(7)));
			$('#categories li').last().find('input').val('');
			$('#categories li').last().find('select').val('PHOTO');
			$('#categories li').last().find('.id_tech').html('&nbsp;');
		}

		function addDate() {
			var newId = 'date_' + generateRandom(7);
			$('#dates').append($('#hiddenClones #date_clone').first().clone().attr('id', newId));
		}

		function removeCategorie(li) {
			if ($('#categories li').size() == 1) {
				alert('Vous ne pouvez pas supprimer toutes les catégories.');
				return;
			}
			$(li).remove();
		}

		function removeDate(li) {
			$(li).remove();
		}

		function calcRemainingChars() {
			$('#nb_remaining_chars').html(TEXTEAREA_MAX_CHARS - $('#remerciements_texte').val().length);
		}

		$(document).on('focus',"input.date_date", function() {
			$(this).datepicker({ dateFormat: 'dd/mm/yy', minDate: 0 });
		});

	</script>
	
</body>
</html>