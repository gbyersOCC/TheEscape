package edu.orangecoastcollege.escapethecatcher;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class  GameActivity extends Activity implements GestureDetector.OnGestureListener{

    private GestureDetector aGesture;

    //FLING THRESHOLD VELOCITY
    final int FLING_THRESHOLD = 500;

    //BOARD INFORMATION
    final int SQUARE = 150;
    final int OFFSET = 5;
    final int COLUMNS = 7;
    final int ROWS = 8;
    final int gameBoard[][] = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 1, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 3},
            {1, 2, 1, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1}
    };

    private Player player;
    private Zombie zombie;
    private ExtraLife heart;

    //LAYOUT AND INTERACTIVE INFORMATION
    private ArrayList<ImageView> visualObjects;
    private RelativeLayout activityGameRelativeLayout;
    private ImageView zombieImageView;
    private ImageView playerImageView;
    private ImageView obstacleImageView;
    private ImageView exitImageView;
    private ImageView heartImageView;
    private int exitRow;
    private int exitCol;

    //  WINS AND LOSSES
    private int wins;
    private int losses;
    private TextView winsTextView;
    private TextView lossesTextView;

    private LayoutInflater layoutInflater;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        activityGameRelativeLayout = (RelativeLayout) findViewById(R.id.activity_game);
        winsTextView = (TextView) findViewById(R.id.winsTextView);
        lossesTextView = (TextView) findViewById(R.id.lossesTextView);
        aGesture= new GestureDetector(this, this);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resources = getResources();

        visualObjects = new ArrayList<ImageView>();

        wins = 0;
        losses = 0;
        winsTextView.setText(resources.getString(R.string.win) + wins);
        lossesTextView.setText(resources.getString(R.string.losses) + losses);

        startNewGame();
    }

    private void startNewGame() {
        //TASK 1:  CLEAR THE BOARD (ALL IMAGE VIEWS)
        for (int i = 0; i < visualObjects.size(); i++) {
            ImageView visualObj = visualObjects.get(i);
            activityGameRelativeLayout.removeView(visualObj);
        }
        visualObjects.clear();

        //TASK 2:  BUILD THE  BOARD
        buildGameBoard();

        //TASK 3:  ADD THE CHARACTERS
        createZombie();
        createPlayer();
        creatExtraLife();
    }

    private void buildGameBoard() {
        // TODO: Inflate the entire game board (obstacles and exit)
        for(int i= 0; i<ROWS; i++)
        {
            for(int c=0;c<COLUMNS; c++)
            {
                if(gameBoard[i][c] == BoardCodes.OBSTACLE){
                    obstacleImageView = (ImageView) layoutInflater.inflate(R.layout.obstacle_layout,null);
                    obstacleImageView.setX(c*SQUARE+OFFSET);
                    obstacleImageView.setY(i*SQUARE+OFFSET);
                    activityGameRelativeLayout.addView(obstacleImageView);
                    //add ImageView obj to arrayList for end-of-game removal
                    visualObjects.add(obstacleImageView);}
                else if(gameBoard[i][c]== BoardCodes.EXIT){
                    exitRow=i;
                    exitCol=c;
                    exitImageView = (ImageView) layoutInflater.inflate(R.layout.exit_layout, null);
                    exitImageView.setX(c*SQUARE);
                    exitImageView.setY(i*SQUARE);
                    activityGameRelativeLayout.addView(exitImageView);
                    //add ImageView obj to arrayList for end-of-game removal
                    visualObjects.add(exitImageView);
                visualObjects.add(exitImageView);}
                else{}


            }
        }

    }

    private void createZombie() {
        //define row and column and istanitate new zombie
        int row = 1;
        int col = 4;
        // TODO: Determine where to place the Zombie (at game start)
        zombie = new Zombie();
        zombie.setRow(row);
        zombie.setCol(col);

        // TODO: Then, inflate the zombie layout
        zombieImageView = (ImageView) layoutInflater.inflate(R.layout.zombie_layout,null);
        //set x coordinate of imageView
        zombieImageView.setX(col*SQUARE + OFFSET);
        zombieImageView.setY(row*SQUARE+OFFSET);
        //display zombie view in relative layout
        activityGameRelativeLayout.addView(zombieImageView);
        visualObjects.add(zombieImageView);
    }

    private void creatExtraLife(){
        int row = 4;
        int col = 1;

        heart = new ExtraLife(row, col);
        heartImageView = (ImageView) layoutInflater.inflate(R.layout.extra_life_layout, null);
        heartImageView.setX(col*SQUARE+OFFSET);
        heartImageView.setY(row*SQUARE+OFFSET);
        activityGameRelativeLayout.addView(heartImageView);
        visualObjects.add(heartImageView);


    }
    private void createPlayer() {
        // TODO: Determine where to place the Player (at game start)
        int row = 1;
        int col = 1;
        player = new Player();
        player.setCol(col);
        player.setRow(row);

        playerImageView = (ImageView) layoutInflater.inflate(R.layout.player_layout, null);
        playerImageView.setX(col*SQUARE+OFFSET);
        playerImageView.setY(row*SQUARE+OFFSET);
        activityGameRelativeLayout.addView(playerImageView);
        visualObjects.add(playerImageView);
        // TODO: Then, inflate the player layout
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return aGesture.onTouchEvent(event);
    }

    private void movePlayer(float velocityX, float velocityY) {
        // TODO: This method gets called in the onFling event

        // TODO: Determine which absolute velocity is greater (x or y)
        String direction="";
        if(Math.abs(velocityX)>Math.abs(velocityY)) {
            if(velocityX< -FLING_THRESHOLD)
                direction = "LEFT";
            else if (velocityX > FLING_THRESHOLD)
                direction="RIGHT";

        }
        else{
            if(velocityY < -FLING_THRESHOLD)
                direction="UP";
            else if(velocityY > FLING_THRESHOLD)
                direction="DOWN";
        }
           //only miove player if direction is not an empty string
        if(!direction.equals(""))
        {
            player.move(gameBoard, direction);
            playerImageView.setX(player.getCol()*SQUARE+OFFSET);
            playerImageView.setY(player.getRow()*SQUARE+OFFSET);
        }
        if(heart.getRow()==player.getRow()&&heart.getCol()==player.getCol()) {
            int index = visualObjects.indexOf(heartImageView);
            ImageView visualObj = visualObjects.get(index);
            activityGameRelativeLayout.removeView(visualObj);
            Toast.makeText(this, "Player awarded new Turn", Toast.LENGTH_SHORT).show();
        }
        if(heart.getRow()==player.getRow()&&heart.getCol()==player.getCol())//move zombie no matter what
        {}else{  zombie.move(gameBoard, player.getCol(), player.getRow());
            zombieImageView.setX(zombie.getCol()*SQUARE + OFFSET);
            zombieImageView.setY(zombie.getRow()*SQUARE + OFFSET);}


if(player.getRow()== exitRow && player.getCol()==exitCol)
{
    Toast.makeText(this,"YOU WIN!!!",Toast.LENGTH_LONG).show();
    startNewGame();
    winsTextView.setText(resources.getString(R.string.win)+ (++wins));
}
        if(player.getCol()==zombie.getCol()&&player.getRow()==zombie.getRow()) {
            Toast.makeText(this,"YOU LOSE!!!",Toast.LENGTH_LONG).show();
            startNewGame();
            lossesTextView.setText("Loses: " + (++losses));
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        movePlayer(v,v1);
        return true;
    }
}
