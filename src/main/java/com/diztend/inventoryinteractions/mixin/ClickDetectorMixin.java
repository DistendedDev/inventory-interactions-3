package com.diztend.inventoryinteractions.mixin;

import com.diztend.inventoryinteractions.interaction.InventoryRMBEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ClickDetectorMixin {

	@Inject(at = @At("HEAD"), method = "onClicked", cancellable = true)
	public void onClickInject(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
		if (clickType == ClickType.RIGHT) {
			if (InventoryRMBEvent.onClick(stack, otherStack, slot, cursorStackReference, player)) {
				cir.setReturnValue(true);
			}
		}
	}

}
