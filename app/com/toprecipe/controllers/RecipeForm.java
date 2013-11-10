package com.toprecipe.controllers;

import play.data.validation.Constraints.Required;

public class RecipeForm {
	@Required
	private String title;

	private String image;

	private String youTubeVideo;

	private String flashVideo;

	@Required
	private String sourceUrl;

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

	public String getYouTubeVideo() {
		return youTubeVideo;
	}

	public void setYouTubeVideo(String youTubeVideo) {
		this.youTubeVideo = youTubeVideo;
	}

	public String getFlashVideo() {
		return flashVideo;
	}

	public void setFlashVideo(String flashVideo) {
		this.flashVideo = flashVideo;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
}
