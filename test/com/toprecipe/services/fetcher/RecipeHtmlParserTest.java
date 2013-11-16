package com.toprecipe.services.fetcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class RecipeHtmlParserTest {

	private RecipeHtmlParser inTest = new RecipeHtmlParser();

	@Before
	public void setup() {
		inTest.setImageFilter(new ImageFilter());
	}

	@Test
	public void testWithFlashObject() throws IOException {
		Media media = inTest.parseHtml(
				new ByteArrayInputStream(flahMedia.getBytes()),
				"http://www.vahrehvah.com/chicken-fried-rice#.UmS37LMYbx8");

		assertTrue(media.getFlashVideos().contains(
				"http://www.youtube.com/v/Q6O2jBMhkt0"));
	}

	@Test
	public void testWithFlashObjectFormat2() throws IOException {
		Media media = inTest.parseHtml(
				new ByteArrayInputStream(flashMediaFormat2.getBytes()),
				"http://showmethecurry.com/appetizers/chakli-chakri-murukku.html");

		assertTrue(media.getFlashVideos().contains(
				"http://www.youtube.com/v/EkPG3rx_Ai8?fs=1&hl=en_US&rel=0&hd=1&color1=0xe1600f&color2=0xfebd01"));
	}

	
	@Test
	public void testWithYoutubeUrl() throws IOException {
		Media media = inTest.parseHtml(
				new ByteArrayInputStream(youTubeMedia.getBytes()),
				"http://www.madhurasrecipe.com/appetizers/Gobi-Manchurian");

		assertTrue(media.getYouTubeVideos().contains(
				"http://www.youtube.com/embed/M4eXMio-Vdk?rel=0"));
	}

	@Test
	public void testWithYoutubeUrlMalformed() throws IOException {
		Media media = inTest.parseHtml(
				new ByteArrayInputStream(youTubeMediaMalformed.getBytes()),
				"http://www.madhurasrecipe.com/appetizers/Gobi-Manchurian");

		assertTrue(media.getYouTubeVideos().contains(
				"http://www.youtube.com/embed/M4eXMio-Vdk?rel=0"));
	}

	
	@Test
	public void testWithImage() throws IOException {
		Media media = inTest.parseHtml(
				new ByteArrayInputStream(imageMedia.getBytes()),
				"http://www.madhurasrecipe.com/appetizers/Gobi-Manchurian");

		Image image = new Image(
				"http://www.madhurasrecipe.com/media/gobi_manchurian_1.jpg",
				317, 249);
		assertTrue(media.getImages().contains(image));

		image = new Image(
				"http://www.madhurasrecipe.com/media/gobi_manchurian_2.jpg",
				31, 24);
		assertFalse(media.getImages().contains(image));
		image = new Image(
				"http://www.madhurasrecipe.com/media/gobi_manchurian_3.jpg",
				null, null);
		assertTrue(media.getImages().contains(image));
	}

	private String flahMedia =

	"<HTML>"
			+ "<HEAD>"
			+ "</HEAD>"
			+ "<BODY>"
			+ ""
			+ "<script type=\"text/javascript\">"
			+ "    var post = '12288';"
			+ "</script>"
			+ "<object type=\"application/x-shockwave-flash\" data=\"http://www.youtube.com/v/Q6O2jBMhkt0\" width=\"582\" height=\"300\"><param name=\"movie\" value=\"http://www.youtube.com/v/Q6O2jBMhkt0\" /><param name=\"allowFullScreen\" value=\"true\" /><param name=\"allowscriptaccess\" value=\"always\" /><param name=\"wmode\" value=\"transparent\" /></object><div class=\"title_2\">"
			+ "<div class=\"reating1\">"
			+ "    <a class=\"selected-star\" href=\"#\">*</a>"
			+ "    <a class=\"selected-star\" href=\"#\">*</a>"
			+ "    <a class=\"selected-star\" href=\"#\">*</a>"
			+ "    <a class=\"selected-star\" href=\"#\">*</a>"
			+ "    <a class=\"selected-star\" href=\"#\">*</a>" + "</div>" + ""
			+ "</BODY>" + "</HTML>";

	private String flashMediaFormat2 = 
			"<p>" +
					"	<object width=\"640\" height=\"385\">" +
					"		<param value=\"http://www.youtube.com/v/EkPG3rx_Ai8?fs=1&amp;hl=en_US&amp;rel=0&amp;hd=1&amp;color1=0xe1600f&amp;color2=0xfebd01\" name=\"movie\">" +
					"		<param value=\"true\" name=\"allowFullScreen\"><param value=\"always\" name=\"allowscriptaccess\">" +
					"		<embed width=\"640\" height=\"385\" allowfullscreen=\"true\" allowscriptaccess=\"always\" type=\"application/x-shockwave-flash\" src=\"http://www.youtube.com/v/EkPG3rx_Ai8?fs=1&amp;hl=en_US&amp;rel=0&amp;hd=1&amp;color1=0xe1600f&amp;color2=0xfebd01\">" +
					"	</object>" +
					"</p>";
	
	private String youTubeMedia = "<div class=\"clear\"></div>"
			+ "<div class=\"comment\" style=\"margin:10px 22px 20px 0px!important;\">"
			+ "    <a href=\"#abc\"> Write comment</a>"
			+ "   |  <a href=\"http://www.madhurasrecipe.com/subscribe-for-newsletter\">Subscribe For Newsletter</a>"
			+ "</div>"
			+ " <div id=\"frame\"> <iframe width=\"520\" height=\"330\" src=\"http://www.youtube.com/embed/M4eXMio-Vdk?rel=0\" frameborder=\"0\" allowfullscreen></iframe></div>";

	private String youTubeMediaMalformed = "<div class=\"clear\"></div>"
			+ "<div class=\"comment\" style=\"margin:10px 22px 20px 0px!important;\">"
			+ "    <a href=\"#abc\"> Write comment</a>"
			+ "   |  <a href=\"http://www.madhurasrecipe.com/subscribe-for-newsletter\">Subscribe For Newsletter</a>"
			+ "</div>"
			+ " <div id=\"frame\"> <iframe width=\"520\" height=\"330\" src=\"//www.youtube.com/embed/M4eXMio-Vdk?rel=0\" frameborder=\"0\" allowfullscreen></iframe></div>";

	
	private String imageMedia = "<div class=\"recipe-img\">"
			+ "        <img src=\"http://www.madhurasrecipe.com/media/gobi_manchurian_1.jpg\" height=\"249\" width=\"317\" style=\"text-align:left;\"  />"
			+ "        <img src=\"http://www.madhurasrecipe.com/media/gobi_manchurian_2.jpg\" height=\"24\" width=\"31\" style=\"text-align:left;\"  />"
			+ "        <img src=\"http://www.madhurasrecipe.com/media/gobi_manchurian_3.jpg\" style=\"text-align:left;\"  />"
			+ "      </div>"
			+ "  <div class=\"intro\" id=\"introduction\">"
			+ "        <p>"
			+ "	<span style=\"font-family: arial,helvetica,sans-serif;\"><span style=\"font-size: small;\">Very popular Indo-Chinese appetizer. Deep fried Cauliflower Fritters cooked in sweet, sour and spicy Chinese based gravy! Goes well with plane boiled rice, noodles!</span></span></p>"
			+ "<p>"
			+ "	<span style=\"font-family: arial,helvetica,sans-serif;\"><span style=\"font-size: small;\">Preparation time : 10 mts<br />"
			+ "	Cooking time : 25 mts<br />"
			+ "	Serves : 4 people</span></span></p>" + "      </div>"
			+ "</div>";
}
