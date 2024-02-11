package com.ehhthan.scholarlee.api.unihex;

import com.ehhthan.scholarlee.api.sizes.Sized;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

public class TrimData implements Sized {
    private final int width, height, leftIndent, rightIndent, topIndent, bottomIndent;

    private TrimData(int width, int height, int leftIndent, int rightIndent, int topIndent, int bottomIndent) {
        this.width = width;
        this.height = height;
        this.leftIndent = leftIndent;
        this.rightIndent = rightIndent;
        this.topIndent = topIndent;
        this.bottomIndent = bottomIndent;
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

    public static TrimData get(boolean[][] array) {
        int xMin = -1, xMax = -1, yMin = -1, yMax = -1;

        final int w = array[0].length, h = array.length;
        // from top
        x1: for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (array[y][x]) {
                    xMin = x;
                    break x1;
                }
            }
        }

        // from bottom
        x2: for (int x = w; x-- > 0;) {
            for (int y = 0; y < h; y++) {
                if (array[y][x]) {
                    xMax = x;
                    break x2;
                }
            }
        }

        // from left
        y1: for (int y = 0; y < h; y++) {
            for (int x = xMin; x <= xMax; x++) {
                if (x == -1 || array[y][x]) {
                    yMin = y;
                    break y1;
                }
            }
        }

        // from right
        y2: for (int y = h; y-- > 0;) {
            for (int x = xMin; x <= xMax; x++) {
                if (x == -1 || array[y][x]) {
                    yMax = y;
                    break y2;
                }
            }
        }

        int width = xMax - xMin + 1;
        int height = yMax - yMin + 1;

        int leftIndent, rightIndent, topIndent, bottomIndent;

        leftIndent = xMin;
        rightIndent = w - xMax - 1;

        topIndent = yMin;
        bottomIndent = h - yMax - 1;

        return new TrimData(width, height, leftIndent, rightIndent, topIndent, bottomIndent);
    }

    public static TrimData get(BufferedImage image) {
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
            return new TrimData(0, 0, 0, 0, 0, 0);
        }

        int leftIndent, rightIndent, topIndent, bottomIndent;

        leftIndent = xMin;
        rightIndent = image.getWidth() - xMax - 1;

        topIndent = yMin;
        bottomIndent = image.getHeight() - yMax - 1;

        return new TrimData(width, height, leftIndent, rightIndent, topIndent, bottomIndent);
    }

    private static boolean isNotTransparent(final BufferedImage image, final int x, final int y) {
        if (x < 0 || y < 0)
            return false;

        return (image.getRGB(x,y) >> 24) != 0x00;
    }
}
