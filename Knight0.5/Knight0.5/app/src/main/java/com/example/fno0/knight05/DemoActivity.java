package com.example.fno0.knight05;

import android.app.Activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DemoActivity extends Activity implements View.OnClickListener {
    ImageButton btChess[] = new ImageButton[64];
    Button btLast;
    Button btNext;
    Button btRestart;
    Button btHelp;
    Button btGame;
    Button btDemo;
    TextView tvStep;
    TextView tvStepInfo;
    TextView tvRestart;
    TextView tvHelp;
    Context mContext;
    LinearLayout acDemo;
    int step;

    int mat[][];//存放回溯结果矩阵

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mContext = this;
        findView();
        setClickListener();
        setGestureListener();
        ChessPoint.createChess();
        showHelp();
    }

    private void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Tip")
                .setMessage("\t1.这是“演示”界面，你可以通过点击“Game”切换到“游戏”界面\n" +
                        "\t2.首先你需要在棋盘里点击一个格子作为起点\n" +
                        "\t3.程序会按照国际象棋“马走日字”的规则走完整个棋盘\n" +
                        "\t4.游戏底部四个按钮分别是“后退”、“前进”、“重新开始”、“查看帮助”\n")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void findView() {
        tvStep = (TextView) this.findViewById(R.id.tv_Step);
        tvStepInfo = (TextView) this.findViewById(R.id.tv_StepInfo);
        btGame = (Button) this.findViewById(R.id.bt_Game);
        btDemo = (Button) this.findViewById(R.id.bt_Demo);
        btLast = (Button) this.findViewById(R.id.bt_Last);
        btNext = (Button) this.findViewById(R.id.bt_Next);
        btRestart = (Button) this.findViewById(R.id.bt_Restart);
        btHelp = (Button) this.findViewById(R.id.bt_Help);
        tvRestart = (TextView) this.findViewById(R.id.tv_Restart);
        tvHelp = (TextView) this.findViewById(R.id.tv_Help);
        acDemo = (LinearLayout) this.findViewById(R.id.activity_demo);
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
        btNext.setOnClickListener(this);
        btHelp.setOnClickListener(this);
        btRestart.setOnClickListener(this);
        btGame.setOnClickListener(this);
        btDemo.setOnClickListener(this);
        btDemo.setClickable(false);
    }

    public void onClick(View v) {
        step = Integer.parseInt(tvStepInfo.getText().toString());//当前的步数
        //放置棋子
        int index = R.id.bt_0;//第一个btChess即btChess[0]在系统中的位置
        int[] idOfChess = new int[64];//存储所有btChess的在系统中的位置信息
        for (int i = 0; i < 64; i++) {
            idOfChess[i] = index++;
        }
        for (int i = 0; i < 64; i++) {
            if (v.getId() == idOfChess[i]) {//btChess[i]被点击
                setStart(i);
            }
            for (int iOthers = 0; iOthers < 64; iOthers++) {
                btChess[iOthers].setOnClickListener(null);//放置了起点后，棋盘其他地方就不能再被电击，所以移除棋盘的动作监听事件
            }
        }
        //前进
        if (v.getId() == R.id.bt_Next) {
            goAhead();
        }
        //后退
        if (v.getId() == R.id.bt_Last) {
            goBack();
        }
        //帮助信息
        if (v.getId() == R.id.bt_Help) {
            for (int i = 0; i < 64; i++) {
                btChess[i].setOnClickListener(this);
            }
            //照理说不应该加这段话，但是如果不加，在Demo界面先点了帮助就不能再放置棋子
            showHelp();
        }
        //重新开始
        if (v.getId() == R.id.bt_Restart) {
            Intent intent = new Intent(this, DemoActivity.class);
            startActivity(intent);
            finish();
        }
        //跳转到Game界面
        if (v.getId() == R.id.bt_Game) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setGestureListener() {
        acDemo.setOnTouchListener(new View.OnTouchListener() {
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
                        if (x2 - x1 > 0 && (Math.abs(x2 - x1) > 25) && (Math.abs(y2 - y1) < Math.abs(x2 - x1))) {
                            step = Integer.parseInt(tvStepInfo.getText().toString());//当前的步数
                            goAhead();//右划前进
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void setStart(int i) {//放置起点函数
        step++;
        tvStepInfo.setText(step  + "");//步数由-1变为0
        ChessPoint.chessPoint[i].setClicked(true);//此刻当前位置已被点击
        btChess[i].setBackgroundColor(Color.BLUE);//设置当前点击的btChess的背景颜色
        btChess[i].setImageResource(R.drawable.prince);//设置当前点击的btChess的前景图片
        int x = 0, y = 0;//左上角作为原点
        for (int m = 0; m < 8; m++)
            for (int n = 0; n < 8; n++) {
                if (n * 8 + m == i) {
                    x = m;
                    y = n;
                }
            }//处理btChess在棋盘中的位置即坐标与i的逻辑关系
        mat = Knight.move(x, y);//调用Knight中的回溯算法，并用mat存储最终的结果矩阵
        for (int l = 0; l < 8; l++)
            for (int j = 0; j < 8; j++) {
                ChessPoint.chessPoint[j * 8 + l].setOrder(mat[l][j]);//记录所有点的顺序
            }
    }

    public void goAhead() {//前进函数
        if (step == 63) {//到达终点
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Tip")
                    .setMessage("You have reached the end")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (step == -1) {//还未选择起点，不能开始
            for (int i = 0; i < 64; i++) {
                btChess[i].setOnClickListener(this);
            }
            //照理说不应该加这段话，但是如果不加，在Demo界面先点了前进就不能再放置棋子
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Tip")
                    .setMessage("You need choose a start")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            step++;
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (mat[i][j] == step) {//mat矩阵对应的下一步
                        ChessPoint.chessPoint[j * 8 + i].setClicked(true);
                        btChess[j * 8 + i].setImageResource(R.drawable.prince);
                    }
                    if (mat[i][j] == step-1) {//mat矩阵对应的当前位置
                        btChess[j * 8 + i].setImageResource(0);
                        btChess[j * 8 + i].setBackgroundColor(Color.BLUE);
                    }
                }
            tvStepInfo.setText(step  + "");
        }
    }

    public void goBack() {//后退函数
        if (step == 0) {//起点不能后退
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Tip")
                    .setMessage("You cant go back because you are in the start")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (step == -1) {//还未选择起点，不能后退
            for (int i = 0; i < 64; i++) {
                btChess[i].setOnClickListener(this);
            }
            //照理说不应该加这段话，但是如果不加，在Demo界面先点了后退就不能再放置棋子

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Tip")
                    .setMessage("You need choose a start")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            step--;
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (mat[i][j] == step) {//mat矩阵当前位置的前一点
                        btChess[j * 8 + i].setBackgroundColor(ChessPoint.chessPoint[j * 8 + i].initial);
                        btChess[j * 8 + i].setImageResource(R.drawable.prince);
                    }
                    if (mat[i][j] == step+1) {//mat矩阵当前位置
                        btChess[j * 8 + i].setImageResource(0);
                        btChess[j * 8 + i].setBackgroundColor(ChessPoint.chessPoint[j * 8 + i].initial);
                    }
                    tvStepInfo.setText(step + "");
                }
        }
    }
}





