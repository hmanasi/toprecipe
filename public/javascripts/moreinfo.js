(function() {

	recipeInfoClicked = function(e) {
		e.preventDefault();
		var recipe = recipes[$(this).attr('itemId')];
		generateDialogMarkup(recipe.title, recipe.image, recipe.youTubeVideo,
				recipe.flashVideo, recipe.sourceUrl);
		$("#recipe-info-modal").modal();
	};

	function isYouTubeVideo(youTubeVideo) {
		return !isEmptyString(youTubeVideo);
	}

	function isFlashVideo(flashVideo) {
		return !isEmptyString(flashVideo);
	}

	function isEmptyString(str) {
		if (str == null || str.trim().length == 0) {
			return true;
		}
		return false
	}

	function isImage(image) {
		return !isEmptyString(image);
	}

	function getYouTubeMarkup(youTubeVideo) {
		return "<iframe width=\"500\" height=\"300\" src=\"" + youTubeVideo
				+ "\" frameborder=\"0\" allowfullscreen></iframe>";
	}

	function getFlashMarkup(flashVideo) {
		return '<object width="500" height="300" data="' + flashVideo
				+ '" type="application/x-shockwave-flash"> '
				+ '<param value="flashVideo" name="movie"> \n'
				+ '<param value="true" name="allowFullScreen"> \n'
				+ '<param value="always" name="allowscriptaccess"> \n'
				+ '<param value="transparent" name="wmode"></object>';
	}

	function getImageMarkup(image) {
		return '<img src="' + image + '" width=\"500\" />';
	}

	function getMediaMarkup(image, youTubeVideo, flashVideo) {
		if (isYouTubeVideo(youTubeVideo)) {
			return getYouTubeMarkup(youTubeVideo);
		} else if (isFlashVideo(flashVideo)) {
			return getFlashMarkup(flashVideo);
		} else if (isImage(image)) {
			return getImageMarkup(image)
		}
	}

	generateDialogMarkup = function(title, image, youTubeVideo, flashVideo,
			sourceUrl) {

		var source = "<a style=\"color:#428BCA\" href=\"" + sourceUrl
				+ "\" target= \"_blank\"><h4>Go to original site</h4></a>"

		$("#recipe-title").html(title);
		$("#media-markup")
				.html(getMediaMarkup(image, youTubeVideo, flashVideo));
		$("#source").html(source);

	}

	$("[name=recipe-info-button]").click(recipeInfoClicked);
	$(".recipe-image-container").click(recipeInfoClicked);
}());
