package org.cocos2d.tests;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItemFont;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.transitions.FlipXTransition;
import org.cocos2d.transitions.SlideInTTransition;
import org.cocos2d.transitions.TransitionScene;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor4B;
import org.cocos2d.events.CCTouchDispatcher;


public class SceneTest extends Activity {
    private static final String LOG_TAG = SceneTest.class.getSimpleName();
    private CCGLSurfaceView mGLSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mGLSurfaceView = new CCGLSurfaceView(this);
        setContentView(mGLSurfaceView);

        applicationDidFinishLaunching(this, mGLSurfaceView);
    }

    static class Layer1 extends CCLayer {

        public Layer1() {
            MenuItemFont item1 = MenuItemFont.item("Test pushScene", this, "onPushScene");
            MenuItemFont item2 = MenuItemFont.item("Test pushScene w/transition", this, "onPushSceneTran");
            MenuItemFont item3 = MenuItemFont.item("Quit", this, "onQuit");

            Menu menu = Menu.menu(item1, item2, item3);
            menu.alignItemsVertically();

            addChild(menu);
        }

        public void onPushScene() {
            Scene scene = Scene.node();
            scene.addChild(new Layer2(), 0);
            CCDirector.sharedDirector().pushScene(scene);
        }

        public void onPushSceneTran() {
            Scene scene = Scene.node();
            scene.addChild(new Layer2(), 0);
            CCDirector.sharedDirector().pushScene(SlideInTTransition.transition(1, scene));
        }


        public void onQuit() {
            CCDirector.sharedDirector().popScene();
        }

        public void onVoid() {
        }
    }

    static class Layer2 extends CCLayer {
        public Layer2() {
            MenuItemFont item1 = MenuItemFont.item("Replace Scene", this, "onReplaceScene");
            MenuItemFont item2 = MenuItemFont.item("Replace Scene Transition", this, "onReplaceSceneTransition");
            MenuItemFont item3 = MenuItemFont.item("Go Back", this, "onGoBack");

            Menu menu = Menu.menu(item1, item2, item3);
            menu.alignItemsVertically();

            addChild(menu);
        }

        public void onGoBack() {
            CCDirector.sharedDirector().popScene();
        }

        public void onReplaceScene() {
            Scene scene = Scene.node();
            scene.addChild(new Layer3(), 0);
            CCDirector.sharedDirector().replaceScene(scene);
        }

        public void onReplaceSceneTransition() {
            Scene s = Scene.node();
            s.addChild(new Layer3(), 0);
            CCDirector.sharedDirector().replaceScene(FlipXTransition.transition(2.0f, s, TransitionScene.Orientation.kOrientationLeftOver));
        }
    }

    static class Layer3 extends CCColorLayer {
        public Layer3() {
            super(new ccColor4B(0, 0, 255, 255));

            isTouchEnabled_ = true;

            CCLabel label = CCLabel.makeLabel("Touch to pop scene", "DroidSans", 32);
            addChild(label);
            float width = CCDirector.sharedDirector().winSize().width;
            float height = CCDirector.sharedDirector().winSize().height;
            label.setPosition(CGPoint.make(width / 2, height / 2));
        }

        @Override
        public boolean ccTouchesEnded(MotionEvent event) {
            CCDirector.sharedDirector().popScene();
            return CCTouchDispatcher.kEventHandled;
        }
    }


    // CLASS IMPLEMENTATIONS
    public void applicationDidFinishLaunching(Context context, View view) {

        // attach the OpenGL view to a window
        CCDirector.sharedDirector().attachInView(view);

        // set landscape mode
        CCDirector.sharedDirector().setLandscape(false);

        // show FPS
        CCDirector.sharedDirector().setDisplayFPS(false);

        // frames per second
        CCDirector.sharedDirector().setAnimationInterval(1.0f / 60);

        Scene scene = Scene.node();
        scene.addChild(new Layer1(), 0);

        // Make the Scene active
        CCDirector.sharedDirector().runWithScene(scene);

    }

    // getting a call, pause the game
    public void applicationWillResignActive(Context context) {
        CCDirector.sharedDirector().pause();
    }

    // call got rejected
    public void applicationDidBecomeActive(Context context) {
        CCDirector.sharedDirector().resume();
    }

    // purge memroy
    public void applicationDidReceiveMemoryWarning(Context context) {
        CCTextureCache.sharedTextureCache().removeAllTextures();
    }

    // next delta time will be zero
    public void applicationSignificantTimeChange(Context context) {
        //	Director.sharedDirector().setNextDeltaTimeZero(true);
    }
}