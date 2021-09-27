package com.maldloader.api.base.itemgroup.mixin;

import com.maldloader.api.content.item.group.CreativeModeTabExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.item.CreativeModeTab;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin implements CreativeModeTabExtension {
    @Mutable
    @Shadow @Final public static CreativeModeTab[] TABS;

    @Override
    public void mald_expand() {
        CreativeModeTab[] tempTabs = TABS;
        TABS = new CreativeModeTab[TABS.length + 1];

        for(int i = 0; i < tempTabs.length; i++) {
            TABS[i] = tempTabs[i];
        }
    }
}
