package me.chon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import me.nohc.Bind;

public class MainActivity extends AppCompatActivity
{
    @Bind(R.id.id_textview)
    TextView mTv;

    @Bind(R.id.id_btn)
    Button mBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

//        ViewInjector.injectView(this);
//
//        mTv.setText("ViewInject");
//        mBtn.setText("ViewInject ~");

    }

}
