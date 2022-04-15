package de.digitaldevs.gameapi.chest;

import de.digitaldevs.core.builder.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LootItem {

    private final Material material;
    private final String displayname;
    private final Map<Enchantment, Integer> enchantmentsWithLevelsMap = new IdentityHashMap<>();

    private final double chance;
    private final int minAmount;
    private final int maxAmount;

    public LootItem(@NotNull final ConfigurationSection section) {
        Material material;
        try {
            material = Material.valueOf(section.getString("material"));
        } catch (IllegalArgumentException e) {
            material = Material.AIR;
        }

        this.material = material;

        this.displayname = section.getString("displayname");

        ConfigurationSection enchantmentSection = section.getConfigurationSection("enchantments");
        if (enchantmentSection != null) {
            for (String key : enchantmentSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByName(key);
                if (enchantment != null) {
                    int level = enchantmentSection.getInt(key);
                    this.enchantmentsWithLevelsMap.put(enchantment, level);
                }
            }
        }

        this.chance = section.getDouble("chance");
        this.minAmount = section.getInt("minAmount");
        this.maxAmount = section.getInt("maxAmount");
    }

    public boolean shouldBeUsed(@NotNull final Random random) {
        return random.nextDouble() >= this.chance;
    }

    public ItemStack build(@NotNull final ThreadLocalRandom random) {
        int amount = random.nextInt(this.minAmount, this.maxAmount + 1);

        ItemBuilder itemBuilder = new ItemBuilder(this.material, amount);

        if (this.displayname != null) itemBuilder.name(ChatColor.translateAlternateColorCodes('&', this.displayname));

        if (!this.enchantmentsWithLevelsMap.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> entry : this.enchantmentsWithLevelsMap.entrySet()) {
                itemBuilder.enchantUnsafe(entry.getKey(), entry.getValue());
            }
        }

        return itemBuilder.build();
    }

}
