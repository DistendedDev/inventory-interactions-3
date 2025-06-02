package com.diztend.inventoryinteractions.kubejs;

import com.diztend.inventoryinteractions.gameplay.InventoryClickEvent;
import dev.architectury.event.EventResult;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.screen.slot.Slot;

public class IIKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void registerEvents() {
        InventoryEvents.GROUP.register();
        InventoryEvent.CLICK.register(this::inventoryClick);
        InventoryClickEvent.addListener(((button, slot, cursorSlot, entity) -> {
            EventResult result = InventoryEvent.CLICK.invoker().clickItem(button, slot, cursorSlot, entity);
            return result.interruptsFurtherEvaluation();
        }));
    }

    private EventResult inventoryClick(int button, Slot slot, StackReference cursorSlot, PlayerEntity player) {
        if (InventoryEvents.CLICKED.hasListeners()) {
            return InventoryEvents.CLICKED.post(player, null, new ItemClickedEventJS(button, slot, cursorSlot, player)).arch();
        }
        return EventResult.pass();
    }

}
