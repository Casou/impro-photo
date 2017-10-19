<?php
include_once 'JSON.php';

DEFINE('JSON_STATUS_OK', 'OK');
DEFINE('JSON_STATUS_WARNING', 'WARNING');
DEFINE('JSON_STATUS_KO', 'KO');

DEFINE('JSON_STATUS_RUNNING', 'RUNNING');

class JsonResponse {
	
	public $status;
	public $message;
	public $infos;
	
	function __construct($status, $message = null, $infos = null) {
		$this->status = $status;
		$this->message = $message;
		$this->infos = $infos;
	}
	
}

function json_encode_utf8($var) {
	$json = new Services_JSON();
	return $json->encode($var);
}

function json_decode_utf8($var) {
	$json = new Services_JSON();
	return $json->decode($var);
}


?>