package game;

/**
 * 
 * Interface class implemented by any class who wishes to be updated when game evolves.
 * 
 * @author ebrunner
 *
 */
public interface GameListener {

	/**
	 * Job to be executed by the listening class at each gameStep
	 * 
	 * @param Game - reference to the game which sends out the notifications
	 */
	public void gameStepJob(Game game);
	
}
