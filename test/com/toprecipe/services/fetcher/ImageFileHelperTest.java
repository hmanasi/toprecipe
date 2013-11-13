package com.toprecipe.services.fetcher;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ImageFileHelperTest {

	private ImageFileHelper inTest;

	@Before
	public void setup() throws IOException {
		inTest = new ImageFileHelper();
		File outFolder = new File("/tmp/test.imageFileHelperTest.out");
		outFolder.deleteOnExit();
		outFolder.mkdirs();

		inTest.setImageFolder(outFolder.getCanonicalPath());
		inTest.setTempImageFolder("/tmp");
	}

	@Test
	public void testSaveImagePermanent() throws IOException {
		File src = File
				.createTempFile("tst", "imgFileHelper", new File("/tmp"));
		FileWriter out = new FileWriter(src);
		out.write("A test string...");
		out.close();

		File dst = inTest.makeImagePermanent(src.getName());
		assertTrue(dst.exists());
		assertEquals(src.getName(), dst.getName());
	}

}
