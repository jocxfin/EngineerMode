package jxl.format;

public final class RGB {
    private int blue;
    private int green;
    private int red;

    public RGB(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }
}
