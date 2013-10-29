package com.toprecipe.services.fetcher;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

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

	/**
	 * Rejects the image if the size can not be determined or if it is smaller
	 * than the minimum dimensions
	 * 
	 * @param image
	 * @return
	 */
	public boolean rejectFile(Image image) {
		try {
			Dimension d = getImageDim(image.getFile().getCanonicalPath());

			if (d != null) {
				if (d.getWidth() >= MIN_WIDTH || d.getHeight() >= MIN_HEIGHT) {
					return false;
				}
			}

		} catch (IOException e) {
			// TODO: fix exception handeling logging
			e.printStackTrace();
		}
		return true;
	}

	private Dimension getImageDim(final String path) {
		Dimension result = null;
		String suffix = this.getFileSuffix(path);

		if (suffix == null) {
			// TODO: fix logging
			System.out.println("File suffix is null");
			return null;
		}

		Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
		if (iter.hasNext()) {
			ImageReader reader = iter.next();
			try {
				ImageInputStream stream = new FileImageInputStream(new File(
						path));
				reader.setInput(stream);
				int width = reader.getWidth(reader.getMinIndex());
				int height = reader.getHeight(reader.getMinIndex());
				result = new Dimension(width, height);
			} catch (IOException ex) {
				ex.printStackTrace(); // TODO: fix exception handling
			} finally {
				reader.dispose();
			}
		} else {
			// TODO: fix logging
			System.out.println("No reader found for given format: " + suffix);
		}
		return result;
	}

	private String getFileSuffix(String path) {
		String fileNames[] = path.split("\\.");
		if (fileNames.length > 1) {
			return fileNames[fileNames.length - 1];
		}
		return null;
	}
}
