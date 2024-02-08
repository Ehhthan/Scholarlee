package com.ehhthan.scholarlee.api.image;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

public class TrimmedImage implements SizedImage {
    private final BufferedImage texture;
    private final int width, height, leftIndent, rightIndent, topIndent, bottomIndent;

    private TrimmedImage(BufferedImage texture, int width, int height, int leftIndent, int rightIndent, int topIndent, int bottomIndent) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.leftIndent = leftIndent;
        this.rightIndent = rightIndent;
        this.topIndent = topIndent;
        this.bottomIndent = bottomIndent;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public int getLeftIndent() {
        return leftIndent;
    }

    public int getRightIndent() {
        return rightIndent;
    }

    public int getTopIndent() {
        return topIndent;
    }

    public int getBottomIndent() {
        return bottomIndent;
    }

    public static TrimmedImage get(BufferedImage image) {
        int xMin = -1, xMax = -1, yMin = -1, yMax = -1;

        final int w = image.getWidth(), h = image.getHeight();
        // from top
        x1: for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (isNotTransparent(image, x, y)) {
                    xMin = x;
                    break x1;
                }
            }
        }

        // from bottom
        x2: for (int x = w; x-- > 0;) {
            for (int y = 0; y < h; y++) {
                if (isNotTransparent(image, x, y)) {
                    xMax = x;
                    break x2;
                }
            }
        }

        // from left
        y1: for (int y = 0; y < h; y++) {
            for (int x = xMin; x <= xMax; x++) {
                if (isNotTransparent(image, x, y)) {
                    yMin = y;
                    break y1;
                }
            }
        }

        // from right
        y2: for (int y = h; y-- > 0;) {
            for (int x = xMin; x <= xMax; x++) {
                if (isNotTransparent(image, x, y)) {
                    yMax = y;
                    break y2;
                }
            }
        }

        int width = xMax - xMin + 1;
        int height = yMax - yMin + 1;

        BufferedImage cropped;
        try {
            cropped = image.getSubimage(xMin, yMin, width, height);
        } catch (RasterFormatException e) {
            return new TrimmedImage(image, 0, 0, 0, 0, 0, 0);
        }

        int leftIndent, rightIndent, topIndent, bottomIndent;

        leftIndent = xMin;
        rightIndent = image.getWidth() - xMax + 1;

        topIndent = yMin;
        bottomIndent = image.getHeight() - yMax + 1;

        return new TrimmedImage(cropped, width, height, leftIndent, rightIndent, topIndent, bottomIndent);
    }

    private static boolean isNotTransparent(final BufferedImage image, final int x, final int y) {
        if (x < 0 || y < 0)
            return false;

        return (image.getRGB(x,y) >> 24) != 0x00;
    }
}
