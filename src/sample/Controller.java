package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import sample.engine.Engine;
import sample.primitives.*;
import sample.utility.*;

public class Controller {
    public static Controller Instance;

    public static Scene Scene;

    /*private Matrix3x3 moveMatrix;
    private Matrix3x3 rotationMatrix;
    private Matrix3x3 scaleMatrix;
*/
    public Vector2 currentMousePosition;
    public Vector2 lastMousePosition;

    public Canvas canvas;
    public AnchorPane anchorPane;

    //public Char[] chars = new Char[26];

    private static Color currentCol = Color.WHITE;
    private static Color canvasCol = Color.BLACK;

    public static void SetColor(Color c) {
        currentCol = c;
    }

    public static Char CapitalC,CapitalK,Plus,Nine,Minus,Line;

    public static boolean NightMode = true;

    @FXML
    public void initialize() {
        //LoadChars();
        anchorPane = (AnchorPane)canvas.getParent();
        currentMousePosition = new Vector2();
        Instance = this;
        canvas.setOnMouseMoved(event -> {
            lastMousePosition = new Vector2(currentMousePosition);
            currentMousePosition = ToScreenV((int) event.getSceneX(), (int) event.getSceneY());
            Engine.HandleInput(currentMousePosition, lastMousePosition);
        });
        canvas.setOnMousePressed(this::HandleMouseEvents);
        canvas.setOnScroll(this::HandleMouseScrollEvents);
        canvas.setOnMouseDragged(this::HandleMouseDragEvents);
        anchorPane.setOnKeyPressed(this::HandleKeyboardPressEvents);
        //canvas.addEventFilter(KeyEvent.KEY_PRESSED, this::HandleKeyboardPressEvents);
        anchorPane.setOnKeyTyped(this::HandleKeyboardHoldEvents);
        anchorPane.setOnKeyReleased(this::HandleKeyboardReleaseEvents);
        //Start();
    }

    public void Start() {
        Scene.setOnKeyPressed(this::HandleKeyboardPressEvents);
        Scene.setOnKeyReleased(this::HandleKeyboardReleaseEvents);
        Scene.setOnKeyTyped(this::HandleKeyboardHoldEvents);
        Refresh();
        Point[] pts = {new Point(6, 0), new Point(-6, 0),
                new Point(-6, 20), new Point(6, 20),
                new Point(6, 16),new Point(-2, 16),
                new Point(-2, 4), new Point(6, 4)};
        CapitalC = new Char('C', new Path[]{new Path(pts)});
        //pts = new Point[]{new Point()};
        pts = new Point[]{new Point(-6, 2), new Point(6,2),
                new Point(6,-2), new Point(-6,-2)};
        Minus = new Char('-', new Path[]{new Path(pts)});
        pts = new Point[]{new Point(-2,2), new Point(-2,6),
                new Point(2,6), new Point(2,2),
                new Point(6,2), new Point(6,-2),
                new Point(2,-2), new Point(2,-6),
                new Point(-2,-6), new Point(-2,-2),
                new Point(-6,-2), new Point(-6,2)};
        Plus = new Char('+', new Path[]{new Path(pts)});
        pts = new Point[]{new Point(-8, -12), new Point(-8, 12),
                new Point(-4, 12), new Point(-4, 3),
                new Point(3, 12),new Point(8, 12),
                new Point(-1.5f, 0),new Point(8, -12),
                new Point(3, -12),new Point(-4, -3),
                new Point(-4, -12)};
        CapitalK = new Char('K', new Path[]{new Path(pts)});
        pts = new Point[]{new Point(-7,-8), new Point(-7,-12),
                new Point(7,-12), new Point(7,12),
                new Point(-7,12),new Point(-7,-2),
                new Point(3,-2),new Point(3,-8)};
        Nine = new Char('9', new Path[]{new Path(pts), new Path(new Point[]{
                new Point(-3,8),new Point(-3,2),new Point(3,2),new Point(3,8)
        })});
        Line = new Char('-', new Path[]{new Path(new Point[]{new Point(-5,0), new Point(5,0)})});
        //moveMatrix = new Matrix3x3(1,0,0,0,1,0,0.1f,0.1f,1);
        //rotationMatrix = new Matrix3x3();
        //scaleMatrix = new Matrix3x3(2,0,0,0,2,0,0,0,1);
        Engine.Init();
    }

    public void draw() {
        DrawChar(CapitalC, new Point(20,(float)canvas.getHeight()-156), new Point(2,2));
        DrawChar(Plus,new Point(48,(float)canvas.getHeight()-150),new Point(1.6f,1.6f));
        DrawChar(Plus,new Point(80,(float)canvas.getHeight()-150),new Point(1.6f,1.6f));
        DrawChar(CapitalK,new Point(40,(float)canvas.getHeight()-56),new Point(4,4));
        DrawChar(Nine,new Point(110,(float)canvas.getHeight()-56),new Point(4,4));

        currentCol = new Color(Math.random()%1f,Math.random()%1f,Math.random()%1f,1f);
    }

    /*private void LoadChars() {
        String data = new File("..chars.obj").toString();

    }*/

    private void HandleMouseEvents(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {
            Engine.HandlePrimaryButton(e);
        } else if (e.isSecondaryButtonDown()) {
            Engine.HandleSecondaryButton(e);
        }
    }

    private void HandleMouseScrollEvents(ScrollEvent e) {
        double deltaY = e.getDeltaY();
        float aspectRatio = (float) (canvas.getWidth()/canvas.getHeight());
        Vector2 deltaMousePosition = new Vector2((float)(( canvas.getWidth()*0.5f-e.getSceneX()) * -1f / canvas.getWidth()),(float)(( canvas.getHeight()*0.5f-e.getSceneY() ) / canvas.getHeight()));
        deltaMousePosition.y*=-1f;
        //System.out.println("X : "+deltaMousePosition.x + "\nY : "+deltaMousePosition.y);
        Vector2 delta = Vector2.Mul(new Vector2((float)canvas.getWidth()*0.5f, (float)canvas.getHeight()*0.5f), deltaMousePosition); delta.Div(Engine.cameraView);
        if (deltaY > 0 && Engine.cameraView < 15f) {
            Engine.cameraView *= 1.111f;
        } else if (deltaY < 0 && Engine.cameraView > 0.01f) {
            delta.y*=-1f;delta.x*=-1f;
            Engine.cameraView *= .9f;
        }
        delta.x/=aspectRatio; delta.y/=aspectRatio;
        Engine.cameraPosition.Add(delta);
        System.out.println("X="+delta.x+" Y="+delta.y+" CamX="+Engine.cameraPosition.x+" CamY="+Engine.cameraPosition.y);
    }

    private void HandleMouseDragEvents(MouseEvent e) {
        lastMousePosition = new Vector2(currentMousePosition);
        currentMousePosition = ToScreenV((int)e.getSceneX(), (int)e.getSceneY());
        if (e.isMiddleButtonDown()) {
            Vector2 delta = Vector2.Sub(lastMousePosition,currentMousePosition); delta.Div(Engine.cameraView);
            delta.y *= -1;
            Engine.cameraPosition.Add(delta);
            //System.out.println("Xmove="+delta.x+" Ymove="+delta.y);
        }
    }

    private void HandleKeyboardPressEvents(KeyEvent e) {
        System.out.println(e.getCode());
        switch(e.getCode()) {
            case G:
                Engine.GrabSelected();
                break;
            case S:
                Engine.ScaleSelected(new Vector2(currentMousePosition));
                break;
            case R:
                Engine.RotateSelected(new Vector2(currentMousePosition));
                break;
            case N:
                NightMode = !NightMode;
                Refresh();
                break;
        }
    }

    private void Refresh() {
        if (NightMode) {
            canvasCol = Color.BLACK;
            currentCol = Color.WHITE;
        } else {
            canvasCol = Color.WHITE;
            currentCol = Color.BLACK;
        }
        canvas.getGraphicsContext2D().setFill(canvasCol);
    }

    private void HandleKeyboardReleaseEvents(KeyEvent e) {
    }

    private void HandleKeyboardHoldEvents(KeyEvent e) {
    }

    public void DrawChar(Char c) {
        DrawChar(c,new Point(0,0),new Point(1,1));
    }

    public void DrawChar(Char c, Point offset) {
        DrawChar(c,offset,new Point(1,1));
    }

    public void DrawChar(Char c, Point offset, Point size) { DrawChar(c,offset,size,0); }

    public void DrawChar(Char c, Point offset, Point size, float rotation) {
        if (c == null || c.paths == null) return;
        //System.out.println("Drawing "+c);
        float cos = (float)Math.cos(Math.toRadians(rotation)), sin = (float)Math.sin(Math.toRadians(rotation));
        Point first = new Point(), last = new Point();

        for (Path p:c.paths) {
            for (int i = 0; i < p.points.length-1; i++) {
                first.x = p.points[i].x * cos - p.points[i].y * sin;
                first.y = p.points[i].x * sin + p.points[i].y * cos;
                last.x = p.points[i+1].x * cos - p.points[i+1].y * sin;
                last.y = p.points[i+1].x * sin + p.points[i+1].y * cos;
                DrawLine(
                        new Point(first.x*size.x+offset.x,first.y*size.y+offset.y),
                        new Point(last.x*size.x+offset.x,last.y*size.y+offset.y)
                        //new Point(p.points[i].x*size.x+offset.x,p.points[i].y*size.y+offset.y),
                        //new Point(p.points[i+1].x*size.x+offset.x,p.points[i+1].y*size.y+offset.y)
                );
            }
            first.x = p.points[p.points.length-1].x * cos - p.points[p.points.length-1].y * sin;
            first.y = p.points[p.points.length-1].x * sin + p.points[p.points.length-1].y * cos;
            last.x = p.points[0].x * cos - p.points[0].y * sin;
            last.y = p.points[0].x * sin + p.points[0].y * cos;
            DrawLine(
                    new Point(first.x*size.x+offset.x,first.y*size.y+offset.y),
                    new Point(last.x*size.x+offset.x,last.y*size.y+offset.y)
                    //new Point(p.points[p.points.length-1].x*size.x+offset.x,p.points[p.points.length-1].y*size.y+offset.y),
                    //new Point(p.points[0].x*size.x+offset.x,p.points[0].y*size.y+offset.y)
            );
        }
    }

    public void DrawRect(Point start, Point end) {
        Point upLeft = new Point(start.x, end.y);
        Point downRight = new Point(end.x, start.y);
        DrawLine(start, upLeft);
        DrawLine(upLeft,end);
        DrawLine(end, downRight);
        DrawLine(downRight, start);
    }

    public void DrawLine(Point start, Point end) {
        DrawLine(start, end, currentCol);
    }

    public void DrawLine(Point start, Point end, Color c) {
        PixelWriter pw = canvas.getGraphicsContext2D().getPixelWriter();
        float dx = end.x-start.x, dy = end.y-start.y;
        float step = 1.0f / Math.max(Math.abs(dx),Math.abs(dy)+0.0001f);
        for (float i = 0; i <= 1.0f; i+=step) {
            int x = (int)(start.x + dx * i + 0.5f);
            if (!(x>=0 && x < canvas.getWidth())) continue;
            int y = (int)(start.y + dy * i + 0.5f);
            Point trased = ToScreen(x,y);
            if (y >= 0 && y < canvas.getHeight()) pw.setColor((int)trased.x,(int)trased.y,c);
        }
    }

    public Point ToScreen(int x, int y) {
        return new Point(x, (float)canvas.getHeight()-y);
    }

    public Vector2 ToScreenV(int x, int y) {
        return new Vector2(x, (float)canvas.getHeight()-y);
    }

    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }
}
