package com.maldloader.api.base.resourceloader;

import net.minecraft.SharedConstants;

/**
 * Represents pack.mcmeta info.
 */
public class PackInfo {

	public final String name;
	public final String description;
	public final int packVersion;

	public PackInfo(String name, String description) {
		this(name, description, SharedConstants.DATA_PACK_FORMAT);
	}

	public PackInfo(String name, String description, int packVersion) {
		// Some mods may not have descriptions.
		if(description == null) {
			description = "";
		}

		// Some mods may also not even have names. This isn't really ideal
		if(name == null) {
			name = this.hashCode() + "";
		}

		this.name = name;
		this.description = description;
		this.packVersion = packVersion;
	}
}
