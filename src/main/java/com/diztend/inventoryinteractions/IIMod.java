package com.diztend.inventoryinteractions;

import com.diztend.inventoryinteractions.gameplay.InventoryClickEvent;
import com.diztend.inventoryinteractions.gameplay.InteractionMethods;
import com.diztend.inventoryinteractions.util.JSONConfig;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IIMod implements ModInitializer {

	public static final String MOD_ID = "inventory-interactions";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static JSONConfig GLOBAL_CONFIG;

	public static final String DO_UNIT_REPAIR = "Repair items by clicking with an item";
	public static final String DO_ENCHANTED_UNIT_REPAIR = "Repair items even if it is enchanted";
	public static final String DO_TOOL_COMBINE = "Combine tools";
	public static final String DO_RENAME = "Rename items with name tags";
	public static final String DO_STRIP = "Strip logs with axe";
	public static final String DO_CONCRETE_SOLIDIFY = "Solidify concrete powder with water bucket";
	public static final String DO_COPPER_WAX = "Apply wax to copper";
	public static final String DO_COPPER_STRIP = "Strip wax or oxidation off copper with axe";
	//public static final String DO_ARMOR_DYE = "Dye leather items";
	public static final String DO_SHAPELESS_CRAFTING = "Apply shapeless crafting when clicking 2 items";

	@Override
	public void onInitialize() {
		JsonObject defaultConfig = new JsonObject();
		defaultConfig.addProperty(DO_UNIT_REPAIR, true);
		defaultConfig.addProperty(DO_ENCHANTED_UNIT_REPAIR, true);
		defaultConfig.addProperty(DO_TOOL_COMBINE, true);
		defaultConfig.addProperty(DO_RENAME, true);
		defaultConfig.addProperty(DO_STRIP, true);
		defaultConfig.addProperty(DO_CONCRETE_SOLIDIFY, true);
		defaultConfig.addProperty(DO_COPPER_WAX, false);
		defaultConfig.addProperty(DO_COPPER_STRIP, false);
		defaultConfig.addProperty(DO_SHAPELESS_CRAFTING, false);
		GLOBAL_CONFIG = new JSONConfig(MOD_ID, defaultConfig, LOGGER);
		InventoryClickEvent.addListener( (button, slot, cursorSlot, entity) -> {
			ItemStack slotStack = slot.getStack();
			ItemStack cursorStack = cursorSlot.get();
			World world = entity.world;
			if (button == 1 && !slotStack.isEmpty() && !cursorStack.isEmpty() && !world.isClient()){
				if (slotStack.isDamaged()) {
					if (slotStack.getItem().canRepair(slotStack, cursorStack) && GLOBAL_CONFIG.getBoolean(DO_UNIT_REPAIR)) {
						if (!slotStack.hasEnchantments() || GLOBAL_CONFIG.getBoolean(DO_ENCHANTED_UNIT_REPAIR)) {
							return InteractionMethods.unitRepairRate(slotStack, cursorStack, 0.25);
						}
					} else if (slotStack.getItem() == cursorStack.getItem() &&
							GLOBAL_CONFIG.getBoolean(DO_TOOL_COMBINE) &&
							!slotStack.hasEnchantments() && !cursorStack.hasEnchantments()) {
						return InteractionMethods.combineTools(slotStack, cursorStack);
					}
				}
				if (cursorStack.getItem() == Items.NAME_TAG && cursorStack.hasCustomName() && !slotStack.hasCustomName() &&
						!slotStack.getItem().equals(Items.NAME_TAG) && GLOBAL_CONFIG.getBoolean(DO_RENAME)) {
					return InteractionMethods.nameItem(slotStack, cursorStack);
				}
				if (GLOBAL_CONFIG.getBoolean(DO_SHAPELESS_CRAFTING)) {
					return InteractionMethods.tryCraft(slotStack, cursorStack, slot, cursorSlot, entity, world);
				}
			}
			return false;
		});
	}

}
