package com.infernalbeast.entity;

import com.infernalbeast.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class InfernalCreatureEntity extends Monster {
    private int specialAttackCooldown;

    public InfernalCreatureEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 18;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 52.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.29D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 36.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.35D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.15D, true));
        this.goalSelector.addGoal(3, new SpecialAttackGoal());
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.specialAttackCooldown > 0) this.specialAttackCooldown--;
    }

    private void useSpecialAttack(LivingEntity target) {
        Vec3 aim = new Vec3(
                target.getX() - this.getX(),
                target.getY(0.5D) - this.getY(0.5D),
                target.getZ() - this.getZ()
        ).normalize();

        if (this.getType() == ModEntities.INFERNAL_BEAST.get()) {
            LargeFireball fireball = new LargeFireball(
                    this.level(), this, aim.x, aim.y, aim.z, 1
            );
            fireball.setPos(this.getX(), this.getY(0.65D), this.getZ());
            this.level().addFreshEntity(fireball);
            this.playSound(net.minecraft.sounds.SoundEvents.BLAZE_SHOOT, 1.4F, 0.75F);
            this.specialAttackCooldown = 100;
        } else if (this.getType() == ModEntities.VOID_DRAGON.get()) {
            DragonFireball bolt = new DragonFireball(
                    this.level(), this, aim.x, aim.y, aim.z
            );
            bolt.setPos(this.getX(), this.getY(0.7D), this.getZ());
            this.level().addFreshEntity(bolt);
            this.playSound(net.minecraft.sounds.SoundEvents.ENDER_DRAGON_SHOOT, 1.5F, 0.8F);
            this.specialAttackCooldown = 80;
        } else {
            Vec3 leap = new Vec3(aim.x * 1.15D, 0.48D, aim.z * 1.15D);
            this.setDeltaMovement(leap);
            this.hasImpulse = true;
            this.playSound(net.minecraft.sounds.SoundEvents.ENDERMAN_SCREAM, 1.0F, 0.65F);
            this.specialAttackCooldown = 65;
        }
    }

    private class SpecialAttackGoal extends Goal {
        private SpecialAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = InfernalCreatureEntity.this.getTarget();
            if (target == null || !target.isAlive() || InfernalCreatureEntity.this.specialAttackCooldown > 0) {
                return false;
            }
            double distance = InfernalCreatureEntity.this.distanceToSqr(target);
            return distance >= 16.0D && distance <= 324.0D
                    && InfernalCreatureEntity.this.getSensing().hasLineOfSight(target);
        }

        @Override
        public void start() {
            LivingEntity target = InfernalCreatureEntity.this.getTarget();
            if (target != null) {
                InfernalCreatureEntity.this.getLookControl().setLookAt(target, 30.0F, 30.0F);
                InfernalCreatureEntity.this.useSpecialAttack(target);
            }
        }
    }
}
