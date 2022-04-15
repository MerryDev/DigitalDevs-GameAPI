package de.digitaldevs.gameapi.gamestate;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GameStateManager {

    private static volatile GameStateManager instance;
    final GameState[] gameStates;

    @Getter private GameState current;
    private int counter;

    private GameStateManager() {
        this.gameStates = new GameState[100];
    }

    public void registerNewGameState(@NotNull final GameState gameState) {
        this.gameStates[this.counter] = gameState;
        this.counter++;
    }

    public void setGameState(@NotNull final GameState gameState) {
        if (this.current != null) this.current.stop();
        if (!this.isRegistered(gameState)) {
            this.current = gameState;
            this.current.start();
        } else {
            Bukkit.getLogger().warning("Could not set GameState " + gameState.getClass().getSimpleName() + " because it's not registered! Make sure it is registered correctly before using it.");
        }
    }

    public void stopCurrentState() {
        if (this.current != null) {
            this.current.stop();
            this.current = null;
        }
    }

    public boolean isRegistered(@NotNull final GameState gameState) {
        return Arrays.asList(this.gameStates).contains(gameState);
    }

    public static synchronized GameStateManager getInstance() {
        if (instance == null) {
            synchronized ((GameStateManager.class)) {
                if (instance == null) {
                    instance = new GameStateManager();
                }
            }
        }
        return instance;
    }
}
