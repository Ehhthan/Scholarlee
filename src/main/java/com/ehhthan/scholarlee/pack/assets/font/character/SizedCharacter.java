package com.ehhthan.scholarlee.pack.assets.font.character;

import com.ehhthan.scholarlee.api.sizes.Sized;

public class SizedCharacter implements Sized {
    private final int codepoint;
    private final int width;
    private final int height;

    public SizedCharacter(int codepoint, int width, int height) {
        this.codepoint = codepoint;
        this.width = width;
        this.height = height;
    }

    public int getCodepoint() {
        return codepoint;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
