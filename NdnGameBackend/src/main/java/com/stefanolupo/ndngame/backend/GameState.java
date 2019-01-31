package com.stefanolupo.ndngame.backend;

import com.stefanolupo.ndngame.Player;
import com.stefanolupo.ndngame.backend.chronosynced.PlayerStatuses;
import com.stefanolupo.ndngame.backend.players.LocalPlayer;
import com.stefanolupo.ndngame.backend.players.RemotePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameState {

    private static final Logger LOG = LoggerFactory.getLogger(GameState.class);

    private final LocalPlayer localPlayer;
    private final boolean automatePlayer;
    private final long gameId;

    private final PlayerStatuses playerStatuses;

    public GameState(LocalPlayer localPlayer, boolean automatePlayer, long gameId) {
        this.localPlayer = localPlayer;
        this.automatePlayer = automatePlayer;
        this.gameId = gameId;
        this.playerStatuses = new PlayerStatuses(localPlayer, gameId);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::printPlayerStatus, 0, 5, TimeUnit.SECONDS);
    }

    public LocalPlayer getLocalPlayer() {
        return localPlayer;
    }

    public List<RemotePlayer> getRemotePlayers() {
        return new ArrayList<>(playerStatuses.getMap().values());
    }

    private void printPlayerStatus() {
        System.out.println();
        playerStatuses.getMap().values().forEach(p -> System.out.println(getPlayerPositionString(p)));
        System.out.println();
    }

    private String getPlayerPositionString(Player player) {
        return String.format("%s - x:%d, y:%d, hp:%d, mana:%d, score:%d",
                player.getPlayerName(),
                player.getPlayerStatus().getX(),
                player.getPlayerStatus().getY(),
                player.getPlayerStatus().getHp(),
                player.getPlayerStatus().getMana(),
                player.getPlayerStatus().getScore());
    }
}
