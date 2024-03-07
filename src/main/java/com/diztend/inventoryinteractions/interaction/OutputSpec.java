package com.diztend.inventoryinteractions.interaction;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;

public class OutputSpec {

    public static final BiFunction<ItemStack, ItemStack, ItemStack> SLOT = (slotStack, cursorStack) -> slotStack;

    public static final BiFunction<ItemStack, ItemStack, ItemStack> CURSOR = (slotStack, cursorStack) -> cursorStack;

    public static BiFunction<ItemStack, ItemStack, ItemStack> itemProvider(Item item) {
        return (slotStack, cursorStack) -> new ItemStack(item);
    }

    public BiFunction<ItemStack, ItemStack, ItemStack> provider;

    public OutputSpec(BiFunction<ItemStack, ItemStack, ItemStack> baseProvider) {
        this.provider = baseProvider;
    }

    public OutputSpec setCount(int count) {
        provider = provider.andThen(result -> {
            result.setCount(Math.min(count, result.getMaxCount()));
            return result;
        });
        return this;
    }

    public OutputSpec setDamage(double damage) {
        provider = provider.andThen(result -> {
            if (result.isDamageable()) {
                result.setDamage(result.getDamage() + (int) damage);
            }
            return result;
        });
        return this;
    }

    public ItemStack getStack(int count, ItemStack slotStack, ItemStack cursorStack) {
        ItemStack stack = this.provider.apply(slotStack, cursorStack);
        if (stack.getDamage() == 0) {
            stack.setCount(stack.getCount() * count);
        }
        return stack;
    }

}
