package sample.engine;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import sample.*;
import sample.utility.*;
import sample.primitives.*;

import java.util.ArrayList;
import java.util.List;

public class Engine {
    private static boolean ACTIVE;
    private static Timeline timeline;

    public static List<MonoBehaviour> entities = new ArrayList<>();
    public static List<Transform> selectedObjects = new ArrayList<>();

    public static Vector2 cameraPosition = new Vector2(400,300);
    public static float cameraView = 1f;

    private static List<Vector2> lastPositions = new ArrayList<>(), lastScales = new ArrayList<>();
    private static List<Float> lastRotations = new ArrayList<>();
    private static Vector2 middlePoint; private static float startDistance; private static float lastDistance = 0;
    private static Vector2 userMidPoint = null;
    private static boolean[] actions = new boolean[3];

    /* =================================================================== */

    public static void Init() {
        System.out.println("Engine started!");

        Object k = Instantiate("CapitalK");
        Char c = k.AddComponent(Char.class);
        c.c = Controller.CapitalK.c; c.paths = Controller.CapitalK.paths;

        k.transform.scale.Mul(5);
        k.transform.position.Add(350,300);

        Object nine = Instantiate("Nine");
        c = nine.AddComponent(Char.class);
        c.c = Controller.Nine.c; c.paths = Controller.Nine.paths;

        nine.transform.scale.Mul(5);
        nine.transform.position.Add(450,300);

        Object plus = Instantiate("Plus");
        c = plus.AddComponent(Char.class);
        c.c = Controller.Plus.c; c.paths = Controller.Plus.paths;

        plus.transform.scale.Mul(5);
        plus.transform.rotation = 22.5f;

        Object line = Instantiate("Line");
        c = line.AddComponent(Char.class);
        c.c = Controller.Line.c; c.paths = Controller.Line.paths;

        line.transform.scale.Mul(5);
        line.transform.rotation = 45;

        Object line2 = Instantiate("Line");
        c = line2.AddComponent(Char.class);
        c.c = Controller.Line.c; c.paths = Controller.Line.paths;

        line2.transform.scale.Mul(5);
        line2.transform.rotation = 90;

        Object line3 = Instantiate("Line");
        c = line3.AddComponent(Char.class);
        c.c = Controller.Line.c; c.paths = Controller.Line.paths;

        line3.transform.scale.Mul(5);
        line3.transform.rotation = 0;

        Object minus = Instantiate("Minus");
        c = minus.AddComponent(Char.class);
        c.c = Controller.Minus.c; c.paths = Controller.Minus.paths;

        minus.transform.position.Add(500,200);
        minus.transform.rotation = 45f;

        timeline = new Timeline(new KeyFrame(Duration.millis(16.666f), event -> EngineUpdate()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private static void EngineUpdate() {
        //HandleInput();
        Draw();
    }

    //Vector2 deltaMove;
    //static float deltaScaler;

    public static void HandleInput(Vector2 currentMousePos, Vector2 lastMousePos) {
        if (actions[0]) {
            Vector2 delta = Vector2.Sub(ScreenToWorldPoint(currentMousePos), ScreenToWorldPoint(lastMousePos));
            //delta.y *= -1f;
            //System.out.println(delta.x+":"+ delta.y);
            for (Transform t:selectedObjects) {
                t.Translate(delta);
            }
            if (userMidPoint != null) userMidPoint.Add(delta);
        } else if (actions[1]) {
            middlePoint = CalcMiddlePoint();
            if (middlePoint == null) { ResetActions(); return; }
            Vector2 mouseWorld = ScreenToWorldPoint(new Vector2(currentMousePos));
            Vector2 middleWorld = userMidPoint == null ? ScreenToWorldPoint(middlePoint) : userMidPoint;
            //System.out.println(mouseWorld.y + " : " + middleWorld.y);
            float delta = Vector2.Distance(mouseWorld, ScreenToWorldPoint(new Vector2(lastMousePos)));
            float dist = Vector2.Distance(mouseWorld, middleWorld);
            float adder = (dist/startDistance)*delta*0.005f*cameraView; if (lastDistance > dist) adder*=-1;
            //deltaScaler += adder;
            //int i = 0;
            for (Transform t:selectedObjects) {
                Vector2 lastScale = new Vector2(t.scale);
                t.Translate(-middleWorld.x,-middleWorld.y);
                t.scale.Add(adder*t.scale.x);
                //Vector2 deltaFromMiddle = Vector2.Sub(t.transform.position, middleWorld); deltaFromMiddle.x *= (t.scale.x - lastScales.get(i).x); deltaFromMiddle.y *= (t.scale.y - lastScales.get(i).y);//deltaFromMiddle.Mul(adder*6*0.01f); //deltaFromMiddle.Mul(adder/deltaScaler);
                t.position.Mul(Vector2.Div(t.scale, lastScale));
                t.Translate(middleWorld);
                //i++;
            }
            lastDistance = dist;
        } else if (actions[2]) {
            middlePoint = CalcMiddlePoint();
            if (middlePoint == null) { ResetActions(); return; }
            Vector2 screenOffset = userMidPoint == null ? middlePoint : WorldToScreenPoint(userMidPoint);
            Vector2 middleWorld = userMidPoint == null ? ScreenToWorldPoint(middlePoint) : userMidPoint;
            Vector2 halfScreenSize = new Vector2((float)Controller.Instance.canvas.getWidth(), (float)Controller.Instance.canvas.getHeight()).Div(2f);
            screenOffset.Sub(halfScreenSize);
            //System.out.println(screenOffset.x + " : " + screenOffset.y);
            Vector2 nCurrent = Vector2.Sub(currentMousePos, halfScreenSize).Sub(screenOffset);//.Normalize();
            Vector2 nLast = Vector2.Sub(lastMousePos, halfScreenSize).Sub(screenOffset);//.Normalize();
            float angle = AngleBetweenLines(nLast,nCurrent);
            for (Transform t:selectedObjects) {
                t.position.Sub(middleWorld);
                t.rotation += Math.toDegrees(angle);
                if (t.rotation < 0f) t.rotation += 360f;
                if (t.rotation >= 360f) t.rotation -= 360f;
                //float angleInRads = angle;//(float)Math.toRadians(t.rotation);
                //System.out.println(angleInRads);
                float sin = (float)Math.sin(-angle), cos = (float)Math.cos(-angle);

                Vector2 lastPosition = new Vector2(t.position);
                t.position.x = lastPosition.x * cos - lastPosition.y * sin;
                t.position.y = lastPosition.x * sin + lastPosition.y * cos;

                t.position.Add(middleWorld);
            }
        }
    }

    public static float AngleBetweenLines(Vector2 first, Vector2 second) {
        double atanA = Math.atan2(first.x, first.y);
        double atanB = Math.atan2(second.x, second.y);

        return (float)(atanA - atanB);
    }

    public static void HandlePrimaryButton(MouseEvent e) {
        if (actions[0] || actions[1] || actions[2]) {
            lastPositions.clear(); lastScales.clear(); lastRotations.clear();
            ResetActions(); return;
        }
        if (e.isControlDown()) { MoveMiddlePoint(new Vector2((int)e.getSceneX(), (int)e.getSceneY())); return; }
        SelectObject(Controller.Instance.ToScreenV((int)e.getSceneX(), (int)e.getSceneY()), e.isShiftDown());
    }

    public static void HandleSecondaryButton(MouseEvent e) {
        if (actions[0] || actions[1] || actions[2]) { ResetActions(); return; }
        if (e.isControlDown() && userMidPoint != null) { userMidPoint = null; return; }
        if (e.isShiftDown()) Engine.ScaleSelected(new Vector2((float)e.getSceneX(),(float)e.getSceneY()));
        else Engine.GrabSelected();
    }

    private static void Draw() {
        Canvas canvas = Controller.Instance.canvas;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        for (MonoBehaviour o:entities) {
            Char c = o.GetComponent(Char.class);
            if (c == null) continue;
            //Controller.SetColor(new Color(Math.random()%1f,Math.random()%1f,Math.random()%1f,1f));

            Vector2 scaled = new Vector2(o.transform.scale); scaled.Mul(cameraView);

            Vector2 transformed = WorldToScreenPoint(new Vector2(o.transform.position));

            Controller.Instance.DrawChar(c, transformed, scaled, o.transform.rotation);
        }
        Vector2[] rect = CalcSelectedRect();
        if (rect != null) {
            Point leftDown = rect[0], rightUp = rect[1];
            if (selectedObjects.size() > 0) {
                Controller.SetColor(new Color(.5f, .5f, .5f, 1f));
                Controller.Instance.DrawRect(leftDown, rightUp);
            }
            Vector2 pos = userMidPoint == null ? Vector2.Add(rect[0], Vector2.Sub(rect[1], rect[0]).Div(2)) : WorldToScreenPoint(userMidPoint);
            Controller.SetColor(Controller.NightMode ? Color.YELLOW : Color.GREEN);
            Controller.Instance.DrawRect(pos.Sub(2,2), Vector2.Add(pos, new Vector2(4, 4)));
            if (Controller.NightMode) Controller.SetColor(Color.WHITE);
            else Controller.SetColor(Color.BLACK);
        }
    }

    private static void MoveMiddlePoint(Vector2 screenMousePosition) {
        //middlePoint = CalcMiddlePoint();
        screenMousePosition = Controller.Instance.ToScreenV((int)screenMousePosition.x, (int)screenMousePosition.y);
        //Vector2 middleWorld = ScreenToWorldPoint(middlePoint);
        System.out.println(screenMousePosition.x + " : " + screenMousePosition.y);
        //Vector2 worldPosition = ScreenToWorldPoint(screenMousePosition);
        userMidPoint = ScreenToWorldPoint(screenMousePosition);//= Vector2.Sub(worldPosition, middleWorld);
    }

    public static void SelectObject(Vector2 mousePosition, boolean add) {
        Vector2 mouseWorld = ScreenToWorldPoint(mousePosition);
        System.out.println(mousePosition.y);
        int i = 0;
        float lastDistance = Float.MAX_VALUE;
        MonoBehaviour current = null;
        for (MonoBehaviour m:entities) {
            float distance = Vector2.Distance(mouseWorld, m.transform.position);
            if (lastDistance > distance && distance <= m.transform.scale.x*12f/cameraView) {
                current = m;
                lastDistance = distance;
            }
            i++;
        }
        if (!add) {
            selectedObjects.clear();
        }
        if (current == null) return;
        if (selectedObjects.contains(current.transform)) {selectedObjects.remove(current.transform); if (lastPositions.size()>i) lastPositions.remove(i); if (lastScales.size()>i) lastScales.remove(i); }
        else { selectedObjects.add(current.transform); if (actions[0]) { lastPositions.add(new Vector2(current.transform.position)); lastScales.add(new Vector2(current.transform.scale)); } }
        //return;
    }

    public static void GrabSelected() {
        if (selectedObjects.size() < 1) return;
        System.out.println("Grabbed!");
        ResetActions();
        for (Transform t: selectedObjects)
            lastPositions.add(new Vector2(t.position));
        actions[0] = true;
    }

    public static void ScaleSelected(Vector2 mousePosition) {
        if (selectedObjects.size() < 1) return;
        System.out.println("Scaled!");
        ResetActions();
        middlePoint = CalcMiddlePoint();
        startDistance = Vector2.Distance(mousePosition, middlePoint);
        for (Transform t: selectedObjects) {
            lastScales.add(new Vector2(t.scale));
            lastPositions.add(new Vector2(t.position));
        }
        actions[1] = true;
    }

    public static void RotateSelected(Vector2 mousePosition) {
        if (selectedObjects.size() < 1) return;
        System.out.println("Rotated!");
        ResetActions();
        for (Transform t : selectedObjects) {
            lastPositions.add(new Vector2(t.position));
            lastRotations.add(t.rotation);
        }
        if (userMidPoint == null) {
            middlePoint = CalcMiddlePoint();
            userMidPoint = new Vector2(ScreenToWorldPoint(middlePoint));
        }
        actions[2] = true;
    }

    private static void ResetActions() {
        int i = 0;
        for (Transform t:selectedObjects) {
            if (lastPositions.size() <= i) continue;
            t.position.x = lastPositions.get(i).x;
            t.position.y = lastPositions.get(i).y;
            i++;
        }
        i = 0;
        for (Transform t:selectedObjects) {
            if (lastScales.size() <= i) continue;
            t.scale.x = lastScales.get(i).x;
            t.scale.y = lastScales.get(i).y;
            i++;
        }
        i = 0;
        for (Transform t:selectedObjects) {
            if (lastRotations.size() <= i) continue;
            t.rotation = lastRotations.get(i);
            i++;
        }
        actions[0] = false;
        actions[1] = false;
        actions[2] = false;
        lastPositions.clear();
        lastScales.clear();
        lastRotations.clear();
    }

    // SCREEN SPACE
    public static Vector2[] CalcSelectedRect() {
        if (selectedObjects.size() < 1) return null;
        Vector2 leftDown = new Vector2(Float.MAX_VALUE), rightUp = new Vector2(Float.MIN_VALUE);
        for (Transform t:selectedObjects) {
            Vector2 transformed = WorldToScreenPoint(new Vector2(t.position));
            float offX = 8*cameraView*t.scale.x, offY = 12*cameraView*t.scale.y;
            if (transformed.x - offX-2 < leftDown.x) leftDown.x = transformed.x - offX-2;
            if (transformed.y - offY-2 < leftDown.y) leftDown.y = transformed.y - offY-2;
            if (transformed.x + offX+2 > rightUp.x) rightUp.x = transformed.x + offX+2;
            if (transformed.y + offY+2 > rightUp.y) rightUp.y = transformed.y + offY+2;
        }
        return new Vector2[]{leftDown, rightUp};
    }

    // SCREEN SPACE
    public static Vector2 CalcMiddlePoint() {
        Vector2[] rect = CalcSelectedRect();
        if (rect == null) return null;
        return new Vector2(rect[0].x + (rect[1].x-rect[0].x)*0.5f, rect[0].y + (rect[1].y-rect[0].y)*0.5f);
    }

    // GIVEN SPACE
    public static Vector2 CalcMiddlePoint(Vector2 leftDown, Vector2 rightUp) {
        return new Vector2(leftDown.x + (rightUp.x-leftDown.y)*0.5f, leftDown.y + (rightUp.y-leftDown.y)*0.5f);
    }

    public static Vector2 WorldToScreenPoint(Vector2 v) {
        v = new Vector2(v).Sub(cameraPosition).Mul(cameraView).Add((float)Controller.Instance.canvas.getWidth()*0.5f,
                (float)Controller.Instance.canvas.getHeight()*0.5f);
        v.y = (float)Controller.Instance.canvas.getHeight()-v.y;
        return v;
    }

    public static Vector2 ScreenToWorldPoint(Vector2 v) {
        v = new Vector2(v);
        v.y = (float)Controller.Instance.canvas.getHeight()-v.y;
        return v.Sub((float)Controller.Instance.canvas.getWidth()*0.5f,(float)Controller.Instance.canvas.getHeight()*0.5f).Div(cameraView).Add(cameraPosition);
    }

    public static Object Instantiate() {
        return Instantiate("Object");
    }

    public static Object Instantiate(String name) {
        return Instantiate(new Vector2(), 0, new Vector2(1), name);
    }

    public static Object Instantiate(Vector2 position) {
        return Instantiate(position, 0);
    }

    public static Object Instantiate(Vector2 position, float rotation) {
        return Instantiate(position, rotation, new Vector2(1));
    }

    public static Object Instantiate(Vector2 position, float rotation, Vector2 scale) {
        return Instantiate(position,rotation,scale,"Object");
    }

    public static Object Instantiate(Vector2 position, float rotation, Vector2 scale, String name) {
        Object obj = new Object(name);
        obj.transform.position = position; obj.transform.rotation = rotation; obj.transform.scale = scale;
        entities.add(obj);
        return obj;
    }

    public static boolean Destroy(Class<MonoBehaviour> instance) {
        return false;
    }

    public static boolean DestroyImmediate(Class<MonoBehaviour> instance) {
        return true;
    }

    public static void ToggleEngine() {
        if (ACTIVE) timeline.pause(); else timeline.play();
        ACTIVE = !ACTIVE;
    }
}
