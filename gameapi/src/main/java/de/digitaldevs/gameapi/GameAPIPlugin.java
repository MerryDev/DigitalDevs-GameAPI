package de.digitaldevs.gameapi;

import de.digitaldevs.gameapi.arena.Arena;
import de.digitaldevs.gameapi.arena.LocalArena;
import de.digitaldevs.gameapi.arena.reset.Resetter;
import de.digitaldevs.gameapi.chest.LootChestManager;
import de.digitaldevs.gameapi.gamestate.GameStateManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Set;

public class GameAPIPlugin extends JavaPlugin implements GameAPI {

    private static volatile GameAPI api;
    private Resetter resetter;
    private LootChestManager lootChestManager;

    @Override
    public void onEnable() {
        api = this;
        this.resetter = new Resetter(this);

        getLogger().info("GameAPI was loaded successfully :)");
    }

    /*-------------------------------------------------------------------------------------
                                API responses
     */

    public static GameAPI getApi() {
        return api;
    }

    @Override
    public Resetter getArenaResetter() {
        return this.resetter;
    }

    @Override
    public Arena createArena(@NotNull File worldFolder, @NotNull String worldName, boolean loadOnInit) {
        return new LocalArena(worldFolder, worldName, loadOnInit);
    }

    @Override
    public LootChestManager registerLootChestManager(@NotNull FileConfiguration config, Set<Location> chestLocations, boolean fillAllChestOnWorld, boolean supportDoubleChests) {
        LootChestManager lootChestManager;
        if (this.lootChestManager == null) {
            lootChestManager = new LootChestManager(config, chestLocations, fillAllChestOnWorld, supportDoubleChests);
        } else {
            lootChestManager = this.getLootChestManager();
        }
        this.lootChestManager = lootChestManager;
        return lootChestManager;
    }

    @Override
    public @Nullable LootChestManager getLootChestManager() {
        return this.lootChestManager;
    }

    @Override
    public GameStateManager getGameStateManager() {
        return GameStateManager.getInstance();
    }
}
