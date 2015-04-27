package org.ggp.base.player.gamer.coursera.statemachine;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.player.gamer.event.GamerSelectedMoveEvent;
import org.ggp.base.player.gamer.exception.GamePreviewException;
import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
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
		Move selection = bestMove(getCurrentState(), getRole());

		long stop = System.currentTimeMillis();


		notifyObservers(new GamerSelectedMoveEvent(moves, selection, stop - start));
		return selection;
	}

	private int maxScore(MachineState state, Role role) throws GoalDefinitionException, TransitionDefinitionException, MoveDefinitionException{
		int score=0;
		StateMachine lState=getStateMachine();
		if (lState.isTerminal(state)){
			return lState.getGoal(state, role);
		}
		List<Move> legalMoves=lState.getLegalMoves(state, getRole());
		MachineState simState;
		List<Move> lMoves=new ArrayList<Move>();
		for (Move move : legalMoves) {
			lMoves.add(move);
			simState=getStateMachine().getNextState(state,lMoves);
			int result=maxScore(simState, role);
			if (result>score){
				score=result;
			}
		}
		return score;
	}





	private Move bestMove(MachineState state, Role role) {

		StateMachine lState=getStateMachine();
		Move move=null;
		int score=0;
		try {
			List<Move> legalMoves=lState.getLegalMoves(state, getRole());
			move=legalMoves.get(0);
			if (move.getContents().toString()=="noop"){
				return move;
			}
			if (lState.isTerminal(state)){
				//return lState.getGoal(state, role);
			}

			for (Move iMove : legalMoves) {
				List<Move> lMoves=new ArrayList<Move>();
				lMoves.add(iMove);
				MachineState simState=getStateMachine().getNextState(state,lMoves);
				int result=maxScore(simState, role);
				if (result==100){
					move=iMove;
					break;
				}
				if (result>score){
					score=result;
					move=iMove;
				}
			}

		} catch (MoveDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransitionDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoalDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return move;
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
