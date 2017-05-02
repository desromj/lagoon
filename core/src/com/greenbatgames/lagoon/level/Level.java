package com.greenbatgames.lagoon.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.lagoon.collision.LagoonContactListener;
import com.greenbatgames.lagoon.hud.ChaseCam;
import com.greenbatgames.lagoon.hud.GameHUD;
import com.greenbatgames.lagoon.physics.NewPhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.QueuedWorld;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.util.Constants;

import box2dLight.RayHandler;

/**
 * Created by Quiv on 23-01-2017.
 */
public class Level implements Disposable {

    public static final String TAG = Level.class.getSimpleName();

    QueuedWorld world;
    Stage stage;

//    RayHandler rayHandler;
    ChaseCam camera;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    Player player;
    GameHUD gameHUD;

    Box2DDebugRenderer debugRenderer;

    public Level() {
        init();
    }

    public void init() {
        player = null;

        world = new QueuedWorld(Constants.GRAVITY, true);
        world.world().setContactListener(new LagoonContactListener());

        // Register the chase camera and set it to the stage
        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, cam));
        this.camera = new ChaseCam(cam, player);
        stage.addActor(this.camera);

        // Handle Box2D light initialization
//        rayHandler = new RayHandler(world.world());
//        rayHandler.setAmbientLight(0.2f);

        gameHUD = new GameHUD();
        debugRenderer = new Box2DDebugRenderer();
    }

    public void render(float delta) {

        // Update logic
        world.step(delta);
        stage.act(delta);

        // Prepare viewports and projection matricies
        stage.getViewport().apply();
        tiledMapRenderer.setView(camera.getCamera());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the level
        tiledMapRenderer.render();
        stage.draw();

        // Render the Box2D Debug view
        debugRenderer.render(
                world.world(),
                stage.getCamera().combined.cpy().scale(
                        Constants.PTM,
                        Constants.PTM,
                        0
                )
        );

        // Lighting: Scale the rayHandler to Box2D values
//        rayHandler.setCombinedMatrix(stage.getViewport().getCamera().combined.cpy().scale(
//                Constants.PTM,
//                Constants.PTM,
//                1));
//        rayHandler.updateAndRender();

        gameHUD.act(delta);

        // Render HUDs after the main game world
        stage.getBatch().begin();
        gameHUD.draw(stage.getBatch(), 1f);
        stage.getBatch().end();
    }

    // Getters and Setters
    public Player getPlayer(){
        return player;
    }
//    public RayHandler getRayHandler(){
//        return rayHandler;
//    }
    public Viewport getViewport() {
        return stage.getViewport();
    }
    public World getWorld(){
        return world.world();
    }

    public void setPlayer(Player player) {
        this.player = player;
        camera.setTarget(player);
    }

    public void queuePhysicsBodyToAdd(NewPhysicsBody newPhysicsBody) { world.addToAdd(newPhysicsBody); }
    public void queuePhysicsBodyToRemove(Body body) { world.addToRemove(body); }
    public void queuePhysicsBodyToRemove(PhysicsBody physicsBody) { world.addToRemove(physicsBody.getBody()); }

    public void addActorToStage(Actor actor) { stage.addActor(actor); }

    // When loading a level, set the TiledMap reference so the level can render it later
    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    /*
    public LagoonLight addLight(float x, float y){
        LagoonLight light = new LagoonLight(x, y, rayHandler, world);
        lights.add(light);
        stage.addActor(light);
        return light;
    }
    */

    // Dispose lights and actor to prepare for next level
    @Override
    public void dispose() {
//        rayHandler.removeAll();
//        rayHandler.dispose();

        for (Actor actor: stage.getActors()) {
            if (actor instanceof Disposable){
                ((Disposable) actor).dispose();
            }
        }
    }
}
