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
	
	<ul>
		<?php
		foreach($categories as $categorie) {
			$transparent = $showAll ? "" : "transparent";
			$class = $categorie->termine ? "termine" : "";
		?>
		<li class="<?= $transparent ?>">
			<a href="#" class="<?= $class ?>" onClick="return false;">
				<?= $categorie->nom ?>
			</a>
		</li>
		<?php } ?>
	</ul>
</section>

<script>

	function init(status) {
		$('section').fadeIn(2000);
	}


	function afficheItem(li_item, temps) {
		$(li_item).animate({ opacity: 1 }, 500, function() {
			$(this).removeClass('transparent');
		});

		if ($(li_item).next('li') == undefined || temps == null) 
			return;

		setTimeout(function() {
			afficheItem($(li_item).next('li'), temps);
		}, temps);
	}

	function afficheListe() {
		afficheItem($('#categories ul li').first(), 100);
	}

	function afficheNext() {
		afficheItem($('#categories ul li.transparent').first(), null);
	}


	function goCategorie(id_categorie) {
		console.log("goCategorie : " + id_categorie);
		var returnObject = newReturnObject();
		var contentToInsert = null;
		
		$.ajax({
			type: 'POST', 
			url: 'categorie.php',
			data: {
				id_categorie : id_categorie
			}, 
			dataType : 'html',
			async : false,
			success: function(data, textStatus, jqXHR) {
				contentToInsert = data;
				
				returnObject.status = "OK";
				returnObject.message = "";
			},
			error: function(jqXHR, textStatus, errorThrown) {
				// alert("Une erreur est survenue : \n" + jqXHR.responseText);
				console.log(jqXHR);
				returnObject.status = "KO";
				returnObject.message = jqXHR.responseText;
			}
		});

		$('section').fadeOut(1500);
		
		setTimeout(function() {
			$('#content').html(contentToInsert);
			$('#currentScreen').val("CATEGORIE");
		}, 2000);

		console.log("returnObject");
		console.log(returnObject);
		return returnObject;
	}
	
	function goRemerciements() {
		var returnObject = newReturnObject();
		var contentToInsert = null;

		$.ajax({
			type: 'POST', 
			url: 'remerciements.php', 
			dataType : 'html',
			async : false,
			success: function(data, textStatus, jqXHR) {
				contentToInsert = data;

				returnObject.status = "OK";
				returnObject.message = "";
			},
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR);
				returnObject.status = "KO";
				returnObject.message = jqXHR.responseText;
			}
		});

		$('section').fadeOut(1500);
		
		setTimeout(function() {
			$('#content').html(contentToInsert);
			$('#currentScreen').val("REMERCIEMENTS");
		}, 2000);

		return returnObject;
	}

	$(document).ready(function() {
		init(STATUS);
		$('#watermark').fadeIn(1000);
	});
	
</script>