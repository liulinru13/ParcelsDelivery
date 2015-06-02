package mmrx.com.parcelsdelivery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import mmrx.com.parcelsdelivery.activity.JobListActivity;


public class MainActivity extends Activity {

    public static final String JOB_LIST_FLAG = "new";
    public static final String JOB_HISTORY_LIST_FLAG = "old";
    public static final String FLAG = "flag";
    public static final String JOB_ID = "jobID";

    LinearLayout mBn1;
    LinearLayout mBn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this, JobListActivity.class);
//        startActivity(intent);

        mBn1 = (LinearLayout)findViewById(R.id.main_new_order_bn);
        mBn2 = (LinearLayout)findViewById(R.id.main_old_order_bn);

        mBn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JobListActivity.class);
                intent.putExtra(FLAG,JOB_LIST_FLAG);
                startActivity(intent);
            }
        });

        mBn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JobListActivity.class);
                intent.putExtra(FLAG,JOB_HISTORY_LIST_FLAG);
                startActivity(intent);
            }
        });
    }

}
