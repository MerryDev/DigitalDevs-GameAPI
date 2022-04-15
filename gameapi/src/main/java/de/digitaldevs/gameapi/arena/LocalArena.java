package de.digitaldevs.gameapi.arena;

import de.digitaldevs.gameapi.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class LocalArena implements Arena {

    private final File sourceWorldFolder;
    private File activeWorldFolder;
    private World bukkitWorld;

    public LocalArena(@NotNull final File worldFolder, @NotNull final String worldName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(worldFolder, worldName);
        if (loadOnInit) this.load();
    }

    @Override
    public boolean load() {
        if (this.isLoaded()) return true;

        this.activeWorldFolder = new File(Bukkit.getWorldContainer().getParentFile(), this.sourceWorldFolder.getName() + "_active_" + System.currentTimeMillis());

        try {
            FileUtil.copy(this.sourceWorldFolder, this.activeWorldFolder);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not load arena " + this.sourceWorldFolder.getName() + "!");
            return false;
        }

        this.bukkitWorld = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));

        if (this.bukkitWorld != null)
            this.bukkitWorld.setAutoSave(false); // It is a temporary world, so we don't want to save any data

        return this.isLoaded();

    }

    @Override
    public void unload() {
        if (this.bukkitWorld != null) Bukkit.unloadWorld(this.bukkitWorld, false);
        if (this.activeWorldFolder != null) FileUtil.delete(this.activeWorldFolder);

        this.bukkitWorld = null;
        this.activeWorldFolder = null;
    }

    @Override
    public boolean restoreFromSource() {
        this.unload();
        return this.load();
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public World getWorld() {
        return this.bukkitWorld;
    }
}
