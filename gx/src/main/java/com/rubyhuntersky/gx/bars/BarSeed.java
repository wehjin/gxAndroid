package com.rubyhuntersky.gx.bars;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;
import com.rubyhuntersky.gx.internal.devices.patches.Patch;
import com.rubyhuntersky.gx.internal.shapes.Shape;

/**
 * @author wehjin
 * @since 1/29/16.
 */

abstract public class BarSeed extends Bar {
    public BarSeed(float fixedHeight, float relatedWidth, int elevation) {
        super(fixedHeight, relatedWidth, elevation, null);
    }

    @NonNull
    @Override
    abstract public Patch addPatch(Frame frame, Shape shape, int argbColor);

    @NonNull
    @Override
    abstract public TextSize measureText(String text, TextStyle textStyle);

    @NonNull
    @Override
    abstract public ShapeSize measureShape(Shape shape);
}
