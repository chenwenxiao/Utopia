package com.Utopia.utopia.app;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by Joe on 14-9-7.
 */
public class EveryDayPushView extends RelativeLayout {

    TextView textView;
    ImageView imageView;
    View view;

    public EveryDayPushView(Context context) {
        super(context);
        init();
    }

    public EveryDayPushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EveryDayPushView(Context context, Map<String, Object> map) {
        super(context);
        init();
        setText(String.valueOf(map.get("value")));
        setImageViewSrc(ObjectAndByte.toByteArray(map.get("bitmap")));
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.every_day_push_view, this, true);
        imageView = (ImageView) view.findViewById(R.id.every_day_push_view_image_view);
        textView = (TextView) view.findViewById(R.id.every_day_push_view_text_view);
    }

    public void setText(String string) {
        textView.setText(string);
    }

    public void setImageViewSrc(byte[] in) {
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(in, 0, in.length));
    }
}
