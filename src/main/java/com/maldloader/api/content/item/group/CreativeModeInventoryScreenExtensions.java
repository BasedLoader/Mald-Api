package com.maldloader.api.content.item.group;

import com.maldloader.api.base.itemgroup.impl.CreativeTabButton;

public interface CreativeModeInventoryScreenExtensions {
    void nextPage();
    void prevPage();
    int getCurrentPage();
    boolean isVisible();
    boolean isEnabled(CreativeTabButton.Type type);
}
