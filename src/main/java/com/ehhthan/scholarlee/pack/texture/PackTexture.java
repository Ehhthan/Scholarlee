package com.ehhthan.scholarlee.pack.texture;

import com.ehhthan.scholarlee.api.image.TrimmedImage;

import java.awt.image.BufferedImage;

public interface PackTexture {
    static TrimmedImage[][] tiles(BufferedImage image, int rows, int columns) {
        final TrimmedImage[][] tiles = new TrimmedImage[rows][columns];

        int tileWidth = image.getWidth() / columns;
        int tileHeight = image.getHeight() / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tiles[i][j] = TrimmedImage.get(image.getSubimage(tileWidth * j, tileHeight * i, tileWidth, tileHeight));
            }
        }

        return tiles;
    }
}
