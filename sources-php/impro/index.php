<?php 
$pathToPhpRoot = './';

include_once $pathToPhpRoot.'constants.php';
include_once $pathToPhpRoot.'constantsPrivate.php';
include_once $pathToPhpRoot.'metier/metierStatus.php';
include_once $pathToPhpRoot.'metier/metierCategorie.php';
include_once $pathToPhpRoot.'metier/metierRemerciements.php';
include_once $pathToPhpRoot.'metier/metierDates.php';

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
	
	<link rel="stylesheet" href="style/style.css" />
	<link rel="stylesheet" href="style/style_index.css" />
	<link rel="shortcut icon" HREF="style/images/GIT-logo.jpg">
</head>
<body id="index">
	<header>
		<img src="style/images/logo/git_logo_rose.png" />
	</header>
	
	<main>
		<div id="menu">
			<ul>
				<li><a href="ihm/admin/newImpro.php" class="button">Préparation</a> <span>Préparation d'une nouvelle impro.</span></li>
				<li><a href="ihm/spectateur" class="button">Impro photo</a> <span>Lancer l'impro photo. Touche "R" pour l'interface Régie.</span></li>
			</ul>
		</div>

		<div id="check_impro">
			<ul>
				<?php
					$status_categories = "OK";
					$status_css = "ok";
					foreach($categories as $categorie) {
						$folder_path = $pathToPhpRoot.FOLDER_PHOTO_IMPRO.$categorie->path_folder;
						if (!file_exists(utf8_decode($folder_path))) {
							$status_categories = "KO";
							$status_css = "ko";
						}
					} ?>
				<li><span class="check_label">Chemin des catégories : </span><span class="check_status <?= $status_css ?>"><?= $status_categories ?></span></li>

				<?php
					$status_presentation = "";
					$status_css = "ok";
					if (!file_exists(FOLDER_VIDEO_INTRO."joueurs.mp4")) {
						$status_presentation .= "Vidéo des joueurs manquant. ";
						$status_css = "ko";
					}
					if (!file_exists(FOLDER_VIDEO_INTRO."presentateur.mp4")) {
						$status_presentation .= "Vidéo du présentateur manquant. ";
						$status_css = "ko";
					}
					if ($status_presentation == "") {
						$status_presentation = "OK";
					}
				?>
				<li><span class="check_label">Vidéos de présentation : </span><span class="check_status <?= $status_css ?>"><?= $status_presentation ?></span></li>

				<?php 
					$status_remerciement = "Aucun";
					$status_css = "neutre";
					if (count($remerciements) > 0) {
						$status_remerciement = 'Rédigé <img src="style/images/info.png" title="'.$remerciements[0]->texte.'" />';
						$status_css = "";
					}
				?>
				<li><span class="check_label">Texte de remerciements : </span><span class="check_status <?= $status_css ?>"><?= $status_remerciement ?></span></li>
				

				<?php 
					$nbPhotos=0;
					$myDirectory = opendir($pathToPhpRoot.FOLDER_JOUEURS) or die('Erreur');
					while(false !== ($entry = @readdir($myDirectory))) {
						$ext = strtoupper(pathinfo($entry, PATHINFO_EXTENSION));
						if($entry != '.' && $entry != '..' 
							&& ($ext == "JPG" || $ext == "JPEG" || $ext == "PNG" || $ext == " GIF")) {
							$nbPhotos++;
						}
					}

					$status_joueurs = "OK";
					$status_css = "ok";
					if ($nbPhotos != 5) {
						$status_joueurs = $nbPhotos." photo(s) dans le dossier (Attendus : 5)";
						$status_css = "ko";
					}
				?>
				<li><span class="check_label">Photos des joueurs : </span><span class="check_status <?= $status_css ?>"><?= $status_joueurs ?></span></li>

				<?php
					$datesDefinies = count($dates);
					if ($datesDefinies > 1) {
						$datesDefinies .= " futures dates définies";
					} else {
						$datesDefinies .= " future date définie";
					}
				?>
				<li><span class="check_label">Photo de présentation des dates : </span><span class="check_status"><?= $datesDefinies ?></span></li>
			</ul>
		</div>

	</main>
	
	<script>

	</script>

</body>
</html>