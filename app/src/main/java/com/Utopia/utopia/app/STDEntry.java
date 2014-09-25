package com.Utopia.utopia.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Utopia.utopia.app.SQL.DataProviderMetaData;

import net.simonvt.numberpicker.NumberPicker;

import java.util.HashMap;

/**
 * Created by Administrator on 2014/8/31 0031.
 */
public class STDEntry extends Activity{

    private Button button1, button2;

    NumberPicker picker01, picker02, picker11, picker12;
    EditText editText1, editText2, editText3;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_std_entry);

    /*
        picker01 = (NumberPicker) findViewById(R.id.picker01);
        picker02 = (NumberPicker) findViewById(R.id.picker02);
        picker11 = (NumberPicker) findViewById(R.id.picker11);
        picker12 = (NumberPicker) findViewById(R.id.picker12);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);


        //button1是取消，button2是确定
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long created, modified, begin, end, finish, kind,
                        beginHour, beginSecond, endHour, endSecond;
                String title, value, hint;
                beginHour = picker01.getValue();
                beginSecond = picker02.getValue();
                endHour = picker11.getValue();
                endSecond = picker12.getValue();

                HashMap<String, Object> map = new HashMap<String, Object>();

                created = TimeUtil.getCurrentTime();
                modified = TimeUtil.getCurrentTime();
                title = editText1.getText().toString();
                if (title.isEmpty()) title = "未命名";
                value = editText2.getText().toString();
                if (value.isEmpty()) value = "";
                hint = editText3.getText().toString();
                if (hint.isEmpty()) hint = "";

                begin = TimeUtil.getToday(created) % 10000 + 100 * beginHour + beginSecond;
                end = TimeUtil.getToday(created) % 10000 + 100 * endHour + endSecond;

                finish = 0;
                kind = DataProviderMetaData.DataTableMetaData.KIND_EVENT;

                Intent intent = new Intent(STDEntry.this, MainActivity.class);
                setResult(RESULT_OK, intent);

                intent.putExtra("create", created);
                intent.putExtra("modified", modified);
                intent.putExtra("title", title);
                intent.putExtra("value", value);
                intent.putExtra("begin", begin);
                intent.putExtra("end", end);
                intent.putExtra("finish", finish);
                intent.putExtra("kind", kind);
                intent.putExtra("myhint", hint);
                finish();
            }
        });
        */
    }


}
