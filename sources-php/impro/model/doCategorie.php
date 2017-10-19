<?php 

class Categorie {
	
	public $id;
	public $nom;
	public $type;
	public $path_folder;
	public $termine;
	public $ordre;
	
	public static function getTableName() {
		return "imp_categorie";
	}
}

?>