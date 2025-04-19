package ar.edu.uade.desa1;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConstraintLayout layout = new ConstraintLayout(this);
        layout.setId(ConstraintLayout.generateViewId());

        TextView textView = new TextView(this);
        textView.setText("¡Bienvenido a HistoryActivity!");
        textView.setTextSize(24);
        textView.setId(TextView.generateViewId());

        layout.addView(textView);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(textView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        set.connect(textView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.connect(textView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        set.connect(textView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);

        set.applyTo(layout);

        setContentView(layout);
    }
}
