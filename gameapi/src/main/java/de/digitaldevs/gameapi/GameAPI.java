package de.digitaldevs.gameapi;

import de.digitaldevs.gameapi.arena.Arena;
import de.digitaldevs.gameapi.arena.reset.Resetter;
import de.digitaldevs.gameapi.chest.LootChestManager;
import de.digitaldevs.gameapi.gamestate.GameStateManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Set;

public interface GameAPI {

    Resetter getArenaResetter();

    Arena createArena(@NotNull final File worldFolder, @NotNull final String worldName, boolean loadOnInit);

    LootChestManager registerLootChestManager(@NotNull final FileConfiguration config, final Set<Location> chestLocations, boolean fillAllChestOnWorld, boolean supportDoubleChests);

    @Nullable LootChestManager getLootChestManager();

    GameStateManager getGameStateManager();

}
