package com.toprecipe.services.fetcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;

@Service
public class ImageFetcher {

	private static final String DEFAULT_IMAGE_FOLDER = "public/images/temp";
	private String imageFolder = DEFAULT_IMAGE_FOLDER;
	private ImageFilter imageFilter;

	@Autowired
	public void setImageFilter(ImageFilter imageFilter) {
		this.imageFilter = imageFilter;
	}

	void setImageFolder(String imageFolder) {
		this.imageFolder = imageFolder;
	}

	/**
	 * This function mutates the passed media
	 * 
	 * @param media
	 * @return
	 */
	public Promise<Media> fetch(final Media media) {
		return fetch(media.getImages()).map(new Function<List<Image>, Media>() {

			@Override
			public Media apply(List<Image> images) throws Throwable {
				media.setImages(images);
				return media;
			}
		});
	}

	public Promise<List<Image>> fetch(List<Image> images) {

		final List<Image> fetchedImages = new ArrayList<Image>();
		List<Promise<Response>> responseList = new ArrayList<>();

		for (Image image : images) {
			try {
				URL url = new URL(image.getUrl());
				URI uri = new URI(url.getProtocol(),url.getHost(),url.getPath(),url.getQuery(),null);
				responseList.add(WS.url(uri.toString()).get());
				fetchedImages.add(image);
			} catch (URISyntaxException | MalformedURLException e) {
				System.out
						.println(String.format(
								"Error fetching image %s. Skipping...",
								image.getUrl()));
			}
		}

		@SuppressWarnings("unchecked")
		Promise<Response>[] responsePromise = responseList
				.toArray(new Promise[responseList.size()]);

		Promise<List<Image>> out = Promise.sequence(responsePromise).map(
				new Function<List<Response>, List<Image>>() {

					@Override
					public List<Image> apply(List<Response> responses)
							throws Throwable {
						List<Image> out = new ArrayList<>();

						for (int i = 0; i < responses.size(); i++) {
							Image inImage = fetchedImages.get(i);
							Image image = new Image(inImage.getUrl(), inImage
									.getWidth(), inImage.getHeight());

							image.setFile(storeImage(responses.get(i)));

							if (imageFilter.rejectFile(image)) {
								image.getFile().delete();
							} else {
								out.add(image);
							}
						}

						return out;
					}
				});

		return out;
	}

	protected File storeImage(Response response) {

		String responsePath = response.getUri().getPath();
		File file = getFileName(responsePath);
		InputStream in = null;
		OutputStream out = null;

		try {
			System.out.println(String.format("Storing %s to %s as %s",
					responsePath, file.getParent(), file.getName()));

			in = new BufferedInputStream(response.getBodyAsStream());
			out = new BufferedOutputStream(new FileOutputStream(file));
			byte buffer[] = new byte[512];
			int bytes = in.read(buffer);

			while (bytes > 0) {
				out.write(buffer, 0, bytes);
				bytes = in.read(buffer);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// ignore
				}
			}
			if (null != null) {
				try {
					in.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		return file;
	}

	private File getFileName(String responsePath) {

		String fileNames[] = responsePath.split("\\.");

		String fileName = UUID.randomUUID().toString();

		if (fileNames.length > 1) {
			fileName += "." + fileNames[fileNames.length - 1];
		}

		return new File(new File(imageFolder), fileName);
	}

}
