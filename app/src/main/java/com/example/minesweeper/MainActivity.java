package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    static public int flags = 10;
    static public int blocks = 71;
    static int rows = 9;
    static int columns = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //table구성
        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
        TableRow[] tableRows = new TableRow[9];
        for(int i = 0; i < rows; i++) {
            tableRows[i] = new TableRow(this);
            table.addView(tableRows[i]);
        }

        BlockButton[][] buttons = new BlockButton[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                buttons[i][j] = new BlockButton(this, i, j);
            }
        }

        minePlanter(buttons);
        numberCounter(buttons);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f);

                buttons[i][j].setLayoutParams(layoutParams);
                //주변 지뢰 수 보이게

                buttons[i][j].setText(buttons[i][j].getNeighborMines() + "");
                if(buttons[i][j].isMine())
                    buttons[i][j].setText("!!");

                tableRows[i].addView(buttons[i][j]);
            }
        }

        //토글 버튼 onclicklistener
        Button finish = (Button) findViewById(R.id.finsish_button);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //남은 지뢰 수 설정
        TextView numOfFlag = (TextView) findViewById(R.id.nums_text);
        numOfFlag.setText(flags + "");


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                final int finalI = i;
                final int finalJ = j;

                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentI = finalI;
                        int currentJ = finalJ;

                        buttons[currentI][currentJ].breakBlock();


                    }
                });
            }
        }


    }


    //지뢰 심기
    void minePlanter(BlockButton[][] buttons) {
        int count = 0, x = 0, y = 0;
        while(count < 10){
            x = (int) (Math.random() * rows);
            y = (int) (Math.random() * columns);

            if(buttons[x][y].isMine())
                continue;
            buttons[x][y].setMine(true);
            count++;
        }
    }

    // 주변 지뢰 숫자 카운팅
    void numberCounter(BlockButton[][] buttons) {
        int k = 0;
        //모서리와 인접하지 않은 경우
        for(int x = 1; x < rows-1; x++){
            for(int y = 1; y < columns-1; y++){
                if(buttons[x-1][y-1].isMine())
                    k++;
                if(buttons[x-1][y].isMine())
                    k++;
                if(buttons[x-1][y+1].isMine())
                    k++;
                if(buttons[x][y-1].isMine())
                    k++;
                if(buttons[x][y+1].isMine())
                    k++;
                if(buttons[x+1][y-1].isMine())
                    k++;
                if(buttons[x+1][y].isMine())
                    k++;
                if(buttons[x+1][y+1].isMine())
                    k++;

                buttons[x][y].setNeighborMines(k);
                k = 0;
            }
        }
        
        //위쪽 모서리와 인접한 블록
        for(int y = 1; y < columns-1; y++){
            k = 0;
            if(buttons[0][y-1].isMine())
                k++;
            if(buttons[0][y+1].isMine())
                k++;
            if(buttons[1][y-1].isMine())
                k++;
            if(buttons[1][y].isMine())
                k++;
            if(buttons[1][y+1].isMine())
                k++;
            buttons[0][y].setNeighborMines(k);
        }
        //아래쪽 모서리와 인접한 블록
        for(int y = 1; y < columns-1; y++){
            k = 0;
            if(buttons[8][y-1].isMine())
                k++;
            if(buttons[8][y+1].isMine())
                k++;
            if(buttons[7][y-1].isMine())
                k++;
            if(buttons[7][y].isMine())
                k++;
            if(buttons[7][y+1].isMine())
                k++;
            buttons[8][y].setNeighborMines(k);
        }
        //왼쪽 모서리와 인접한 블록
        for(int x = 1; x < rows-1; x++){
            k = 0;
            if(buttons[x-1][0].isMine())
                k++;
            if(buttons[x+1][0].isMine())
                k++;
            if(buttons[x-1][1].isMine())
                k++;
            if(buttons[x][1].isMine())
                k++;
            if(buttons[x+1][1].isMine())
                k++;

            buttons[x][1].setNeighborMines(k);
        }
        //오른쪽 모서리와 인접한 블록
        for(int x = 1; x < rows-1; x++){
            k = 0;
            if(buttons[x-1][8].isMine())
                k++;
            if(buttons[x+1][8].isMine())
                k++;
            if(buttons[x-1][7].isMine())
                k++;
            if(buttons[x][7].isMine())
                k++;
            if(buttons[x+1][7].isMine())
                k++;
            buttons[x][8].setNeighborMines(k);
        }

        //좌상단
        k = 0;
        if(buttons[0][1].isMine())
            k++;
        if(buttons[1][0].isMine())
            k++;
        if(buttons[1][1].isMine())
            k++;
        buttons[0][0].setNeighborMines(k);
        //우상단
        k = 0;
        if(buttons[0][7].isMine())
            k++;
        if(buttons[1][8].isMine())
            k++;
        if(buttons[1][7].isMine())
            k++;
        buttons[0][8].setNeighborMines(k);
        //좌하단
        k = 0;
        if(buttons[7][0].isMine())
            k++;
        if(buttons[8][1].isMine())
            k++;
        if(buttons[8][0].isMine())
            k++;
        buttons[8][0].setNeighborMines(k);
        //우하단
        k = 0;
        if(buttons[8][7].isMine())
            k++;
        if(buttons[7][8].isMine())
            k++;
        if(buttons[7][7].isMine())
            k++;
        buttons[8][8].setNeighborMines(k);
    }

}