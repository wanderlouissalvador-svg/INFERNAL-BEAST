package com.infernalbeast.registry;

import com.infernalbeast.InfernalBeast;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
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

    public static final RegistryObject<Item> INFERNAL_BEAST_SPAWN_EGG = ITEMS.register(
            "infernal_beast_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.INFERNAL_BEAST, 0x8B1A0E, 0xFF6A00, new Item.Properties())
    );

    public static final RegistryObject<Item> DARK_STALKER_SPAWN_EGG = ITEMS.register(
            "dark_stalker_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.DARK_STALKER, 0x17131F, 0x6B2737, new Item.Properties())
    );

    public static final RegistryObject<Item> VOID_DRAGON_SPAWN_EGG = ITEMS.register(
            "void_dragon_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.VOID_DRAGON, 0x21133D, 0x9D42E8, new Item.Properties())
    );

    private ModItems() {}
}
