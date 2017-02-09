package com.example.lab2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import com.example.lab2.model.CornerPuzzleModel;

/**
 * Created by Raluca on 16-Feb-16.
 * Parts of code from CE881 Lab, Spyros Samothrakis
 */

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    int nGrid;
    static String modelKey = "Model";
    static String aboutItem = "About";
    static String helpItem = "Help";
    static String resetItem = "Reset Level";
    static String restartItem = "Restart Game";
    static String quitItem = "Quit";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        nGrid = PuzzleView.gridSize;
    }

    PuzzleView getPuzzleView() {
        return (PuzzleView) findViewById(R.id.puzzleView);
    }

    public void resetModel (){
        PuzzleView puzzleView = getPuzzleView();
        PuzzleView.model.reset(puzzleView);
        puzzleView.postInvalidate();
    }

    public void restartGame() {
        PuzzleView puzzleView = getPuzzleView();
        PuzzleView.model = null;
        puzzleView.recreate();
        puzzleView.postInvalidate();
    }

    public void updateScore(View view){
        //textView textView = (TextView) findViewById(R.id.textView);
        PuzzleView puzzleView = getPuzzleView();
        puzzleView.onCustomClick(puzzleView);

        /*
        if (puzzleView.model.isSolved()) {
            textView.setText(win);
        }*/
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //save current game state
        try {
            savedInstanceState.putSerializable(modelKey, getModel());
        } catch (Exception e) {}
        //call superclass to save view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            PuzzleView.model = (CornerPuzzleModel) savedInstanceState.getSerializable(modelKey);
        } catch (Exception e) {}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(resetItem);
        menu.add(restartItem);
        menu.add(helpItem);
        menu.add(aboutItem);
        menu.add(quitItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(aboutItem)) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getTitle().equals(helpItem)) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getTitle().equals(resetItem)) {
            resetModel();
        }
        if (item.getTitle().equals(restartItem)) {
            restartGame();
        }
        if (item.getTitle().equals(quitItem)) {
            PuzzleView.model = null;
            System.exit(1);
        }
        return super.onOptionsItemSelected(item);
    }

    public CornerPuzzleModel getModel() {
        if (PuzzleView.model == null) {
            PuzzleView.model = new CornerPuzzleModel(nGrid, getPuzzleView());
        }
        return PuzzleView.model;
    }

}
