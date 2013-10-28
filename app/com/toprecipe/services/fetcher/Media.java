package com.toprecipe.services.fetcher;

import java.util.ArrayList;
import java.util.List;

public class Media {

	private List<Image> images = new ArrayList<>();
	private List<String> youTubeVideos = new ArrayList<>();
	private List<String> flashVideos = new ArrayList<>();

	public List<Image> getImages() {
		return images;
	}

	public void addImage(Image image) {
		images.add(image);
	}

	public List<String> getYouTubeVideos() {
		return youTubeVideos;
	}

	public void addYouTubeVideo(String youTubeVideo) {
		youTubeVideos.add(youTubeVideo);
	}

	public List<String> getFlashVideos() {
		return flashVideos;
	}

	public void addFlashVideos(String flashVideo) {
		flashVideos.add(flashVideo);
	}
}
