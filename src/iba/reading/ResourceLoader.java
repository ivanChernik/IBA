package iba.reading;

import java.io.InputStream;

import java.net.URL;


public final class ResourceLoader {

	private ResourceLoader() {
		super();
	}

	public static InputStream loadResource(String name) {
		String resource = name;
		return ResourceLoader.class.getClassLoader().getResourceAsStream(
				resource);
	}

	public static URL loadResourceUrl(String name) {
		String resource = name;
		return ResourceLoader.class.getResource(resource);
	}
}