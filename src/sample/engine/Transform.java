package sample.engine;

import sample.utility.*;

public class Transform extends MonoBehaviour {
    /** Object position in world space */
    public Vector2 position;
    /** Object rotation in world space */
    public float rotation;
    /** Object scale in world space */
    public Vector2 scale = new Vector2(1);

    public Transform() {}

    /** Translates object on given Vector values */
    public void Translate(Vector2 v) {
        position.Add(v);
    }

    /** Translates object on given x and y values */
    public void Translate(float x, float y) {
        position.Add(x,y);
    }

    public void Translate(Vector2 v, TransformSpace tSpace) {
        Translate(v, tSpace, TransformMode.ADD);
    }

    public void Translate(Vector2 v, TransformMode tMode) {
        Translate(v, TransformSpace.WORLD, tMode);
    }

    public void Translate(Vector2 v, TransformSpace tSpace, TransformMode tMode) {
        switch (tMode) {
            case ADD: {
                switch (tSpace) {
                    case LOCAL: {

                    }
                    case WORLD: {

                    }
                }
            }
            case SET: {
                switch (tSpace) {
                    case LOCAL: {

                    }
                    case WORLD: {

                    }
                }
            }
        }
    }

    public void Rotate(Vector2 r) {
        Rotate(r, TransformSpace.WORLD);
    }

    public void Rotate(float x, float y) {
        Rotate(new Vector2(x,y), TransformMode.ADD);
    }

    public void Rotate(Vector2 r, TransformSpace rSpace) {
        Rotate(r, TransformMode.ADD, rSpace);
    }

    public void Rotate(Vector2 r, TransformMode rMode) {
        Rotate(r,rMode,TransformSpace.WORLD);
    }

    public void Rotate(Vector2 r, TransformMode rMode, TransformSpace rSpace) {
        switch (rMode) {
            case ADD: {
                switch (rSpace) {
                    case LOCAL: {

                    }
                    case WORLD: {

                    }
                }
            }
            case SET: {
                switch (rSpace) {
                    case LOCAL: {

                    }
                    case WORLD: {

                    }
                }
            }
        }
    }

    /** Rotation mode used in some methods.<p> ADD - add given rotation to current.<p> SET - set current rotation to given. */
    public enum TransformMode {
        ADD,
        SET
    }

    /** Rotation space declares in which space rotation applies.<p> WORLD - rotate in global world space.<p> LOCAL - rotate in local parent object space */
    public enum TransformSpace {
        LOCAL,
        WORLD,
    }
}
