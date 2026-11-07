package com.infernalbeast.client;

import com.infernalbeast.InfernalBeast;
import com.infernalbeast.registry.ModEntities;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = InfernalBeast.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModEntityRenderers {
    private static final ResourceLocation TEMP_TEXTURE =
            new ResourceLocation("minecraft", "textures/entity/zombie/zombie.png");

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.INFERNAL_BEAST.get(),
                context -> new MobRenderer<>(context,
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.8F) {
                    @Override
                    public ResourceLocation getTextureLocation(com.infernalbeast.entity.InfernalCreatureEntity entity) {
                        return TEMP_TEXTURE;
                    }
                });
        event.registerEntityRenderer(ModEntities.DARK_STALKER.get(),
                context -> new MobRenderer<>(context,
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.55F) {
                    @Override
                    public ResourceLocation getTextureLocation(com.infernalbeast.entity.InfernalCreatureEntity entity) {
                        return TEMP_TEXTURE;
                    }
                });
        event.registerEntityRenderer(ModEntities.VOID_DRAGON.get(),
                context -> new MobRenderer<>(context,
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 1.0F) {
                    @Override
                    public ResourceLocation getTextureLocation(com.infernalbeast.entity.InfernalCreatureEntity entity) {
                        return TEMP_TEXTURE;
                    }
                });
    }

    private ModEntityRenderers() {}
}
