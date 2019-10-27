package com.mycodefu.application;

import com.mycodefu.animation.Animation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mycodefu.animation.AnimationCompiler.compileCatAnimation;
import static javafx.animation.Animation.INDEFINITE;

public class Application extends javafx.application.Application{

	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 768;

	public static void start(String[] args) {
	Application.launch(args);
}

	@Override
	public void start(Stage stage) throws Exception {

		Animation idleRightCat = compileCatAnimation("Idle", 10, Duration.seconds(1));
		idleRightCat.setCycleCount(INDEFINITE);
		double middleX = SCREEN_WIDTH / 2 - idleRightCat.getImageView().getViewport().getWidth() / 2;
		double middleY = SCREEN_HEIGHT / 2 - idleRightCat.getImageView().getViewport().getHeight() / 2;
		idleRightCat.getImageView().setX(middleX);
		idleRightCat.getImageView().setY(middleY);
		idleRightCat.play();

		Animation idleLeftCat = compileCatAnimation("Idle", 10, Duration.seconds(1), true);
		idleLeftCat.setCycleCount(INDEFINITE);
		idleLeftCat.getImageView().setX(middleX);
		idleLeftCat.getImageView().setY(middleY);
		idleLeftCat.getImageView().setVisible(false);

		Animation walkingRightCat = compileCatAnimation("Walk", 10, Duration.seconds(1), false);
		walkingRightCat.setCycleCount(INDEFINITE);
		walkingRightCat.getImageView().setX(middleX);
		walkingRightCat.getImageView().setY(middleY);
		walkingRightCat.getImageView().setVisible(false);

		Animation walkingLeftCat = compileCatAnimation("Walk", 10, Duration.seconds(1), true);
		walkingLeftCat.setCycleCount(INDEFINITE);
		walkingLeftCat.getImageView().setX(middleX);
		walkingLeftCat.getImageView().setY(middleY);
		walkingLeftCat.getImageView().setVisible(false);

		List<Animation> catAnimations = List.of(idleLeftCat, idleRightCat, walkingLeftCat, walkingRightCat);
		List<Node> catAnimationImageViews = catAnimations.stream().map(Animation::getImageView).collect(Collectors.toList());

		Group g = new Group(catAnimationImageViews);

		Scene s = new Scene(g);

		stage.setScene(s);
		stage.setWidth(SCREEN_WIDTH);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setOnCloseRequest(e->{
			System.exit(0);
		});
		stage.setTitle("werekitten");

		stage.show();

		stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			if (keyEvent.getCode() == KeyCode.RIGHT) {
				playOneAnimation(catAnimations, walkingRightCat);
			} else if (keyEvent.getCode() == KeyCode.LEFT) {
				playOneAnimation(catAnimations, walkingLeftCat);
			}
		});
		stage.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			if (keyEvent.getCode() == KeyCode.RIGHT) {
				playOneAnimation(catAnimations, idleRightCat);
			} else if (keyEvent.getCode() == KeyCode.LEFT) {
				playOneAnimation(catAnimations, idleLeftCat);
			}
		});
	}

	private void playOneAnimation(List<Animation> allAnimations, Animation animationToPlay) {
		for (Animation animation : allAnimations) {
			if (animation == animationToPlay) {
				animation.getImageView().setVisible(true);
				animation.play();
			} else {
				animation.stop();
				animation.getImageView().setVisible(false);
			}
		}
	}

}