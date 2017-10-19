<?php 

if (!isset($pathToPhpRoot)) {
	throw new Exception('$pathToPhpRoot n\'est pas défini'); 
}
require_once $pathToPhpRoot."model/database.php";
require_once $pathToPhpRoot."model/doCategorie.php";

class MetierCategorie {
	
	public static function getAllCategorie() {
		return Database::getResultsObjects("SELECT * FROM ".Categorie::getTableName()." ORDER BY ordre ASC", "Categorie");
	}
	
	public static function getCategorieById($id) {
		return Database::getResultsObjects("SELECT * FROM ".Categorie::getTableName()." where id=$id", "Categorie")[0];
	}
	
	public static function updateCategorieTerminee($id_categorie) {
		Database::executeUpdate("UPDATE ".Categorie::getTableName()." SET termine = 1 WHERE id=$id_categorie");
	}

	public static function manageCategories($post) {
		$id_array = $post['categorie_id'];
		$nom_array = $post['categorie_nom'];
		$type_array = $post['categorie_type'];
		$path_array = $post['categorie_path'];
		$indice = 0;
		$in_clause = '';

		Database::beginTransaction();
		
		foreach($id_array as $id) {
			$nom = str_replace("'", "''", $nom_array[$indice]);
			$type = $type_array[$indice];
			$path = str_replace("'", "''", $path_array[$indice]);
			$indice++;
			
			if ($id == "") {
				Database::executeUpdate("INSERT INTO ".Categorie::getTableName()."(nom, type, ordre, path_folder) VALUES ('$nom', '$type', $indice, '$path')");
				$id = Database::getLastInsertId();
			} else {
				Database::executeUpdate("UPDATE ".Categorie::getTableName()." SET nom = '$nom', type='$type', path_folder='$path', ordre=$indice WHERE id=$id");
			}
			
			$in_clause .= ($in_clause != "") ? ", $id" : $id;
		}
		
		if ($in_clause != "") {
			Database::executeUpdate("DELETE FROM ".Categorie::getTableName()." WHERE id NOT IN ($in_clause)");
		}
		
		Database::commit();
	}
	
	
}

?>