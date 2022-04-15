package de.digitaldevs.gameapi.arena.reset;

import de.digitaldevs.gameapi.GameAPIPlugin;
import lombok.RequiredArgsConstructor;

import java.io.File;

@SuppressWarnings("ResultOfMethodCallIgnored")
@RequiredArgsConstructor
public class Resetter {

    private final GameAPIPlugin plugin;

    public void setup() {
        this.plugin.getDataFolder().mkdirs();

        File gameMaps = new File(this.plugin.getDataFolder(), "maps");
        if (!gameMaps.exists()) {
            gameMaps.mkdirs();
        }
    }

}
