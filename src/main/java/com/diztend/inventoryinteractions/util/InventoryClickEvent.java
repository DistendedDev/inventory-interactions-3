package com.diztend.inventoryinteractions.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;

public class InventoryClickEvent {

    public interface InventoryClickListener {
        boolean onClick(int button, Slot slot, ItemStack cursorStack, StackReference cursorSlot, PlayerEntity entity);
    }

    private static final ArrayList<InventoryClickListener> Listeners = new ArrayList<>();

    public static void addListener(InventoryClickListener listener) {
        Listeners.add(listener);
    }

    public static boolean onClick(int button, Slot slot, ItemStack cursorStack,  StackReference cursorSlot, PlayerEntity entity) {
        for (InventoryClickListener l : Listeners) {
            if (l.onClick(button, slot, cursorStack, cursorSlot, entity)) {
                return true;
            }
        }
        return false;
    }

}
