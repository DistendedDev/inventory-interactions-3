package com.diztend.inventoryinteractions.kubejs;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.screen.slot.Slot;

public class ItemClickedEventJS extends PlayerEventJS {

    private final PlayerEntity player;
    private final int button;
    private final Slot slot;
    private final StackReference cursorSlot;

    public ItemClickedEventJS(int button, Slot slot, StackReference cursorSlot, PlayerEntity player) {
        this.button = button;
        this.player = player;
        this.slot = slot;
        this.cursorSlot = cursorSlot;
    }

    @Override
    public PlayerEntity getEntity() {
        return player;
    }

    public Slot getSlot() {
        return slot;
    }

    public StackReference getCursorSlot() {
        return cursorSlot;
    }

    public int getButton() {
        return button;
    }

}
