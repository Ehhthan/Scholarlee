package com.ehhthan.scholarlee.api.image;

import java.awt.image.BufferedImage;
import com.ehhthan.scholarlee.api.sizes.Sized;

public interface SizedImage extends Sized {
    BufferedImage getTexture();
}
