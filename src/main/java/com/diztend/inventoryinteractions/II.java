package com.diztend.inventoryinteractions;

import com.diztend.inventoryinteractions.loader.IIReloadListener;
import com.diztend.inventoryinteractions.util.IIConfig;
import com.diztend.inventoryinteractions.interaction.InventoryRMBEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class II implements ModInitializer {

	public static final String MOD_ID = "inventory-interactions";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		IIConfig.init();
		InventoryRMBEvent.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new IIReloadListener());
	}

}
