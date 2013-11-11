package com.toprecipe.services.fetcher;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import play.libs.F.Promise;

public class ImageFetcherTest {

	ImageFetcher inTest = new ImageFetcher();

	@Before
	public void setup() {
		File f = new File ("/tmp/images");
		f.mkdirs();
		inTest.setImageFolder("/tmp/images");
		inTest.setImageFilter(new ImageFilter());
	}

	@Test
	public void test() throws IOException {
		Image i1 = new Image(
				"http://www.nps.gov/pwr/yose/images/C5CD2647-1DD8-B71C-0E0221E76128B545.jpg",
				960, 241);
		Image i2 = new Image(
				"http://www.nature.nps.gov/nnl/assets/images/banner07.jpg",
				707, 283);

		Promise<List<Image>> imagePromises = inTest
				.fetch(Arrays.asList(i1, i2));
		List<Image> images = imagePromises.get(1, TimeUnit.MINUTES);

		for (Image i : images) {
			System.out.println(String.format("%s = %s", i.getUrl(), i.getFile()
					.getCanonicalPath()));
		}

	}

}
