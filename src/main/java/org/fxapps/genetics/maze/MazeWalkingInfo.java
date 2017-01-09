package org.fxapps.genetics.maze;

public class MazeWalkingInfo {
	
	private int steps = 0;
	private int stepsToReachTarget = Integer.MAX_VALUE;
	
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
	public int getStepsToReachTarget() {
		return stepsToReachTarget;
	}
	public void setStepsToReachTarget(int stepsToReachTarget) {
		this.stepsToReachTarget = stepsToReachTarget;
	}
	
}
