<?php 

if (!isset($pathToPhpRoot)) {
	throw new Exception('$pathToPhpRoot n\'est pas defini'); 
}
require_once $pathToPhpRoot."model/database.php";
require_once $pathToPhpRoot."model/doStatus.php";
require_once $pathToPhpRoot."model/doCategorie.php";

class MetierStatus {
	
	public static function reinitStatus() {
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur=null");
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur='".Status::$ECRAN_DEFAUT."' where champ='ecran'");
		Database::executeUpdate("UPDATE ".Categorie::getTableName()." SET termine=null");
	}
	
	
	public static function getStatus() {
		$statusObj = new Status();
		
		$status_attr = Database::getResultsObjects("SELECT * FROM ".Status::getTableName(), "StdObject");
		foreach($status_attr as $status) {
				
			switch ($status->champ) {
				case "ecran" :
					$statusObj->ecran = $status->valeur;
					break;
				case "id_categorie" :
					$statusObj->id_categorie = $status->valeur;
					break;
				case "type_ecran" :
					$statusObj->type_ecran = $status->valeur;
					break;
				case "photos_choisies" :
					$statusObj->photos_choisies = array();
					if ($status->valeur != null) {
						$photos = explode(',', $status->valeur);
						foreach ($photos as $photo) {
							$statusObj->photos_choisies[$photo] = $photo;
						}
					}
					break;
				case "integralite" :
					$statusObj->integralite = ($status->valeur == 1);
					break;
				case "photo_courante" :
					$statusObj->photo_courante = $status->valeur;
					break;
				case "statut_diapo" :
					$statusObj->statut_diapo = $status->valeur;
					break;
				case "block_masques" :
					$statusObj->block_masques = array();
					if ($status->valeur != null) {
						$photos = explode(',', $status->valeur);
						foreach ($photos as $photo) {
							$statusObj->block_masques[$photo] = $photo;
						}
					}
					break;
				default :
					break;
			}
		}
	
		if ($statusObj->integralite == null) {
			$statusObj->integralite = false;
		}
	
		if ($statusObj->ecran == null) {
			$statusObj->ecran = Status::$ECRAN_DEFAUT;
			$statusObj->save();
		}
		
		return $statusObj;
	}
	
	public static function saveStatus($statusObj) {
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur='$statusObj->ecran' where champ='ecran'");
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur='$statusObj->id_categorie' where champ='id_categorie'");
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur='$statusObj->type_ecran' where champ='type_ecran'");
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur='".implode(',', $statusObj->photos_choisies)."' where champ='photos_choisies'");
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur='$statusObj->integralite' where champ='integralite'");
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur='$statusObj->photo_courante' where champ='photo_courante'");
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur='$statusObj->statut_diapo' where champ='statut_diapo'");
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur='".implode(',', $statusObj->block_masques)."' where champ='block_masques'");
		Database::executeUpdate("UPDATE ".Status::getTableName()." SET valeur=null where valeur=''");
	}
	
}

?>