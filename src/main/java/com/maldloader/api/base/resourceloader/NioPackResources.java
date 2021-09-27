package com.maldloader.api.base.resourceloader;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Represents a resource pack based on NIO
 */
public class NioPackResources extends AbstractPackResources implements PackResources {
	protected static final Logger LOGGER = LogManager.getLogger("Mod Resource Pack");
	protected static final Pattern RESOURCE_PACK_PATH = Pattern.compile("[a-z0-9-_]+");
	protected static final String FILE_SEPARATOR = File.separator;
	protected final Path root;
	protected final PackType packType;
	protected final PackInfo packInfo;

	public NioPackResources(Path root, PackType packType, PackInfo packInfo) {
		super(null);
		this.root = root;
		this.packType = packType;
		this.packInfo = packInfo;
	}

	@Override
	protected InputStream getResource(String fileName) throws IOException {
		Path path = getPath(fileName);

		if (path != null && Files.isRegularFile(path)) {
			return Files.newInputStream(path);
		}
		try (InputStream stream = openResource(this.packType, fileName, packInfo)) {
			if (stream != null) {
				return stream;
			}
		}

		throw new RuntimeException("Failed to open file \"" + fileName + "\" in pack \"" + packInfo.name + "\"");
	}

	@Override
	public Collection<ResourceLocation> getResources(PackType packType, String namespace, String path, int depth, Predicate<String> predicate) {
		List<ResourceLocation> resourceLocations = new ArrayList<>();
		Path namespacePath = getPath(packType.getDirectory() + "/" + path.replace("/", FILE_SEPARATOR));

		if (namespacePath != null) {
			Path searchPath = namespacePath.resolve(namespacePath).toAbsolutePath().normalize();

			if (Files.exists(searchPath)) {
				try {
					Files.walk(searchPath, depth)
							.filter(Files::isRegularFile)
							.filter((p) -> {
								String fileName = p.getFileName().toString();
								return !fileName.endsWith(".mcmeta") && predicate.test(fileName);
							})
							.map(namespacePath::relativize)
							.map((p) -> p.toString().replace(FILE_SEPARATOR, "/"))
							.forEach((s) -> {
								try {
									resourceLocations.add(new ResourceLocation(namespace, s));
								} catch (ResourceLocationException e) {
									LOGGER.error(e.getMessage());
								}
							});
				} catch (IOException e) {
					LOGGER.warn("could not find resources at \"{}\" in the namespace \"{}\"." + path, namespace, e);
				}
			}
		}
		return resourceLocations;
	}

	@Override
	public Set<String> getNamespaces(PackType packType) {
		try {
			Path typePath = getPath(packType.getDirectory());

			if (typePath == null || !(Files.isDirectory(typePath))) {
				return Collections.emptySet();
			}

			Set<String> validNamespaces = new HashSet<>();

			try (DirectoryStream<Path> stream = Files.newDirectoryStream(typePath, Files::isDirectory)) {
				for (Path path : stream) {
					String namespace = path.getFileName().toString();
					namespace = namespace.replace(FILE_SEPARATOR, "");

					if (RESOURCE_PACK_PATH.matcher(namespace).matches()) {
						validNamespaces.add(namespace);
					} else {
						LOGGER.warn("Ignoring invalid namespace \"{}\"", namespace);
					}
				}
			}

			return validNamespaces;
		} catch (IOException e) {
			LOGGER.warn("getNamespaces failed!", e);
			return Collections.emptySet();
		}
	}

	@Override
	public void close() {
	}

	@Override
	protected boolean hasResource(String fileName) {
		Path path = getPath(fileName);
		return path != null && Files.isRegularFile(path) || fileName.endsWith("pack.mcmeta");
	}

	/**
	 * Returns the location of a file relative to the base path
	 *
	 * @param fileName the file you are trying to locate
	 * @return a path to that file
	 */
	private Path getPath(String fileName) {
		Path childPath = root.resolve(fileName.replace("/", FILE_SEPARATOR)).toAbsolutePath().normalize();
		return childPath.startsWith(root) && Files.exists(childPath) ? childPath : null;
	}

	/**
	 * Takes a fileName, and returns an {@link InputStream}
	 */
	private InputStream openResource(PackType packType, String fileName, PackInfo info) {
		if ("pack.mcmeta".equals(fileName)) {
			String json = String.format("{\"pack\":{\"pack_format\":" + info.packVersion + ",\"description\":\"%s\"}}", info.description);
			return IOUtils.toInputStream(json, StandardCharsets.UTF_8);
		}
		return null;
	}
}
