package com.toprecipe.services.dataimport;

public class RecipeJson {
	private String title;

	private String image;

	private String videoUrl;

	private String sourceUrl;

	private String foodItemTitle;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getFoodItemTitle() {
		return foodItemTitle;
	}

	public void setFoodItemTitle(String foodItemTitle) {
		this.foodItemTitle = foodItemTitle;
	}
}
