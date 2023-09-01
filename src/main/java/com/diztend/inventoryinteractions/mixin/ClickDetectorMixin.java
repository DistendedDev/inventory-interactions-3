package com.diztend.inventoryinteractions.mixin;

import com.diztend.inventoryinteractions.InventoryInteractions;
import com.diztend.inventoryinteractions.util.InventoryClickEvent;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ScreenHandler.class)
public class ClickDetectorMixin {
	@Shadow @Final public List<Slot> slots;

	@Inject(at = @At("HEAD"), method = "onSlotClick", cancellable = true)
	private void onSlotClickInject(int slotIndex, int button, SlotActionType actionType, PlayerEntity playerEntity, CallbackInfoReturnable<ItemStack> info) {
		if (slotIndex > 0 && slotIndex < slots.size() &&
				InventoryClickEvent.onClick(slotIndex, button, slots.get(slotIndex),
						playerEntity.inventory.getCursorStack(), actionType, playerEntity)) {
			info.setReturnValue(slots.get(slotIndex).getStack());
			info.cancel();
		}
	}
}
