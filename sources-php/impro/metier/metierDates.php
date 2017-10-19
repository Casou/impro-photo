<?php
if (!isset($pathToPhpRoot)) {
	throw new Exception('$pathToPhpRoot n\'est pas défini'); 
}
require_once $pathToPhpRoot."model/database.php";
require_once $pathToPhpRoot."model/doDates.php";

class MetierDates {

	public static function getFutureDates() {
		return Database::getResultsObjects("SELECT * FROM ".Dates::getTableName()." WHERE date >= now() ORDER BY date ASC LIMIT 0,5", "Dates");
	}

	public static function insertDate($date, $nom) {
		Database::beginTransaction();

		$nom = str_replace("'", "''", $nom);
		
		Database::executeUpdate("INSERT INTO ".Dates::getTableName()."(date, nom) VALUES('$date', '$nom')");
		
		Database::commit();
	}

	public static function manageDates($post) {
		$id_array = $post['date_id'];
		$date_array = $post['date_date'];
		$nom_array = $post['date_nom'];
		$indice = 0;
		$in_clause = '';

		Database::beginTransaction();
		
		foreach($id_array as $id) {
			$nom = str_replace("'", "''", $nom_array[$indice]);
			$date = $date_array[$indice];
			$indice++;
			
			if ($id == "") {
				Database::executeUpdate("INSERT INTO ".Dates::getTableName()."(nom, date) VALUES ('$nom', STR_TO_DATE('$date', '%d/%m/%Y'))");
				$id = Database::getLastInsertId();
			} else {
				Database::executeUpdate("UPDATE ".Dates::getTableName()." SET nom = '$nom', date=STR_TO_DATE('$date', '%d/%m/%Y') WHERE id=$id");
			}
			
			$in_clause .= ($in_clause != "") ? ", $id" : $id;
		}
		
		if ($in_clause != "") {
			Database::executeUpdate("DELETE FROM ".Dates::getTableName()." WHERE id NOT IN ($in_clause)");
		}
		
		Database::commit();
	}

}

?>