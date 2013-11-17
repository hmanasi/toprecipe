(function() {

	selectImage = function (e)
	{
		e.preventDefault();
		var $parent = $(this).parent();
		
		if ($parent.hasClass ('selected')){
			$parent.removeClass ('selected');
			$('#recipe-form input[name=Image]').attr('value','');
		}else{
			$('.image-selector').removeClass ('selected');
			
			$parent.addClass('selected');
			$('#recipe-form input[name=Image]').attr('value',$parent.attr('img'));
		}
	}
	
	selectVideo = function (e)
	{
		e.preventDefault();
		var $parent = $(this).parent();

		if ($parent.hasClass ('selected')){
			$parent.removeClass ('selected');
			
			$('#recipe-form input[name=YouTubeVideo]').attr('value','');
			$('#recipe-form input[name=FlashVideo]').attr('value','');

		}else {
			$('.video-selector').removeClass ('selected');
			
			$parent.addClass('selected');
			
			$('#recipe-form input[name=YouTubeVideo]').attr('value',$parent.attr('youTubeVideo'));
			$('#recipe-form input[name=FlashVideo]').attr('value',$parent.attr('flashVideo'));
		}
	}
	
	createRecipe = function (e)
	{
		$('#recipe-form').submit();
	}
	
	cancelRecipe = function (e)
	{
		$('#cancel-handler-form').submit();
	}
	
	$(".select-image-button").click (selectImage);
	$(".select-video-button").click (selectVideo);
	$("#create-button").click (createRecipe);
	$("#cancel-button").click (cancelRecipe);
	
})();