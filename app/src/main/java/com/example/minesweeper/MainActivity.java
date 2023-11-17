package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
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
        TableLayout table = findViewById(R.id.tableLayout);
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
                tableRows[i].addView(buttons[i][j]);
            }
        }

        //toggle 버튼 onclicklistener
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButton.setText("Flag");
                } else {
                    toggleButton.setText("Break");
                }
            }
        });

        //남은 지뢰 수
        TextView numOfFlag = findViewById(R.id.nums_text);
        numOfFlag.setText(flags + "");

        //남은 블록 수
        TextView numOfBlock = findViewById(R.id.nums_block);
        numOfBlock.setText(blocks + "");

        //blockbutton onclicklistener
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int globalI = i;
                int globalJ = j;

                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!toggleButton.isChecked()){
                            recursiveOpen(buttons, globalI, globalJ);
                        }
                        else if(toggleButton.isChecked()){
                            buttons[globalI][globalJ].toggleFlag();
                        }

                        //남은 지뢰, 블록 수 설정
                        numOfFlag.setText(flags + "");
                        numOfBlock.setText(blocks + "");

                        //게임 승리
                        if(blocks == 0)
                            showWinDialog();

                    }//End of onClick method
                });//End of setOnClickListener
            }//End of for J
        }//End of for I
    }//End of OnCreate

    //승리시 AlertDialog
    private void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Victory! (성공)");
        builder.setMessage("Congratulations, You win the game.");

        builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                flags = 10;
                blocks = 71;
                finish();
            }
        });
        builder.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restartGame(); //다시시작
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });

        // AlertDialog 객체 생성 및 보여주기
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //패배시 AlertDialog
    private void showFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pow! (실패)");
        builder.setMessage("Watch Out! You Open the Mine");

        builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                flags = 10;
                blocks = 71;
                finish();
            }
        });
        builder.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restartGame(); //다시시작
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });

        // AlertDialog 객체 생성 및 보여주기
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void restartGame() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 현재 액티비티 위에 있는 모든 액티비티 제거
        startActivity(intent);
        flags = 10;
        blocks = 71;
        finish(); // 현재 액티비티 종료
    }




    //재귀적으로 열기
    void recursiveOpen(BlockButton[][] buttons, int x, int y) {
        if (x < 0 || y < 0
                || x >= rows || y >= columns
                || !(buttons[x][y].isClickable())
                || buttons[x][y].isFlag()) {
            return;
        }

        boolean powOrNot = buttons[x][y].breakBlock();
        //게임 오버
        if(powOrNot){
            showFailDialog();
        }

        //주변에 블록이 없는 경우
        if (buttons[x][y].getNeighborMines() == 0) {
            // 주변 8방향 블록 오픈
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    recursiveOpen(buttons, i, j);
                }
            }
        }
    }


    //지뢰 심기
    void minePlanter(BlockButton[][] buttons) {
        int count = 0, x, y;
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