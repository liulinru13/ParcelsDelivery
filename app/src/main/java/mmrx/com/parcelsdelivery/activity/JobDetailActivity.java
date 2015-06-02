package mmrx.com.parcelsdelivery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import mmrx.com.parcelsdelivery.MainActivity;
import mmrx.com.parcelsdelivery.R;
import mmrx.com.parcelsdelivery.bean.JobBean;
import mmrx.com.parcelsdelivery.http.MyHttpHelper;

public class JobDetailActivity extends Activity {

    Button mSubmit;
    Button mCompleted;
    String mJobID;
    String flag;
    JobBean mBean;

    TextView mSouAddrTv;
    TextView mDestiAddrTv;
    TextView mDueDateTv;
    TextView mPsizeTv;
    TextView mPweightTv;
    TextView mSalaryTv;

    //async update content
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //get json data and notify adapter
            String bundleResult = msg.getData().getString("RESPONSE");
            Log.v("JobDetailActivity",bundleResult);
            ObjectMapper mapper = new ObjectMapper();
            try{
                mBean = mapper.readValue(bundleResult,JobBean.class);
                //get data successfully
                if(mBean != null){
                    mSouAddrTv.setText(mBean.getSource_addr());
                    mDestiAddrTv.setText(mBean.getDestination_addr());
                    mDueDateTv.setText(mBean.getDue_date());
                    mPsizeTv.setText(mBean.getSize());
                    mPweightTv.setText(mBean.getWeight()+" kg");
                    mSalaryTv.setText("预付金额为"+mBean.getSalary()+"元哦!");

                    if(mBean.getJob_status().equals("1")) {
                        mSubmit.setVisibility(View.VISIBLE);
                        mCompleted.setVisibility(View.GONE);
                    }else if(mBean.getJob_status().equals("2")){
                        mSubmit.setVisibility(View.GONE);
                        mCompleted.setVisibility(View.VISIBLE);
                    }else{
                        mSubmit.setVisibility(View.GONE);
                        mCompleted.setVisibility(View.GONE);
                    }
                }
                // no data
                else{
                    Toast.makeText(JobDetailActivity.this, "服务器没有数据", Toast.LENGTH_LONG);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        init();
    }

    private void init(){
        Intent intent = getIntent();
        mJobID = intent.getStringExtra(MainActivity.JOB_ID);
        flag = intent.getStringExtra(MainActivity.FLAG);
        mSubmit = (Button)findViewById(R.id.job_detail_bn);
        mCompleted = (Button)findViewById(R.id.job_detail_comp_bn);

        mSouAddrTv = (TextView)findViewById(R.id.job_detail_source_tv);
        mDestiAddrTv = (TextView)findViewById(R.id.job_detail_desti_tv);
        mDueDateTv = (TextView)findViewById(R.id.job_detail_date_tv);
        mPsizeTv = (TextView)findViewById(R.id.job_detail_p_size_tv);
        mPweightTv = (TextView)findViewById(R.id.job_detail_p_weight_tv);
        mSalaryTv = (TextView)findViewById(R.id.job_detail_pay_tv);

        View view = (View)findViewById(R.id.job_detail_title_bar);
        ImageButton ib = (ImageButton)view.findViewById(R.id.back_bn);
        TextView tv = (TextView)view.findViewById(R.id.title_tv);

        tv.setText("订单详情");
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackLastActivity();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mJobID!=null)
                    MyHttpHelper.performRequest(MyHttpHelper.URL_TYPE.JOB_SET_DE,
                            mJobID, mHandler,JobDetailActivity.this);
                goBackLastActivity();
            }
        });
        mCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mJobID!=null)
                    MyHttpHelper.performRequest(MyHttpHelper.URL_TYPE.JOB_SET_COMP,
                            mJobID, mHandler,JobDetailActivity.this);
                goBackLastActivity();
            }
        });

        setTvEmpty();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataFromServer();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * get data from server
     * */
    private void getDataFromServer(){
        if(mJobID != null)
            MyHttpHelper.performRequest(MyHttpHelper.URL_TYPE.JOB_GET,
                    mJobID, mHandler, this);
    }

    /**
     * set TextView empty
     * */

    private void setTvEmpty(){
        mSouAddrTv.setText("");
        mDestiAddrTv.setText("");
        mDueDateTv.setText("");
        mPsizeTv.setText("");
        mPweightTv.setText("");
        mSalaryTv.setText("");
    }

    /**
     * return the last activity
     * */
    private void goBackLastActivity(){
        Intent intent = new Intent(JobDetailActivity.this,JobListActivity.class);
        intent.putExtra(MainActivity.FLAG,flag);
        startActivity(intent);
    }
 }
