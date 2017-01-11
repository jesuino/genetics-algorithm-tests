package org.fxapps.genetics.maze;

import java.util.Random;

public class Maze {

	private static final String WALL = "█";

	private static final String FINAL_STEP = "X";

	private static final String EMPTY = "░";

	private static final String TARGET = "§";

	// it is not the width/height in pixels - but actually the number of columns
	// and rows (must refactor)
	private int width, height;

	int mazeMap[][];

	// counting from the middle of the maze
	int[] initialPos;

	int targetX;
	int targetY;

	int[][] blocks;

	private int numberOfBlocks;

	public Maze(int width, int height) {
		this(width, height, 0);
	}

	public Maze(int width, int height, int numberOfBlocks) {
		this(width, height, width -1, height / 2, 0);
	}
	
	public Maze(int width, int height, int targetX, int targetY, int numberOfBlocks) {
		this.width = width;
		this.height = height;
		mazeMap = new int[width][height];
		this.targetX = targetX;
		this.targetY = targetY;
		initialPos = new int[] { 0, height / 2 };
		this.numberOfBlocks = numberOfBlocks;
		this.blocks = generateRandomBlocks(numberOfBlocks);
	}

	public MazeWalkingInfo getInfoForDirections(Direction[] directions) {
		MazeWalkingInfo info = new MazeWalkingInfo();
		int[][] positions = getPositions(directions);
		int steps = positions.length;
		int lastX = positions[positions.length - 1][0];
		int lastY = positions[positions.length - 1][1];
		int stepsToReachTarget = stepsToReachTarget(lastX, lastY);
		info.setSteps(steps);
		info.setStepsToReachTarget(stepsToReachTarget);
		return info;

	}

	public int stepsToReachTarget(int lastX, int lastY) {
		int a = Math.abs(lastX - targetX);
		int b = Math.abs(lastY - targetY);
		int stepsToReachTarget = a + b;
		return stepsToReachTarget;
	}

	public void printMaze(Direction[] directions) {
		String[][] mazeStr = new String[width][height];

		// initialize empty maze
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				mazeStr[i][j] = EMPTY;
			}
		}
		// print blocks
		for (int[] block : blocks) {
			mazeStr[block[0]][block[1]] = WALL;
		}

		mazeStr[0][height / 2] = directions[0].toString();
		int[][] positions = getPositions(directions);

		// print positions
		for (int i = 1; i < positions.length; i++) {
			int x = positions[i][0];
			int y = positions[i][1];
			if (i == directions.length - 1 && (x != targetX || y != targetY)) {
				mazeStr[x][y] = FINAL_STEP;
			} else {
				mazeStr[x][y] = directions[i].toString();
			}
		}

		mazeStr[targetX][targetY] = TARGET;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(mazeStr[j][i]);
			}
			System.out.println();
		}
	}

	public int[][] getPositions(Direction[] directions) {
		int[][] positions = new int[directions.length][2];
		positions[0] = initialPos;
		int totalSteps = 0;
		for (int i = 1; i < directions.length; i++) {
			int x = positions[i - 1][0];
			int y = positions[i - 1][1];
			// move if allowed
			switch (directions[i]) {
			case DOWN:
				if (y + 1 < height && !hitABlock(x, y + 1))
					y += 1;
				break;
			case UP:
				if (y - 1 >= 0 && !hitABlock(x, y - 1))
					y -= 1;
				break;
			case RIGHT:
				if (x + 1 < width && !hitABlock(x + 1, y))
					x += 1;
				break;
			case LEFT:
				if (x - 1 >= 0 && !hitABlock(x - 1, y))
					x -= 1;
				break;
			default:
				break;
			}
			positions[i] = new int[] { x, y };
			totalSteps++;
			if (x == targetX && y == targetY) {
				break;
			}
		}
		if (totalSteps < directions.length - 1) {
			int[][] newPositions = new int[totalSteps][2];
			for (int i = 0; i < newPositions.length; i++) {
				newPositions[i] = positions[i];
			}
			return newPositions;
		}
		return positions;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int[][] getBlocks() {
		return blocks;
	}

	public void setNumberOfBlocks(int numberOfBlocks) {
		this.numberOfBlocks = numberOfBlocks;
		generateRandomBlocks(numberOfBlocks);
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	public int getNumberOfBlocks() {
		return numberOfBlocks;
	}

	@Override
	public String toString() {
		return "Maze [width=" + width + ", height=" + height + ", targetX=" + targetX + ", targetY=" + targetY
				+ ", numberOfBlocks=" + numberOfBlocks + "]";
	}

	private int[][] generateRandomBlocks(int numberOfBlocks) {
		int[][] blocks = new int[numberOfBlocks][2];
		Random r = new Random();
		for (int i = 0; i < blocks.length; i++) {
			int x = r.nextInt(width);
			int y = r.nextInt(height);
			// won't allow the block to hide the target, ok?
			if (x == targetX && y == targetY) {
				x = y = 0;
			}
			blocks[i][0] = x;
			blocks[i][1] = y;
		}
		return blocks;
	}

	private boolean hitABlock(int x, int y) {
		for (int i = 0; i < blocks.length; i++) {
			if (x == blocks[i][0] && y == blocks[i][1]) {
				return true;
			}
		}
		return false;
	}

}