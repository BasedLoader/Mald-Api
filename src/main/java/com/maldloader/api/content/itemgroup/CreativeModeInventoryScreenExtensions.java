package com.maldloader.api.content.itemgroup;

import com.maldloader.api.content.item.group.impl.CreativeTabButton;

public interface CreativeModeInventoryScreenExtensions {
    void nextPage();
    void prevPage();
    int getCurrentPage();
    boolean isVisible();
    boolean isEnabled(CreativeTabButton.Type type);
}
