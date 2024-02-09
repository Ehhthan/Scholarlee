package com.ehhthan.scholarlee.pack.assets.font.character;

public class AscentedCharacter extends SizedCharacter {
    private final int ascent;

    public AscentedCharacter(int codepoint, int width, int height, int ascent) {
        super(codepoint, width, height);
        this.ascent = ascent;
    }

    public int getAscent() {
        return ascent;
    }
}
