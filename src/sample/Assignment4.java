package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import javafx.stage.Stage;
import sample.LongValue;
import sample.Sprite;

import java.io.PrintWriter;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Assignment4 extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();

        Scene theScene = new Scene(root);

        primaryStage.setScene(theScene);

        Canvas canvas = new Canvas(600, 700);
        root.getChildren().add(canvas);

        primaryStage.setTitle("HUBMB-Racer");
        ArrayList<String> input = new ArrayList<>();

        theScene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        String code = keyEvent.getCode().toString();

                        if (!input.contains(code)){
                            input.add(code);
                        }
                    }
                }
        );

        theScene.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        String code = keyEvent.getCode().toString();
                        input.remove(code);
                    }
                }
        );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Sprite myCar = new Sprite();
        myCar.setImage("Adsız.png");
        myCar.setPosition(275,550);

        Image kenarLeft = new Image("kenar.png");
        Image kenarRight = new Image("kenar.png");
        Image anaYol = new Image("anayol.png");


        int maxX = 350;
        int minX = 100;
        int maxY = 500;
        int minY = 0;

        LongValue lastNanoTime = new LongValue (System.nanoTime());

        new AnimationTimer(){

            private long lastUpdate = 0;
            ArrayList<Node> greenCars = new ArrayList<>();
            double velocity = 3;

            @Override
            public void handle(long currentNanoTime) {

                int px = minX + (int)(Math.random()*maxX);
                int py = minY + (int)(Math.random()*maxY);



                if (currentNanoTime - lastUpdate >= 880000000){
                    creatingCars(px,py,root);
                    lastUpdate = currentNanoTime;
                }


                if (moveCars(myCar,greenCars,velocity,root))
                    ;
                else stop();



                double elapsedTime = (currentNanoTime - lastNanoTime.value) /  1000000000.0;
                lastNanoTime.value = currentNanoTime;

                myCar.setVelocity(0,0);

                if (input.contains("UP")){
                    myCar.addVelocity(0,0);
                    velocity += 0.2*elapsedTime;
                }

                if (input.contains("LEFT"))
                    myCar.addVelocity(-100,0);
                if (input.contains("RIGHT"))
                    myCar.addVelocity(100,0);


                myCar.update(elapsedTime);

                gc.clearRect(0,0,600,700);
                gc.drawImage(anaYol,100,0);
                myCar.render(gc);
                gc.drawImage(kenarLeft,0,0);
                gc.drawImage(kenarRight,500,0);



            }

        }.start();

        primaryStage.show();

    }

    private static void creatingCars(int px, int py, Group root){

        Araba araba=new Araba(105+(int)(Math.random()*350),-5,50,100,Color.YELLOW);
        root.getChildren().add(araba);

    }

    private boolean moveCars(Sprite myCar, ArrayList<Node> cars, double velocity, Group root) {
        for (Node n:root.getChildren()){
            if(n instanceof Araba){
                n.setTranslateY(n.getTranslateY()+velocity);
                if (myCar.getPositonY() < n.getTranslateY()-50 ){
                    ((Araba) n).setFill(Color.GREEN);
                    cars.add(n);

                }
                if ((myCar.getPositonY() <= n.getTranslateY()+100 && myCar.getPositonY() >= n.getTranslateY()  )) {
                    if (myCar.getPositonX() <= n.getTranslateX()+50 && myCar.getPositonX() >= n.getTranslateX()){
                        myCar.setImage("carpisma.png");
                        ((Araba) n).setFill(Color.BLACK);

                        return false;
                    }
                    else if (myCar.getPositonX()+50 >= n.getTranslateX() && myCar.getPositonX()+50 <= n.getTranslateX()+50){
                        myCar.setImage("carpisma.png");
                        ((Araba) n).setFill(Color.BLACK);

                        return false;
                    }
                }
            }
        }
        return true;

    }


    public static void main(String[] args) {
        launch(args);
    }

    private static class Araba extends Rectangle{


        public Araba(int x, int y, int width, int height,Color color) {
            super(width,height,color);
            setTranslateX(x);
            setTranslateY(y);
        }


    }
}
