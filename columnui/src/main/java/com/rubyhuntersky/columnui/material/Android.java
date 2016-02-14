package com.rubyhuntersky.columnui.material;

import android.support.annotation.NonNull;

import com.rubyhuntersky.columnui.Observer;
import com.rubyhuntersky.columnui.bars.BarUi;
import com.rubyhuntersky.columnui.bars.BarUi1;
import com.rubyhuntersky.columnui.basics.Frame;
import com.rubyhuntersky.columnui.basics.ShapeSize;
import com.rubyhuntersky.columnui.columns.ColumnUi;
import com.rubyhuntersky.columnui.patches.Patch;
import com.rubyhuntersky.columnui.presentations.PatchPresentation;
import com.rubyhuntersky.columnui.presenters.OnPresent;
import com.rubyhuntersky.columnui.presenters.Presenter;
import com.rubyhuntersky.columnui.shapes.SpinnerViewShape;
import com.rubyhuntersky.columnui.tiles.Mosaic;
import com.rubyhuntersky.columnui.tiles.Tile0;
import com.rubyhuntersky.columnui.tiles.Tile1;

import java.util.List;

/**
 * @author wehjin
 * @since 1/27/16.
 */

public class Android {

    public static Tile0 spinnerTile(final List<String> options, final int selectedOption) {
        return Tile0.create(new OnPresent<Mosaic>() {
            @Override
            public void onPresent(Presenter<Mosaic> presenter) {
                final Mosaic mosaic = presenter.getDisplay();
                final SpinnerViewShape spinnerViewShape = new SpinnerViewShape(options, selectedOption, Observer.EMPTY);
                final ShapeSize shapeSize = mosaic.measureShape(spinnerViewShape);
                final int adjustedWidth = shapeSize.measuredWidth + 2; // A few pixels short causes ellipsis.
                final Frame frame = new Frame(adjustedWidth, shapeSize.measuredHeight, mosaic.elevation);
                final SpinnerViewShape shape = new SpinnerViewShape(options, selectedOption, presenter);
                final Patch patch = mosaic.addPatch(frame, shape);
                presenter.addPresentation(new PatchPresentation(patch, frame));
            }
        });
    }

    public static Tile1<Integer> spinnerTile(final List<String> options) {
        return Tile1.create(new Tile1.OnBind<Integer>() {
            @NonNull
            @Override
            public Tile0 onBind(Integer condition) {
                return spinnerTile(options, condition);
            }
        });
    }

    public static ColumnUi spinnerColumn(final List<String> options, final int selectedOption) {
        return spinnerTile(options, selectedOption).toColumn();
    }

    public static BarUi spinnerBar(final List<String> options, final int selectedOption) {
        return spinnerTile(options, selectedOption).toBar();
    }

    public static BarUi1<Integer> spinnerBar(final List<String> options) {
        return BarUi1.create(new BarUi1.OnBind<Integer>() {
            @Override
            public BarUi onBind(Integer condition) {
                return spinnerBar(options, condition);
            }
        });
    }

}
