package com.example.minesweeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class BlockButton extends Button {
    private int x, y, neighborMines;
    private boolean mine, flag;

    public BlockButton(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        neighborMines = 0;
        mine = false;
        flag = false;
        setTextColor(Color.RED);
    }


    public void toggleFlag(){
        if(!isFlag()){
            setText("+");
            setFlag(true);
            MainActivity.flags--;
        }
        else {
            setText("");
            setFlag(false);
            MainActivity.flags++;
        }
    }


    public boolean breakBlock(){
        setClickable(false);
        if(!isMine()) {
            setText(neighborMines + "");
            setTypeface(null, Typeface.BOLD);
            if(neighborMines == 0)
                setText("");
            else if(neighborMines == 1)
                setTextColor(Color.BLACK);
            else if(neighborMines == 2)
                setTextColor(Color.BLUE);
            else if(neighborMines == 3)
                setTextColor(Color.YELLOW);
            setBackgroundColor(Color.LTGRAY);
            MainActivity.blocks--;
            return false;
        }
        return true;
    }


    @Override
    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getNeighborMines() {
        return neighborMines;
    }

    public void setNeighborMines(int neighborMines) {
        this.neighborMines = neighborMines;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isFlag() {
        return flag;
    }

    private void setFlag(boolean flag) {
        this.flag = flag;
    }
}