package org.bitbucket.sunrise.maneuver.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import org.bitbucket.sunrise.maneuver.ManeuverGame;
import org.bitbucket.sunrise.maneuver.game.ScoreManager;

/**
 * Created by takahawk on 29.03.16.
 */
public class ScoreScreen implements Screen {
    private static final int WIDTH = 1184 / 3;
    private static final int HEIGHT = 720 / 3;
    private static final float RATIO = (float) WIDTH / HEIGHT;

    public ManeuverGame game;
    public Stage stage = new Stage(new FitViewport(
            WIDTH,
            (int) (WIDTH / RATIO)
    ));
    static {
        if (!VisUI.isLoaded())
            VisUI.load();
    }

    public ScoreScreen(ManeuverGame game) {
        this.game = game;
        initUI();
    }

    public void initUI() {
        ScoreManager scoreManager = game.getScoreManager();
        VisWindow table = new VisWindow("HIGH SCORES");
        table.setFillParent(true);
        table.setMovable(false);
        table.setFillParent(true);
        int number = 1;
        for (ScoreManager.ScoreResult sr : scoreManager.getScores()) {
            if (sr == null)
                break;
            table.add(new VisLabel(Integer.toString(number)));
            table.add(new VisLabel(sr.name));
            table.add(new VisLabel(Integer.toString(sr.score))).padLeft(100);
            table.row();
            number++;
        }
        VisTextButton toMenu = new VisTextButton("MENU");
        toMenu.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        table.add().expand();
        table.row();
        table.left();
        table.add(toMenu);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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
