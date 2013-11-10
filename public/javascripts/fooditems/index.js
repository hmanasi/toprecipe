(function () {
    
	recipeinfoClicked = function (e) 
	{
		e.preventDefault();
		var recipe = recipes[$(this).attr('foodItemId')];
		generateDialogMarkup (recipe.title, recipe.videoUrl, recipe.sourceUrl);
		$("#recipe-info-modal").modal();
	};
	
	generateDialogMarkup = function (title, videoUrl, sourceUrl) 
	{
		var iframe = "<iframe width=\"537\" height=\"315\" src=\"" +videoUrl+ "\" frameborder=\"0\" allowfullscreen></iframe>";
		var source = "<a href=\"" +sourceUrl+ "\" target= \"_blank\"><h4>Go to original site</h4></a>"
		$("#recipe-title").html (title);
		$("#iframe").html (iframe);
		$("#source").html (source);
		
	}
	
	$("[name=recipe-info-button]").click (recipeinfoClicked);
	$("#recipe-image-container").click (recipeinfoClicked);
}());
