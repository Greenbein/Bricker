package bricker.main;

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
import static bricker.main.ManagerConstants.*;


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
    private ImageReader imageReader;
    private SoundReader soundReader;
    private Ball ball;
    private WindowController windowController;
    private int hearts;
    private Counter bricksCounter;
    private UserInputListener userInputListener;
    private  Sound collisionSound;
    private boolean extraPaddleExists;
    private boolean isTurbo;

    /**
     * constructor for gameBrickerManager with default values
     * @param windowTitle game's name
     * @param windowDimensions the size of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.userNumOfRows = DEFAULT_ROWS_AMOUNT;
        this.userNumOfBricks = DEFAULT_BRICKS_AMOUNT;
        this.hearts = START_MAX_ATTEMPTS;
        this.bricksCounter =
                new Counter((int)(this.userNumOfRows*this.userNumOfBricks));
        this.isTurbo = false;

    }

    /**
     * constructor for gameBrickerManager with row and col values
     * @param windowTitle game's name
     * @param windowDimensions the size of the window
     * @param userNumOfRows given number of rows
     * @param userNumOfBricks given number of bricks in each row
     */
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

    /**
     *
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
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
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController.setTargetFramerate(FPS);
        extractAssetsFiles();
        createBall();
        spawnBall();
        heartsBarArray = new GameObject[MAX_ATTEMPTS];
        createPaddle();
        createWalls(windowController);
        createBackground();
        createBricks( this.userNumOfBricks,this.userNumOfRows);
    }

    // applying all images and audio files from assets
    private void extractAssetsFiles(){
        this.collisionSound = this.soundReader.readSound(ASSETS_DIR+COLLISION_SOUND_DIR);
        this.ballImage = this.imageReader.readImage(ASSETS_DIR+BALL_DIR,true);
        this.brickImage = this.imageReader.readImage(ASSETS_DIR+BRICK_DIR, true);
        this.heartImage = this.imageReader.readImage(ASSETS_DIR+HEART_DIR, true);
        this.paddleImage = this.imageReader.readImage(ASSETS_DIR+PADDLE_DIR,true);
        this.puckImage = this.imageReader.readImage(ASSETS_DIR+MOCK_BALL_DIR,true);
        this.turboBallImage = this.imageReader.readImage(ASSETS_DIR+RED_BALL_DIR,true);
    }

    // create ball
    private void createBall(){
        this.ball = new Ball(Vector2.ZERO,
                new Vector2(DEFAULT_BALL_SIZE,DEFAULT_BALL_SIZE),
                this.ballImage,this.collisionSound);
        this.ball.setTag(MAIN_BALL_TAG);
        this.isTurbo = false;
    }

    //spawns ball on the screen
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
        if(!this.isTurbo) {
            this.ball.setVelocity(new Vector2(ballVelX, ballVelY));
            this.ball.renderer().setRenderable(ballImage);
        }
        else{
            this.ball.setVelocity(new Vector2(ballVelX, ballVelY).mult(TURBO_BALL_SPEED_FACTOR));
            this.ball.renderer().setRenderable(turboBallImage);
        }
        this.ball.setCenter(windowDimensions.mult(HALF));
        this.gameObjects().addGameObject(ball);
    }

    // create paddle
    private void createPaddle(){
        this.userPaddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH,PADDLE_HEIGHT),
                this.paddleImage,
                this.userInputListener,
                this.windowController.getWindowDimensions().x()
        );
        this.userPaddle.setCenter(new Vector2(this.windowDimensions.x()/2,
                this.windowDimensions.y()-30));
        this.userPaddle.setTag(ORIGINAL_PADDLE);
        gameObjects().addGameObject(this.userPaddle);
    }

    // create background
    private void createBackground(){
        Renderable backgroundImage = this.imageReader.readImage(ASSETS_DIR
                + BACKGROUND_DIR,false);
        GameObject background = new Background(Vector2.ZERO,
                this.windowDimensions,backgroundImage);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     *  update game every window time of delta
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
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

    // create the hearts bar at the bottom of the screen
    private void createHeartsBar(){
        deleteHeartsBar();
        HeartCollisionStrategy basicCollisionStrategy =
                new StaticHeartStrategy();
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

    // implement the counter of the hearts graphically
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
        this.heartsCounter = new GameObject
                (new Vector2(WALL_WIDTH + HEARTS_COUNTER_MARGIN,
                        this.windowDimensions.y()- HEART_SIZE - WALL_HEIGHT),
                        new Vector2(HEART_COUNTER_SIZE,HEART_COUNTER_SIZE),
                        graphicHeartsCounter
                );
        this.gameObjects().addGameObject(this.heartsCounter,Layer.UI);
    }

    /**
     *  increases amount of hearts in brickerGameManager
     */
    public void increaseAmountOfHearts() {
        if(this.hearts<MAX_ATTEMPTS){
            this.hearts++;
        }
    }

    // every time window delta we delete hearts bar
    // in update
    private void deleteHeartsBar(){
        for (GameObject gameObject : this.heartsBarArray) {
            if (gameObject != null) {
                this.gameObjects().removeGameObject(gameObject, Layer.UI);
            }
        }
    }

    /**
     * create falling heart
     * @param x coordinate x
     * @param y coordinate y
     */
    public void createFallingHeart(float x, float y){
        HeartCollisionStrategy addingHeartStrategy =
                new AddingHeartStrategy(this);
        GameObject heart = new Heart(
                new Vector2(x, y),
                new Vector2(HEART_SIZE,HEART_SIZE),
                this.heartImage,
                addingHeartStrategy,
                this.userPaddle);
        heart.setVelocity(Vector2.DOWN.mult(FALLING_HEART_SPEED));
        heart.setTag(ORIGINAL_PADDLE);
        this.gameObjects().addGameObject(heart);
    }

    // create walls
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

    // creating bricks
    private void createBricks(float amountOfBricksInRow,
                             float amountOfRows){
        float screenWidth = this.windowDimensions.x();
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
                        this.brickImage,
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

    /**
     * reduce amount of breaks
     */
    public void reduceAmountOfBricks(){
        this.bricksCounter.decrement();
    }

    /**
     * Spawn two puck balls in given coordinates
     * @param x - x coordinate
     * @param y - y coordinate
     */
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

    /**
     * create extra paddle after we hit the bricks with AddPaddle strategy
     */
    public void createExtraPaddle(){
        if(!this.extraPaddleExists){
            Paddle extraPaddle = new AddPaddle(new Vector2(this.windowDimensions).mult(HALF),
                    new Vector2(PADDLE_WIDTH,PADDLE_HEIGHT),
                    this.paddleImage,
                    this.userInputListener,
                    this.windowController.getWindowDimensions().x(),this);
            extraPaddle.setCenter(new Vector2(this.windowDimensions).mult(HALF));
            gameObjects().addGameObject(extraPaddle);
            this.extraPaddleExists = true;
        }
    }

    /**
     * Update extraPaddleExists parameter
     * we use it in order to avoid creating two or more extra paddles on the screen game
     * @param exists
     */
    public void setExtraPaddleExists(boolean exists){
        this.extraPaddleExists = exists;
    }

    /**
     * Finish the game after we press W button
     * After that we win
     */
    private void pressW(){
        if(this.userInputListener.isKeyPressed(KeyEvent.VK_W)) {
            String prompt = VICTORY+NEXT_GAME_QUESTION;
            if(this.windowController.openYesNoDialog(prompt)){
                this.hearts = START_MAX_ATTEMPTS;
                this.bricksCounter = new Counter((int)(this.userNumOfRows*this.userNumOfBricks));
                this.windowController.resetGame();
            }
            else{
                this.windowController.closeWindow();
            }

        }

    }

    /**
     * Check if we have won or not
     * if hearts amount greater than 1 and there is no bricks -> victory
     * if hearts = 0 and there are still some bricks -> defeat
     */
    public void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt = EMPTY_STRING;
        if(ballHeight>this.windowDimensions.y()){
            //we lost
            if(this.hearts>1){
                this.hearts--;
                this.ball.setCenter(new Vector2(this.windowDimensions).mult(HALF));
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
            if(this.windowController.openYesNoDialog(prompt)){
                this.hearts = START_MAX_ATTEMPTS;
                this.bricksCounter = new Counter((int)(this.userNumOfRows*this.userNumOfBricks));
                this.windowController.resetGame();
            }
            else{
                this.windowController.closeWindow();
            }
        }
    }

    /**
     * Change player ball characteristics after we hit brick with TurboBallStrategy
     * Its speed multiplied by 1.4f
     * We change it color to red
     * isTubro variable = true (by this way we turn on turbo mode only when te ball is green
     */
    public void onTurbo(){
        if(!isTurbo) {
            this.ball.setCollisionCounter(0);
            this.isTurbo = true;
            this.ball.setVelocity(ball.getVelocity().mult(TURBO_BALL_SPEED_FACTOR));
            this.ball.renderer().setRenderable(turboBallImage);
        }
    }

    /**
     * Set the ball settings be original
     */
    public void offTurbo(){
        if(isTurbo && ball.getCollisionCounter()>=6){
            this.isTurbo = false;
            this.ball.setCollisionCounter(0);
            this.ball.setVelocity(ball.getVelocity().mult(1/TURBO_BALL_SPEED_FACTOR));
            this.ball.renderer().setRenderable(ballImage);
        }
    }

    /**
     * remove object from gameObjects(). If the object is brick,
     * then we also should to decrease the amount of hearts
     * @param object - the object we want to remove
     * @param layerLevel - its layer
     */
    public void removeObject(GameObject object, int layerLevel){
        String tag = object.getTag();
        boolean deleted = this.gameObjects().removeGameObject(object,layerLevel);
        if(tag.equals(BRICK_TAG) && deleted){
                this.reduceAmountOfBricks();
            }

    }
    //check if an object is out of game frame
    private boolean isObjectOutOfFrame(GameObject gameObject){
        return gameObject.getCenter().x()<0
                || gameObject.getCenter().x()>this.windowDimensions.x()
                ||gameObject.getCenter().y()<0
                || gameObject.getCenter().y()>this.windowDimensions.y();
    }
    /**
     * Delete all objects that exit the game screen frame
     */
    public void deleteObjectsOutOfFrame(){
        Iterable<GameObject> objectInDefaultLayer= gameObjects().objectsInLayer(Layer.DEFAULT);
        for(GameObject gameObject: objectInDefaultLayer){
            if(isObjectOutOfFrame(gameObject)){
                removeObject(gameObject,Layer.DEFAULT);
            }
        }
    }


    /**
     * main method
     * if we get two parameters from user about bricks amount in row and row amount,we will enter the if
     * in other case we will use default value parameters
     * @param args - input array from user
     */
    public static void main(String[] args) {
        BrickerGameManager g = null;
        if(args.length >= PARAMETERS_AMOUNT){
            if(args[0]!=null && args[1]!=null){
                float amountOfBricksInRow = Float.parseFloat(args[0]);
                float amountOfRows = Float.parseFloat(args[1]);
                g = new BrickerGameManager(GAME_NAME,
                        new Vector2(SCREEN_WIDTH,SCREEN_HEIGHT),
                        amountOfRows,
                        amountOfBricksInRow);
            }
        }
        else{
            g = new BrickerGameManager(GAME_NAME,new Vector2(SCREEN_WIDTH,SCREEN_HEIGHT));
        }
        g.run();
    }


}