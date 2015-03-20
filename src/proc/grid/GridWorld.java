package proc.grid;

import action.PrimitiveAction;
import action.ActionNames;
import action.ActionType;
import agent.Agent;
import env.Cue;
import env.State;
import proc.Procedure;
import reward.Reward;
import reward.RewardValue;

public class GridWorld extends Procedure {

	private static final long serialVersionUID = -1311653291633208947L;

	private int xdim, ydim;

	int xpos, ypos;

	int preyXPos;

	int preyYPos;

	public GridWorld(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials, int xdim, int ydim) {
		super(agent, rewardValue, maxNumberOfTrials);
		randomInit(xdim, ydim);

		this.xdim = xdim;
		this.ydim = ydim;
		setITI(300);
	}

	private void randomInit(int xdim, int ydim) {
		xpos = (int) (Math.random() * xdim) + 1;
		ypos = (int) (Math.random() * ydim) + 1;
		preyXPos = (int) (Math.random() * xdim) + 1;
		preyYPos = (int) (Math.random() * ydim) + 1;
	}

	@Override
	public Cue[] getCue() {
		return null;
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		return true;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		if (action.getActionType() == ActionType.get(ActionNames.down)) {
			if (ypos <= 1)
				;
			else
				ypos--;
		}
		if (action.getActionType() == ActionType.get(ActionNames.up)) {
			if (ypos >= ydim)
				;
			else
				ypos++;
		}
		if (action.getActionType() == ActionType.get(ActionNames.left)) {
			if (xpos <= 0)
				;
			else
				xpos--;
		}
		if (action.getActionType() == ActionType.get(ActionNames.right)) {
			if (xpos >= xdim)
				;
			else
				xpos++;
		}
		
//		System.out.println(xpos + "  " + ypos + " " + preyXPos + " " + preyYPos);
		if (xpos == preyXPos && ypos == preyYPos){
			newTrial();
			randomInit(xdim, ydim);
			return rewardValue.getBigReward();
		}
		else
			return rewardValue.punishment();
	}
	
	public int getXdim() {
		return xdim;
	}
	
	public int getYdim() {
		return ydim;
	}
	
	public int getYpos() {
		return preyYPos - ypos;
	}
	
	public int getXpos() {
		return preyXPos - xpos;
	}
	
	
}
