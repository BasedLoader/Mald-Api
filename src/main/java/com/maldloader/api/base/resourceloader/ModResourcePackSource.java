package com.maldloader.api.base.resourceloader;

import com.maldloader.MaldLoader;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModResourcePackSource implements RepositorySource {

	public static final PackSource RESOURCE_PACK_SOURCE = str -> new TranslatableComponent("pack.name", str, new TranslatableComponent("pack.source"));
	public static final ModResourcePackSource CLIENT_RESOURCE_PROVIDER = new ModResourcePackSource(PackType.CLIENT_RESOURCES);
	private final PackType packType;
	private Pack.PackConstructor constructor;

	public ModResourcePackSource(PackType packType) {
		this.packType = packType;
		this.constructor = (name, text, bl, supplier, metadata, position, source) ->
				new Pack(name, text, bl, supplier, metadata, packType, position, source);
	}

	@Override
	public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor packConstructor) {
		List<NioPackResources> packResources = new ArrayList<>();
		//MaldLoader.getInstance().getMod("mald_api").getPath().resolveExists("assets");
		//TODO: loader needs to be able to get the "root path" of assets inside of mods
	}

	public void loadPacks(Consumer<Pack> consumer) {
		loadPacks(consumer, constructor);
	}
}
