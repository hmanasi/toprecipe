(function () {
    
	recipeinfoClicked = function (e) 
	{
		var recipe = recipes[$(this).attr('foodItemId')];
		generateDialogMarkup (recipe.title, recipe.videoUrl);
		$("#recipe-info-modal").modal();
	};
	
	generateDialogMarkup = function (title, videoUrl) 
	{
		var iframe = "<iframe width=\"537\" height=\"315\" src=\"" +videoUrl+ "\" frameborder=\"0\" allowfullscreen></iframe>";
		$("#recipe-title").html (title);
		$("#iframe").html (iframe);
	}
	
	$("[name=recipe-info-button]").click (recipeinfoClicked);
	$("#recipe-image-container").click (recipeinfoClicked);
	
	
}());
