import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// ---------------------------- MODEL ----------------------------
class GameModel {
    final int width;
    final int height;

    final int HAMSTER_W = 40;
    final int HAMSTER_H = 40;
    int hamsterX;
    double hamsterY;
    double hamsterVy;
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

    Random random = new Random();

    public GameModel(int width, int height) {
        this.width = width;
        this.height = height;
        reset();
    }

    public void reset() {
        hamsterX = 60;
        groundY = height - 50;
        hamsterY = groundY - HAMSTER_H;
        hamsterVy = 0;
        onGround = true;
        obstacles.clear();
        obstacleSpeed = 6;
        spawnCounter = 0;
        nextSpawnIn = 60 + random.nextInt(80);
        paused = false;
        gameOver = false;
        score = 0;
    }

    public void jump() {
        if (gameOver || paused) return;
        if (onGround) {
            hamsterVy = JUMP_POWER;
            onGround = false;
        }
    }

    // called by controller on each tick
    public void update() {
        if (paused || gameOver) return;

        updateHamsterPhysics();
        updateObstacles();
        spawnObstaclesIfNeeded();
        increaseDifficulty();
        detectCollisions();

        score++;
    }

    private void updateHamsterPhysics() {
        hamsterVy += GRAVITY;
        hamsterY += hamsterVy;

        if (hamsterY >= groundY - HAMSTER_H) {
            hamsterY = groundY - HAMSTER_H;
            hamsterVy = 0;
            onGround = true;
        }
    }

    private void updateObstacles() {
        Iterator<Rectangle> iterator = obstacles.iterator();

        while (iterator.hasNext()) {
            Rectangle obstacle = iterator.next();
            obstacle.x -= obstacleSpeed;

            if (obstacle.x + obstacle.width < 0) {
                iterator.remove();
                score += 10;
            }
        }
    }

    private void spawnObstaclesIfNeeded() {
        spawnCounter++;

        if (spawnCounter >= nextSpawnIn) {
            spawnCounter = 0;
            nextSpawnIn = spawnIntervalMin + random.nextInt(spawnIntervalMax - spawnIntervalMin + 1);
            spawnObstacle();
        }
    }

    private void increaseDifficulty() {
        if (score > 0 && score % 200 == 0) {
            obstacleSpeed = 6 + (int)(score / 200);
            if (obstacleSpeed > 18) obstacleSpeed = 18;
        }
    }

    private void detectCollisions() {
        Rectangle hamsterRect = new Rectangle(hamsterX, (int)Math.round(hamsterY), HAMSTER_W, HAMSTER_H);

        for (Rectangle obstacle : obstacles) {
            if (hamsterRect.intersects(obstacle)) {
                gameOver = true;
                paused = true;
                break;
            }
        }
    }



    //создаёт новый кактус
    private void spawnObstacle() {
        // Randomize obstacle size
        int w = 20 + random.nextInt(30);
        int h = 30 + random.nextInt(40);
        int x = width + 20;
        int y = groundY - h;
        Rectangle r = new Rectangle(x, y, w, h);
        obstacles.add(r);
    }


    public Rectangle getHamsterBounds() {
        return new Rectangle(hamsterX, (int) Math.round(hamsterY), HAMSTER_W, HAMSTER_H);
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
