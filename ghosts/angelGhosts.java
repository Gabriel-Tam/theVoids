package aaaimx.theVoids.ghosts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class angelGhosts extends GhostController {
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        for (GHOST ghost : GHOST.values()) {
            if (!game.isGhostEdible(ghost)) { // your code please here
                this.moves.put(ghost, game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
                        game.getPacmanCurrentNodeIndex(),
                        DM.PATH));

            } else {
                this.moves.put(ghost,
                        game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
                                game.getPacmanCurrentNodeIndex(),
                                DM.PATH));

            }
        }
        // TODO Auto-generated method stub
        return this.moves;
    }
}
