package org.ggp.base.player.gamer.coursera.statemachine;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.player.gamer.event.GamerSelectedMoveEvent;
import org.ggp.base.player.gamer.exception.GamePreviewException;
import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.StateMachine;
import org.ggp.base.util.statemachine.cache.CachedStateMachine;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.statemachine.implementation.prover.ProverStateMachine;

public class Deliberator extends StateMachineGamer {

	@Override
	public StateMachine getInitialStateMachine() {
		return new CachedStateMachine(new ProverStateMachine());
	}

	@Override
	public void stateMachineMetaGame(long timeout)
			throws TransitionDefinitionException, MoveDefinitionException,
			GoalDefinitionException {
		// TODO Auto-generated method stub

	}

	@Override
	public Move stateMachineSelectMove(long timeout)
			throws TransitionDefinitionException, MoveDefinitionException,
			GoalDefinitionException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		List<Move> moves = getStateMachine().getLegalMoves(getCurrentState(), getRole());
		Move selection = bestMove(moves);

		long stop = System.currentTimeMillis();

		notifyObservers(new GamerSelectedMoveEvent(moves, selection, stop - start));
		return selection;
	}

	private int maxScore(List<Move> moves) throws GoalDefinitionException, TransitionDefinitionException{
		int score=0;
		if (getStateMachine().isTerminal(getCurrentState())){
				//getGoal(getCurrentState(), getRole())==100){
			return 100;
		}
		MachineState state = getCurrentState();
		for (Move move : moves) {
			ArrayList<Move> lMoves=new ArrayList<Move>();
			lMoves.add(move);
//			state=getNextState(state,lMoves);
//			score=maxScore(state);
		}
		return score;

	}



	private Move bestMove(List<Move> moves) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stateMachineStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateMachineAbort() {
		// TODO Auto-generated method stub

	}

	@Override
	public void preview(Game g, long timeout) throws GamePreviewException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "Deliberator";
	}

}
