<?php 

define('FOLDER_CSS', 'style/');
define('FOLDER_CSS_IMAGES', 'style/images/');
define('FOLDER_RESSOURCES', 'style/ressources/');
define('FOLDER_JS', 'js/');
define('FOLDER_PHOTO_INTRO', 'photos/intro/');
define('FOLDER_VIDEO_INTRO', 'photos/videos_intro/');
define('FOLDER_PHOTO_IMPRO', 'photos/impro/');
define('FOLDER_PHOTO_DATES', 'photos/dates/');
define('FOLDER_JOUEURS', 'photos/joueurs/');

define('CATEGORY_TYPE_PHOTO', 'PHOTO');
define('CATEGORY_TYPE_POLAROID', 'POLAROID');

$ALL_TYPES = array(
	CATEGORY_TYPE_PHOTO => "Affichage normal",
	CATEGORY_TYPE_POLAROID => "Affichage polaroïd"
);


function generateRandom($nb_chars = 10) {
	$text = "";
	$possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	for($i = 0; $i < $nb_chars; $i++) {
		$text .= substr($possible, floor(rand(0, strlen($possible))), 1);
	}

	return $text;
}

function startsWith($haystack, $needle) {
    // search backwards starting from haystack length characters from the end
    return $needle === "" || strrpos($haystack, $needle, -strlen($haystack)) !== false;
}

function endsWith($haystack, $needle) {
    // search forward starting from end minus needle length characters
    return $needle === "" || (($temp = strlen($haystack) - strlen($needle)) >= 0 && strpos($haystack, $needle, $temp) !== false);
}

function parseDate($date) {

}

function formatDate($date, $format = 'd/m/Y') {
	$dateTime = new DateTime($date);
	return $dateTime->format($format);
}


$MONTH_LABEL = array(1 => "Janv", 
	2 => "Fév", 
	3 => "Mars", 
	4 => "Avr", 
	5 => "Mai", 
	6 => "Juin", 
	7 => "Juil", 
	8 => "Août", 
	9 => "Sept", 
	10 => "Oct", 
	11 => "Nov", 
	12 => "Déc");

function formatDateToDisplay($date) {
	global $MONTH_LABEL;
	$dateTime = new DateTime($date);
	return '<span class="date_display"><span class="date_day">'.$dateTime->format("d").'</span><span class="date_month">'.$MONTH_LABEL[$dateTime->format("n")].'</span></span>';
}

?>