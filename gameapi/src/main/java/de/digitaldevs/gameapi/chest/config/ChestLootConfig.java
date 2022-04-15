package de.digitaldevs.gameapi.chest.config;

import de.digitaldevs.core.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ChestLootConfig implements Config {

    private final File rootFolder;
    private final File configFile;
    private final File enchantmentFile;
    private FileConfiguration configuration;

    public ChestLootConfig(JavaPlugin plugin) {
        this.rootFolder = new File(plugin.getDataFolder(), "ChestLoot");
        this.configFile = new File(this.rootFolder.getPath(), "config.yml");
        this.enchantmentFile = new File(this.rootFolder.getPath(), "enchantments.yml");
        this.create();
    }

    @Override
    public Config create() {
        if (!this.rootFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            this.rootFolder.mkdirs();
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);

        if (!this.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                this.configFile.createNewFile();
                this.initDefault();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not create config.yml for ChestLoot implementation!");
            }
        }

        if (!this.enchantmentFile.exists()) {
            new SupportedEnchantmentFile(this.enchantmentFile).create();
        }

        return this;
    }

    @Override
    public void initDefault() {
        this.configuration.options().header("This is a demonstration about how each segment of the config should looks like.");
        this.configuration.options().header("The number must be increased for each loot item.");
        this.configuration.options().header("");
        this.configuration.options().header("A list of supported enchantments can be found in the enchantments.yml!");

        final String root = "items.0";
        final HashMap<String, Integer> enchantments = new HashMap<>();
        enchantments.put("FIRE_ASPECT", 1);
        enchantments.put("DURABILITY", 3);

        this.configuration.set(root + "material", "IRON_SWORD");
        this.configuration.set(root + "displayname", "&Thors hammer");
        this.configuration.set(root + "enchantments", enchantments);
        this.configuration.set(root + "chance", 0.2);
        this.configuration.set(root + "minAmount", 1);
        this.configuration.set(root + "maxAmount", 1);
        this.save();
    }

    @Override
    public Object get(@NotNull String path) {
        return this.configuration.get(path);
    }

    @Override
    public void set(@NotNull String path, Object o) {
        this.configuration.set(path, o);
    }

    @Override
    public void save() {
        try {
            this.configuration.save(this.configFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not save config for ChestLoot implementation!");
        }
    }

    private boolean exists() {
        return this.configFile.exists();
    }

    private static final class SupportedEnchantmentFile {

        private final File enchantmentFile;

        public SupportedEnchantmentFile(File enchantmentFile) {
            this.enchantmentFile = enchantmentFile;
        }

        public void create() {
            try {
                //noinspection ResultOfMethodCallIgnored
                this.enchantmentFile.createNewFile();
                final FileConfiguration configuration = YamlConfiguration.loadConfiguration(this.enchantmentFile);
                configuration.options().header("This is a list with the names of supported enchantments.");
                configuration.options().header("");
                final HashMap<String, String> enchantmentsToNames = new HashMap<>();
                enchantmentsToNames.put("Protection", "PROTECTION_ENVIRONMENTAL");
                enchantmentsToNames.put("Fire Protection", "PROTECTION_FIRE");
                enchantmentsToNames.put("Feather Falling", "PROTECTION_FALL");
                enchantmentsToNames.put("Blast Protection", "PROTECTION_EXPLOSIONS");
                enchantmentsToNames.put("Projectile Protection", "PROTECTION_PROJECTILE");
                enchantmentsToNames.put("Respiration", "OXYGEN");
                enchantmentsToNames.put("Aqua Affinity", "WATER_WORKER");
                enchantmentsToNames.put("Thorns", "THORNS");
                enchantmentsToNames.put("Depth Strider", "DEPTH_STRIDER");
                enchantmentsToNames.put("Sharpness", "DAMAGE_ALL");
                enchantmentsToNames.put("Smite", "DAMAGE_UNDEAD");
                enchantmentsToNames.put("Bane of Arthropods", "DAMAGE_ARTHROPODS");
                enchantmentsToNames.put("Knockback", "KNOCKBACK");
                enchantmentsToNames.put("Fire Aspect", "FIRE_ASPECT");
                enchantmentsToNames.put("Looting", "LOOT_BONUS_MOBS");
                enchantmentsToNames.put("Efficiency", "DIG_SPEED");
                enchantmentsToNames.put("Silk Touch", "SILK_TOUCH");
                enchantmentsToNames.put("Unbreaking", "DURABILITY");
                enchantmentsToNames.put("Fortune", "LOOT_BONUS_BLOCKS");
                enchantmentsToNames.put("Power", "ARROW_DAMAGE");
                enchantmentsToNames.put("Punch", "ARROW_KNOCKBACK");
                enchantmentsToNames.put("Flame", "ARROW_FIRE");
                enchantmentsToNames.put("Infinity", "ARROW_INFINITE");
                enchantmentsToNames.put("Luck of the Sea", "Sea");
                enchantmentsToNames.put("Lure", "LURE");

                configuration.set("Enchantments with their names", enchantmentsToNames);
                configuration.save(this.enchantmentFile);

            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not save enchantment file of ChestLoot implementation!");
            }
        }
    }

}
