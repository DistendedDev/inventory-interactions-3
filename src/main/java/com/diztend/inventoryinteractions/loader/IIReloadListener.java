package com.diztend.inventoryinteractions.loader;

import com.diztend.inventoryinteractions.II;
import com.diztend.inventoryinteractions.interaction.InputSpec;
import com.diztend.inventoryinteractions.interaction.Interaction;
import com.diztend.inventoryinteractions.interaction.InventoryRMBEvent;
import com.diztend.inventoryinteractions.interaction.OutputSpec;
import com.diztend.inventoryinteractions.loader.parser.InteractionSerializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Items;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Add data, add json files in a custom datapack in the folder ~/data/ii/interactions/
 */
public class IIReloadListener implements SimpleSynchronousResourceReloadListener {

    public JsonObject parseJson(ResourceManager manager, Identifier id) throws IOException {
        InputStream stream = manager.getResource(id).get().getInputStream();
        return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(II.MOD_ID, "ii");
    }

    @Override
    public void reload(ResourceManager manager) {
        InventoryRMBEvent.reset();
        InventoryRMBEvent.addListener(new Interaction(
                new InputSpec(InputSpec.Criteria.ofItem(Items.BIRCH_LOG), InputSpec.UseAction.consumes(1)),
                new InputSpec(InputSpec.Criteria.ofItem(Items.IRON_AXE), InputSpec.UseAction.damages(1)),
                new OutputSpec(OutputSpec.itemProvider(Items.STRIPPED_BIRCH_LOG)),
                new OutputSpec(OutputSpec.itemProvider(Items.STICK))));
        Map<Identifier, Resource> resources = manager.
                findResources("interactions", id -> id.toString().endsWith(".json"));
        for(Identifier id : resources.keySet()) {
            try {
                JsonObject json = parseJson(manager, id);
                InventoryRMBEvent.addListener(InteractionSerializer.serializeInteraction(json));
            } catch(Exception e) {
                II.LOGGER.error(String.format("Error occurred loading interaction %s, error message: %s",
                        id.toString(), e.getMessage()));
            }
        }
    }

}