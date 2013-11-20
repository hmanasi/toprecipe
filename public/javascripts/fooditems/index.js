(function() {
	listRecipesClicked = function(e) {
		var recipeId = $(this).attr('itemId');
		$("#list-recipes-form-" + recipeId).submit();
	};

	$("[name=list-recipes-button]").click(listRecipesClicked);
}());
