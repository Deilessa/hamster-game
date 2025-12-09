import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// ---------------------------- MODEL ----------------------------
class GameModel {
    final int width;
    final int height;

    final int DINO_W = 40;
    final int DINO_H = 40;
    int dinoX;
    double dinoY;
    double dinoVy;
    boolean onGround;

    //физика прыжка
    final double GRAVITY = 0.9;
    final double JUMP_POWER = -14;

    //препятствичя
    ArrayList<Rectangle> obstacles = new ArrayList<>();
    int obstacleSpeed = 6;
    int spawnCounter = 0;
    int spawnIntervalMin = 60; // frames
    int spawnIntervalMax = 140;
    int nextSpawnIn = 80;

    int groundY;

    //вызывает рестарт
    boolean paused = false;
    boolean gameOver = false;
    long score = 0; // score increases with time

    Random rnd = new Random();

    public GameModel(int width, int height) {
        this.width = width;
        this.height = height;
        reset();
    }

    public void reset() {
        dinoX = 60;
        groundY = height - 50;
        dinoY = groundY - DINO_H;
        dinoVy = 0;
        onGround = true;
        obstacles.clear();
        obstacleSpeed = 6;
        spawnCounter = 0;
        nextSpawnIn = 60 + rnd.nextInt(80);
        paused = false;
        gameOver = false;
        score = 0;
    }

    public void jump() {
        if (gameOver || paused) return;
        if (onGround) {
            dinoVy = JUMP_POWER;
            onGround = false;
        }
    }

    // called by controller on each tick
    public void update() {
        if (paused || gameOver) return;

        // Physics update
        dinoVy += GRAVITY;
        dinoY += dinoVy;
        if (dinoY >= groundY - DINO_H) { //Если коснулся земли — остановить, вернуть в точку
            dinoY = groundY - DINO_H;
            dinoVy = 0;
            onGround = true;
        }

        // Obstacles movement
        Iterator<Rectangle> it = obstacles.iterator();
        while (it.hasNext()) {
            Rectangle r = it.next();
            r.x -= obstacleSpeed;
            if (r.x + r.width < 0) { //Ушли за экран → удалить + дать очки
                it.remove();
                score += 10;
            }
        }

        // Spawn logic
        spawnCounter++;
        if (spawnCounter >= nextSpawnIn) {
            spawnCounter = 0;
            nextSpawnIn = spawnIntervalMin + rnd.nextInt(spawnIntervalMax - spawnIntervalMin + 1);
            spawnObstacle();
        }

        //увеличение сложности с ростом очков
        if (score > 0 && score % 200 == 0) {
            obstacleSpeed = 6 + (int) (score / 200);
            if (obstacleSpeed > 18) obstacleSpeed = 18;
        }


score++;


        Rectangle dinoRect = new Rectangle(dinoX, (int) Math.round(dinoY), DINO_W, DINO_H);
        for (Rectangle r : obstacles) {
            if (dinoRect.intersects(r)) {
                gameOver = true;
                paused = true;//проигрыш при столкновении
                break;
            }
        }
    }

//создаёт новый кактус
    private void spawnObstacle() {
        // Randomize obstacle size
        int w = 20 + rnd.nextInt(30);
        int h = 30 + rnd.nextInt(40);
        int x = width + 20;
        int y = groundY - h;
        Rectangle r = new Rectangle(x, y, w, h);
        obstacles.add(r);
    }


    public Rectangle getDinoBounds() {
        return new Rectangle(dinoX, (int) Math.round(dinoY), DINO_W, DINO_H);
    }

    public ArrayList<Rectangle> getObstacles() {
        return obstacles;
    }

    public int getGroundY() {
        return groundY;
    }

    public boolean isPaused() {
        return paused;
    }

    public void togglePause() {
        if (gameOver) return; // can't unpause when game over (use restart)
        paused = !paused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public long getScore() {
        return score;
    }

    public void setGameOver(boolean v) {
        gameOver = v;
    }
}
