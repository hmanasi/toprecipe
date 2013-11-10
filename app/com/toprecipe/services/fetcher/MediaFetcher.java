package com.toprecipe.services.fetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import play.libs.F;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;

@Service
public class MediaFetcher {

	private RecipeHtmlParser parser;
	private ImageFetcher imageFetcher;

	@Autowired
	public void setRecipeHtmlParser(RecipeHtmlParser parser) {
		this.parser = parser;
	}

	@Autowired
	public void setImageFetcher(ImageFetcher imageFetcher) {
		this.imageFetcher = imageFetcher;
	}

	public Promise<Media> fetch(final String url) {
		return WS.url(url).get()
				.flatMap(new Function<WS.Response, F.Promise<Media>>() {

					@Override
					public Promise<Media> apply(Response response)
							throws Throwable {
						Media media = parser.parseHtml(
								response.getBodyAsStream(), url);
						return imageFetcher.fetch(media);
					}
				});
	}
}
