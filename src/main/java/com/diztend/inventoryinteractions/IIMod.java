package com.diztend.inventoryinteractions;

import com.diztend.inventoryinteractions.gameplay.InventoryClickEvent;
import com.diztend.inventoryinteractions.gameplay.RepairMethods;
import com.diztend.inventoryinteractions.util.JSONConfig;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IIMod implements ModInitializer {

	public static final String MOD_ID = "inventory-interactions";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static JSONConfig GLOBAL_CONFIG;

	public static final String DO_UNIT_REPAIR= "doUnitRepair";
	public static final String DO_ENCHANTED_UNIT_REPAIR= "doUnitRepairEnchanted";
	public static final String DO_TOOL_COMBINE= "doToolCombine";
	public static final String DO_QUICK_RENAME= "doQuickRename";
	public static final String DO_QUICK_CRAFTING= "doQuickCrafting";

	@Override
	public void onInitialize() {
		JsonObject defaultConfig = new JsonObject();
		defaultConfig.addProperty(DO_UNIT_REPAIR, true);
		defaultConfig.addProperty(DO_ENCHANTED_UNIT_REPAIR, true);
		defaultConfig.addProperty(DO_TOOL_COMBINE, true);
		defaultConfig.addProperty(DO_QUICK_RENAME, true);
		defaultConfig.addProperty(DO_QUICK_CRAFTING, false);
		GLOBAL_CONFIG = new JSONConfig(MOD_ID, defaultConfig, LOGGER);
		InventoryClickEvent.addListener( (button, slot, cursorStack, cursorSlot, entity) -> {
			ItemStack tool = slot.getStack();
			World world = entity.world;
			if (button == 1 && !tool.isEmpty() && !cursorStack.isEmpty() && !world.isClient()){
				if (tool.isDamaged()) {
					if (tool.getItem().canRepair(tool, cursorStack) && GLOBAL_CONFIG.getBoolean(DO_UNIT_REPAIR)) {
						if (!tool.hasEnchantments() || GLOBAL_CONFIG.getBoolean(DO_ENCHANTED_UNIT_REPAIR)) {
							return RepairMethods.unitRepairRate(tool, cursorStack, 0.25);
						}
					} else if (tool.getItem() == cursorStack.getItem() &&
							GLOBAL_CONFIG.getBoolean(DO_TOOL_COMBINE) &&
							!tool.hasEnchantments() && !cursorStack.hasEnchantments()) {
						return RepairMethods.combineTools(tool, cursorStack);
					}
				}
				if (cursorStack.getItem() == Items.NAME_TAG && cursorStack.hasCustomName() && !tool.hasCustomName() &&
						!tool.getItem().equals(Items.NAME_TAG) && GLOBAL_CONFIG.getBoolean(DO_QUICK_RENAME)) {
					return RepairMethods.nameItem(tool, cursorStack);
				}
				if (GLOBAL_CONFIG.getBoolean(DO_QUICK_CRAFTING)) {
					return RepairMethods.tryCraft(tool, cursorStack, slot, cursorSlot, entity, world);
				}
			}
			return false;
		});
	}

}
