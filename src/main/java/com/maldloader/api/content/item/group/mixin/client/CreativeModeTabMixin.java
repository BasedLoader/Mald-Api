package com.maldloader.api.content.item.group.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.CreativeModeTab;

@Mixin(CreativeModeTab.class)
public abstract class CreativeModeTabMixin {

    @Shadow public abstract boolean isTopRow();

    @Shadow public abstract int getId();

    @Inject(method = "isTopRow", at = @At("HEAD"), cancellable = true)
    public void isTop(CallbackInfoReturnable<Boolean> cir) {
        if(getId() > 11) {
            cir.setReturnValue((getId() - 12) % 9 < 4);
        }
    }

    @Inject(method = "getColumn", cancellable = true, at = @At("HEAD"))
    private void getColumn(CallbackInfoReturnable<Integer> info) {
        if (getId() > 11) {
            if (isTopRow()) {
                info.setReturnValue((getId() - 12) % 9);
            } else {

                info.setReturnValue((getId() - 12) % 9 - 4);
            }
        }
    }
}
