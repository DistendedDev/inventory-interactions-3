package com.diztend.inventoryinteractions.mixin;

import com.diztend.inventoryinteractions.InventoryInteractions;
import com.diztend.inventoryinteractions.util.InventoryClickEvent;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public class ClickDetectorMixin {

	@Inject(at = @At("HEAD"), method = "onClicked", cancellable = true)
	private void onClickInject(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
		if (InventoryClickEvent.onClick(clickType == ClickType.RIGHT ? 1 : 0, slot, otherStack, cursorStackReference, player)) {
			cir.setReturnValue(true);
		}
	}

}
