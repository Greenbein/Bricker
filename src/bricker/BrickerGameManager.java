package bricker;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import bricker.heart_strategies.AddingHeartStrategy;
import bricker.heart_strategies.StaticHeartStrategy;
import bricker.heart_strategies.HeartCollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import static bricker.ManagerConstants.*;


public class BrickerGameManager extends GameManager {
    private Vector2 windowDimensions;
    private Renderable brickImage;
    private Renderable heartImage;
    private Renderable paddleImage;
    private Renderable ballImage;
    private Renderable puckImage;
    private Renderable turboBallImage;
    private GameObject heartsCounter;
    private GameObject userPaddle;
    private GameObject[] heartsBarArray;
    private final float userNumOfRows;
    private final float userNumOfBricks;
    private Ball ball;
    private WindowController windowController;
    private int hearts;
    private Counter bricksCounter;
    private UserInputListener userInputListener;
    private  Sound collisionSound;
    private boolean extraPaddleExists;
    private boolean isTurbo;


    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.userNumOfRows = DEFAULT_ROWS_AMOUNT;
        this.userNumOfBricks = DEFAULT_BRICKS_AMOUNT;
        this.hearts = START_MAX_ATTEMPTS;
        this.bricksCounter =
                new Counter((int)(this.userNumOfRows*this.userNumOfBricks));
        this.isTurbo = false;

    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              float userNumOfRows, float userNumOfBricks) {
        super(windowTitle, windowDimensions);
        this.userNumOfRows = userNumOfRows;
        this.userNumOfBricks = userNumOfBricks;
        this.hearts = START_MAX_ATTEMPTS;
        this.bricksCounter
                = new Counter((int)(this.userNumOfRows*this.userNumOfBricks));
        this.isTurbo = false;
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader,
                soundReader,
                inputListener,
                windowController);
        this.extraPaddleExists = false;
        this.userInputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        windowController.setTargetFramerate(70);

        Renderable backgroundImage = imageReader.readImage(ASSETS_DIR+BACKGROUND_DIR,false);
        collisionSound = soundReader.readSound(ASSETS_DIR+COLLISION_SOUND_DIR);
        ballImage = imageReader.readImage(ASSETS_DIR+BALL_DIR,true);
        brickImage = imageReader.readImage(ASSETS_DIR+BRICK_DIR, true);
        heartImage = imageReader.readImage(ASSETS_DIR+HEART_DIR, true);
        paddleImage = imageReader.readImage(ASSETS_DIR+PADDLE_DIR,true);
        puckImage = imageReader.readImage(ASSETS_DIR+MOCK_BALL_DIR,true);
        turboBallImage = imageReader.readImage(ASSETS_DIR+RED_BALL_DIR,true);

        //Creating ball
        ball = new Ball(Vector2.ZERO,
                new Vector2(DEFAULT_BALL_SIZE,DEFAULT_BALL_SIZE),
                ballImage,collisionSound);
        ball.setTag(MAIN_BALL_TAG);
        isTurbo = false;
        spawnBall();


        heartsBarArray = new GameObject[MAX_ATTEMPTS];
        //Create user paddle
        userPaddle = new UserPaddle(
                Vector2.ZERO,
                new Vector2(100,15),
                paddleImage,
                inputListener,
                windowController.getWindowDimensions().x()
        );
        userPaddle.setCenter(new Vector2(windowDimensions.x()/2,windowDimensions.y()-30));
        userPaddle.setTag(ORIGINAL_PADDLE);
        gameObjects().addGameObject(userPaddle);


        //Creating walls
        createWalls(windowController);

        //Creating background
        GameObject background = new Background(Vector2.ZERO,windowDimensions,backgroundImage);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);

        //creating bricks
        buildBricks( this.userNumOfBricks,this.userNumOfRows);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        pressW();
        checkForGameEnd();
        createHeartsCounter();
        createHeartsBar();
        deleteObjectsOutOfFrame();
        offTurbo();
    }

    private void spawnBall(){
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()){
            ballVelX *= -1;
        }
        if(rand.nextBoolean()){
            ballVelY *= -1;
        }
        if(!isTurbo) {
            ball.setVelocity(new Vector2(ballVelX, ballVelY));
            ball.renderer().setRenderable(ballImage);
        }
        else{
            ball.setVelocity(new Vector2(ballVelX, ballVelY).mult(1.4f));
            ball.renderer().setRenderable(turboBallImage);
        }
        ball.setCenter(windowDimensions.mult(HALF));
        this.gameObjects().addGameObject(ball);
    }







    // this function creates the hearts bar at the bottom of the screen
    private void createHeartsBar(){
        deleteHeartsBar();
        HeartCollisionStrategy basicCollisionStrategy =
                new StaticHeartStrategy(this);
        float startX = WALL_WIDTH + HEARTS_COUNTER_MARGIN*2 + HEART_COUNTER_SIZE;
        for(int i =0; i<this.hearts; i++){
            GameObject heart = new Heart(
                    new Vector2(startX, this.windowDimensions.y() - HEART_SIZE - WALL_HEIGHT),
                    new Vector2(HEART_SIZE, HEART_SIZE),
                    heartImage,
                    basicCollisionStrategy,
                    null) {
            };
            this.heartsBarArray[i] = heart;
            startX+=HEART_SIZE+SPACE_BETWEEN_HEARTS;
            this.gameObjects().addGameObject(heart,Layer.UI);
        }
    }

    // this function implements the counter of the hearts graphically
    private void createHeartsCounter(){
        gameObjects().removeGameObject(heartsCounter,Layer.UI);
        TextRenderable graphicHeartsCounter = new TextRenderable(String.valueOf(this.hearts));
        switch(this.hearts){
            case 1:
                graphicHeartsCounter.setColor(Color.RED);
                break;
            case 2:
                graphicHeartsCounter.setColor(Color.YELLOW);
                break;
            default:
                graphicHeartsCounter.setColor(Color.GREEN);
        }
        heartsCounter = new GameObject
                (new Vector2(WALL_WIDTH + HEARTS_COUNTER_MARGIN,
                        this.windowDimensions.y()- HEART_SIZE - WALL_HEIGHT),
                        new Vector2(HEART_COUNTER_SIZE,HEART_COUNTER_SIZE),
                        graphicHeartsCounter
                );
        this.gameObjects().addGameObject(heartsCounter,Layer.UI);
    }

    public void increaseAmountOfHearts() {
        if(this.hearts<MAX_ATTEMPTS){
            this.hearts++;
        }
    }

    private void deleteHeartsBar(){
        for (GameObject gameObject : this.heartsBarArray) {
            if (gameObject != null) {
                this.gameObjects().removeGameObject(gameObject, Layer.UI);
            }
        }
    }

    public void createFallingHeart(float x, float y){
        HeartCollisionStrategy addingHeartStrategy =
                new AddingHeartStrategy(this);
        GameObject heart = new Heart(
                new Vector2(x, y),
                new Vector2(HEART_SIZE,HEART_SIZE),
                heartImage,
                addingHeartStrategy,
                userPaddle);
        heart.setVelocity(Vector2.DOWN.mult(FALLING_HEART_SPEED));
        heart.setTag(ORIGINAL_PADDLE);
        this.gameObjects().addGameObject(heart);
    }







    private void createWalls(WindowController windowController) {
        float screenWidth = windowController.getWindowDimensions().x();
        float screenHeight = windowController.getWindowDimensions().y();
        GameObject topWall = new Wall(Vector2.ZERO,
                new Vector2(screenWidth,WALL_HEIGHT),null);
        GameObject leftWall = new Wall(Vector2.ZERO,
                new Vector2(WALL_WIDTH,screenHeight),null);
        GameObject rightWall = new Wall(new Vector2(screenWidth-WALL_WIDTH,0),
                new Vector2(WALL_WIDTH,screenHeight),null);
        this.gameObjects().addGameObject(topWall,Layer.STATIC_OBJECTS);
        this.gameObjects().addGameObject(leftWall,Layer.STATIC_OBJECTS);
        this.gameObjects().addGameObject(rightWall,Layer.STATIC_OBJECTS);
    }







    private void buildBricks(float amountOfBricksInRow,
                             float amountOfRows){
        float screenWidth = windowDimensions.x();
        float numberOfSpaces = (amountOfBricksInRow+1);
        float brickWidth = (screenWidth-
                2*WALL_WIDTH-
                numberOfSpaces*SPACE_BETWEEN_BRICKS) /amountOfBricksInRow;
        CollisionStrategyFactory factory = new CollisionStrategyFactory(this);
        float startY = 2*WALL_WIDTH;
        for(int row = 0;row<amountOfRows;row++){
            float startX = WALL_WIDTH+SPACE_BETWEEN_BRICKS;
            for(int brickIndex = 0;brickIndex<amountOfBricksInRow;brickIndex++){
                CollisionStrategy strategy = factory.getCollisionStrategy();
                GameObject brick = new Brick(new Vector2(startX,startY),
                        new Vector2(brickWidth,BRICK_HEIGHT),
                        brickImage,
                        strategy
                );
                //we have to use brick's tag in order to decrease the number of bricks, when we remove the brick from
                // gameObjects list
                brick.setTag(BRICK_TAG);
                this.gameObjects().addGameObject(brick,Layer.STATIC_OBJECTS);
                startX+=brickWidth+SPACE_BETWEEN_BRICKS;
            }
            startY+=BRICK_HEIGHT+SPACE_BETWEEN_BRICKS;
        }
    }

    public void reduceAmountOfBricks(){
        this.bricksCounter.decrement();
    }










    public void spawnPuckBalls(float x, float y){
        Random rand = new Random();
        for(int i =0;i<PUCK_BALLS_AMOUNT_AFTER_COLLISION;i++){
            Ball puck = new Ball(new Vector2(x,y),
                    new Vector2(DEFAULT_BALL_SIZE,DEFAULT_BALL_SIZE).mult(PUCK_BALL_SPEED_FACTOR),
                    puckImage, collisionSound);
            puck.setTag(PUCK_BALL_TAG);
            double angle = rand.nextDouble()*Math.PI;
            float velocityX = (float)Math.cos(angle)*BALL_SPEED;
            float velocityY = (float)Math.sin(angle)*BALL_SPEED;
            puck.setVelocity(new Vector2(velocityX,velocityY));
            gameObjects().addGameObject(puck);
        }
    }







    public void createExtraPaddle(){
        if(!this.extraPaddleExists){
            UserPaddle extraPaddle = new AddPaddle(this.windowDimensions.mult(HALF),
                    new Vector2(PADDLE_WIDTH,PADDLE_HEIGHT),
                    paddleImage,
                    userInputListener,
                    windowController.getWindowDimensions().x(),this);
            extraPaddle.setCenter(new Vector2(windowDimensions.x(),windowDimensions.y()).mult(HALF));
            gameObjects().addGameObject(extraPaddle);
            this.extraPaddleExists = true;
        }
    }

    public void setExtraPaddleExists(boolean exists){
        this.extraPaddleExists = exists;
    }






    private void pressW(){
        if(this.userInputListener.isKeyPressed(KeyEvent.VK_W)) {
            String prompt = VICTORY+NEXT_GAME_QUESTION;
            if(windowController.openYesNoDialog(prompt)){
                this.hearts = START_MAX_ATTEMPTS;
                this.bricksCounter = new Counter((int)(this.userNumOfRows*this.userNumOfBricks));
                windowController.resetGame();
            }
            else{
                windowController.closeWindow();
            }

        }

    }







    public void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt = EMPTY_STRING;
        if(ballHeight>windowDimensions.y()){
            //we lost
            if(this.hearts>1){
                this.hearts--;
                ball.setCenter(new Vector2(this.windowDimensions.x(),
                        this.windowDimensions.y()).mult(HALF));
                spawnBall();
            }
            else{
                prompt = DEFEAT;
            }
        }
        else if(this.bricksCounter.value() == 0){
            prompt = VICTORY;
        }
        if(!prompt.isEmpty()){
            prompt += NEXT_GAME_QUESTION;
            if(windowController.openYesNoDialog(prompt)){
                this.hearts = START_MAX_ATTEMPTS;
                this.bricksCounter = new Counter((int)(this.userNumOfRows*this.userNumOfBricks));
                windowController.resetGame();
            }
            else{
                windowController.closeWindow();
            }
        }
    }








    public void onTurbo(){
        if(!isTurbo) {
            ball.setCollisionCounter(0);
            isTurbo = true;
            ball.setVelocity(ball.getVelocity().mult(TURBO_BALL_SPEED_FACTOR));
            ball.renderer().setRenderable(turboBallImage);
        }
    }

    public void offTurbo(){
        if(isTurbo && ball.getCollisionCounter()>=6){
            isTurbo = false;
            ball.setCollisionCounter(0);
            ball.setVelocity(ball.getVelocity().mult(1/TURBO_BALL_SPEED_FACTOR));
            ball.renderer().setRenderable(ballImage);
        }
    }









    public void removeObject(GameObject object, int layerLevel){
        String tag = object.getTag();
        boolean deleted = this.gameObjects().removeGameObject(object,layerLevel);
        if(tag.equals(BRICK_TAG) && deleted){
                this.reduceAmountOfBricks();
            }

    }

    private boolean isObjectOutOfFrame(GameObject gameObject){
        return gameObject.getCenter().x()<0
                || gameObject.getCenter().x()>this.windowDimensions.x()
                ||gameObject.getCenter().y()<0
                || gameObject.getCenter().y()>this.windowDimensions.y();
    }

    public void deleteObjectsOutOfFrame(){
        Iterable<GameObject> objectInDefaultLayer= gameObjects().objectsInLayer(Layer.DEFAULT);
        for(GameObject gameObject: objectInDefaultLayer){
            if(isObjectOutOfFrame(gameObject)){
                removeObject(gameObject,Layer.DEFAULT);
            }
        }
    }






    public Vector2 getWindowDimensions(){
        return this.windowDimensions;
    }


    public static void main(String[] args) {
        BrickerGameManager g = null;
        if(args.length >= 2){
            if(args[0]!=null && args[1]!=null){
                float amountOfBricksInRow = Float.parseFloat(args[0]);
                float amountOfRows = Float.parseFloat(args[1]);
                g = new BrickerGameManager("Bouncing ball",
                        new Vector2(700,500),
                        amountOfRows,
                        amountOfBricksInRow);
            }
        }
        else{
            g = new BrickerGameManager("Bouncing ball",new Vector2(700,500));
        }
        g.run();
    }


}