package aaaimx.theVoids.ghosts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class brianGhosts extends GhostController
{	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST,MOVE>(GHOST.class);
	private GHOST[] ghostnames = GHOST.values();
	private MOVE[] allMoves = MOVE.values();
	private Random rnd = new Random();
	
	private boolean pacManClosePowerPills(Game game, GHOST blinky)
		{	int [] powerPills = game.getActivePowerPillsIndices();
			for(int i : powerPills)
				{	int n = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), i , game.getPacmanLastMoveMade());
					if(n<=65) 
					{	return true;
					}
				
				}	
			return false;
		}
	
	//meth of blinky
	
	private void blinkyFollowsMsPacMan(Game game, GHOST blinky)
		{	int blinkyNode = game.getGhostCurrentNodeIndex(blinky);
			int msPacManNode = game.getPacmanCurrentNodeIndex();
			//from node, to node, measure
			MOVE blinkyMove;
			if(this.pacManClosePowerPills(game, blinky)||game.getGhostEdibleTime(blinky)>0)
				{	blinkyMove = game.getNextMoveAwayFromTarget(blinkyNode, msPacManNode, DM.PATH);
				}	
			else
				{ blinkyMove = game.getNextMoveTowardsTarget(  blinkyNode ,msPacManNode, DM.PATH);	
				}
			
			
			
			this.moves.put(blinky, blinkyMove);
		}
	
	private boolean pacManCloseGhost(Game game, GHOST ghost)
		{	int pacManNode = game.getPacmanCurrentNodeIndex();
			int ghostNode = game.getGhostCurrentNodeIndex(ghost);
				{	int n = game.getShortestPathDistance(pacManNode, ghostNode ,game.getPacmanLastMoveMade());
					if(n<=100) 
						{	return true;
						}
				}
			return false;
		}
				
	
	private void clydeFollowPacMan (Game game, GHOST clyde)
			{	int pacManNode = game.getPacmanCurrentNodeIndex();
				int clydeNode = game.getGhostCurrentNodeIndex(clyde);
				MOVE clydeMove;
				if(this.pacManCloseGhost(game, clyde))
					{	clydeMove = game.getNextMoveTowardsTarget(clydeNode, pacManNode, DM.PATH);
					}
				if(game.getGhostEdibleTime(clyde)>0)
					{	clydeMove = game.getNextMoveAwayFromTarget(clydeNode, pacManNode, DM.PATH);
					}
				else
					{	clydeMove = allMoves[rnd.nextInt(allMoves.length)];
					}
				this.moves.put(clyde, clydeMove);
			}
		
		private void pinkyLockPacMan (Game game, GHOST pinky)
			{	int pinkyNode = game.getGhostCurrentNodeIndex(pinky);
				int pacManNode = game.getPacmanCurrentNodeIndex();
				
				MOVE pinkyMove;
				if(game.getGhostEdibleTime(pinky)>0)
					{	pinkyMove = game.getNextMoveAwayFromTarget(pinkyNode, pacManNode, DM.PATH);
					}	
				else
					{ pinkyMove = game.getNextMoveTowardsTarget(  pinkyNode ,pacManNode+5, DM.PATH);
					}
				this.moves.put(pinky, pinkyMove);
			}
		
		private void inkyLockPacMan (Game game, GHOST inky)
			{	int inkyNode = game.getGhostCurrentNodeIndex(inky);
				int pacManNode = game.getPacmanCurrentNodeIndex();
				
				MOVE pinkyMove;
				if(game.getGhostEdibleTime(inky)>0)
					{	pinkyMove = game.getNextMoveAwayFromTarget(inkyNode, pacManNode, DM.PATH);
					}	
				else
					{ pinkyMove = game.getNextMoveTowardsTarget(  inkyNode ,pacManNode-10, DM.PATH);
					}
				this.moves.put(inky, pinkyMove);
			}
		
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) 
		{	for(GHOST ghost : this.ghostnames)
			{	if(game.doesGhostRequireAction(ghost))
				{	//your code please here	
					if(ghost.equals(GHOST.BLINKY))
						{	blinkyFollowsMsPacMan(game, ghost);
						}
					if(ghost.equals(GHOST.SUE))
						{	clydeFollowPacMan(game, ghost);
						}
					if(ghost.equals(GHOST.PINKY))
						{	pinkyLockPacMan(game, ghost);
						}
					if(ghost.equals(GHOST.INKY))
						{	inkyLockPacMan(game, ghost);
						}
				}
			}	
		// TODO Auto-generated method stub
		return this.moves;
		}
}