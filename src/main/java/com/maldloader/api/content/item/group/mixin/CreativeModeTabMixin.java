package com.maldloader.api.content.item.group.mixin;

import com.maldloader.api.content.item.group.api.MaldCreativeModeTab;
import com.maldloader.api.content.item.group.impl.CreativeModeTabExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin implements CreativeModeTabExtension {
    @Mutable
    @Shadow @Final public static CreativeModeTab[] TABS;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void conc(int param0, String param1, CallbackInfo ci) {
        MaldCreativeModeTab.create(new ResourceLocation("mald:test"), () -> Items.DIAMOND_HOE);
    }

    @Override
    public void mald_expand() {
        CreativeModeTab[] tempTabs = TABS;
        TABS = new CreativeModeTab[TABS.length + 1];

        System.arraycopy(tempTabs, 0, TABS, 0, tempTabs.length);
    }
}
