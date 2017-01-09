package org.fxapps.genetics.maze;

import java.util.Random;

public class Maze {

	private static final String WALL = "█";

	private static final String FINAL_STEP = "X";

	private static final String EMPTY = "░";

	private static final String TARGET = "§";

	int width, height;

	int mazeMap[][];

	// counting from the middle of the maze
	int[] initialPos;

	int targetX;
	int targetY;

	int[][] blocks;

	public Maze(int width, int height) {
		this.width = width;
		this.height = height;
		mazeMap = new int[width][height];
		targetX = width - 1;
		targetY = height / 2;
		initialPos = new int[] { 0, height / 2 };
		blocks = new int[0][0];
	}

	public Maze(int width, int height, int numberOfBlocks) {
		this.width = width;
		this.height = height;
		mazeMap = new int[width][height];
		targetX = width - 1;
		targetY = height / 2;
		initialPos = new int[] { 0, height / 2 };
		this.blocks = generateRandomBlocks(numberOfBlocks);
	}

	public MazeWalkingInfo getInfoForDirections(Direction[] directions) {
		MazeWalkingInfo info = new MazeWalkingInfo();
		int[][] positions = getPositions(directions);
		int steps = positions.length;
		int lastX = positions[positions.length - 1][0];
		int lastY = positions[positions.length - 1][1];
		int a = Math.abs(lastX - targetX);
		int b = Math.abs(lastY - targetY);
		info.setSteps(steps);
		info.setStepsToReachTarget(a + b);
		return info;

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
		for (int i = 1; i < directions.length; i++) {
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

	private int[][] getPositions(Direction[] directions) {
		int[][] positions = new int[directions.length][2];
		positions[0] = initialPos;
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
			// we reached the target!
			if (x == targetX && y == targetY) {
				break;
			}
		}
		return positions;
	}

	boolean hitABlock(int x, int y) {
		for (int i = 0; i < blocks.length; i++) {
			if (x == blocks[i][0] && y == blocks[i][1]) {
				return true;
			}
		}
		return false;
	}
	private int[][] generateRandomBlocks(int numberOfBlocks) {
		if (numberOfBlocks > width || numberOfBlocks > height) {
			System.out.println("TOO MANY BLOCKS - IT MAY COMPLETELY BLOCK THE TARGET!");
		}
		int[][] blocks = new int[numberOfBlocks][2];
		Random r = new Random();
		for (int i = 0; i < blocks.length; i++) {
			blocks[i][0] = r.nextInt(width);
			blocks[i][1] = r.nextInt(height);
		}
		return blocks;
	}

}