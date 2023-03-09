package aaaimx.theVoids.msPacMan;

import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class gaboPacMan extends PacmanController {
    private MOVE pacmanMove;
    private int pacManNode;
    private int pacmanPowerTime;
    private Random rnd = new Random();

    private boolean nearestEdibleGhosts(Game game) {
        this.pacManNode = game.getPacmanCurrentNodeIndex();
        this.pacmanMove = game.getPacmanLastMoveMade();
        for (GHOST ghost : GHOST.values()) {
            int ghostNode = game.getGhostCurrentNodeIndex(ghost);
            if (game.isGhostEdible(ghost)) {
                int n = game.getShortestPathDistance(pacManNode, ghostNode, pacmanMove);
                if (n <= 60) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean roadsWherePacmanIsSafe(Game game, int objective) {
        this.pacManNode = game.getPacmanCurrentNodeIndex();
        this.pacmanMove = game.getPacmanLastMoveMade();
        for (int i : game.getShortestPath(pacManNode, objective, pacmanMove)) {
            for (GHOST ghost : GHOST.values()) {
                if (!game.isGhostEdible(ghost)) {
                    if (i == game.getGhostCurrentNodeIndex(ghost)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private MOVE defaultMoveLocation(Game game, int destination) {
        MOVE move;
        this.pacmanMove = game.getPacmanLastMoveMade();
        int[] path = game.getShortestPath(pacManNode, destination, pacmanMove);
        move = game.getMoveToMakeToReachDirectNeighbour(pacManNode, path[0]);
        return move;
    }

    private MOVE runAwayGhosts(Game game) {
        MOVE move;
        this.pacManNode = game.getPacmanCurrentNodeIndex();
        this.pacmanMove = game.getPacmanLastMoveMade();

        for (GHOST ghost : GHOST.values()) {
            int ghostNode = game.getGhostCurrentNodeIndex(ghost);
            if (!game.isGhostEdible(ghost)) {
                double n = game.getDistance(pacManNode, ghostNode, DM.PATH);
                if (n <= 58) {
                    move = game.getNextMoveAwayFromTarget(pacManNode, ghostNode, DM.PATH);
                    this.pacmanMove = move;
                }
            }
        }
        return pacmanMove;
    }

    private boolean nearestPowerPill(Game game) {
        this.pacManNode = game.getPacmanCurrentNodeIndex();
        this.pacmanMove = game.getPacmanLastMoveMade();
        for (int pPillLocation : game.getActivePowerPillsIndices()) {
            int pPillDistance = game.getShortestPathDistance(pacManNode, pPillLocation, pacmanMove);
            if (pPillDistance <= 1000) {
                return true;
            }
        }
        return false;
    }

    private int nearestOrdinaryPillLocation(Game game) {
        this.pacManNode = game.getPacmanCurrentNodeIndex();
        this.pacmanMove = game.getPacmanLastMoveMade();
        int nearestOPillLocation = 0;
        int nearestOPillDistance = 100;
        for (int ordinaryPillLocation : game.getActivePillsIndices()) {
            int pPillDistance = game.getShortestPathDistance(pacManNode, ordinaryPillLocation, pacmanMove);
            if (pPillDistance < nearestOPillDistance) {
                nearestOPillLocation = ordinaryPillLocation;
                nearestOPillDistance = pPillDistance;
            }
        }
        return nearestOPillLocation;
    }

    private int pacmanPowerTime(Game game) {

        for (GHOST ghost : GHOST.values()) {
            if (game.getGhostEdibleTime(ghost) > 0) {
                if (game.isGhostEdible(ghost)) {
                    pacmanPowerTime = game.getGhostEdibleTime(ghost);
                }
            } else {
                pacmanPowerTime = 0;
            }
        }
        System.out.println(pacmanPowerTime + " pacmanPowerTime");
        return this.pacmanPowerTime;
    }

    private MOVE eatGhosts(Game game) {
        MOVE move;
        this.pacManNode = game.getPacmanCurrentNodeIndex();
        this.pacmanMove = game.getPacmanLastMoveMade();
        for (GHOST ghost : GHOST.values()) {
            int ghostNode = game.getGhostCurrentNodeIndex(ghost);
            if (nearestEdibleGhosts(game)) {
                move = game.getNextMoveTowardsTarget(pacManNode, ghostNode, DM.PATH);
                this.pacmanMove = move;
            }
        }
        return pacmanMove;
    }

    private MOVE eatPowerPills(Game game) {
        MOVE move = MOVE.values()[rnd.nextInt(MOVE.values().length)];
        int[] powerPills = game.getActivePowerPillsIndices();
        int destination = powerPills[rnd.nextInt(powerPills.length)];
        // System.out.println(destination + " desination");
        if (roadsWherePacmanIsSafe(game, destination)) {
            if (nearestPowerPill(game)) {
                move = defaultMoveLocation(game, destination);
                this.pacmanMove = move;
            }
        }
        return pacmanMove;
    }

    private MOVE eatRemPills(Game game) {
        MOVE move = MOVE.values()[rnd.nextInt(MOVE.values().length)];
        int[] remPills = game.getActivePillsIndices();
        int destination = remPills[rnd.nextInt(remPills.length)];
        // System.out.println(destination + " desination");
        if (roadsWherePacmanIsSafe(game, destination)) {
            move = defaultMoveLocation(game, nearestOrdinaryPillLocation(game));
        } else {
            move = runAwayGhosts(game);
        }
        return move;
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {

        MOVE move;
        if (pacmanPowerTime(game) > 0) {
            move = eatGhosts(game);
        } else {
            if (game.getActivePowerPillsIndices().length > 0) {
                move = eatPowerPills(game);
            } else {
                move = eatRemPills(game); // Eat remaining pills
            }
        }
        return move;
    }
}