(function() {
	
	function recipeRated ()
	{
		var ratingUrl = '/recipes/'+$(this).attr('recipe-id')+'/rating/'+$(this).rateit('value');
		$.post (ratingUrl);
	}
	
	$('.rateit').bind ('rated', recipeRated);
	$('.rateit').bind ('reset', recipeRated);
})();