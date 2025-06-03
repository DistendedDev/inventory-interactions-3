package com.diztend.inventoryinteractions.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.player.InventoryEventJS;

public interface InventoryEvents {

    EventGroup GROUP = EventGroup.of("InventoryEvents");
    EventHandler CLICKED = GROUP.common("clicked", () -> InventoryEventJS.class).hasResult();

}
