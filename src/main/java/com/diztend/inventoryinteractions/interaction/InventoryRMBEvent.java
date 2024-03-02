package com.diztend.inventoryinteractions.interaction;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

/**
 * An event which detects right click in the inventory
 * @see com.diztend.inventoryinteractions.mixin.ClickDetectorMixin#onClickInject(ItemStack, ItemStack, Slot, ClickType, PlayerEntity, StackReference, CallbackInfoReturnable)
 */
public class InventoryRMBEvent {

    public interface Listener {
        boolean onClick(ItemStack slotStack, ItemStack cursorStack, Slot slot, StackReference cursorSlot, PlayerEntity player);
    }

    private static final ArrayList<Listener> listeners = new ArrayList<>();

    public static void addListener(Listener listener) {
        listeners.add(listener);
    }

    public static void init() {
        addListener(Interactions::tryInteract);
    }
    
    public static void reset() {
        int count = listeners.size() - 1;
        while (count > 0) {
            listeners.remove(listeners.size() - 1);
            count -= 1;
        }
    }

    public static boolean onClick(ItemStack slotStack, ItemStack cursorStack, Slot slot, StackReference cursorSlot, PlayerEntity player) {
        World world = player.getWorld();
        if (!world.isClient()) {
            for (Listener l : listeners) {
                if (l.onClick(slotStack, cursorStack, slot, cursorSlot, player)) {
                    return true;
                }
            }
        }
        return false;
    }

}
