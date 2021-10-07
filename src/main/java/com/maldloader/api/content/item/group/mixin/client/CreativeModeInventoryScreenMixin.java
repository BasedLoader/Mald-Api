package com.maldloader.api.content.item.group.mixin.client;

import com.maldloader.api.content.item.group.impl.CreativeTabButton;
import com.maldloader.api.content.item.group.impl.CreativeModeInventoryScreenExtensions;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> implements CreativeModeInventoryScreenExtensions {

    public CreativeModeInventoryScreenMixin(AbstractContainerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super((CreativeModeInventoryScreen.ItemPickerMenu) abstractContainerMenu, inventory, component);
    }

    @Shadow
    public abstract int getSelectedTab();

    @Shadow
    protected abstract void selectTab(CreativeModeTab $$0);

    @Unique
    private static int currentPage = 0;

    @Unique
    private int getOffsetPage(int offset) {
        if (offset < 12) {
            return 0;
        } else {
            return 1 + ((offset - 12) / 9);
        }
    }

    @Override
    public void nextPage() {
        if (getOffsetPage(currentPage + 1) >= CreativeModeTab.TABS.length)
            return;
        currentPage++;
    }

    @Override
    public void prevPage() {
        if (currentPage > 0)
            currentPage--;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public boolean isVisible() {
        return CreativeModeTab.TABS.length > 12;
    }

    @Override
    public boolean isEnabled(CreativeTabButton.Type type) {
        if (type == CreativeTabButton.Type.NEXT) {
            return !(getOffsetPage(currentPage + 1) >= CreativeModeTab.TABS.length);
        }

        if (type == CreativeTabButton.Type.PREVIOUS) {
            return currentPage != 0;
        }

        return false;
    }

    @Unique
    private void updateSelection() {
        int minPos = getOffsetPage(currentPage);
        int maxPos = getOffsetPage(currentPage + 1) - 1;
        int curPos = getSelectedTab();

        if (curPos < minPos || curPos > maxPos) {
            selectTab(CreativeModeTab.TABS[getOffsetPage(currentPage)]);
        }
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo info) {
        updateSelection();

        int xpos = topPos + 116;
        int ypos = leftPos - 10;

        addRenderableWidget(new CreativeTabButton(xpos + 11, ypos, CreativeTabButton.Type.NEXT, (CreativeModeInventoryScreen) (Object) this));
        addRenderableWidget(new CreativeTabButton(xpos, ypos, CreativeTabButton.Type.PREVIOUS, (CreativeModeInventoryScreen) (Object) this));
    }

    @Inject(method = "selectTab", at = @At("HEAD"), cancellable = true)
    private void setSelectedTab(CreativeModeTab creativeModeTab, CallbackInfo info) {
        if (isGroupInvisible(creativeModeTab)) {
            info.cancel();
        }
    }

    @Inject(method = "checkTabHovering", at = @At("HEAD"), cancellable = true)
    private void renderTabTooltipIfHovered(PoseStack poseStack, CreativeModeTab creativeModeTab, int mx, int my, CallbackInfoReturnable<Boolean> info) {
        if (isGroupInvisible(creativeModeTab)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "checkTabClicked", at = @At("HEAD"), cancellable = true)
    private void isClickInTab(CreativeModeTab creativeModeTab, double mx, double my, CallbackInfoReturnable<Boolean> info) {
        if (isGroupInvisible(creativeModeTab)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "renderTabButton", at = @At("HEAD"), cancellable = true)
    private void renderTabIcon(PoseStack poseStack, CreativeModeTab creativeModeTab, CallbackInfo info) {
        if (isGroupInvisible(creativeModeTab)) {
            info.cancel();
        }
    }

    @Unique
    private boolean isGroupInvisible(CreativeModeTab creativeModeTab) {
        if (CreativeModeTab.TAB_HOTBAR.equals(creativeModeTab) || creativeModeTab.equals(CreativeModeTab.TAB_INVENTORY) || creativeModeTab.equals(CreativeModeTab.TAB_SEARCH))
            return false;

        return currentPage != getOffsetPage(creativeModeTab.getId());
    }

}
