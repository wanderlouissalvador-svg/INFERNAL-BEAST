package com.infernalbeast.events;

import com.infernalbeast.InfernalBeast;
import com.infernalbeast.registry.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = InfernalBeast.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.INFERNAL_BEAST.get(), com.infernalbeast.entity.InfernalCreatureEntity.createAttributes().build());
        event.put(ModEntities.DARK_STALKER.get(), com.infernalbeast.entity.InfernalCreatureEntity.createAttributes().build());
        event.put(ModEntities.VOID_DRAGON.get(), com.infernalbeast.entity.InfernalCreatureEntity.createAttributes().build());
    }

    private ModEntityEvents() {}
}
