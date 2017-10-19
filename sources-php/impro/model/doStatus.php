<?php 

class Status {
	
	public $ecran;
	public $id_categorie;
	public $type_ecran;
	public $photos_choisies;
	public $integralite;
	public $photo_courante;
	public $statut_diapo;
	public $block_masques;
	
	public static $ECRAN_DEFAUT = 'SALLE_ATTENTE';
	
	public static function getTableName() {
		return 'imp_statut';
	}
	
}

?>