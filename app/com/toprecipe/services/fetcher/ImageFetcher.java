package com.toprecipe.services.fetcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

	public Promise<List<Image>> fetch(final List<Image> in) {

		@SuppressWarnings("unchecked")
		Promise<Response>[] responsePromise = new Promise[in.size()];

		for (int i = 0; i < in.size(); i++) {
			responsePromise[i] = WS.url(in.get(i).getUrl()).get();
		}

		Promise<List<Image>> out = Promise.sequence(responsePromise).map(
				new Function<List<Response>, List<Image>>() {

					@Override
					public List<Image> apply(List<Response> a) throws Throwable {
						List<Image> out = new ArrayList<>();

						for (int i = 0; i < a.size(); i++) {
							Image inImage = in.get(i);
							Image image = new Image(inImage.getUrl(), inImage
									.getWidth(), inImage.getHeight());

							image.setFile(storeImage(a.get(i)));

							if (!imageFilter.rejectFile(image)) {
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

		try {
			System.out.println(String.format("Storing %s to %s as %s",
					responsePath, file.getParent(), file.getName()));

			InputStream in = new BufferedInputStream(response.getBodyAsStream());
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					file));
			byte buffer[] = new byte[512];
			int bytes = in.read(buffer);

			while (bytes > 0) {
				out.write(buffer, 0, bytes);
				bytes = in.read(buffer);
			}

			out.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
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
