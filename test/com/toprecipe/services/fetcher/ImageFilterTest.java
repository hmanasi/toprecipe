package com.toprecipe.services.fetcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class ImageFilterTest {

	private ImageFilter inTest = new ImageFilter();

	@Test
	public void testWithGoodFile() {
		Image image = new Image(null, null, null);
		image.setFile(new File(
				"data/test/images/0dace5f4-ebe0-463e-ae3a-30c1687ba18f.jpg"));
		assertFalse(inTest.rejectFile(image));
	}

	@Test
	public void testWithFileHavingNoExtension() {
		Image image = new Image(null, null, null);
		image.setFile(new File("data/test/images/invalid1"));
		assertTrue(inTest.rejectFile(image));
	}

	@Test
	public void testWithFileHavingNoContent() {
		Image image = new Image(null, null, null);

		image.setFile(new File("data/test/images/invalid2.jpeg"));
		assertTrue(inTest.rejectFile(image));

		image.setFile(new File("data/test/images/invalid3.jpg"));
		assertTrue(inTest.rejectFile(image));
	}

}
