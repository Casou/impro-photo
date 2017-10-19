<?php 

$pathToPhpRoot = '../../';
include_once $pathToPhpRoot.'constants.php';
include_once $pathToPhpRoot.'constantsPrivate.php';
require_once $pathToPhpRoot."metier/metierCategorie.php";

$categories = MetierCategorie::getAllCategorie();

$showAll = isset($_GET['show_all']);

?>

<section id="categories">
	<h1>Catégories</h1>
	
	<?php if (!$showAll) { ?>
	<div id="showOneCategories" class="button" onClick="afficheNext();">Afficher 1 catégorie</div>
	<div id="showCategories" class="button" onClick="afficheListe();">Afficher les catégories</div>
	<?php } ?>
	
	<div id="retourIntro" class="button nextScreen" onClick="goRemerciements();">Remerciements >></div>
	
	<ul>
		<?php
		foreach($categories as $categorie) {
			$transparent = $showAll ? "" : "transparent";
			$class = $categorie->termine ? "termine" : "";
		?>
		<li class="<?= $transparent ?>">
			<a href="#" class="<?= $class ?>" onClick="launchCategorie(<?= $categorie->id ?>); return false;">
				<?= $categorie->nom ?>
			</a>
		</li>
		<?php } ?>
	</ul>
</section>

<script>

	function init(status) {
		console.log("Init categorie_liste.php");
		$('section').fadeIn(2000);
	}

	function afficheItem(li_item, temps) {
		$(li_item).animate({ opacity: 1 }, 500, function() {
			$(this).removeClass('transparent');
		});
		if ($(li_item).next('li') == undefined || temps == null) 
			return;

		setTimeout(function() {
			afficheItem($(li_item).next('li'), temps);
		}, temps);
	}

	function afficheListe() {
		getWindowOpener().afficheListe();

		afficheItem($('#categories ul li').first(), 100);
	}

	function afficheNext() {
		getWindowOpener().afficheNext();

		afficheItem($('#categories ul li.transparent').first(), null);
	}

	function launchCategorie(id_categorie) {
		debug("Launch categories");

		var params = new Object();
		params.action = "changeProperties";
		params.newScreen = "CATEGORIE";
		params.id_categorie = id_categorie;

		$.ajax({
			type: 'POST', 
			url: "<?= $pathToPhpRoot  ?>controller/mainController.php", 
			dataType : 'json',
			data : {
				action : "changeStatus",
				param : JSON.stringify(params)
			},
			async : false,
			success: function(data, textStatus, jqXHR) {
				console.log(data);
				if (data.status != 'OK') {
					alert("[" + data.status + "] " + data.message);
					console.log(data);
					return;
				} else {
					goCategorie(id_categorie);
				}
			}
		});
	}

	function goCategorie(id_categorie) {
		debug("Launch 1 categorie");

		var returnObject = newReturnObject();
		var contentToInsert = null;
		// Récupération du contenu HTML
		var urlScreen = "categorie.php";
		$.ajax({
			type: 'POST', 
			url: urlScreen,
			data: {
				id_categorie : id_categorie
			}, 
			dataType : 'html',
			async : false,

			success: function(data, textStatus, jqXHR) {
				contentToInsert = data;
				
				console.log("Catégorie récupérée");
				returnObject.status = "OK";
				returnObject.message = "";
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Une erreur est survenue lors de la recuperation de " + urlScreen + " : \n" + jqXHR.responseText);
				console.log(jqXHR);
				returnObject.status = "KO";
				returnObject.message = jqXHR.responseText;
			}
		});

		if (returnObject.status != "OK") {
			return;
		}

		// On envoie l'ordre au parent (spectateur)
		debug("OPENER Launch 1 categorie");
		var resultManage = getWindowOpener().goCategorie(id_categorie);
		if (resultManage == undefined || resultManage.status != "OK") {
			if (!confirm("Un problème semble être apparu sur le traitement spectateur : " + 
				"[" + resultManage.status + "] " + resultManage.message + ".\n" + 
				"Si l'action a bien été lancée malgré tout, cliquez sur OK. " +
				"Si l'interface spectateur n'a pas reçu l'ordre, cliquez sur Annuler."))
			return;
		}
		
		// On anime le changement d'écran
		$('section').fadeOut(1500);
		setTimeout(function() {
			$('#content').html(contentToInsert);
			$('#currentScreen').val("CATEGORIE");
		}, 2000);
	}


	function goRemerciements() {
		debug("goRemerciements");

		var returnObject = newReturnObject();
		var contentToInsert = null;
		// Récupération du contenu HTML
		var urlScreen = "remerciements.php";
		$.ajax({
			type: 'POST', 
			url: urlScreen,
			dataType : 'html',
			async : false,

			success: function(data, textStatus, jqXHR) {
				contentToInsert = data;
				
				returnObject.status = "OK";
				returnObject.message = "";
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Une erreur est survenue lors de la recuperation de " + urlScreen + " : \n" + jqXHR.responseText);
				console.log(jqXHR);
				returnObject.status = "KO";
				returnObject.message = jqXHR.responseText;
			}
		});

		if (returnObject.status != "OK") {
			return;
		}

		// On envoie l'ordre au parent (spectateur)
		debug("OPENER goRemerciements");
		var resultManage = getWindowOpener().goRemerciements();
		if (resultManage == undefined || resultManage.status != "OK") {
			if (!confirm("Un problème semble être apparu sur le traitement spectateur : " + 
				"[" + resultManage.status + "] " + resultManage.message + ".\n" + 
				"Si l'action a bien été lancée malgré tout, cliquez sur OK. " +
				"Si l'interface spectateur n'a pas reçu l'ordre, cliquez sur Annuler."))
			return;
		}

		var params = new Object();
		params.action = "changeProperties";
		params.newScreen = "REMERCIEMENTS";
		params.cancelSelection = "cancelSelection";
		$.ajax({
			type: 'POST', 
			url: "<?= $pathToPhpRoot  ?>controller/mainController.php", 
			dataType : 'json',
			data : {
				action : "changeStatus",
				param : JSON.stringify(params)
			},
			async : false,
			success: function(data, textStatus, jqXHR) {
				console.log(data);
				if (data.status != 'OK') {
					alert("[" + data.status + "] " + data.message);
					console.log(data);
					return;	
				}
			}
		});
		
		// On anime le changement d'écran
		$('section').fadeOut(1500);
		setTimeout(function() {
			$('#content').html(contentToInsert);
			$('#currentScreen').val("REMERCIEMENTS");
		}, 2000);

	}
	

	$(document).ready(function() {
		init(STATUS);
	});
</script>