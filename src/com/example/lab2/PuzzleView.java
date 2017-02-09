package com.example.lab2;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.example.lab2.model.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static com.example.lab2.Constants.*;

/**
 * Created by Raluca on 16-Feb-16.
 * Parts of code from CE881 Lab, Spyros Samothrakis
 */

public class PuzzleView extends View implements View.OnTouchListener {
    //color constants

    static int bg = Color.rgb(142,226,215);
    static Map<Integer,Bitmap> spriteMap, overlayMap, background, backgroundV;
    static Bitmap borderH, borderV;
    static double mult = 7.5;
    static int divide = 8;
    public static int gridSize;

    static String tag = "Puzzle view: ";
    static boolean set = false;

    static CornerPuzzleModel model;
    static int size; //size of grid cell
    static int n; //height of grid
    static int m; //width of grid

    //offsets
    int xOff, yOff, minWidth, minHeight, gridWidth, gridHeight;

    public PuzzleView(Context context) {
        super(context);
        setup();
    }

    public PuzzleView(Context context, CornerPuzzleModel model) {
        super(context);
        PuzzleView.model = model;
        setup();
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public void recreate() {
        checkModel();
    }

    private void setup() {
        //System.out.println(tag + cons);


        // initialise sprites
        if (!set) { // load resources only once to improve performance
            set = true;

            spriteMap = new TreeMap<Integer, Bitmap>();
            overlayMap = new TreeMap<Integer, Bitmap>();
            background = new TreeMap<Integer, Bitmap>();
            backgroundV = new TreeMap<Integer, Bitmap>();

            Bitmap playerSprite = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
            Bitmap grassSprite = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
            Bitmap grassSprite2 = BitmapFactory.decodeResource(getResources(), R.drawable.grass_2);
            Bitmap grassSprite3 = BitmapFactory.decodeResource(getResources(), R.drawable.grass_3);
            Bitmap rockSprite = BitmapFactory.decodeResource(getResources(), R.drawable.big_rock);
            Bitmap rockSprite3 = BitmapFactory.decodeResource(getResources(), R.drawable.big_rock_3);
            Bitmap pebbleSprite = BitmapFactory.decodeResource(getResources(), R.drawable.rocks);
            Bitmap pebbleSprite3 = BitmapFactory.decodeResource(getResources(), R.drawable.rocks_3);
            Bitmap leverOnSprite = BitmapFactory.decodeResource(getResources(), R.drawable.lever_on);
            Bitmap leverOffSprite = BitmapFactory.decodeResource(getResources(), R.drawable.lever_off);
            Bitmap dirt = BitmapFactory.decodeResource(getResources(), R.drawable.dirt);
            Bitmap dirt2 = BitmapFactory.decodeResource(getResources(), R.drawable.dirt_2);
            Bitmap dirt3 = BitmapFactory.decodeResource(getResources(), R.drawable.dirt_3);
            Bitmap tree = BitmapFactory.decodeResource(getResources(), R.drawable.tree);
            Bitmap levelExit = BitmapFactory.decodeResource(getResources(), R.drawable.exit);
            Bitmap switchOFF = BitmapFactory.decodeResource(getResources(), R.drawable.switch_off);
            Bitmap switchON = BitmapFactory.decodeResource(getResources(), R.drawable.switch_on);
            Bitmap house = BitmapFactory.decodeResource(getResources(), R.drawable.house2);
            Bitmap shed = BitmapFactory.decodeResource(getResources(), R.drawable.shed2);
            Bitmap nails = BitmapFactory.decodeResource(getResources(), R.drawable.nails);
            Bitmap flower = BitmapFactory.decodeResource(getResources(), R.drawable.flower);
            Bitmap st = BitmapFactory.decodeResource(getResources(), R.drawable.string);
            Bitmap widget = BitmapFactory.decodeResource(getResources(), R.drawable.widget);
            Bitmap obj = BitmapFactory.decodeResource(getResources(), R.drawable.obj_1);
            Bitmap hole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
            Bitmap char1 = BitmapFactory.decodeResource(getResources(), R.drawable.char1);

            borderH = BitmapFactory.decodeResource(getResources(), R.drawable.border_h_1);
            borderV = BitmapFactory.decodeResource(getResources(), R.drawable.border_v);

            Bitmap bg0 = BitmapFactory.decodeResource(getResources(), R.drawable.background_0);
            Bitmap bg1 = BitmapFactory.decodeResource(getResources(), R.drawable.background_1);
            Bitmap bg2 = BitmapFactory.decodeResource(getResources(), R.drawable.background_2);
            Bitmap bgv0 = BitmapFactory.decodeResource(getResources(), R.drawable.background_v_0);
            Bitmap bgv1 = BitmapFactory.decodeResource(getResources(), R.drawable.background_v_1);
            Bitmap bgv2 = BitmapFactory.decodeResource(getResources(), R.drawable.background_v_2);

            Bitmap intro = BitmapFactory.decodeResource(getResources(), R.drawable.intro);
            Bitmap level2 = BitmapFactory.decodeResource(getResources(), R.drawable.level2);
            Bitmap level3 = BitmapFactory.decodeResource(getResources(), R.drawable.level3);
            Bitmap end = BitmapFactory.decodeResource(getResources(), R.drawable.end);

            spriteMap.put(TYPE_PLAYER, playerSprite);
            spriteMap.put(TYPE_GRASS, grassSprite);
            spriteMap.put(TYPE_GRASS_2, grassSprite2);
            spriteMap.put(TYPE_GRASS_3, grassSprite3);
            spriteMap.put(TYPE_ROCK, rockSprite);
            spriteMap.put(TYPE_ROCK_3, rockSprite3);
            spriteMap.put(TYPE_ROCKS, pebbleSprite);
            spriteMap.put(TYPE_ROCKS_3, pebbleSprite3);
            spriteMap.put(TYPE_LEVER_OFF, leverOffSprite);
            spriteMap.put(TYPE_LEVER_ON, leverOnSprite);
            spriteMap.put(TYPE_DIRT, dirt);
            spriteMap.put(TYPE_DIRT_2, dirt2);
            spriteMap.put(TYPE_DIRT_3, dirt3);
            spriteMap.put(TYPE_TREE, tree);
            spriteMap.put(TYPE_LEVEL_EXIT, levelExit);
            spriteMap.put(TYPE_SWITCH_OFF, switchOFF);
            spriteMap.put(TYPE_SWITCH_ON, switchON);
            spriteMap.put(TYPE_HOUSE, house);
            spriteMap.put(TYPE_SHED, shed);
            spriteMap.put(TYPE_NAILS, nails);
            spriteMap.put(TYPE_FLOWER, flower);
            spriteMap.put(TYPE_STRING, st);
            spriteMap.put(TYPE_WIDGET, widget);
            spriteMap.put(TYPE_HOLE, hole);
            spriteMap.put(TYPE_OBJ1, obj);
            spriteMap.put(TYPE_CHAR1, char1);

            overlayMap.put(0, intro);
            overlayMap.put(1, level2);
            overlayMap.put(2, level3);
            overlayMap.put(3, end);

            background.put(0, bg0);
            background.put(1, bg1);
            background.put(2, bg2);
            background.put(3, bg0);

            backgroundV.put(0, bgv0);
            backgroundV.put(1, bgv1);
            backgroundV.put(2, bgv2);
            backgroundV.put(3, bgv0);

            size = playerSprite.getWidth();
        }

        //System.out.println("Density: " + playerSprite.getDensity());

        checkModel();
        setOnTouchListener(this);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void checkModel() {
        m = 1; n = 1;
        if (model != null) {
            if (getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                n = model.modelH.n;
                m = model.modelH.m;
            } else if (getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                n = model.modelV.n;
                m = model.modelV.m;
            }
        } else {
            //this is an emergency: no model was made
            System.out.println(tag + "no model");

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point s = new Point();
            display.getSize(s);
            int w = s.x;
            int h = s.y;
            //gridSize = (w > h ? h : w ) / SPRITE_SIZE;
            gridSize = GRID_SIZE;
            System.out.println("GRID: " + gridSize);

            if (getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                m = gridSize;
                n = m/2;
            } else if (getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                n = gridSize;
                m = n/2;
            }
            model = new CornerPuzzleModel(gridSize, this);
        }
    }


    int getOrientation() {
        return getResources().getConfiguration().orientation;
    }


    float curX, curY;


    public void onCustomClick(View view) {
        // use x, y from the onTouch event, assume geometry set
        int i = (int) ((curX - xOff) / size);
        int j = (int) ((curY - yOff) / size);
        model.execute(j, i, getOrientation(), this);
        //model.update(getOrientation());

        postInvalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        curX = motionEvent.getX();
        curY = motionEvent.getY();

        //return false to ensure event helps make an onClick event
        return false;
    }

    private void setGeometry() {
        minWidth = getWidth();
        minHeight = getHeight();

        //size of individual tiles
        //size = minHeight / n;

        //grid square len = size of grid in pixels X axis
        gridWidth = size * m;
        gridHeight = size * n;
        xOff = (getWidth() - gridWidth) / 4;
        yOff = (getHeight() - gridHeight) / 2;
    }

    public void draw (Canvas g) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(bg);
        setGeometry();
        model.updateModels(this);
        int orientation = getOrientation();

        //draw view background
        //g.drawRect(0, 0, getWidth(), getHeight(), p);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            try {
                g.drawBitmap(Bitmap.createScaledBitmap(backgroundV.get(model.level), getWidth(), getHeight(), true), 0, 0, p);
            } catch (Exception e) {
                System.exit(1);
            }
        } else {
            try {
                g.drawBitmap(Bitmap.createScaledBitmap(background.get(model.level), getWidth(), getHeight(), true), 0, 0, p);
            } catch (Exception e) {
                System.exit(1);
            }
        }

        //draw map

        PuzzleModel pm;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            pm = model.modelH;
        }
        else {
            pm = model.modelV;
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int cx = xOff + size * i + size / 2 + i;
                int cy = yOff + size * j + size / 2 + j;

                //draw background
                if (pm.layers.get(0)[j][i] != null) {
                    drawTile(g, cx, cy, pm.layers.get(0)[j][i], p);
                }

                //draw objects
                if (pm.layers.get(1)[j][i] != null) {
                    drawObject(g, cx, cy, pm.layers.get(1)[j][i], p);
                }
            }
        }

        //draw player
        int x, y;
        int playerOrientation = model.getPlayerOrientation();
        if ((playerOrientation == 0 || playerOrientation == 2) && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Vector2D pos = model.translateToHorizontal(model.player.getPos().x, model.player.getPos().y);
            x = pos.x;
            y = pos.y;
        } else if ((playerOrientation == 1 || playerOrientation == 2) && orientation == Configuration.ORIENTATION_PORTRAIT) {
            Vector2D pos = model.translateToVertical(model.player.getPos().x, model.player.getPos().y);
            x = pos.x;
            y = pos.y;
        } else {
            return;
        }
        int cx = xOff + size * x + size / 2 + x - size/2;
        int cy = yOff + size * y + size / 2 + y - size;
        g.drawBitmap(spriteMap.get(model.player.id), cx, cy, p);

        //draw border
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            g.drawBitmap(Bitmap.createScaledBitmap(borderV, getWidth(), getHeight(), true), 0, 0, p);
        } else {
            g.drawBitmap(Bitmap.createScaledBitmap(borderH, getWidth(), getHeight(), true), 0, 0, p);
        }

        //Display other game information
        ArrayList<Collectible> inv = model.player.getItems();
        int off0, off1;
        double off2;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //display player inventory
            cx = (int) (getWidth() - size / 1.5);
            off0 = (int) (getHeight() / 4.4);
            off1 = (int) (getHeight() / 7);
            off2 = 1.15;
            drawInventory(g, inv, cx, off0, off1, off2, p);

            //display game state info
            cx = (int) (getWidth() * 10 / 11);
            cy = (int) (getHeight() * 7 / 8);
            drawGameStats(g, cx, cy, model.level + 1, orientation, p);

        } else {
            //display player inventory
            cx = (int) (getWidth() - size / 1.2);
            off0 = (int) (getHeight() / 4.5);
            off1 = 0;
            off2 = 1.23;
            drawInventory(g, inv, cx, off0, off1, off2, p);

            //display game state info
            cx = (int) (getWidth() * 7 / 8);
            cy = (int) (getHeight() * 6 / 8);
            drawGameStats(g, cx, cy, model.level + 1, orientation, p);
        }

        // display overlay
        if (model.nextLevel) {
            drawOverlay(g, p, model.level);
        }

    }

    private void drawOverlay (Canvas g, Paint p, int level) {
        //draw in the middle of the screen
        int w = getWidth();
        int h = getHeight();
        int min = w > h ? h : w;
        double scale = 0.75;
        Bitmap i = overlayMap.get(level);
        Bitmap img = Bitmap.createScaledBitmap(i, (int)(i.getWidth() * scale), (int)(i.getHeight() * scale), true);
        int x = (getWidth() - img.getWidth()) / 2;
        int y = (getHeight() - img.getHeight()) / 2;
        g.drawBitmap(img, x, y, p);
    }

    private void drawInventory (Canvas g, ArrayList<Collectible> inv, int cx, int off0, int off1, double off2, Paint p) {
        for (Collectible c : inv) {
            int i = inv.indexOf(c);
            int cy = (int) (off0 + size * off2 * i + size / 2);
            if (i > 1) cy += off1;
            //System.out.println("item: " + c.id);
            //cx = getWidth() / 2; cy = getHeight() / 2;
            drawObject(g, cx, cy, c, p);
        }
    }

    private void drawGameStats (Canvas g, int cx, int cy, int level, int o, Paint p) {
        p.setColor(Color.LTGRAY);
        if (o == Configuration.ORIENTATION_LANDSCAPE) {
            g.drawText("Current level: " + level, cx, cy, p);
        } else {
            g.drawText("Current", cx, cy, p);
            g.drawText("level:", cx, cy + getHeight() / 40, p);
            g.drawText("" + level, cx, cy + getHeight() / 20, p);
        }
    }

    // i & j : position in grid
    // cx & cy : position on screen
    private void drawTile (Canvas g, int cx, int cy, GameObject o, Paint p) {
        //System.out.println(tag + " cx, cy: " + cx + " : " + cy);
        int length = size;
        int x = cx - length / 2;
        int y = cy - length / 2;
        try {
            g.drawBitmap(spriteMap.get(o.id), x, y, p);
        } catch (Exception ignored) {}
    }

    private void drawObject (Canvas g, int cx, int cy, GameObject o, Paint p) {
        //System.out.println(tag + " cx, cy: " + cx + " : " + cy);
        int length = size;
        int x = cx - length / 2;
        int y = cy - length;
        if (o.id == TYPE_SWITCH_ON || o.id == TYPE_SWITCH_OFF || o.id == TYPE_LEVEL_EXIT ||
                o.id == TYPE_ROCKS || o.id == TYPE_ROCKS_3 || o.id == TYPE_HOLE)
            y = cy - length / 2;
        try {
            //System.out.println("Object id: " + o.id);
            g.drawBitmap(spriteMap.get(o.id), x, y, p);
        } catch (Exception ignored) {}
    }
}
