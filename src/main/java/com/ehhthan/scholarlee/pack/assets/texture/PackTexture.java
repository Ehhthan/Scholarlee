package com.ehhthan.scholarlee.pack.assets.texture;

import com.ehhthan.scholarlee.api.unihex.TrimData;

import java.awt.image.BufferedImage;

public interface PackTexture {
    static TrimData[][] tiles(BufferedImage image, int rows, int columns) {
        final TrimData[][] tiles = new TrimData[rows][columns];

        int tileWidth = image.getWidth() / columns;
        int tileHeight = image.getHeight() / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tiles[i][j] = TrimData.get(image.getSubimage(tileWidth * j, tileHeight * i, tileWidth, tileHeight));
            }
        }

        return tiles;
    }
}
