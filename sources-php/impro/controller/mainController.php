<?php
$pathToPhpRoot = "../";

include_once $pathToPhpRoot."constants.php";
include_once $pathToPhpRoot."constantsPrivate.php";
include_once $pathToPhpRoot.'tools/json/jsonResponse.php';
include_once $pathToPhpRoot.'metier/metierStatus.php';
include_once $pathToPhpRoot.'metier/metierCategorie.php';

$action = $_REQUEST['action'];

$response = new JsonResponse(JSON_STATUS_WARNING, "No response defined");
switch ($action) {
	case 'status':
		$status = MetierStatus::getStatus();
		$response = new JsonResponse(JSON_STATUS_OK, $status);
		break;

	case 'resetStatus':
		MetierStatus::reinitStatus();
		$status = MetierStatus::getStatus();
		$response = new JsonResponse(JSON_STATUS_OK, $status);
		break;

	case 'changeStatus':
		$message = changeStatus(json_decode_utf8($_POST['param']));
		if (startsWith($message, "******")) {
			$response = new JsonResponse(JSON_STATUS_KO, $message);
		} else {
			$response = new JsonResponse(JSON_STATUS_OK, $message);
		}
		break;

	case 'categorie':
		$message = changeCategorie(json_decode_utf8($_POST['param']));
		if (startsWith($message, "******")) {
			$response = new JsonResponse(JSON_STATUS_KO, $message);
		} else {
			$response = new JsonResponse(JSON_STATUS_OK, $message);
		}
		break;

	default:
		break;
}




function changeStatus($json_obj) {
	$message = "";
	$status = MetierStatus::getStatus();
	$action = $json_obj->action;
	if ($action == "changeProperties") {

		$message .= "*** CHANGE PROPERTIES ***\n";

		if (isset($json_obj->newScreen)) {
			$status->ecran = $json_obj->newScreen;
			$message .= "New Screen : ".$status->ecran."\n";
		}
		
		if (isset($json_obj->id_categorie)) {
			$status->id_categorie = $json_obj->id_categorie;
			$message .= "New id categorie : ".$status->id_categorie."\n";
		}
		
		if (isset($json_obj->addPhoto)) {
			$status->photos_choisies[$json_obj->addPhoto] = $json_obj->addPhoto;
			$message .= "Nouvelle photo : ".print_r($status->photos_choisies, true)."\n";
		}
		
		if (isset($json_obj->removePhoto)) {
			unset($status->photos_choisies[$json_obj->removePhoto]);
			$message .= "Photo retiree : ".print_r($status->photos_choisies, true)."\n";
		}
		if (isset($json_obj->cancelSelection)) {
			$status->photos_choisies = array();
			$status->block_masques = array();
			$status->statut_diapo = null;
			$status->photo_courante = null;
			$status->integralite = null;
			$message .= "Toutes les photos sont retirees : ".print_r($status->photos_choisies, true)."\n";
		}
		
		if (isset($json_obj->statutDiapo)) {
			$status->statut_diapo = $json_obj->statutDiapo;
			$message .= "Statut diapo : ".$status->statut_diapo."\n";
		}
		
		if (isset($json_obj->photo_courante)) {
			$status->photo_courante = $json_obj->photo_courante;
			$status->block_masques = array();
			$message .= "Photo affichée : ".$status->photo_courante."\n";
		}
		
		if (isset($json_obj->unset_photo_courante)) {
			$status->photo_courante = null;
			$message .= "Aucune photo affichée : ".$status->photo_courante."\n";
		}
		
		if (isset($json_obj->addAllPhoto)) {
			$status->integralite = 1;
			$status->photos_choisies = $json_obj->addAllPhoto;
			$message .= "Toutes les photos sont choisies : ".print_r($status->photos_choisies, true)."\n";
		}
		
		if (isset($json_obj->reveal_block)) {
			$status->block_masques[$json_obj->reveal_block] = $json_obj->reveal_block;
			$message .= "Block révélé : ".$json_obj->reveal_block."\n";
		}
		
		MetierStatus::saveStatus($status);
		
	} else {
		$message .= "********** ERREUR : changeStatus, action inconnue : $action **********\n";
	}

	return $message;
}

function changeCategorie($json_obj) {
	try {
		$action = $json_obj->action;
		if ($action == "termine") {
			MetierCategorie::updateCategorieTerminee($json_obj->id_categorie);
			return "Categorie ".$json_obj->id_categorie." terminee\n"; 
		} else {
			return "********** ERREUR : changeCategorie, action inconnue : $action **********\n";
		}
	} catch (Exception $e) {
		return "********** ERREUR : changeCategorie, Exception : ".print_r($e, true)." **********\n";
	}
}



echo json_encode_utf8($response);

?>