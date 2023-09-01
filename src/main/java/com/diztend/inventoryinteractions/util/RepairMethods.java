package com.diztend.inventoryinteractions.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.Optional;

public class RepairMethods {

    public static boolean unitRepair(ItemStack tool, ItemStack resource, int amount) {
        tool.setDamage(Math.max(tool.getDamage() - amount, 0));
        resource.decrement(1);
        return true;
    }

    public static boolean unitRepairRate(ItemStack tool, ItemStack otherStack, double rate) {
        return unitRepair(tool, otherStack, (int) Math.floor(tool.getMaxDamage() * rate));
    }

    public static boolean combineTools(ItemStack tool, ItemStack otherTool) {
        tool.setDamage(Math.max(tool.getDamage() - otherTool.getMaxDamage() + otherTool.getDamage(), 0));
        otherTool.decrement(1);
        return true;
    }

    public static boolean nameItem(ItemStack stack, ItemStack otherStack) {
        otherStack.decrement(1);
        stack.setCustomName(otherStack.getName());
        return true;
    }

    public static boolean tryCraft(ItemStack stack, ItemStack otherStack, Slot slot, StackReference cursorSlot, PlayerEntity player, World world) {
        CraftingInventory inventory = new CraftingInventory(player.playerScreenHandler, 2, 2);
        inventory.setStack(2, stack);
        inventory.setStack(0, otherStack);
        Optional<CraftingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory, world);
        if (recipe.isPresent() && !recipe.get().getOutput(world.getRegistryManager()).isEmpty()) {
            ItemStack output = recipe.get().getOutput(world.getRegistryManager()).copy();
            int stackCount = stack.getCount();
            int otherStackCount = otherStack.getCount();
            int minStackCount = Math.min(stackCount, otherStackCount);
            stackCount -= minStackCount;
            otherStackCount -= minStackCount;
            int craftedStackCount = output.getCount() * minStackCount;
            if (craftedStackCount <= output.getMaxCount() * 2 && stackCount == 0 && otherStackCount == 0) {
                output.setCount(Math.min(output.getMaxCount(), craftedStackCount));
                craftedStackCount = Math.max(0, craftedStackCount - output.getMaxCount());
                slot.setStack(output.copy());
                output.setCount(craftedStackCount);
                cursorSlot.set(output.copy());
                return true;
            } else if (craftedStackCount <= output.getMaxCount()) {
                output.setCount(craftedStackCount);
                if (stackCount == 0) {
                    slot.setStack(output.copy());
                    otherStack.setCount(otherStackCount);
                    return true;
                } else {
                    stack.setCount(stackCount);
                    cursorSlot.set(stack.copy());
                    slot.setStack(output.copy());
                    return true;
                }
            }
        }
        return false;
    }

}
