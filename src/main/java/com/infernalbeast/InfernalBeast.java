package com.infernalbeast;

import com.infernalbeast.registry.ModBlocks;
import com.infernalbeast.registry.ModEntities;
import com.infernalbeast.registry.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

@Mod(InfernalBeast.MOD_ID)
public class InfernalBeast {
    public static final String MOD_ID = "infernal_beast";

    public InfernalBeast() {
        GeckoLib.initialize();
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
    }
}
