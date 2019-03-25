package com.example.fno0.knight05;


import android.graphics.Color;

public class ChessPoint {
    int order;//该区域是棋子经过的第几个区域
    int initial;//该区域的初始颜色
    boolean isClicked;//该区域是否被走过
    static ChessPoint chessPoint[] = new ChessPoint[64];

    public ChessPoint(int order, int initial, boolean isClicked) {
        super();
        this.order = order;
        this.initial = initial;
        this.isClicked = isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public static void createChess() {
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                chessPoint[i] = new ChessPoint(-2, Color.BLACK, false);
            } else {
                chessPoint[i] = new ChessPoint(-2, Color.WHITE, false);
            }
        }
        for (int i = 8; i < 16; i++) {
            if (i % 2 == 0) {
                chessPoint[i] = new ChessPoint(-2, Color.WHITE, false);
            } else {
                chessPoint[i] = new ChessPoint(-2, Color.BLACK, false);
            }
        }
        for (int i = 16; i < 24; i++) {
            if (i % 2 == 0) {
                chessPoint[i] = new ChessPoint(-2, Color.BLACK, false);
            } else {
                chessPoint[i] = new ChessPoint(-2, Color.WHITE, false);
            }
        }
        for (int i = 24; i < 32; i++) {
            if (i % 2 == 0) {
                chessPoint[i] = new ChessPoint(-2, Color.WHITE, false);
            } else {
                chessPoint[i] = new ChessPoint(-2, Color.BLACK, false);
            }
        }
        for (int i = 32; i < 40; i++) {
            if (i % 2 == 0) {
                chessPoint[i] = new ChessPoint(-2, Color.BLACK, false);
            } else {
                chessPoint[i] = new ChessPoint(-2, Color.WHITE, false);
            }
        }
        for (int i = 40; i < 48; i++) {
            if (i % 2 == 0) {
                chessPoint[i] = new ChessPoint(-2, Color.WHITE, false);
            } else {
                chessPoint[i] = new ChessPoint(-2, Color.BLACK, false);
            }
        }
        for (int i = 48; i < 56; i++) {
            if (i % 2 == 0) {
                chessPoint[i] = new ChessPoint(-2, Color.BLACK, false);
            } else {
                chessPoint[i] = new ChessPoint(-2, Color.WHITE, false);
            }
        }
        for (int i = 56; i < 64; i++) {
            if (i % 2 == 0) {
                chessPoint[i] = new ChessPoint(-2, Color.WHITE, false);
            } else {
                chessPoint[i] = new ChessPoint(-2, Color.BLACK, false);
            }
        }

    }


}

