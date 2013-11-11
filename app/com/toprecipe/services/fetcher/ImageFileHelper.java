package com.toprecipe.services.fetcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

@Service
public class ImageFileHelper {

	private static String IMAGE_FOLDER = "public/images/recipes";
	private String imageFolder = IMAGE_FOLDER;

	void setImageFolder(String imageFolder) {
		this.imageFolder = imageFolder;
	}

	public File makeImagePermanent(File tmpImage) throws IOException {
		byte buffer[] = new byte[512];

		File toFile = new File(imageFolder, tmpImage.getName());

		System.out.println(String.format("Copying image file from %s to %s",
				tmpImage.getCanonicalPath(), toFile.getAbsolutePath()));

		InputStream in = null;
		OutputStream out = null;

		try {
			in = new BufferedInputStream(new FileInputStream(tmpImage));
			out = new BufferedOutputStream(new FileOutputStream(toFile));
			int bytesRead = in.read(buffer);

			while (bytesRead > 0) {
				out.write(buffer, 0, bytesRead);
				bytesRead = in.read(buffer);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Ignore
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}

		return toFile;
	}
}
