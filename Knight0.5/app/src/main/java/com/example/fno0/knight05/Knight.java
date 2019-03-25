package com.example.fno0.knight05;

public class Knight {
    private int positionX, positionY;// 当前坐标
    private static int nextX, nextY;// 下一步坐标
    final static int[] directionX = {2, 1, -1, -2, -2, -1, 1, 2};
    final static int[] directionY = {-1, -2, -2, -1, 1, 2, 2, 1};// 骑士走法，每个区域最多有8种走法
    private boolean used[] = new boolean[8];// 定义当前区域的下一步可能到达的区域是否被占用
    static int chess[][] = new int[8][8];// 棋盘64个区域
    private final static int[][] access = {{2, 3, 4, 4, 4, 4, 3, 2},
            {3, 4, 6, 6, 6, 6, 4, 3}, {4, 6, 8, 8, 8, 8, 6, 4},
            {4, 6, 8, 8, 8, 8, 6, 4}, {4, 6, 8, 8, 8, 8, 6, 4},
            {4, 6, 8, 8, 8, 8, 6, 4}, {3, 4, 6, 6, 6, 6, 4, 3},
            {2, 3, 4, 4, 4, 4, 3, 2}};// 棋盘每一个区域下一步可到达多少个区域的矩阵（不考虑下一步区域是否被占用）

    public Knight(int x, int y) {// 构造方法
        for (int i = 0; i < used.length; i++)
            used[i] = false;// 默认当前区域的下一步可能的到达的区域都没有被占用
        this.positionX = x;
        this.positionY = y;
    }

    public void init(int chess[][]) {// 初始化当前区域(每走一步都需要初始化当前区域)，在8种可能的走法中筛选出当前区域的下一步已被占用的区域
        for (int k = 0; k < 8; k++) {
            nextX = positionX + directionX[k];
            nextY = positionY + directionY[k];// 下一步可能的走法，即下一步可能到达的区域，总共有8种
            if (nextX >= 8 || nextX < 0 || nextY < 0 || nextY >= 8
                    || chess[nextX][nextY] != -1) {
                used[k] = true;// 第k种走法被使用
            }
        }
    }

    public boolean getNext(int access[][]) {// 判断能否到达下一区域,并筛选出access值较小的区域
        boolean reach = false;// 默认不能到达下一区域
        int maxAccess = 10, choiceK = 0;// 任意指定一个大于8的数maxAccess
        for (int k = 0; k < 8; k++) {
            if (used[k] == false) {
                // 注意：如果没有以下的if语句，程序运行很慢
                if (maxAccess > access[this.positionX + directionX[k]][this.positionY
                        + directionY[k]]) { // 如果有可以到达的区域，则筛选最佳的一个区域，即access值最小的区域
                    maxAccess = access[this.positionX + directionX[k]][this.positionY
                            + directionY[k]];// 下一步选择走法最少的区域，即先走边缘，再走中间，这样到了后期才有更多选择
                    choiceK = k;// 选择第k种走法
                    reach = true;// 可以达到下一区域
                }
            }
        }
        if (reach == false) {
            return false;// 如果不能到达，返回false
        } else {
            Knight.nextX = this.positionX + directionX[choiceK];
            Knight.nextY = this.positionY + directionY[choiceK];
            used[choiceK] = true;
            return true;// 如果可以到达，返回true，并选择第choiceK种走法，生成nextX值和nextY值
        }
    }

    public static int[][] move(int startX, int startY) {// 开始走，回溯算法
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                chess[i][j] = -1;// 8*8棋盘的二维数组每个元素值默认设置为-1
        int step = 0;// 步数，初始值为0
        Knight[] knights = new Knight[64];// 64个棋子
        chess[startX][startY] = step;// 8*8棋盘的二维数组每个元素值设置为对应的步数
        knights[step] = new Knight(startX, startY);// Knight[0]设置为起点
        knights[step].init(chess);// 初始化起点区域
        while (true) {
            if (step >= 63) {// 63步走完棋盘所有区域
                break;
            } else {
                if (knights[step].getNext(access)) {// 如果能够到达下一区域
                    nextX = knights[step].getNextX();
                    nextY = knights[step].getNextY();// 变为当前区域的下一区域
                    ++step;// 步数加一
                    chess[nextX][nextY] = step;// 当前区域下一步区域的值设置为其对应的步数
                    knights[step] = new Knight(nextX, nextY);// 以下一区域的坐标new一个Knight
                    knights[step].init(chess);// 初始化下一步的区域
                } else {// 如果不能到达下一区域
                    chess[knights[step].getPositionX()][knights[step]
                            .getPositionY()] = -1;// 当前区域重新定义为没有被占用
                    knights[step] = null;// 除去当前位置区域中的步数值
                    --step;// 步数减一
                }
            }
        }
        return chess;// 返回一个走法矩阵
    }

    // 各种getter和setter
    public int getNextX() {// 获得当前区域下一步到达的区域的横坐标
        return nextX;
    }

    public int getNextY() {// 获得当前区域下一步到达的区域的纵坐标
        return nextY;
    }

    public int getPositionX() {// 获得当前区域的横坐标
        return positionX;
    }

    public int getPositionY() {// 获得当前区域的纵坐标
        return positionY;
    }
}

