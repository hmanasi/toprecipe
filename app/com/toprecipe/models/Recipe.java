package com.toprecipe.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

import play.data.validation.Constraints.Required;

@Entity
@SequenceGenerator(name = "recipe_seq_gen", sequenceName = "recipe_seq", initialValue = 1)
@Table(name = "recipe")
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_seq_gen")
	private Long id;

	@Required
	@Column(nullable = false)
	private String title;

	private String image;

	@Column(name = "you_tube_video")
	private String youTubeVideo;

	@Column(name = "flash_video")
	private String flashVideo;

	@Column(name = "source_url")
	private String sourceUrl;

	@ManyToOne
	@JoinColumn(name = "food_item_id")
	private FoodItem foodItem;

	@Column(name="average_rating")
	private Float averageRating;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYouTubeVideo() {
		return youTubeVideo;
	}

	public void setYouTubeVideo(String youTubeVideo) {
		this.youTubeVideo = youTubeVideo;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public FoodItem getFoodItem() {
		return foodItem;
	}

	public void setFoodItem(FoodItem foodItem) {
		this.foodItem = foodItem;
	}

	public String getFlashVideo() {
		return flashVideo;
	}

	public void setFlashVideo(String flashVideo) {
		this.flashVideo = flashVideo;
	}

	public boolean isVideoRecipe() {
		return isYouTubeRecipe() || isFlashRecipe();
	}

	public boolean isFlashRecipe() {
		return !StringUtils.isEmpty(getFlashVideo());
	}

	public boolean isYouTubeRecipe() {
		return !StringUtils.isEmpty(getYouTubeVideo());
	}

	public String getVideoUrl() {
		if (isYouTubeRecipe()) {
			return getYouTubeVideo();
		} else {
			return getFlashVideo();
		}
	}

	public Float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Float averageRating) {
		this.averageRating = averageRating;
	}
}
