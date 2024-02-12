package com.diztend.inventoryinteractions;

import com.diztend.inventoryinteractions.util.InventoryClickEvent;
import com.diztend.inventoryinteractions.util.RepairMethods;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class InventoryInteractions implements ModInitializer {

	public static final String MOD_ID = "inventory-interactions";

	public static final GameRules.Key<GameRules.BooleanRule> DO_UNIT_REPAIR = GameRuleRegistry
			.register("doUnitRepair", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	public static final GameRules.Key<GameRules.BooleanRule> DO_ENCHANTED_UNIT_REPAIR = GameRuleRegistry
			.register("doUnitRepairEnchanted", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	public static final GameRules.Key<GameRules.BooleanRule> DO_TOOL_COMBINE = GameRuleRegistry
			.register("doToolCombine", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	public static final GameRules.Key<GameRules.BooleanRule> DO_QUICK_RENAME = GameRuleRegistry
			.register("doQuickRename", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

	public static final GameRules.Key<GameRules.BooleanRule> DO_QUICK_CRAFTING = GameRuleRegistry
			.register("doQuickCrafting", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));

	@Override
	public void onInitialize() {
		InventoryClickEvent.addListener( (button, slot, cursorStack, cursorSlot, entity) -> {
			ItemStack tool = slot.getStack();
			World world = entity.getWorld();
			if (button == 1 && !tool.isEmpty() && !cursorStack.isEmpty() && !world.isClient()){
				if (tool.isDamaged()) {
					if (tool.getItem().canRepair(tool, cursorStack) && world.getGameRules().getBoolean(DO_UNIT_REPAIR)) {
						if (!tool.hasEnchantments() || world.getGameRules().getBoolean(DO_ENCHANTED_UNIT_REPAIR)) {
							return RepairMethods.unitRepairRate(tool, cursorStack, 0.25);
						}
					} else if (tool.getItem() == cursorStack.getItem() &&
							world.getGameRules().getBoolean(DO_TOOL_COMBINE) &&
							!tool.hasEnchantments() && !cursorStack.hasEnchantments()) {
						return RepairMethods.combineTools(tool, cursorStack);
					}
				}
				if (cursorStack.getItem() == Items.NAME_TAG && cursorStack.hasCustomName() && !tool.hasCustomName() &&
						!tool.getItem().equals(Items.NAME_TAG) && world.getGameRules().getBoolean(DO_QUICK_RENAME)) {
					return RepairMethods.nameItem(tool, cursorStack);
				}
				if (world.getGameRules().getBoolean(DO_QUICK_CRAFTING)) {
					return RepairMethods.tryCraft(tool, cursorStack, slot, cursorSlot, entity, world);
				}
			}
			return false;
		});
	}

}
