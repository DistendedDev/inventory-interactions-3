package com.diztend.inventoryinteractions.kubejs;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.screen.slot.Slot;

public interface InventoryEvent {

    Event<InventoryEvent.Click> CLICK = EventFactory.createEventResult();

    interface Click {
        /**
         * Invoked when an item is clicked by a player in the inventory.
         *
         * @param button        Right - 1; Left - 0;
         * @param slot          The item slot being clicked on
         * @param cursorSlot    The reference to the cursor's itemstack
         * @param player        The player performing the click
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla block breaking may be cancelled by the result.
         */
        EventResult clickItem(int button, Slot slot, StackReference cursorSlot, PlayerEntity player);
    }

}
