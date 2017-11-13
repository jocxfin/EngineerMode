package jxl.format;

public interface Font {
    int getBoldWeight();

    Colour getColour();

    String getName();

    int getPointSize();

    ScriptStyle getScriptStyle();

    UnderlineStyle getUnderlineStyle();

    boolean isItalic();

    boolean isStruckout();
}
