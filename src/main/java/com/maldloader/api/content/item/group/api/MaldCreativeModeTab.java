package com.maldloader.api.content.item.group.api;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class MaldCreativeModeTab extends CreativeModeTab {
    private Supplier<ItemLike> icon;

    private MaldCreativeModeTab(int index, String id, Supplier<ItemLike> icon) {
        super(index, id);
        this.icon = icon;
    }

    public static MaldCreativeModeTab create(ResourceLocation id, Supplier<ItemLike> item) {
        CreativeModeTab.TAB_SEARCH.mald_expand();
        return new MaldCreativeModeTab(CreativeModeTab.TABS.length - 1, String.format("%s.%s", id.getNamespace(), id.getPath()), item);
    }

    public static MaldCreativeModeTab create(ResourceLocation id, Supplier<ItemLike> item, BiConsumer<List<ItemStack>, CreativeModeTab> stacksToAppend) {
        CreativeModeTab.TAB_SEARCH.mald_expand();
        return new MaldCreativeModeTab(CreativeModeTab.TABS.length - 1, String.format("%s.%s", id.getNamespace(), id.getPath()), item) {
            @Override
            public void fillItemList(NonNullList<ItemStack> stacks) {
                stacksToAppend.accept(stacks, this);
            }
        };
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(icon.get());
    }
}
