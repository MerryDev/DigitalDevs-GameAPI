package de.digitaldevs.gameapi.chest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class LootChestManager implements Listener {

    private final boolean fillAllChestOnWorld;
    private final boolean supportDoubleChests;
    private final Set<Location> chests;

    private final Set<Location> openedChest = new HashSet<>();
    private final List<LootItem> items = new ArrayList<>();

    public LootChestManager(@NotNull final FileConfiguration config, final Set<Location> chestLocations, boolean fillAllChestOnWorld, boolean supportDoubleChests) {
        ConfigurationSection section = config.getConfigurationSection("items");

        if (section == null) Bukkit.getLogger().severe("Could not load config.yml of ChestLoot implementation!");

        assert section != null;
        for (String key : section.getKeys(false)) {
            ConfigurationSection itemParameter = section.getConfigurationSection(key);
            this.items.add(new LootItem(itemParameter));
        }

        this.chests = chestLocations;
        this.fillAllChestOnWorld = fillAllChestOnWorld;
        this.supportDoubleChests = supportDoubleChests;
    }

    @EventHandler
    public void onOpenChest(InventoryOpenEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!supportDoubleChests) {
            if (!(holder instanceof Chest)) return;
            Chest chest = (Chest) holder;
            Location location = chest.getLocation();

            if (!fillAllChestOnWorld) {
                if (!this.chests.contains(location)) return;
            }
            if (this.hasBeenOpenedBefore(location)) return;

            this.openChest(location);
            this.fillChests(chest.getBlockInventory());

        } else {
            if (!(holder instanceof DoubleChest)) return;
            DoubleChest chest = (DoubleChest) holder;
            Location location = chest.getLocation();

            if (!this.chests.contains(location)) return;
            if (this.hasBeenOpenedBefore(location)) return;

            this.openChest(location);
            this.fillChests(chest.getInventory());
        }
    }


    public void fillChests(@NotNull final Inventory inventory) {
        inventory.clear();

        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<LootItem> usedItems = new HashSet<>();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            LootItem randomItem = this.items.get(random.nextInt(this.items.size()));
            if (usedItems.contains(randomItem)) continue;
            usedItems.add(randomItem);

            if (randomItem.shouldBeUsed(random)) {
                ItemStack itemStack = randomItem.build(random);
                inventory.setItem(slot, itemStack);
            }
        }
    }

    public void openChest(@NotNull final Location location) {
        this.openedChest.add(location);
    }

    public boolean hasBeenOpenedBefore(@NotNull final Location location) {
        return (this.chests.contains(location) && (!this.openedChest.contains(location)));
    }

    public void resetChests() {
        this.openedChest.clear();
    }
}
