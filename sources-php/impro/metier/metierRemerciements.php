<?php
if (!isset($pathToPhpRoot)) {
	throw new Exception('$pathToPhpRoot n\'est pas défini'); 
}
require_once $pathToPhpRoot."model/database.php";
require_once $pathToPhpRoot."model/doRemerciements.php";

class MetierRemerciements {

	public static function getRemerciements() {
		return Database::getResultsObjects("SELECT * FROM ".Remerciements::getTableName(), "Remerciements");
	}

	public static function setRemerciement($post) {
		$texte = $post["remerciements_texte"];
		Database::beginTransaction();

		$texte = trim(str_replace("'", "''", $texte));

		Database::executeUpdate("DELETE FROM ".Remerciements::getTableName());
		if ($texte != "") {
			Database::executeUpdate("INSERT INTO ".Remerciements::getTableName()."(texte) VALUES('$texte')");
		}
		
		Database::commit();
	}

}

?>