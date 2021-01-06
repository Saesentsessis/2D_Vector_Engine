package sample.primitives;

import sample.utility.*;

public class Char {
    public char c;
    public Path[] paths;

    public Char() {}
    public Char(char representationChar, Path[] paths) {
        c = representationChar;
        this.paths = paths;
    }
}
