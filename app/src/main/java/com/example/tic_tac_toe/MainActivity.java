package com.example.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.view.View.OnClickListener;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MainActivity extends AppCompatActivity
implements OnClickListener {

    //define all widget variables
    private TextView txtDisplay;
    private Button btnNewGame;

    private Button[][] btns = new Button[3][3];

    //define playerTurn flags and turn counter
    private boolean playerOneTurn;
    private int turnCount;

    //define SharedPreferences object for saving data
    private SharedPreferences savedValues;

    /*
    //define variables for saving data
    private String txtDisplayString;
    private String[][] btnsString = new String[3][3];
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to the widgets
        txtDisplay = findViewById(R.id.txtDisplay);
        btnNewGame = findViewById(R.id.btnNewGame);
        setBtns();
        //setBtnsString();

        //sets the new game listener independently
        btnNewGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });



        //get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }

    //This method uses the multi-dimensional array to get references to each button
    //and set up the onClickListeners for each
    public void setBtns()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                String btnID = "btn_" + i + j;
                int pointerID = getResources().getIdentifier(btnID, "id", getPackageName());
                btns[i][j] = findViewById(pointerID);
                btns[i][j].setOnClickListener(this);
            }
        }
    }

    /*
    //This method populates the multi-dimensional btnsString array
    public void setBtnsString()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                String btnID = "btn_" + i + j + "String";
                btnsString[i][j] = btnID;

            }
        }
    }

     */

    @Override
    public void onClick(View v)
    {
        //prevents buttons from being overwritten by another player
        if (!((Button) v).getText().toString().equals(""))
        {
            return;
        }

        //this if else statement handles the changing of buttons and switching turns for display
        if (playerOneTurn)
        {
            ((Button) v).setText("X");
            txtDisplay.setText("Player Two's Turn");
        }
        else
        {
            ((Button) v).setText("O");
            txtDisplay.setText("Player One's Turn");
        }

        //max number of possible turns in a game of tic-tac-toe is 9
        //once this counter reaches 9 a win condition is fulfilled in the next if statements
        turnCount++;

        //these if statements check for all possible win conditions. If no condition is met,
        //then the game continues and turn is swapped.
        if(gameCheck())
        {
            if(playerOneTurn)
            {
                playerOneWin();
            }
            else
            {
                playerTwoWin();
            }
        }
        else if (turnCount == 9)
        {
            draw();
        }
        else
        {
            playerOneTurn = !playerOneTurn;
        }

    }

    // these next three methods just change the display text.
    public void playerOneWin()
    {

        txtDisplay.setText("Player One Wins!");

    }

    public void playerTwoWin()
    {

        txtDisplay.setText("Player Two Wins!");

    }

    public void draw()
    {

        txtDisplay.setText("It's a Tie!");

    }

    //using a multi-dimensional String array, this method checks to see if any of the possible win conditions exist and returns true if one is found
    private boolean gameCheck()
    {
        String[][] checkStringArray = new String[3][3];

        //this nested for loop populates the String array used for checking the buttons' contents
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                checkStringArray[i][j] = btns[i][j].getText().toString();
            }
        }

        //this nested for loop checks if win conditions exist for any of the rows
        for (int i = 0; i < 3; i++)
        {
            if (checkStringArray[i][0].equals(checkStringArray[i][1])
            && checkStringArray[i][0].equals(checkStringArray[i][2])
            && !checkStringArray[i][0].equals(""))
            {
                return true;
            }
        }

        //this nested for loop checks if win conditions exist for any of the columns
        for (int i = 0; i < 3; i++)
        {
            if (checkStringArray[0][i].equals(checkStringArray[1][i])
                    && checkStringArray[0][i].equals(checkStringArray[2][i])
                    && !checkStringArray[0][i].equals(""))
            {
                return true;
            }
        }

        //this if statement check the top left to bottom right diagonal win condition
        if (checkStringArray[0][0].equals(checkStringArray[1][1])
                && checkStringArray[0][0].equals(checkStringArray[2][2])
                && !checkStringArray[0][0].equals(""))
        {
            return true;
        }

        //this if statement check the bottom left to top right diagonal win condition
        if (checkStringArray[0][2].equals(checkStringArray[1][1])
                && checkStringArray[0][2].equals(checkStringArray[2][0])
                && !checkStringArray[0][2].equals(""))
        {
            return true;
        }

        //if now win condition is found, method returns false and game continues
        return false;
    }

    //this method is used in the btnNewGame's onClick method.
    //It is used to reset the buttons, the turn counter and set the turn back to player one.
    public void newGame()
    {

        txtDisplay.setText("Player One's Turn");

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                btns[i][j].setText("");
            }
        }

        turnCount = 0;
        playerOneTurn = true;
    }

    //These two methods are simpler methods that will save variable data.
    //The button and display data is frozen in the .xml files
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("turnCount", turnCount);
        outState.putBoolean("playerOneTurn", playerOneTurn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        turnCount = savedInstanceState.getInt("turnCount");
        playerOneTurn = savedInstanceState.getBoolean("playerOneTurn");
    }

    /*
    //These two methods populate the two multi-dimensional button arrays for data saving purposes
    public void saveButtonText()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                btnsString[i][j] = btns[i][j].toString();
            }
        }
    }

    public void restoreButtonText()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                btns[i][j].setText(btnsString[i][j]);
            }
        }
    }

    //These two methods save and restore the game data in case of a orientation change
    // or when the user navigates away from the app but does not close it
    @Override
    public void onPause() {
        //saves all instance variables
        Editor editor = savedValues.edit();
        editor.putString("txtDisplayString", txtDisplayString);
        saveButtonText();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                editor.putString("btn_" + i + j + "String", btnsString[i][j]);
            }
        }
        editor.putInt("turnCount", turnCount);
        editor.putBoolean("playerOneTurn", playerOneTurn);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //restores all instance variables
        txtDisplayString = savedValues.getString("txtDisplayString", "Press New Game to Begin");

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                btnsString[i][j] = savedValues.getString("btn_" + i + j + "String", "");
            }
        }

        restoreButtonText();
        turnCount = savedValues.getInt("turnCount", 0);
        playerOneTurn = savedValues.getBoolean("playerOneTurn", true);

        //sets buttons to restored values
        txtDisplay.setText(txtDisplayString);

    }

     */

}
