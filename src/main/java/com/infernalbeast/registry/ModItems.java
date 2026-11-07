package com.infernalbeast.registry;

import com.infernalbeast.InfernalBeast;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, InfernalBeast.MOD_ID);

    public static final RegistryObject<Item> MECHANICAL_METAL_BLOCK_ITEM = ITEMS.register(
            "mechanical_metal_block",
            () -> new BlockItem(ModBlocks.MECHANICAL_METAL_BLOCK.get(), new Item.Properties())
    );

    private ModItems() {}
}
