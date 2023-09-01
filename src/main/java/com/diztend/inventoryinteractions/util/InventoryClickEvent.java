package com.diztend.inventoryinteractions.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;

public class InventoryClickEvent {

    public interface InventoryClickListener {
        boolean onClick(int slotIndex, int button,
                        Slot slot, ItemStack cursorStack,
                        SlotActionType actionType, PlayerEntity entity);
    }

    private static final ArrayList<InventoryClickListener> Listeners = new ArrayList<>();

    public static void addListener(InventoryClickListener listener) {
        Listeners.add(listener);
    }

    public static boolean onClick(int slotIndex, int button, Slot slot, ItemStack cursorStack, SlotActionType actionType, PlayerEntity entity) {
        for (InventoryClickListener l : Listeners) {
            if (l.onClick(slotIndex, button, slot, cursorStack, actionType, entity)) {
                return true;
            }
        }
        return false;
    }

}
