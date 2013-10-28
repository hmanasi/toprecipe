(function () {
    
	recipeinfoClicked = function (e) 
	{
		var recipe = recipes[$(this).attr('foodItemId')];
		generateDialogMarkup (recipe.title, recipe.videoUrl);
		$("#recipe-info-modal").modal();
	};
	
	generateDialogMarkup = function (title, videoUrl) 
	{
		$("#recipe-title").html (title);
		$("#recipe-video-url").html (videoUrl);
	}
	
	$("[name=recipe-info-button]").click (recipeinfoClicked);
	$("#recipe-image-container").click (recipeinfoClicked);
}());
