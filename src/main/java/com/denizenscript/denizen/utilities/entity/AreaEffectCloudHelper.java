package com.denizenscript.denizen.utilities.entity;

import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.objects.core.ElementTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;

public class AreaEffectCloudHelper {
    private AreaEffectCloud entity;

    public AreaEffectCloudHelper(Entity entity) {
        this.entity = (AreaEffectCloud) entity;
    }

    ////////////////
    // Base Potion Data
    /////////

    private Potion getBPData() {
        return entity.getPotion();
    }

    public String getBPName() {
        return getBPData().getName("");
    }

    public boolean getBPUpgraded() {
        return getBPData().getEffects().getFirst().getAmplifier() > 1;
    }

//    public boolean getBPExtended() {
//        return getBPData().getEffects().getFirst().;
//    }

    public void setBP(Potion  type, boolean extended, boolean upgraded) {
        CompoundTag Potiontag = new CompoundTag();
        Potiontag.putByte("Amplifier",  (byte)(upgraded ? 2 : 1));
        Potiontag.putInt("Duration", extended ? 480 : 180);
        //todo make these available
        Potiontag.putBoolean("ShowIcon", true);
        Potiontag.putBoolean("ShowParticles", true);
        Potiontag.putBoolean("Ambient", true);
        entity.setPotion(new Potion(MobEffectInstance.load(Potiontag)));
    }

    ////////////////
    // Particles
    /////////
//todo
//    public Color getColor() {
//        return entity.getColor();
//    }

//    public void setColor(Color color) {
//        entity.setColor(color);
//    }

//    public String getParticle() {
//        return entity.getParticle().name();
//    }

//    public void setParticle(String name) {
//        Particle particle = Utilities.elementToEnumlike(new ElementTag(name, true), Particle.class);
//        if (particle != null) {
//            entity.setParticle(particle);
//        }
//    }

    ////////////////
    // Radius
    /////////

    public float getRadius() {
        return entity.getRadius();
    }

    public float getRadiusOnUse() {
        return entity.getRadiusOnUse();
    }

    public float getRadiusPerTick() {
        return entity.getRadiusPerTick();
    }

    public void setRadius(float radius) {
        entity.setRadius(radius);
    }

    public void setRadiusOnUse(float radius) {
        entity.setRadiusOnUse(radius);
    }

    public void setRadiusPerTick(float radius) {
        entity.setRadiusPerTick(radius);
    }

    ////////////////
    // Duration
    /////////

    public long getDuration() {
        return (long) entity.getDuration();
    }

    public long getDurationOnUse() {
        return (long) entity.getDurationOnUse();
    }
//todo
//    public long getReappDelay() {
//        return (long) entity.getReapplicationDelay();
//    }

    public long getWaitTime() {
        return (long) entity.getWaitTime();
    }

    public void setDuration(int ticks) {
        entity.setDuration(ticks);
    }

    public void setDurationOnUse(int ticks) {
        entity.setDurationOnUse(ticks);
    }
    //todo
//    public void setReappDelay(int ticks) {
//        entity.setReapplicationDelay(ticks);
//    }

    public void setWaitTime(int ticks) {
        entity.setWaitTime(ticks);
    }

    ////////////////
    // Custom Effects
    /////////
//todo
//    public List<PotionEffect> getCustomEffects() {
//        return entity.getCustomEffects();
//    }

//    public boolean hasCustomEffects() {
//        return entity.hasCustomEffects();
//    }

//    public void clearEffects() {
//        entity.clearCustomEffects();
//    }

//    public void removeEffect(PotionEffectType type) {
//        entity.removeCustomEffect(type);
//    }

//    public void addEffect(PotionEffect effect, boolean override) {
//        entity.addCustomEffect(effect, override);
//    }

    ////////////////
    // Misc
    /////////

    public LivingEntity getSource() {
        return entity.getOwner();
    }

    public void setSource(LivingEntity source) {
        entity.setOwner(source);
    }
}
