package com.diztend.inventoryinteractions.util;

import com.diztend.inventoryinteractions.II;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class IIConfig {

    public static SimpleConfig CONFIG = SimpleConfig.of("inventory_interactions")
            .provider(s -> "# Default Gamerules")
            .request();

    public static GameRules.Key<GameRules.BooleanRule> addBooleanConfig(String name, GameRules.Category category, boolean df) {
        boolean config = CONFIG.getOrDefault(name, df);
        return GameRuleRegistry.register(name, category, GameRuleFactory.createBooleanRule(config));
    }

    public static final GameRules.Key<GameRules.BooleanRule> DO_UNIT_REPAIR =
            addBooleanConfig("doUnitRepair", GameRules.Category.PLAYER, true);

    public static final GameRules.Key<GameRules.BooleanRule> DO_ENCHANTED_UNIT_REPAIR =
            addBooleanConfig("doUnitRepairEnchanted", GameRules.Category.PLAYER,true);

    public static final GameRules.Key<GameRules.BooleanRule> DO_TOOL_COMBINE =
            addBooleanConfig("doToolCombine", GameRules.Category.PLAYER, true);

    public static final GameRules.Key<GameRules.BooleanRule> DO_QUICK_RENAME =
            addBooleanConfig("doQuickRename", GameRules.Category.PLAYER, true);

    public static final GameRules.Key<GameRules.BooleanRule> DO_QUICK_CRAFTING =
            addBooleanConfig("doQuickCrafting", GameRules.Category.PLAYER, false);

    public static void init() {
        II.LOGGER.info("inventory interactions loading configs");
    }

}
