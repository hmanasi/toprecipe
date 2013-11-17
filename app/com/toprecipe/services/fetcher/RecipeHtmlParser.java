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
	public void setImageFilter(ImageFilter imageFilter) {
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
			Elements param = flash.select("param[name$=movie]");
			Elements embed = flash.select("embed");

			if (param != null && param.hasAttr("abs:value")) {
				media.addFlashVideos(param.attr("abs:value"));
			} else if (f.hasAttr("type")
					&& f.attr("type").equals("application/x-shockwave-flash")) {
				media.addFlashVideos(f.attr("abs:data"));
			} else if (embed != null && embed.hasAttr("type")
					&& f.attr("type").equals("application/x-shockwave-flash")) {
				media.addFlashVideos(embed.attr("abs:src"));
			}
		}

		for (Element y : youtubeIframe) {
			if (y.hasAttr("abs:src")
					&& (y.attr("abs:src").startsWith(
							"http://www.youtube.com/v/") || y.attr("abs:src")
							.startsWith("http://www.youtube.com/embed/"))) {
				media.addYouTubeVideo(y.attr("abs:src"));
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
