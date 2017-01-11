package org.fxapps.genetics.mazefx;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.fxapps.drawingfx.DrawingApp;
import org.fxapps.genetics.maze.Direction;
import org.fxapps.genetics.maze.Maze;
import org.fxapps.genetics.maze.MazeSolver;
import org.jenetics.AnyGene;
import org.jenetics.Genotype;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class MazeFXApp extends DrawingApp {
	// TODO: extract more constants - improve this code heh

	// constants
	private static final int MAX_MAZE_WIDTH = 300;
	private static final int MAX_MAZE_HEIGHT = 200;
	private static final Color VISITED = Color.LIGHTGRAY;
	private static final Color TARGET = Color.RED;
	private static final Color REPLAY = Color.LIGHTGREEN;
	private static final Color DEFAULT_BG = Color.web("#D2E3F9");

	// our maze and the solver
	Maze maze;
	MazeSolver solver;

	// UI elements
	private Spinner<Integer> spGenes;
	private Slider slPopulation;
	private Slider slBlocks;
	private Slider slWidth;
	private Slider slHeight;
	private TextArea txtLog;
	private Slider slGenerations;

	// other variables
	private boolean mustDrawMaze = false;
	private SimpleIntegerProperty currentPos = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty positionsLengthProperty = new SimpleIntegerProperty(0);

	// data retrieved from current maze
	private int[][] positions;
	private Direction[] directions;
	private Genotype<AnyGene<Direction>> lastGenotype;

	private SimpleBooleanProperty solving = new SimpleBooleanProperty(false);
	private int totalGenes;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void setup() {
		title = "MazeFX";
		width = 1200;
		height = 820;
		createConfigurationPane();
		background = DEFAULT_BG;
		frames = 100;
		newMaze();
	}

	public void draw() {
		double rectWidth = width / maze.getWidth();
		double rectHeight = height / maze.getHeight();
		if (mustDrawMaze) {
			drawMaze(rectWidth, rectHeight);
		}

		if (currentPos.get() < positions.length) {
			ctx.setFill(TARGET);
			ctx.fillRect(rectWidth * maze.getTargetX(), rectHeight * maze.getTargetY(), rectWidth, rectHeight);
			if (currentPos.get() > 0) {
				int lastX = positions[currentPos.get() - 1][0];
				int lastY = positions[currentPos.get() - 1][1];
				double lastMazeX = rectWidth * lastX;
				double lastMazeY = rectHeight * lastY;
				ctx.setFill(VISITED);
				ctx.fillRect(lastMazeX, lastMazeY, rectWidth, rectHeight);
				ctx.strokeRect(lastMazeX, lastMazeY, rectWidth, rectHeight);
				ctx.setFill(Color.WHITE);
				ctx.setFont(Font.font((rectWidth + rectHeight) / 6));
				ctx.fillText(directions[currentPos.get()].toString(), lastMazeX + rectWidth / 2 - 5,
						lastMazeY + rectHeight / 2);
			}
			int x = positions[currentPos.get()][0];
			int y = positions[currentPos.get()][1];
			double mazeX = rectWidth * x;
			double mazeY = rectHeight * y;

			ctx.setFill(Color.WHITE);
			ctx.fillRect(mazeX, mazeY, rectWidth, rectHeight);

			if (currentPos.get() == positions.length - 1) {
				if (x == maze.getTargetX() && y == maze.getTargetY()) {
					ctx.setFill(REPLAY);
					log("Reached the target in " + positions.length + " steps out of " + totalGenes
							+ " possible steps!");
				} else {
					ctx.setFill(Color.DARKGREY);
					log("Didn't reach the target. Click in Apply to generate a new Genotype (or try to evolve again)");
				}
				ctx.fillRect(mazeX, mazeY, rectWidth, rectHeight);
			}
			currentPos.set(currentPos.get() + 1);
		}
	}

	private void drawMaze(double rectWidth, double rectHeight) {
		ctx.setFill(DEFAULT_BG);
		ctx.fillRect(0, 0, width, height);
		ctx.setFill(Color.DARKBLUE);
		int[][] blocks = maze.getBlocks();
		for (int i = 0; i < blocks.length; i++) {
			int x = blocks[i][0];
			int y = blocks[i][1];
			ctx.fillRect(rectWidth * x, rectHeight * y, rectWidth, rectHeight);
		}
		mustDrawMaze = false;
	}

	private void createConfigurationPane() {
		Font fontTitle = Font.font(null, FontWeight.EXTRA_BOLD, 12);

		Label lblEvolutionTitle = new Label("Evolution control");
		lblEvolutionTitle.setFont(fontTitle);

		Label lblMazeConfigTitle = new Label("New Maze Conf");
		lblMazeConfigTitle.setFont(fontTitle);

		Label lblLogTitle = new Label("Execution Log");
		lblLogTitle.setFont(fontTitle);

		spGenes = new Spinner<>(1, 15, 3);
		
		slPopulation = new Slider(20, 400, 50);
		VBox vbPopulation = paneWithInfoLabel(slPopulation);
		
		slWidth = new Slider(10, MAX_MAZE_WIDTH, 20);
		VBox vbWidth = paneWithInfoLabel(slWidth);

		slHeight = new Slider(5, MAX_MAZE_HEIGHT, 20);
		VBox vbHeight = paneWithInfoLabel(slHeight);

		slBlocks = new Slider(0, (MAX_MAZE_WIDTH + MAX_MAZE_HEIGHT * 8), 20);
		VBox vbBlocks = paneWithInfoLabel(slBlocks);

		slGenerations = new Slider(10, 15000, 200);
		slGenerations.setPrefWidth(220);
		VBox vbGenerations = paneWithInfoLabel(slGenerations);

		GridPane gpMazeConfig = new GridPane();
		gpMazeConfig.setVgap(5);
		gpMazeConfig.setHgap(5);
		gpMazeConfig.add(new Label("Columns"), 0, 0);
		gpMazeConfig.add(vbWidth, 1, 0);

		gpMazeConfig.add(new Label("Rows"), 0, 1);
		gpMazeConfig.add(vbHeight, 1, 1);

		gpMazeConfig.add(new Label("Blocks"), 0, 2);
		gpMazeConfig.add(vbBlocks, 1, 2);

		gpMazeConfig.add(new Label("Genes Multiplier"), 2, 0);
		gpMazeConfig.add(spGenes, 3, 0);

		gpMazeConfig.add(new Label("Population"), 2, 1);
		gpMazeConfig.add(vbPopulation, 3, 1);

		Button btnNewMaze = new Button("New Maze");
		gpMazeConfig.add(btnNewMaze, 2, 2);

		Button btnNewGenotype = new Button("New Genotype");
		gpMazeConfig.add(btnNewGenotype, 3, 2);

		Button btnStart = new Button("Evolve");
		Button btnReplay = new Button("Replay");

		Button btnStop = new Button("Stop Animation");

		BooleanBinding playing = currentPos.greaterThanOrEqualTo(positionsLengthProperty).not();
		BooleanBinding playingOrSolving = playing.or(solving);
		btnStart.disableProperty().bind(playingOrSolving);
		btnReplay.disableProperty().bind(playingOrSolving);
		btnNewGenotype.disableProperty().bind(playingOrSolving);
		btnNewMaze.disableProperty().bind(playingOrSolving);
		btnStop.disableProperty().bind(playing.not().or(solving));
		
		txtLog = new TextArea("Initializing...");
		txtLog.setFont(Font.font(null, FontPosture.ITALIC, 12));
		txtLog.setPrefWidth(480);
		txtLog.setPrefHeight(100);
		txtLog.setEditable(false);

		HBox n = new HBox(5, new VBox(5, lblMazeConfigTitle, new Separator(), gpMazeConfig),
				new Separator(Orientation.VERTICAL),
				new VBox(5, lblEvolutionTitle, new Separator(), new Label("Number of Generations"), vbGenerations,
						new HBox(5, btnStart, btnReplay, btnStop)),
				new Separator(Orientation.VERTICAL), new VBox(5, lblLogTitle, new Separator(), txtLog));
		n.setTranslateX(5);
		n.setTranslateY(5);
		n.setAlignment(Pos.CENTER_LEFT);
		btnNewGenotype.setOnAction(e -> newGenotype());
		btnNewMaze.setOnAction(e -> this.newMaze());
		btnStart.setOnAction(e -> startEvolution());
		btnReplay.setOnAction(e -> replay());
		btnStop.setOnAction(e -> stopAnimation());
		setBottom(n);
	}

	private VBox paneWithInfoLabel(Slider sl) {
		Label lblHeight = new Label();
		VBox vbHeight = new VBox(0, sl, lblHeight);
		lblHeight.textProperty()
		.bind(new SimpleStringProperty("(").concat(sl.valueProperty().asString("%.0f")).concat(")"));
		sl.setBlockIncrement(1);
		vbHeight.setAlignment(Pos.CENTER);
		return vbHeight;
	}

	private void stopAnimation() {
		currentPos.set(positions.length);
	}

	private void replay() {
		mustDrawMaze = true;
		currentPos.set(0);
		positionsLengthProperty.set(positions.length);
	}

	private void startEvolution() {
		long milis = System.currentTimeMillis();
		int generation = (int) slGenerations.getValue();
		log("Running evolution for " + generation + " generations.");
		solving.set(true);
		doAsyncWork(() -> {
			lastGenotype = solver.evolveToGenotype(lastGenotype, generation, (int) slPopulation.getValue());
			return null;

		}, v -> {
			log("Finished evolution in " + (System.currentTimeMillis() - milis) + " miliseconds.");
			solving.set(false);
			retrieveValuesFromGenotype();
			currentPos.set(0);
		}, (str) -> {
			System.err.println(str);
		});

	}

	private void newMaze() {
		int w = new Double(slWidth.getValue()).intValue();
		int h = new Double(slHeight.getValue()).intValue();
		int targetX = random.nextInt(w / 2) + w / 2;
		int targetY = random.nextInt(h);
		maze = new Maze(w, h, targetX, targetY, (int) slBlocks.getValue());
		solver = new MazeSolver(maze);
		newGenotype();
		log("new " + maze.toString());
	}

	private void newGenotype() {
		int w = new Double(slWidth.getValue()).intValue();
		int h = new Double(slHeight.getValue()).intValue();
		totalGenes = (w + h) * spGenes.getValue();
		lastGenotype = solver.newGenotype(totalGenes);
		retrieveValuesFromGenotype();
	}

	private void retrieveValuesFromGenotype() {
		directions = MazeSolver.getDirections(lastGenotype);
		positions = maze.getPositions(directions);
		positionsLengthProperty.set(positions.length);
		currentPos.set(0);
		mustDrawMaze = true;
	}


	private void log(String log) {
		txtLog.setText(log + "\n" + txtLog.getText());
	}

	private static <T extends Object> void doAsyncWork(Supplier<T> action, Consumer<T> success,
			Consumer<Throwable> error) {
		Task<T> tarefaCargaPg = new Task<T>() {
			@Override
			protected T call() throws Exception {
				return action.get();
			}

			@Override
			protected void succeeded() {
				success.accept(getValue());
			}

			@Override
			protected void failed() {
				error.accept(getException());
			}
		};
		Thread t = new Thread(tarefaCargaPg);
		t.setDaemon(true);
		t.start();
	}
}
