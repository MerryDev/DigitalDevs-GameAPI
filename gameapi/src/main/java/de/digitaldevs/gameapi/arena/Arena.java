package de.digitaldevs.gameapi.arena;

import org.bukkit.World;

public interface Arena {

    boolean load();

    void unload();

    boolean restoreFromSource();

    boolean isLoaded();

    World getWorld();

}
