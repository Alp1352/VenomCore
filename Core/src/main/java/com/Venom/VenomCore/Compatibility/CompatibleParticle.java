package com.Venom.VenomCore.Compatibility;
import com.Venom.VenomCore.Server.ServerVersion;

/**
 * @author Alp Beji
 * @apiNote A class for version compatible particles.
 * Supports 1.8 - 1.16.5
 */
public enum CompatibleParticle {
    EXPLOSION_NORMAL(ServerVersion.v1_8_R1,   "explode"),
    EXPLOSION_LARGE(ServerVersion.v1_8_R1,  "largeexplosion"),
    EXPLOSION_HUGE(ServerVersion.v1_8_R1,  "hugeexplosion"),
    FIREWORKS_SPARK(ServerVersion.v1_8_R1,  "fireworksSpark"),
    WATER_BUBBLE(ServerVersion.v1_8_R1,  "bubble"),
    WATER_SPLASH(ServerVersion.v1_8_R1,  "splash"),
    WATER_WAKE(ServerVersion.v1_8_R1,  "wake"),
    SUSPENDED(ServerVersion.v1_8_R1,  "suspended"),
    SUSPENDED_DEPTH(ServerVersion.v1_8_R1,  "depthsuspend"),
    CRIT(ServerVersion.v1_8_R1, "crit"),
    CRIT_MAGIC(ServerVersion.v1_8_R1, "magicCrit"),
    SMOKE_NORMAL(ServerVersion.v1_8_R1, "smoke"),
    SMOKE_LARGE(ServerVersion.v1_8_R1, "largesmoke"),
    SPELL(ServerVersion.v1_8_R1, "spell"),
    SPELL_INSTANT(ServerVersion.v1_8_R1, "instantSpell"),
    SPELL_MOB(ServerVersion.v1_8_R1,"mobSpell"),
    SPELL_MOB_AMBIENT(ServerVersion.v1_8_R1, "mobSpellAmbient"),
    SPELL_WITCH(ServerVersion.v1_8_R1, "witchMagic"),
    DRIP_WATER(ServerVersion.v1_8_R1, "dripWater"),
    DRIP_LAVA(ServerVersion.v1_8_R1, "dripLava"),
    VILLAGER_ANGRY(ServerVersion.v1_8_R1, "angryVillager"),
    VILLAGER_HAPPY(ServerVersion.v1_8_R1, "happyVillager"),
    TOWN_AURA(ServerVersion.v1_8_R1, "townaura"),
    NOTE(ServerVersion.v1_8_R1, "note"),
    PORTAL(ServerVersion.v1_8_R1, "portal"),
    ENCHANTMENT_TABLE(ServerVersion.v1_8_R1, "enchantmenttable"),
    FLAME(ServerVersion.v1_8_R1, "flame"),
    LAVA(ServerVersion.v1_8_R1, "lava"),
    FOOTSTEP(ServerVersion.v1_8_R1, "footstep"),
    CLOUD(ServerVersion.v1_8_R1, "cloud"),
    REDSTONE(ServerVersion.v1_8_R1, "reddust"), //DustOptions
    SNOWBALL(ServerVersion.v1_8_R1, "snowballpoof"),
    SNOW_SHOVEL(ServerVersion.v1_8_R1, "snowshovel"),
    SLIME(ServerVersion.v1_8_R1, "slime"),
    HEART(ServerVersion.v1_8_R1, "heart"),
    BARRIER(ServerVersion.v1_8_R1,"barrier"),
    ITEM_CRACK(ServerVersion.v1_8_R1, "iconcrack"), // ItemStack
    BLOCK_CRACK(ServerVersion.v1_8_R1, "blockcrack"), // BlockData
    BLOCK_DUST(ServerVersion.v1_8_R1, "blockdust"), // BlockData
    WATER_DROP(ServerVersion.v1_8_R1, "droplet"),
    // 1.8-1.12 included ITEM_TAKE
    MOB_APPEARANCE(ServerVersion.v1_8_R1, "mobappearance"),
    /// End 1.8 particles ///
    DRAGON_BREATH(ServerVersion.v1_9_R1, "SPELL_MOB_AMBIENT","mobSpellAmbient"),
    END_ROD(ServerVersion.v1_9_R1, "ENCHANTMENT_TABLE","enchantmenttable"),
    DAMAGE_INDICATOR(ServerVersion.v1_9_R1, "VILLAGER_ANGRY", "angryVillager"),
    SWEEP_ATTACK(ServerVersion.v1_9_R1, "CRIT", "crit"),
    /// End 1.9 particles ///
    FALLING_DUST(ServerVersion.v1_10_R1, "BLOCK_DUST", "blockdust"), // BlockData
    /// End 1.10 ///
    TOTEM(ServerVersion.v1_11_R1, "VILLAGER_HAPY","happyVillager"),
    SPIT(ServerVersion.v1_11_R1, "REDSTONE", "reddust"),
    /// End 1.11-1.12 ///
    SQUID_INK(ServerVersion.v1_13_R1, "CRIT","crit"),
    BUBBLE_POP(ServerVersion.v1_13_R1, "CRIT","crit"),
    CURRENT_DOWN(ServerVersion.v1_13_R1, "CRIT","crit"),
    BUBBLE_COLUMN_UP(ServerVersion.v1_13_R1, "CRIT","crit"),
    NAUTILUS(ServerVersion.v1_13_R1, "ENCHANTMENT_TABLE","enchantmenttable"),
    DOLPHIN(ServerVersion.v1_13_R1, "TOWN_AURA","townaura"),
    /// End 1.13 ///
    SNEEZE(ServerVersion.v1_14_R1, "REDSTONE","reddust"),
    CAMPFIRE_COSY_SMOKE(ServerVersion.v1_14_R1, "SMOKE_NORMAL","smoke"),
    CAMPFIRE_SIGNAL_SMOKE(ServerVersion.v1_14_R1, "SMOKE_LARGE","largesmoke"),
    COMPOSTER(ServerVersion.v1_14_R1, "CRIT","crit"),
    FLASH(ServerVersion.v1_14_R1, "EXPLODE","explode"), // idk
    FALLING_LAVA(ServerVersion.v1_14_R1, "DRIP_LAVA","dripLava"),
    LANDING_LAVA(ServerVersion.v1_14_R1, "LAVA","lava"),
    FALLING_WATER(ServerVersion.v1_14_R1, "DRIP_WATER","dripWater"),
    /// End 1.14 ///
    DRIPPING_HONEY(ServerVersion.v1_15_R1, "DRIP_WATER","dripWater"),
    FALLING_HONEY(ServerVersion.v1_15_R1, "DRIP_WATER","dripWater"),
    FALLING_NECTAR(ServerVersion.v1_15_R1, "DRIP_WATER","dripWater"),
    LANDING_HONEY(ServerVersion.v1_15_R1, "DRIP_WATER","dripWater"),
    /// End 1.15 ///
    SOUL_FIRE_FLAME(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater"),
    ASH(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater"),
    CRIMSON_SPORE(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater"),
    WARPED_SPORE(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater"),
    SOUL(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater"),
    DRIPPING_OBSIDIAN_TEAR(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater"),
    FALLING_OBSIDIAN_TEAR(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater"),
    LANDING_OBSIDIAN_TEAR(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater"),
    REVERSE_PORTAL(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater"),
    WHITE_ASH(ServerVersion.v1_16_R1, "DRIP_WATER","dripWater");
    /// End 1.16 ///

    private final ServerVersion since;
    private String compatibleName;

    /**
     * A compatible particle.
     * @param since Since when does this particle exist?
     * @param name The fall back name for 1.8.
     */
    CompatibleParticle(ServerVersion since, String name) {
        this(since, name, name);
    }

    /**
     * A compatible particle.
     * @param since Since when does this particle exist?
     * @param compatibleName The fall back name for 1.8+.
     * @param oneEight The fall back name for 1.8.
     */
    CompatibleParticle(ServerVersion since, String compatibleName, String oneEight) {
        this.since = since;
        if (isOneEight()) {
            this.compatibleName = oneEight;
        } else if (ServerVersion.isServerVersionHigherOrEqual(since)){
            this.compatibleName = name();
        } else if (ServerVersion.isServerVersionLowerThan(since)) {
            this.compatibleName = compatibleName;
        }
    }

    /**
     * Gets the compatible name for this version.
     * @return The compatible name of the particle.
     */
    public String getCompatibleName() {
        return compatibleName;
    }

    /**
     * Gets the release version of the particle.
     * @return The version.
     */
    public ServerVersion getSince() {
        return since;
    }

    private static boolean isOneEight() {
        return ServerVersion.isServerVersion(ServerVersion.v1_8_R1) || ServerVersion.isServerVersion(ServerVersion.v1_8_R2) || ServerVersion.isServerVersion(ServerVersion.v1_8_R3);
    }
}
