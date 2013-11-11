package com.toprecipe.services;

/**
 * POJO that has information of food-item and the top recipe in the food-item
 */
public class TopRecipe {

	private Long foodItemId;
	private String foodItemTitle;

	private Long recipeId;
	private String image;
	private String youTubeVideo;
	private String sourceUrl;
	private String flashVideo;

	public TopRecipe(Long foodItemId, String foodItemTitle, Long maxId,
			Long recipeId, String image, String youTubeVideo,
			String flashVideo, String sourceUrl) {
		this.foodItemId = foodItemId;
		this.foodItemTitle = foodItemTitle;
		this.recipeId = recipeId;
		this.image = image;
		this.youTubeVideo = youTubeVideo;
		this.flashVideo = flashVideo;
		this.sourceUrl = sourceUrl;
	}

	public Long getFoodItemId() {
		return foodItemId;
	}

	public void setFoodItemId(Long foodItemId) {
		this.foodItemId = foodItemId;
	}

	public String getFoodItemTitle() {
		return foodItemTitle;
	}

	public void setFoodItemTitle(String foodItemTitle) {
		this.foodItemTitle = foodItemTitle;
	}

	public Long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Long recipeId) {
		this.recipeId = recipeId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getFlashVideo() {
		return flashVideo;
	}

	public void setFlashVideo(String flashVideo) {
		this.flashVideo = flashVideo;
	}

	public String getYouTubeVideo() {
		return youTubeVideo;
	}

	public void setYouTubeVideo(String youTubeVideo) {
		this.youTubeVideo = youTubeVideo;
	}
	
	@Override
	public String toString() {
		return "TopRecipe [foodItemId=" + foodItemId + ", foodItemTitle="
				+ foodItemTitle + ", recipeId=" + recipeId + ", image=" + image
				+ ", youTubeVideo=" + youTubeVideo + ", sourceUrl=" + sourceUrl
				+ ", flashVideo=" + flashVideo + "]";
	}
}
