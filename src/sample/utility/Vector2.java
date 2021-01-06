package sample.utility;

public class Vector2 extends Point {
    public Vector2() {this.x = 0; this.y = 0;}
    public Vector2(float v) {this.x = v; this.y = v;}
    public Vector2(float x, float y) {this.x = x; this.y = y;}
    public Vector2(Vector2 v) {this.x = v.x; this.y = v.y;}

    public static Vector2 Add(Vector2 f, Vector2 s) {
        return new Vector2(f.x + s.x, f.y + s.y);
    }

    public static Vector2 Sub(Vector2 f, Vector2 s) { return new Vector2(f.x-s.x, f.y-s.y); }

    public static Vector2 Mul(Vector2 f, Vector2 s) { return new Vector2(f.x*s.x, f.y*s.y); }

    public static Vector2 Div(Vector2 f, Vector2 s) { return new Vector2(f.x/s.x, f.y/s.y); }

    public static Vector2 Div(Vector2 f, float v) { return new Vector2(f.x/v, f.y/v); }

    /** Adds two Vectors by their coordinates accordingly */
    public Vector2 Add(Vector2 v) {this.x += v.x; this.y += v.y; return this; }

    /** Adds given value to both x and y coordinates of Vector */
    public Vector2 Add(float v) {this.x += v; this.y += v;return this;}

    /** Adds given values x and y coordinates of Vector accordingly */
    public Vector2 Add(float x, float y) {this.x += x; this.y += y;return this;}

    /** Subtracts from this Vector another Vector */
    public Vector2 Sub(Vector2 v) {this.x -= v.x; this.y -= v.y;return this;}

    /** Subtracts given value from both x and y coordinates of Vector */
    public Vector2 Sub(float v) {this.x -= v; this.y -= v;return this;}

    /** Subtracts given values x and y coordinates of Vector accordingly */
    public Vector2 Sub(float x, float y) {this.x -= x; this.y -= y;return this;}

    /** Multiplies Vector by given value */
    public Vector2 Mul(float m) { this.x *= m; this.y *= m; return this; }

    /** Multiplies Vectors by coordinates accordingly */
    public Vector2 Mul(Vector2 v) { this.x *= v.x; this.y *= v.y; return this; }

    /** Divides Vector by given value */
    public Vector2 Div(float d) {this.x /= d; this.y /= d;return this;}

    public Vector2 Absolute() {if (this.x < 0) this.x = -this.x; if (this.y < 0) this.y = -this.y; return this;}

    public Vector2 Normalize() { float divider = Math.abs(this.x) + Math.abs(this.y); this.x/=divider; this.y/=divider; return this; }

    /** Returns distance between two Vectors */
    public static float Distance(Vector2 s, Vector2 e) { return (float)Math.sqrt(Vector2.SqrDistance(s,e)); }

    /** Returns squared distance between two Vectors */
    public static float SqrDistance(Vector2 s, Vector2 e) { return ((s.x-e.x) * (s.x-e.x)) + ((s.y-e.y) * (s.y-e.y));}
}
