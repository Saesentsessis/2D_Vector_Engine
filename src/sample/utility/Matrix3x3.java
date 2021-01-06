package sample.utility;

public class Matrix3x3 {
    public float[][] m = new float[3][3];

    /** Matrix params : <p>
     * | @param x1y1 @param x2y1 @param x3y1 | <p>
     * | @param x1y2 @param x2y2 @param x3y2 | <p>
     * | @param x1y3 @param x2y3 @param x3y3 |
     */
    public Matrix3x3(float x1y1, float x1y2, float x1y3,
                     float x2y1, float x2y2, float x2y3,
                     float x3y1, float x3y2, float x3y3)
    {
        this.m[0][0] = x1y1; this.m[0][1] = x1y2; this.m[0][2] = x1y3;
        this.m[1][0] = x2y1; this.m[1][1] = x2y2; this.m[2][1] = x2y3;
        this.m[2][0] = x3y1; this.m[1][2] = x3y2; this.m[2][2] = x3y3;
    }
}
