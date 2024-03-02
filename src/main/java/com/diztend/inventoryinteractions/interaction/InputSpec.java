package com.diztend.inventoryinteractions.interaction;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.util.function.Predicate;

public class InputSpec {

    public static class Criteria implements Predicate<ItemStack> {

        public enum Type {
            ITEM,
            TAG
        }

        private final Type type;

        private final Item item;

        private final TagKey<Item> tag;

        private Criteria(Type type, Item item, TagKey<Item> tag) {
            this.type = type;
            this.item = item;
            this.tag = tag;
        }

        public static Criteria ofItem(Item item) {
            return new Criteria(Type.ITEM, item, null);
        }

        public static Criteria ofTag(TagKey<Item> tag) {
            return new Criteria(Type.TAG, null, tag);
        }

        @Override
        public boolean test(ItemStack stack) {
            if (type == Type.ITEM) {
                return stack.isOf(item);
            }
            return stack.isIn(tag);
        }

    }

    public static class UseAction {

        public enum Type {
            CONSUME,
            DAMAGE,
            REPLACE
        }

        private final Type type;

        private final int consumeAmount;

        private final double damage;

        private final Item replace;

        private UseAction(Type type, int consumeAmount, double damage, Item replace) {
            this.type = type;
            this.consumeAmount = consumeAmount;
            this.damage = damage;
            this.replace = replace;
        }

        public static UseAction consumes(int consumeAmount) {
            return new UseAction(Type.CONSUME, consumeAmount, 0, null);
        }

        public static UseAction damages(double damage) {
            return new UseAction(Type.DAMAGE, 0, damage, null);
        }

        public static UseAction replacedBy(Item item) {
            return new UseAction(Type.REPLACE, 0, 0, item);
        }

        public int maxUseCount(ItemStack stack) {
            if (type == Type.CONSUME) {
                if (consumeAmount > 0) {
                    return stack.getCount() / consumeAmount;
                }
                return 64;
            }
            if (type == Type.DAMAGE) {
                if (stack.isDamageable() && damage > 0) {
                    return MathHelper.clamp((stack.getMaxDamage() - stack.getDamage()) / (int)damage, 0, 64);
                }
                return 64;
            }
            return 1;
        }

        public ItemStack use(int count, ItemStack stack, PlayerEntity player) {
            if (type == Type.CONSUME) {
                stack.decrement(count * consumeAmount);
                return stack;
            }
            if (type == Type.DAMAGE) {
                stack.setDamage(stack.getDamage() + (int)(damage * count));
                if (stack.getDamage() > stack.getMaxDamage()) {
                    stack.decrement(1);
                }
                return stack;
            }
            ItemStack returnStack = new ItemStack(replace);
            returnStack.setCount(stack.getCount());
            return returnStack;
        }

    }

    public final Criteria itemCriteria;

    public final UseAction action;

    public InputSpec(Criteria itemCriteria, UseAction action) {
        this.itemCriteria = itemCriteria;
        this.action = action;
    }

}
