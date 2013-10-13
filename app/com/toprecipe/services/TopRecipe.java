package com.toprecipe.services;

/**
 * POJO that has information of food-item and the top recipe in the food-item
 */
public class TopRecipe {

	private Long foodItemId;
	private String foodItemTitle;

	private Long recipeId;
	private String image;
	private String videoUrl;
	private String sourceUrl;

	public TopRecipe(Long foodItemId, String foodItemTitle, Long maxId, Long recipeId,
			String image, String videoUrl, String sourceUrl) {
		this.foodItemId = foodItemId;
		this.foodItemTitle = foodItemTitle;
		this.recipeId = recipeId;
		this.image = image;
		this.videoUrl = videoUrl;
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

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	@Override
	public String toString() {
		return "TopRecipe [foodItemId=" + foodItemId + ", foodItemTitle="
				+ foodItemTitle + ", recipeId=" + recipeId + ", image=" + image
				+ ", videoUrl=" + videoUrl + ", sourceUrl=" + sourceUrl + "]";
	}

}
