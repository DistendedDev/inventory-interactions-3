package com.diztend.inventoryinteractions.loader.parser;

import com.diztend.inventoryinteractions.interaction.OutputSpec;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.function.BiFunction;

public class SerializerUtil {

    public static RuntimeException illegalFields(String... fields) {
        StringBuilder builder = new StringBuilder("One of ");
        for (String field : fields) {
            builder.append("[").append(field).append("] ");
        }
        builder.append("should be present");
        return new RuntimeException(builder.toString());
    }

    public static int checkOnePresent(JsonObject json, String... fields) {
        int sum = 0;
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            if (json.has(fields[i])) {
                index = i;
                sum += 1;
            }
        }
        if (sum != 1) {
            throw illegalFields(fields);
        }
        return index;
    }

    public static Item stringToItem(String string) {
        Item item = Registries.ITEM.get(new Identifier(string));
        if (item != Items.AIR) {
            return item;
        } else {
            throw new RuntimeException("item " + item + " does not exist");
        }
    }

    public static TagKey<Item> stringToTag(String string) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(string));
    }

    public static BiFunction<ItemStack, ItemStack, ItemStack> stringToItemProvider(String string) {
        if (Objects.equals(string, "ii:slot")) {
            return OutputSpec.SLOT;
        } else if (Objects.equals(string, "ii:cursor")) {
            return OutputSpec.CURSOR;
        } else {
            return OutputSpec.itemProvider(stringToItem(string));
        }
    }

}
