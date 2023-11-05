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
/*
                buttons[i][j].setText(buttons[i][j].getNeighborMines() + "");
                if(buttons[i][j].isMine())
                    buttons[i][j].setText("!!");
*/
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


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                final int finalI = i;
                final int finalJ = j;

                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentI = finalI;
                        int currentJ = finalJ;

                        buttons[currentI][currentJ].toggleFlag();

                        //남은 지뢰 수 설정
                        TextView numOfFlag = (TextView) findViewById(R.id.nums_text);
                        numOfFlag.setText(flags + "");
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
        for(int i = 0; i < rows; i++){
            for(int j = 0 ; j < columns; j++){
                buttons[i][j].setNeighborMines(countAdjacentMines(buttons, i, j));
            }
        }
    }

    int countAdjacentMines(BlockButton[][] buttons, int x, int y) {
        int count = 0;

        for (int i = Math.max(0, x - 1); i <= Math.min(x + 1, rows - 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(y + 1, columns - 1); j++) {
                if (buttons[i][j].isMine() && (i != x || j != y)) {
                    count++;
                }
            }
        }
        return count;
    }

}