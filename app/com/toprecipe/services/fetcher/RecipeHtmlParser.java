package com.toprecipe.services.fetcher;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeHtmlParser {

	private ImageFilter imageFilter;
	
	@Autowired
	public void setImageFilter (ImageFilter imageFilter)
	{
		this.imageFilter = imageFilter;
	}

	public Media parseHtml(InputStream in, String baseUri) throws IOException {
		Media media = new Media();

		Document doc = Jsoup.parse(in, null, baseUri);

		Elements flash = doc.select("object");
		Elements img = doc.select("img");
		Elements youtubeIframe = doc.select("iframe");

		for (Element src : img) {
			Image image = new Image(src.attr("abs:src"),
					parseDimension(src.attr("width")),
					parseDimension(src.attr("height")));
			if (!imageFilter.reject(image)) {
				media.addImage(image);
			}
		}

		for (Element f : flash) {
			if (f.attr("type") != null
					&& f.attr("type").equals("application/x-shockwave-flash")) {
				media.addFlashVideos(f.attr("abs:data"));
			}
		}

		for (Element y : youtubeIframe) {
			if (y.attr("src") != null
					&& y.attr("src").startsWith("http://www.youtube.com/embed")) {
				media.addYouTubeVideo(y.attr("src"));
			}
		}

		return media;
	}

	private Integer parseDimension(String attr) {
		if (attr.length() == 0)
			return null;
		try {
			return Integer.valueOf(attr);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
