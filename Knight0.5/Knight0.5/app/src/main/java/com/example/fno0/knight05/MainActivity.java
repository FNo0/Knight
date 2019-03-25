package com.example.fno0.knight05;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btChess[] = new ImageButton[64];
    Button btRestart;
    Button btHelp;
    Button btGame;
    Button btDemo;
    Button btLast;
    TextView tvStep;
    TextView tvStepInfo;//实际步数，用于判定游戏是否结束
    int step;//实际步数
    TextView tvRestart;
    TextView tvHelp;
    TextView tvNumOfStep;//操作步数，用于评分
    int numOfStep;//操作步数
    Spinner spDifficulty;
    LinearLayout acMain;//整个线性布局，用于左滑右滑操作
    Context mContext;
    int endStep = 59;//真实的结束步数，用于难度区分，默认为Easy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findView();
        setClickListener();
        setGestureListener();
        ChessPoint.createChess();
        showHelp();
        numOfStep = Integer.parseInt(tvNumOfStep.getText().toString());
        step = Integer.parseInt(tvStepInfo.getText().toString());//当前的真实步数
    }

    private void findView() {
        tvStep = (TextView) this.findViewById(R.id.tv_Step);
        tvStepInfo = (TextView) this.findViewById(R.id.tv_StepInfo);
        btGame = (Button) this.findViewById(R.id.bt_Game);
        btDemo = (Button) this.findViewById(R.id.bt_Demo);
        btRestart = (Button) this.findViewById(R.id.bt_Restart);
        btHelp = (Button) this.findViewById(R.id.bt_Help);
        tvRestart = (TextView) this.findViewById(R.id.tv_Restart);
        tvHelp = (TextView) this.findViewById(R.id.tv_Help);
        tvNumOfStep = (TextView) this.findViewById(R.id.tv_NumberOfStep);
        btLast = (Button) this.findViewById(R.id.bt_Last);
        spDifficulty = (Spinner) this.findViewById(R.id.sp_Difficulty);
        acMain = (LinearLayout) this.findViewById(R.id.activity_main);
        int index = R.id.bt_0;
        for (int i = 0; i < 64; i++) {
            btChess[i] = (ImageButton) this.findViewById(index++);
        }
    }

    public void setClickListener() {
        for (int i = 0; i < 64; i++) {
            btChess[i].setOnClickListener(this);
        }
        btLast.setOnClickListener(this);
        btHelp.setOnClickListener(this);
        btRestart.setOnClickListener(this);
        btGame.setOnClickListener(this);
        btDemo.setOnClickListener(this);
        btGame.setClickable(false);

        spDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Easy")) {
                    endStep = 60;
                }
                if (parent.getItemAtPosition(position).toString().equals("Middle")) {
                    endStep = 61;
                }
                if (parent.getItemAtPosition(position).toString().equals("Hard")) {
                    endStep = 63;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Tip")
                .setMessage("\t1.这是“游戏”界面，你可以通过点击“Demo”切换到“演示”界面\n" +
                        "\t2.首先你需要选择难度并在棋盘里点击一个格子作为起点\n" +
                        "\t3.你的目标按照国际象棋“马走日字”的规则走完整个棋盘\n" +
                        "\t4.简单难度可以剩余3个格子没有走完，中等难度可以剩余2个格子没有走完，困难难度全部格子都要走完" +
                        "\t5.游戏底部三个按钮分别是“后退”、“重新开始”、“查看帮助”\n" +
                        "\t6.游戏很难，“后退”按钮会很有帮助")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onClick(View v) {
        int index = R.id.bt_0;//第一个btChess即btChess[0]在系统中的位置
        int[] idOfChess = new int[64];//存储所有btChess的在系统中的位置信息
        for (int i = 0; i < 64; i++) {
            idOfChess[i] = index++;
        }
        //前进
        for (int i = 0; i < 64; i++) {
            if (v.getId() == idOfChess[i]) {
                goAhead(i);
            }
        }
        //后退
        if (v.getId() == R.id.bt_Last) {
            goBack();
        }
        //帮助信息
        if (v.getId() == R.id.bt_Help) {
            showHelp();
        }
        //重新开始
        if (v.getId() == R.id.bt_Restart) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();//结束this
        }
        //跳转到Demo界面
        if (v.getId() == R.id.bt_Demo) {
            Intent intent = new Intent(this, DemoActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setGestureListener() {
        acMain.setOnTouchListener(new View.OnTouchListener() {
            float x1, x2, y1, y2;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = event.getX();
                        y2 = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (x2 - x1 < 0 && (Math.abs(x2 - x1) > 25) && (Math.abs(y2 - y1) < Math.abs(x2 - x1))) {
                            step = Integer.parseInt(tvStepInfo.getText().toString());//当前的步数
                            goBack();//左划后退
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void goAhead(int i) {//前进函数
        numOfStep++;
        tvNumOfStep.setText(numOfStep + "");//前进，操作步数增加
        step++;
        tvStepInfo.setText(step + "");// 真实步数加一
        for (int iOthers = 0; iOthers < 64; iOthers++) {
            btChess[iOthers].setOnClickListener(null);//为每个格子去除动作监听事件
            btChess[iOthers].setImageResource(0);
            if (!ChessPoint.chessPoint[iOthers].isClicked) {
                btChess[iOthers].setBackgroundColor(ChessPoint.chessPoint[iOthers].initial);
            }//如果当前位置没有被点击过,则还原棋盘颜色
        }//其他的btChess此刻移除所有的监听和前景图片
        ChessPoint.chessPoint[i].setClicked(true);//此刻当前位置已被点击
        if (ChessPoint.chessPoint[i].isClicked) {
            btChess[i].setBackgroundColor(Color.BLUE);//设置当前点击的btChess的背景颜色
        }
        btChess[i].setImageResource(R.drawable.prince);//设置当前点击的btChess的前景图片
        ChessPoint.chessPoint[i].setOrder(step);//当前点击的btChess的顺序

        int x = 0, y = 0;
        for (int m = 0; m < 8; m++)
            for (int j = 0; j < 8; j++) {
                if (j * 8 + m == i) {
                    x = m;
                    y = j;
                }
            }//处理btChess在棋盘中的位置即坐标与i的逻辑关系
        for (int a = 0; a < 8; a++) {
            int b = x + Knight.directionX[a];
            int c = y + Knight.directionY[a];//下一个位置的坐标
            if (b < 8
                    && b >= 0
                    && c >= 0
                    && c < 8
                    && !ChessPoint.chessPoint[c * 8 + b].isClicked) {
                btChess[c * 8 + b].setOnClickListener(this);
                btChess[c * 8 + b].setBackgroundColor(Color.YELLOW);
            }//棋子的前进逻辑
        }

        //游戏结束提示
        if (step == endStep) {
            String difficulty = null;
            if (endStep == 63) difficulty = "Hard";
            if (endStep == 61) difficulty = "Middle";
            if (endStep == 60) difficulty = "Easy";
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Tip")
                    .setMessage("YOU WIN!\n" +
                            "\tStep：" + numOfStep + "\n" +
                            "\tDifficulty: " + difficulty)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void goBack() {//后退函数
        if (step == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Tip")
                    .setMessage("You cant go back because you are in the start")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });//起点不能后退
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (step == -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Tip")
                    .setMessage("You cant go back because you dont start")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });//还未开始不能后退
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            numOfStep++;
            tvNumOfStep.setText(numOfStep + "");//后退，操作步数增加
            step--;
            tvStepInfo.setText(step  + "");//真实步数减1
            for (int i = 0; i < 64; i++) {
                btChess[i].setOnClickListener(null);
                if (!ChessPoint.chessPoint[i].isClicked) {
                    btChess[i].setBackgroundColor(ChessPoint.chessPoint[i].initial);
                }
            }//后退前的准备，初始化棋盘，为每个棋盘点去除动作监听事件并设置好各自的颜色
            for (int i = 0; i < 64; i++) {
                int j = -1;// 初值设为1，不要设为0，不然会出现bug
                if (ChessPoint.chessPoint[i].isClicked)
                    j = ChessPoint.chessPoint[i].order;//获得每个点的顺序信息
                if (j == step+1) {//如果当前点的顺序正好等于步数，则当前点则为要后退的点
                    btChess[i].setBackgroundColor(ChessPoint.chessPoint[i].initial);
                    ChessPoint.chessPoint[i].setClicked(false);
                    btChess[i].setImageResource(0);
                    ChessPoint.chessPoint[i].setOrder(-2);
                }
            }
            for (int i = 0; i < 64; i++) {
                int j = -1;
                if (ChessPoint.chessPoint[i].isClicked)
                    j = ChessPoint.chessPoint[i].order;
                if (j == step ) {//处理当前点后退到上一个点的逻辑
                    btChess[i].setImageResource(R.drawable.prince);
                    ChessPoint.chessPoint[i].setClicked(true);
                    int x = 0, y = 0;
                    for (int m = 0; m < 8; m++)
                        for (int n = 0; n < 8; n++) {
                            if (n * 8 + m == i) {
                                x = m;
                                y = n;
                            }
                        }
                    for (int a = 0; a < 8; a++) {
                        int b = x + Knight.directionX[a];
                        int c = y + Knight.directionY[a];
                        if (b < 8
                                && b >= 0
                                && c >= 0
                                && c < 8
                                && !ChessPoint.chessPoint[c * 8 + b].isClicked) {
                            btChess[c * 8 + b]
                                    .setOnClickListener(this);//只允许黄色的格子被点击
                            btChess[c * 8 + b]
                                    .setBackgroundColor(Color.YELLOW);
                        }
                    }
                }
            }
        }
    }
}





