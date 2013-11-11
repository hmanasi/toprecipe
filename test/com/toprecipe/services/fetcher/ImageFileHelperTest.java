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
		File outFolder = new File("/tmp/test.imageFileHelperTest");
		outFolder.deleteOnExit();
		outFolder.mkdirs();

		inTest.setImageFolder(outFolder.getCanonicalPath());
	}

	@Test
	public void testSaveImagePermanent() throws IOException {
		File src = File.createTempFile("tst", "imgFileHelper");

		FileWriter out = new FileWriter(src);
		out.write("A test string...");
		out.close();

		File dst = inTest.makeImagePermanent(src);
		assertTrue(dst.exists());
	}

}
