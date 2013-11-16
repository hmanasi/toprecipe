package com.toprecipe.services.fetcher;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import play.libs.F.Promise;

public class MediaFetcherTest {

	private MediaFetcher inTest = new MediaFetcher();

	@Before
	public void setup() throws IOException {
		File imageFolder = new File ("/tmp/images");
		imageFolder.mkdirs();
		imageFolder.deleteOnExit();
		ImageFetcher imageFetcher = new ImageFetcher();
		inTest.setImageFetcher(imageFetcher);
		RecipeHtmlParser parser = new RecipeHtmlParser();
		ImageFilter imageFilter = new ImageFilter();
		parser.setImageFilter(imageFilter);
		imageFetcher.setImageFilter(imageFilter);
		imageFetcher.setImageFolder(imageFolder.getCanonicalPath());
		inTest.setRecipeHtmlParser(parser);
	}

	@Ignore
	@Test
	public void testFetchMedia() throws IOException {
		Promise<Media> mediaPromise = inTest
				.fetch("http://www.nps.gov/yell/index.htm");
		Media m = mediaPromise.get(1, TimeUnit.MINUTES);

		for (Image i : m.getImages()) {
			System.out.println(String.format("Image: %s %d %d", i.getFile()
					.getCanonicalFile(), i.getWidth(), i.getHeight()));
		}
	}

	@Test
	public void testFetchYouTubeVideo() {

		Promise<Media> mediaPromise = inTest
				.fetch("http://www.nature.nps.gov/multimedia/wns01/index.cfm");
		Media m = mediaPromise.get(1, TimeUnit.MINUTES);

		for (String video : m.getYouTubeVideos()) {
			System.out.println(String.format("Video: %s", video));
		}
	}	
}
