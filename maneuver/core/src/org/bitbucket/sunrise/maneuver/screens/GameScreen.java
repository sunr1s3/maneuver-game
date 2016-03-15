package org.bitbucket.sunrise.maneuver.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.bitbucket.sunrise.maneuver.game.GameWorld;
import org.bitbucket.sunrise.maneuver.game.Plane;
import org.bitbucket.sunrise.maneuver.game.Rocket;
import org.bitbucket.sunrise.maneuver.render.PhysicsActor;

/**
 * Created by takahawk on 07.03.16.
 */
public class GameScreen implements Screen {
    private static final float ROCKET_FORCE = 100f;
    private GameWorld world;
    private GameWorld.DebugRenderer debugRenderer;
    private GameWorld.GameBody plane;
    private GameWorld.GameBody rocket;
    private Stage stage = new Stage();
    private OrthographicCamera cam;

    private SpriteBatch batch;
//    private Plane plane;
//    private Rocket rocket;
    private TextureRegion rocketTexture = new TextureRegion(new Texture(Gdx.files.internal("rocket.png")));
    private TextureRegion planeTexture = new TextureRegion(new Texture(Gdx.files.internal("plane.png")));
    private Texture background = new Texture(Gdx.files.internal("background.png"));

//    private Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound.mp3"));

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        world = new GameWorld(new Vector2(0, 0), 0.01f);
        debugRenderer = world.getDebugRenderer();
        plane = world.addRectangularBody(
                new Vector2(400, 300),
                planeTexture.getRegionWidth(),
                planeTexture.getRegionHeight()
        );
        plane.setLinearVelocity(0, 200);
        rocket = world.addRectangularBody(
                new Vector2(200, 100),
                rocketTexture.getRegionWidth(),
                rocketTexture.getRegionHeight()
        );
        stage.addActor(new PhysicsActor(plane, planeTexture));
        stage.addActor(new PhysicsActor(rocket, rocketTexture));
        Actor backgroundActor = new Actor() {

            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.draw(background, getX(), getY(), background.getWidth(), background.getHeight());
            }
        };
        stage.addActor(backgroundActor);
        backgroundActor.toBack();
        cam = (OrthographicCamera) stage.getViewport().getCamera();

        world.addContactHandler(plane, rocket, new GameWorld.ContactListener() {
            @Override
            public void beginContact() {
                System.out.println("KABOOM!");
            }

            @Override
            public void endContact() {

            }
        });
    }

    public void handleTouch() {
        if(Gdx.input.getX() < Gdx.graphics.getWidth() / 2) {
            plane.rotateVelocity(1f);
        }
        if(Gdx.input.getX() > Gdx.graphics.getWidth() / 2) {
            plane.rotateVelocity(-1f);
        }
    }

    public void handleInput() {
        if(Gdx.input.isTouched()) {
            handleTouch();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // sound.play();
            plane.rotateVelocity(1f);
            System.out.println(getCameraAngle(cam));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            plane.rotateVelocity(-1f);
            System.out.println(getCameraAngle(cam));
        }
    }

    private float getCameraAngle(Camera cam) {
        return (float) Math.atan2(cam.up.x, cam.up.y) * MathUtils.radiansToDegrees;
    }

    private void updateCam() {
        cam.position.x = plane.getPosition().x;
        cam.position.y = plane.getPosition().y;
        cam.rotate(180f - plane.getVelocityAngle() - 90f - getCameraAngle(cam));
        cam.update();
    }

    public void update(float delta) {
        world.update();
        rocket.applyForce(
                plane.getPosition().sub(rocket.getPosition()).setLength(ROCKET_FORCE),
                true
        );
        updateCam();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        handleInput();
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        Matrix4 debugMatrix = cam.combined.cpy().scale(1 / world.getScale(), 1 / world.getScale(), 0);
        debugRenderer.render(debugMatrix);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
