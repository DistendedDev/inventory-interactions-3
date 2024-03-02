package com.diztend.inventoryinteractions.interaction;

import com.diztend.inventoryinteractions.II;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;

public class Interaction implements InventoryRMBEvent.Listener {

    private final InputSpec slotSpec;

    private final InputSpec cursorSpec;

    private final OutputSpec[] outputSpecs;

    public Interaction(InputSpec slotSpec, InputSpec cursorSpeck, OutputSpec... outputSpecs) {
        this.slotSpec = slotSpec;
        this.cursorSpec = cursorSpeck;
        this.outputSpecs = outputSpecs;
    }

    private int useLimit(ItemStack slotStack, ItemStack cursorStack) {
        return Math.min(slotSpec.action.maxUseCount(slotStack), cursorSpec.action.maxUseCount(cursorStack));
    }

    @Override
    public boolean onClick(ItemStack slotStack, ItemStack cursorStack, Slot slot, StackReference cursorSlot, PlayerEntity player) {
        if (slotSpec.itemCriteria.test(slotStack) && cursorSpec.itemCriteria.test(cursorStack)) {
            ItemStack slotCopy = slotStack.copy();
            ItemStack cursorCopy = cursorStack.copy();
            int limiter = useLimit(slotStack, cursorStack);
            ItemStack newSlotStack = slotSpec.action.use(limiter, slotStack, player);
            slot.setStack(newSlotStack);
            ItemStack newCursorStack = cursorSpec.action.use(limiter, cursorStack, player);
            cursorSlot.set(newCursorStack);
            ArrayList<ItemStack> toGive = new ArrayList<>();
            for (OutputSpec spec : outputSpecs) {
                ItemStack stack = spec.getStack(limiter, slotCopy, cursorCopy);
                while (stack.getCount() > stack.getMaxCount()) {
                    stack.decrement(stack.getMaxCount());
                    toGive.add(new ItemStack(stack.getItem(), stack.getMaxCount()));
                }
                toGive.add(stack);
            }
            boolean slotted = false;
            boolean cursored = false;
            for (ItemStack stack : toGive) {
                if (!slotted && slot.getStack().isEmpty()) {
                    slot.setStack(stack);
                    slotted = true;
                } else if (!cursored && cursorSlot.get().isEmpty()) {
                    cursorSlot.set(stack);
                    cursored = true;
                } else {
                    player.giveItemStack(stack);
                }
            }
            return limiter > 0;
        }
        return false;
    }

}
