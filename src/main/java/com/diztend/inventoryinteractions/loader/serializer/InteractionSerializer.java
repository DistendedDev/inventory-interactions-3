package com.diztend.inventoryinteractions.loader.serializer;

import com.diztend.inventoryinteractions.interaction.InputSpec;
import com.diztend.inventoryinteractions.interaction.Interaction;
import com.diztend.inventoryinteractions.interaction.OutputSpec;
import com.google.gson.JsonObject;

public class InteractionSerializer {

    public static InputSpec serializeInputSpec(JsonObject json) {
        int criteriaIndex = SerializerUtil.checkOnePresent(json, "tag", "item");
        int useActionIndex = SerializerUtil.checkOnePresent(json, "consume", "replace", "damage");
        InputSpec.Criteria criteria;
        if (criteriaIndex == 0) {
            criteria = InputSpec.Criteria.ofTag(SerializerUtil.stringToTag(json.get("tag").getAsString()));
        } else {
            criteria = InputSpec.Criteria.ofItem(SerializerUtil.stringToItem(json.get("item").getAsString()));
        }
        InputSpec.UseAction useAction;
        if (useActionIndex == 0) {
            useAction = InputSpec.UseAction.consumes(json.get("consume").getAsInt());
        } else if (useActionIndex == 1) {
            useAction = InputSpec.UseAction.replacedBy(SerializerUtil.stringToItem(json.get("replace").getAsString()));
        } else {
            useAction = InputSpec.UseAction.damages(json.get("damage").getAsDouble());
        }
        return new InputSpec(criteria, useAction);
    };

    public static OutputSpec serializeOutputSpec(JsonObject json) {
        SerializerUtil.checkOnePresent(json, "item");
        int modifierIndex = SerializerUtil.checkOnePresent(json, "damage", "count");
        OutputSpec outputSpec = new OutputSpec(SerializerUtil.stringToItemProvider(json.get("item").getAsString()));
        if (modifierIndex == 0) {
            outputSpec.setDamage(json.get("damage").getAsDouble());
        } else {
            outputSpec.setCount(json.get("count").getAsInt());
        }
        return outputSpec;
    }

    public static Interaction serializeInteraction(JsonObject json) {
        SerializerUtil.checkOnePresent(json, "slot");
        SerializerUtil.checkOnePresent(json, "cursor");
        SerializerUtil.checkOnePresent(json, "output");
        InputSpec slot = serializeInputSpec(json.get("slot").getAsJsonObject());
        InputSpec cursor = serializeInputSpec(json.get("cursor").getAsJsonObject());
        OutputSpec[] outputs = json.get("output").getAsJsonArray().asList()
                .stream().map(e -> serializeOutputSpec(e.getAsJsonObject()))
                .toArray(OutputSpec[]::new);
        return new Interaction(slot, cursor, outputs);
    }

}
