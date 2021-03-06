package com.rubyhuntersky.gx.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rubyhuntersky.gx.R;
import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.Range;
import com.rubyhuntersky.gx.basics.Removable;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.basics.TextHeight;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.internal.screen.Screen;
import com.rubyhuntersky.gx.internal.shapes.RectangleShape;
import com.rubyhuntersky.gx.internal.shapes.Shape;
import com.rubyhuntersky.gx.internal.shapes.TextShape;
import com.rubyhuntersky.gx.internal.shapes.ViewShape;
import com.rubyhuntersky.gx.internal.surface.Jester;
import com.rubyhuntersky.gx.uis.divs.Div;
import com.rubyhuntersky.gx.uis.divs.Div0;

import kotlin.NotImplementedError;

/**
 * @author wehjin
 * @since 2/11/16.
 */

public class ScreenView extends FrameLayout implements Screen {

    public static final String TAG = ScreenView.class.getSimpleName();
    private TextRuler textRuler;
    private ShapeRuler shapeRuler;
    private int elevationPixels;


    public ScreenView(Context context) {
        super(context);
        initPatchDeviceView(context);
    }

    public ScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPatchDeviceView(context);
    }

    public ScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPatchDeviceView(context);
    }

    private void initPatchDeviceView(Context context) {
        textRuler = new TextRuler(context);
        shapeRuler = new ShapeRuler(context);
        elevationPixels = getResources().getDimensionPixelSize(R.dimen.elevationGap);
    }

    @NonNull
    @Override
    public TextSize measureText(@NonNull String text, @NonNull TextStyle textStyle) {
        return textRuler.measure(text, textStyle);
    }

    @NonNull
    @Override
    public ShapeSize measureShape(@NonNull Shape shape) {
        return shapeRuler.measure(shape);
    }

    @NonNull
    @Override
    public Patch addPatch(@NonNull Frame frame, @NonNull Shape shape, int argbColor) {
        if (shape instanceof RectangleShape) {
            return getRectanglePatch(frame, (RectangleShape) shape, argbColor);
        } else if (shape instanceof TextShape) {
            return getTextPatch(frame, (TextShape) shape);
        } else if (shape instanceof ViewShape) {
            final ViewShape viewShape = (ViewShape) shape;
            return getViewPatch(viewShape.createView(getContext()), frame, 0);
        } else {
            return Patch.EMPTY;
        }
    }

    @NonNull
    @Override
    public Removable addSurface(@NonNull Frame frame, @NonNull Jester jester) {
        throw new NotImplementedError("addSurface");
    }

    @NonNull
    @Override
    public Div.Presentation present(@NonNull Div0 div, float offset, @NonNull Div.Observer observer) {
        throw new UnsupportedOperationException("present/div0");
    }

    @NonNull
    private Patch getRectanglePatch(Frame frame, RectangleShape rectangleShape, int argbColor) {
        final View view = new View(getContext());
        view.setBackgroundColor(argbColor);
        view.setContentDescription(String.format("Rectangle{%x}", argbColor));
        return getViewPatch(view, frame, 0);
    }

    @NonNull
    private Patch getTextPatch(Frame frame, TextShape textShape) {
        final TextView textView = AndroidKt.toTextView(textShape, getContext());
        final TextHeight textHeight = textShape.textSize.textHeight;
        Frame newFrame = frame.withVerticalShift(-textHeight.topPadding)
              .withVerticalLength(textHeight.topPadding + textHeight.height + textHeight.topPadding);
        return getViewPatch(textView, newFrame, textHeight.height);
    }

    @NonNull
    private Patch getViewPatch(final View view, final Frame frame, float additionalHeight) {
        setElevation(view, frame);
        Log.d(TAG, "Add view: " + view + " frame: " + frame);
        addView(view, getPatchLayoutParams(frame, additionalHeight));
        return new Patch() {
            @Override
            public void remove() {
                Log.d(TAG, "Remove view: " + view + " frame: " + frame);
                ScreenView.this.removeView(view);
            }
        };
    }

    @NonNull
    private LayoutParams getPatchLayoutParams(Frame frame, float additionalHeight) {
        Range horizontal = frame.getHorizontal();
        Range vertical = frame.getVertical();
        final LayoutParams layoutParams = new FrameLayout.LayoutParams((int) horizontal.toLength(),
                                                                       (int) (vertical.toLength() + additionalHeight));
        layoutParams.leftMargin = (int) horizontal.getStart();
        layoutParams.topMargin = (int) vertical.getStart();
        return layoutParams;
    }


    private void setElevation(View view, Frame frame) {
        ViewCompat.setElevation(view, elevationPixels * frame.getElevation());
    }
}
