package com.ehhthan.scholarlee.api.image;

import com.ehhthan.scholarlee.api.sizes.Sized;

import java.awt.image.BufferedImage;

public interface SizedImage extends Sized {
    BufferedImage getTexture();
}
