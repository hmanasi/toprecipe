package com.toprecipe.services.fetcher;

import org.springframework.stereotype.Component;

@Component
public class ImageFilter {
	private static final int MIN_WIDTH = 100;
	private static final int MIN_HEIGHT = 100;

	public boolean reject(Image image) {
		if (image.getWidth() == null || image.getHeight() == null) {
			return false;
		}

		if (image.getWidth() < MIN_WIDTH || image.getHeight() < MIN_HEIGHT) {
			return true;
		}

		return false;
	}

}
