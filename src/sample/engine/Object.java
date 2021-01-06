package sample.engine;

public class Object extends MonoBehaviour {
    public String name;

    public Object(String name) {
        this.name = name;
        object = this;
        transform = new Transform();
        transform.object = this;
        transform.transform = transform;
    }
}
