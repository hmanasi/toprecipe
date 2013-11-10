package com.toprecipe.services.fetcher;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;

public class HttpClientTest {

	//URL embeded as object
	String url = "http://www.madhurasrecipe.com/appetizers/Gobi-Manchurian";
	//URL embeded as iframe
	//String url = "http://showmethecurry.com/curries/spinach-curry-with-channas.html";
	//String url = "http://www.madhurasrecipe.com/appetizers/Gobi-Manchurian";

	class RecipeRetriever {

		private Response response;
		private String baseUri;

		public RecipeRetriever(Response response, String uri) {
			System.out.println("Inside recipe retriever");
			this.response = response;
			this.baseUri = uri;
		}
		
		public void printHtml ()
		{
			
		}

		public void printUrls() throws IOException {
			
			InputStream in = response.getBodyAsStream();

			Document doc = Jsoup.parse(in, null, baseUri);

			Elements flash = doc.select("object");
			Elements img = doc.select("img");
			Elements youtubeIframe = doc.select("iframe");

			for (Element src : img) {
				print(" * %s: <%s> %sx%s (%s)", src.tagName(),
						src.attr("abs:src"), src.attr("width"),
						src.attr("height"), trim(src.attr("alt"), 20));
			}
			
			for (Element f: flash)
			{
				if (f.attr("type") != null && f.attr("type").equals("application/x-shockwave-flash"))
				{
					print (" * %s ",f.attr("abs:data"));
				}
			}
			
			for (Element y: youtubeIframe)
			{
				if (y.attr("src") != null && y.attr("src").startsWith("http://www.youtube.com/embed"))
				{
					print (" * %s ",y.attr("abs:src"));
				}
			}
			
			System.out.println("=====================================================================");
			System.out.println(response.getBody());
		}

		private void print(String msg, Object... args) {
			System.out.println(String.format(msg, args));
		}

		private String trim(String s, int width) {
			if (s.length() > width)
				return s.substring(0, width - 1) + ".";
			else
				return s;
		}

	}

	class RecipeRetrieverFunction implements
			Function<Response, RecipeRetriever> {

		private String uri;

		public RecipeRetrieverFunction(String uri) {
			this.uri = uri;
		}

		@Override
		public RecipeRetriever apply(Response a) throws Throwable {
			return new RecipeRetriever(a, uri);
		}

	}

	@Test
	public void test() throws IOException {

		final CountDownLatch latch = new CountDownLatch(1);

		Function<Response, RecipeRetriever> recipeRetrieverFunction = new RecipeRetrieverFunction(
				url) {
			@Override
			public RecipeRetriever apply(Response a) throws Throwable {
				latch.countDown();
				return super.apply(a);
			}

		};

		Promise<RecipeRetriever> retrieverPromise = WS.url(url).get()
				.map(recipeRetrieverFunction);

		try {
			latch.await(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			fail();
		}

		RecipeRetriever retriever = retrieverPromise.get(100);
		retriever.printUrls();
	}

}
