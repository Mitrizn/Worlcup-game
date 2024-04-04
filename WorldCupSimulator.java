import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.beans.value.*;
import javafx.event.*; 
import javafx.animation.*;
import javafx.geometry.*;
import java.io.*;
import java.util.*;
import javafx.scene.layout.HBox;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

import javafx.collections.*;
import javafx.scene.control.cell.*;
import javafx.scene.control.TableColumn.*;
import javafx.util.converter.*;
import javafx.scene.chart.*;

import javafx.scene.media.*;


/**
 *   
 *   JavaFX API: https://openjfx.io/javadoc/11/
 */ 
public class WorldCupSimulator extends Application 
{
    
    int score=0;
    double y;
    boolean ballInPlay;
    double time;
    double goalKeeperSpeed=2;
    double goalKeeperDirection=1;
    double goalMidY;
    double ySpeed;
    
    // run the application
    public static void main(String[] args) 
    {
        try
        {
            // creates Stage, calls the start method
            launch(args);
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        finally
        {
            System.exit(0);
        }
    }

    // Application is an abstract class,
    //  requires the method: public void start(Stage s)
    public void start(Stage mainStage) 
    {
        // set the text that appears in the title bar
        mainStage.setTitle("Collection Game");
        mainStage.setResizable(true);

        // layout manager: organize window contents
        BorderPane root = new BorderPane();

        // set font size of objects
        root.setStyle(  "-fx-font-size: 18;"  );

        // May want to use a Box to add multiple items to a region of the screen
        VBox box = new VBox();
        // add padding/margin around area
        box.setPadding( new Insets(16) );
        // add space between objects
        box.setSpacing( 16 );
        // set alignment of objects (default: Pos.TOP_LEFT)
        box.setAlignment( Pos.CENTER );
        // Box objects store contents in a list
        List<Node> boxList = box.getChildren();
        // if you choose to use this, add it to one of the BorderPane regions

        // Scene: contains window content
        // parameters: layout manager; width window; height window
        Scene mainScene = new Scene(root);
        // attach/display Scene on Stage (window)
        mainStage.setScene( mainScene );

        // custom application code below -------------------

        // use Image add icon to main (stage) window
        Image appIcon = new Image("icons/application.png");
        mainStage.getIcons().add( appIcon );
        
        //Label style for my labels
        String labelStyle = "";
        
        labelStyle += " -fx-font-family: Impact; ";
        labelStyle += " -fx-font-size: 32; ";
        labelStyle += " -fx-text-fill: #000000; ";
        labelStyle += " -fx-background-color: white; ";
        labelStyle += " -fx-font-weight: bold; ";
        labelStyle += " -fx-font-style: italic;  ";
        labelStyle += " -fx-underline: true; ";
        
        
        
        Canvas canvas = new Canvas(600,600);
        GraphicsContext context = canvas.getGraphicsContext2D();

        root.setCenter( canvas );

        MenuBar menuBar = new MenuBar();
        root.setTop(menuBar);
        // add this menu to the list of menus managed by menu bar
        Menu fileMenu = new Menu("Game");
        menuBar.getMenus().add( fileMenu );
        
        Menu helpMenu= new Menu("Help");
        menuBar.getMenus().add( helpMenu );
       
        // menu item is an object you can click on or select
        //creating the menu items under File menu
        MenuItem newGame = new MenuItem("New Game");
        MenuItem aboutFile = new MenuItem("Credits");
        MenuItem instructFile= new MenuItem("Instructions");
        MenuItem quit     = new MenuItem("Quit");
       
        // add Image and shortcut to each menu item
        newGame.setGraphic( new ImageView( new Image("icons/new game.png") ) );
        newGame.setAccelerator(
            new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN) );

        quit.setGraphic( new ImageView( new Image("icons/door_out.png") ) );
        quit.setAccelerator(
            new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN) );
       
        aboutFile.setGraphic( new ImageView( new Image("icons/information.png") ) );
        instructFile.setGraphic( new ImageView( new Image("icons/information.png") ) );
        
        fileMenu.getItems().add( newGame );
        helpMenu.getItems().add( aboutFile );
        helpMenu.getItems().add( instructFile );
        fileMenu.getItems().add( quit );
        
        Label timeLabel = new Label("");        
        timeLabel.setStyle( labelStyle );

        
        newGame.setOnAction(
            (ActionEvent event)->
            {
                newGame();
            }
        );
        
        quit.setOnAction(
            (ActionEvent event) ->
            {
                mainStage.close();
            }
        );
        
        aboutFile.setOnAction(
            (ActionEvent event) ->
            {
                Alert infoAlert = new Alert( AlertType.INFORMATION );
                infoAlert.setTitle("Credits ");
                 // set title bar text
                 infoAlert.setHeaderText("World Cup Simulator ");
                 
                 // set body text
                 infoAlert.setContentText(
                  "This app was created by Dimitri Maragh. ");
                 
                 // display the Alert window
                 infoAlert.showAndWait();
                }
        );
       
        instructFile.setOnAction(
            (ActionEvent event) ->
            {
                Alert infoAlert = new Alert( AlertType.INFORMATION );
                infoAlert.setTitle("Instructions ");
                 // set title bar text
                 infoAlert.setHeaderText("How the World Cup Simulator works");
                 
                 // set body text
                 infoAlert.setContentText(
                  "This World Cup Simulator allows users to go 1v1 against a keeper, and have to time their shot to beat him.\n"+
                  "They accumulate points by avoiding the keeper and scoring in the goal and has a 30s timer to do so.\n"
                   );
                 
                 // display the Alert window
                 infoAlert.showAndWait();
                }
        );
        
        //Declaring variables to hold the points of the goal from the background 
        double goalLeft = 91;
        double goalRight = 511;
        double goalTop = 248;
        double goalBottom = 499;
        
        //calculating the keeper's starting x position based on the right goal post and the width of the keeper.
        double goalKeeperX = goalRight - 20;
        
        //Setting up our sprite objects 
        Sprite goalKeeper = new Sprite(goalKeeperX, ( goalTop+ goalBottom) / 2, 150, 100, "goalkeeper.png");
        Sprite ball = new Sprite(280,500, 70,70, "ball.png");
        
        //Declaring the time to count down from 
        time=30.0;
        
        //Adding picure for the background ont the app
        Image field = new Image("goal.png", 600,600, false, true);
        

        
        //Creating kick button, this function will project the ball in the direction of the center of the goal based on the center of the goal and the ball position
        Button kickButton = new Button("Kick");
        kickButton.setOnAction(
            (event) ->
            {
                if(!ballInPlay)
                {
                    ballInPlay = true;
                    
                    //Calculate the center of the goal
                    goalMidY = (goalTop + goalBottom) / 2;
            
                    // Set ball speed
                    ySpeed = -2;
                }
            }
        );
        
        
        ballInPlay=false;
        
        // animation timer redraws the game 100 times per second
        AnimationTimer timer = new AnimationTimer()
            {
                public void handle(long nanoSeconds)
                {
                    // 1/100 of a second has elapsed (
                    time -= 1.0 / 100;
                    
                    //Wanted to just use the whole numbe rof the time
                    int wholeNumber= (int) time;
                                
                    //update time label
                    timeLabel.setText(Integer.toString( wholeNumber) + "s" );
                    
                    context.drawImage(field,0,0, 600,600);

                    // draw sprite objects. 
                    ball.draw( context );
                    goalKeeper.draw(context);
                    
                    //Move the ball if it is in play
                    if( ballInPlay)
                    {
                        ball.move(0,ySpeed);
                        ball.width = ball.width - 1;
                        ball.height = ball.height - 1;
                        
                        if(ball.y< goalMidY)
                        {
                            if (!ball.overlaps(goalKeeper))
                            {
                                score+=5;
                                
                                //Display goal message after each goal
                                // context.setFill(Color.BLUE);
                                // context.setFont(new Font("Arial", 50));
                                // context.fillText("Goal!", 200,300);
                            }
                            // If the ball is not overlapping the keeper, score accumulates
                            else
                            {
                                //Display Miss message after each time it overlaps the keeper
                                // context.setFill(Color.BLUE);
                                // context.setFont(new Font("Arial", 50));
                                // context.fillText("You Missed!", 200,300);
                            }
                        
                            
                            //Reset ball position
                            resetBall(ball);
                            ballInPlay=false;
                        }
                    }
                
                    
                    //Display the current score in the top left of the background
                    context.setFill(Color.WHITE);
                    context.setFont(new Font("Arial", 40));
                    context.fillText("Score: " + score,10, 30);
                    
                    
                    //Moving the goal keeper back and forth between the posts
                    goalKeeper.x += goalKeeperSpeed *goalKeeperDirection;
                    if (goalKeeper.x + goalKeeper.width > goalRight) 
                    {
                        goalKeeperDirection=-1;
                    }
                    else if (goalKeeper.x<goalLeft)
                    {
                        goalKeeperDirection=1;
                    }
                    
                    
                    
                    //Check if the time is up and display game over message with score
                    if (time<=0.00)
                    {
                        context.setFill(Color.BLUE);
                        context.setFont(new Font("Arial", 50));
                        context.fillText("Game Over! Score :"+ score + "!", 60,300);
                        
                        //Play crowd sound
                        File worldCupFile = new File("audio/crowd.wav");
                        AudioClip worldCupSound = new AudioClip( worldCupFile.toURI().toString() );
                        
                        worldCupSound.play();
                        

                        this.stop();
                    }
                    
                    
                    
                    
                }
            };

        timer.start();
        root.setCenter(box);
        
        HBox box1= new HBox();
        box1.setSpacing(20);
        box1.setAlignment(Pos.TOP_RIGHT);
        box1.getChildren().add( timeLabel );
        
        box.getChildren().addAll(box1, canvas , kickButton );
        
        // custom application code above -------------------

        // after adding all content, make the Stage visible
        mainStage.show();
        mainStage.sizeToScene();
    }
    
    public void resetBall(Sprite ball)
    {
        ball.x=230;
        ball.y=500;
        
        ball.width=100;
        ball.height=100;
    }
    
    //Start a new game and reset the attributes
    public void newGame()
    {
        score=0;
        time=30.0;
        
        Sprite ball=new Sprite(280, 500, 70, 70, "ball.png");
        
        resetBall(ball);
        ballInPlay= false;
        
        
    }
}