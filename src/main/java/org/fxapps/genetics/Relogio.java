package org.fxapps.genetics;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Relogio extends Application {

	
	// criando o Label que vai informar as horas
	private Label lblRelogio = new Label();
	
	// SimpleDateFormat é a classe do Java que transforma datas para Strings 
	// usando o formato passado
	private SimpleDateFormat formatador = new SimpleDateFormat("hh:mm:ss a");


	// isso poderia ser emitido no Java 8
	public static void main(String[] args) {
		launch();
	}
	
	
	public void start(Stage stage) throws Exception {
		// criamos a fonte usando o método de fábrica.
		Font font = Font.font("Arial", FontWeight.EXTRA_BOLD, 60);
		lblRelogio.setFont(font);
		
		// vamos colocar um pequeno efeito no label pra deixar ele bonitin
		lblRelogio.setEffect(new DropShadow(10, Color.RED));
		
		// agora ligamos um loop infinito que roda a cada segundo e atualiza nosso label chamando atualizaHoras.
		KeyFrame frame = new KeyFrame(Duration.millis(1000), e -> atualizaHoras());
		Timeline timeline = new Timeline(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		
		// por fim criamos o stage, a scene com um stack pane e colocamos o nosso label lá
		Scene cena = new Scene(new StackPane(lblRelogio), 450, 150);
		stage.setScene(cena);
		stage.show();
	}


	private void atualizaHoras() {
		Date agora = new Date();
		lblRelogio.setText(formatador.format(agora)); 
	}

}
