package com.infernalbeast.registry;

import com.infernalbeast.InfernalBeast;
import com.infernalbeast.entity.InfernalCreatureEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, InfernalBeast.MOD_ID);

    public static final RegistryObject<EntityType<InfernalCreatureEntity>> INFERNAL_BEAST =
            ENTITIES.register("infernal_beast", () -> EntityType.Builder
                    .of(InfernalCreatureEntity::new, MobCategory.MONSTER)
                    .sized(1.45F, 2.35F).clientTrackingRange(10).updateInterval(2)
                    .build("infernal_beast"));

    public static final RegistryObject<EntityType<InfernalCreatureEntity>> DARK_STALKER =
            ENTITIES.register("dark_stalker", () -> EntityType.Builder
                    .of(InfernalCreatureEntity::new, MobCategory.MONSTER)
                    .sized(0.9F, 2.15F).clientTrackingRange(8).updateInterval(2)
                    .build("dark_stalker"));

    public static final RegistryObject<EntityType<InfernalCreatureEntity>> VOID_DRAGON =
            ENTITIES.register("void_dragon", () -> EntityType.Builder
                    .of(InfernalCreatureEntity::new, MobCategory.MONSTER)
                    .sized(2.4F, 2.8F).clientTrackingRange(12).updateInterval(2)
                    .build("void_dragon"));

    private ModEntities() {}
}
