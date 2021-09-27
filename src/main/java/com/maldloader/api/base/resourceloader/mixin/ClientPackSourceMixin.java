package com.maldloader.api.base.resourceloader.mixin;

import com.maldloader.api.base.resourceloader.ModResourcePackSource;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Used to register the mod resource pack's
 */
@Mixin(ClientPackSource.class)
public class ClientPackSourceMixin {

	@Inject(method = "loadPacks", at = @At("RETURN"))
	private void loadModResourcePacks(Consumer<Pack> consumer, Pack.PackConstructor constructor, CallbackInfo ci) {
		ModResourcePackSource.CLIENT_RESOURCE_PROVIDER.loadPacks(consumer, constructor);
	}

	//TODO: programmer art support
}
