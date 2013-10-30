(function () {
    
	deleteClicked = function (e)
	{
		var $deleteForm = $('#deleteForm-'+$(this).attr('recipeId'));
		$deleteForm.submit();
	}
	
	editClicked = function (e)
	{
		var $editForm = $('#editForm-'+$(this).attr('recipeId'));
		$editForm.submit();
	}
	
	$("[name=delete-button]").click (deleteClicked);
	$("[name=edit-button]").click (editClicked);
	
}());

