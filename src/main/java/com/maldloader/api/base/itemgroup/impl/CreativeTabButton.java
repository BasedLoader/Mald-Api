package com.maldloader.api.base.itemgroup.impl;

import com.maldloader.api.content.itemgroup.CreativeModeInventoryScreenExtensions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.function.Consumer;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class CreativeTabButton extends Button {
    private static final ResourceLocation BUTTON_TEX = new ResourceLocation("mald", "textures/gui/creative_buttons.png");

    private CreativeModeInventoryScreen gui;
    private Type type;

    public CreativeTabButton(int x, int y, Type type, CreativeModeInventoryScreen gui) {
        super(x, y, 11, 10, type.text, (bw) -> type.clickConsumer.accept(gui));
        this.type = type;
        this.gui = gui;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float float_1) {
        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        this.visible = gui.isVisible();
        this.active = gui.isEnabled(type);

        if (this.visible) {
            int u = active && this.isHovered() ? 22 : 0;
            int v = active ? 0 : 10;

            RenderSystem.setShaderTexture(0, BUTTON_TEX);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            this.blit(poseStack, this.x, this.y, u + (type == Type.NEXT ? 11 : 0), v, 11, 10);

            if (this.isHovered) {
                gui.renderTooltip(poseStack, new TranslatableComponent("fabric.gui.creativeTabPage", gui.getCurrentPage() + 1, ((CreativeModeTab.TABS.length - 12) / 9) + 2), mouseX, mouseY);
            }
        }
    }

    public enum Type {
        NEXT(new TextComponent(">"), CreativeModeInventoryScreenExtensions::nextPage),
        PREVIOUS(new TextComponent("<"), CreativeModeInventoryScreenExtensions::prevPage);

        Component text;
        Consumer<CreativeModeInventoryScreenExtensions> clickConsumer;

        Type(Component text, Consumer<CreativeModeInventoryScreenExtensions> clickConsumer) {
            this.text = text;
            this.clickConsumer = clickConsumer;
        }
    }
}