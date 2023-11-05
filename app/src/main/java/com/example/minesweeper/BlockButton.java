package com.example.minesweeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class BlockButton extends Button {
    private int x = 0, y = 0, neighborMines = 0;
    private boolean mine = false, flag = false;

    public BlockButton(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        neighborMines = 0;
        mine = false;
        flag = false;
    }
    public void toggleFlag(){
        if(isFlag()){
            setFlag(false);
            MainActivity.flags--;
            setText("+");
        }
        else {
            setFlag(true);
            MainActivity.flags++;
            setText("");
        }
    }

    public boolean breakBlock(){
        setClickable(false);
        if(isMine())
            return true;
        else return false;
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
