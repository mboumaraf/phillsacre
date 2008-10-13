<!DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Mail</title>

<link rel="stylesheet" type="text/css" href="<?= url::site('css/main.css') ?>" />

</head>

<body>

<div id="header">
<?= $header ?>
</div>
<div id="content">
<h1><?= $title ?></h1>

<?php
if (isset($errors)) {
	echo '<ul class="errors">';
	foreach ($errors as $error) {
		echo '<li>' . $error . '</li>';
	}
	echo '</ul>';
}
?>

<?= $content ?>
</div>
<div id="footer">
<?= $footer ?>
</div>

</body>
</html>