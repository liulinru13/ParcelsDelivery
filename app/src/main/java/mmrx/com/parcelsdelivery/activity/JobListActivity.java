package mmrx.com.parcelsdelivery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import mmrx.com.parcelsdelivery.MainActivity;
import mmrx.com.parcelsdelivery.R;
import mmrx.com.parcelsdelivery.adapter.JobAdapter;
import mmrx.com.parcelsdelivery.bean.JobListBean;
import mmrx.com.parcelsdelivery.http.MyHttpHelper;

public class JobListActivity extends Activity implements
        AdapterView.OnItemClickListener,View.OnClickListener{

    PullToRefreshListView mlist;
    JobAdapter mAdapter;
    List<JobListBean> mlistContent;
    ImageButton mBackBn;
    TextView mTitleTv;
    //identify if the content is history
    String flag;

    //async update list content
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //get json data and notify adapter
            refreshCompleted();
            String bundleResult = msg.getData().getString("RESPONSE");
            Log.v("JobListActivity", bundleResult);
            ObjectMapper mapper = new ObjectMapper();
            try{
                JavaType javaType = mapper.getTypeFactory().constructParametricType(
                        ArrayList.class, JobListBean.class);
                List<JobListBean> temp = mapper.readValue(bundleResult,javaType);
                //get data successfully
                if(temp != null){
                    mlistContent.clear();
                    for(JobListBean jlb: temp)
                        mlistContent.add(jlb);
                    //update
                    mAdapter.notifyDataSetChanged();
                }
                // no data
                else{
                    Toast.makeText(JobListActivity.this,"服务器没有数据",Toast.LENGTH_LONG);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        Intent intent = getIntent();
        flag = intent.getStringExtra(MainActivity.FLAG);

        //the content of list is depend on the string in intent
//        if(flag.equals(MainActivity.JOB_HISTORY_LIST_FLAG))
//            isHistory = true;
//        else if(flag.equals(MainActivity.JOB_LIST_FLAG))
//            isHistory = false;
        init();
    }

    /**
     * initialize the component
     * */
    private void init(){
        View view = (View)findViewById(R.id.job_list_title_bar);
        mBackBn = (ImageButton)view.findViewById(R.id.back_bn);
        mTitleTv = (TextView)view.findViewById(R.id.title_tv);
        mBackBn.setOnClickListener(this);
        mTitleTv.setText("快递列表");

        mlist = (PullToRefreshListView)findViewById(R.id.job_list);
        mlistContent = new ArrayList<JobListBean>();
        //set pull mode
        mlist.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //set adapter for list view
        mAdapter = new JobAdapter(mlistContent,this);
        mlist.getRefreshableView().setAdapter(mAdapter);
        mlist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //update date from server
                Toast.makeText(JobListActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();
                updateFromServer();
            }
        });
        //set item click listener for list view
        mlist.setOnItemClickListener(this);

//        updateFromServer();
    }

    /**
     * get date from server
     * which kind of date is depend on the value of flag
     * */

    private void updateFromServer(){
        if(flag.equals(MainActivity.JOB_HISTORY_LIST_FLAG)){
            MyHttpHelper.performRequest(MyHttpHelper.URL_TYPE.JOB_HIST,
                    null,mHandler,this);
        }else if(flag.equals(MainActivity.JOB_LIST_FLAG)){
            MyHttpHelper.performRequest(MyHttpHelper.URL_TYPE.JOB_AVAIL,
                    null,mHandler,this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateFromServer();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * finish refresh
     * */
    private void refreshCompleted(){
        mlist.onRefreshComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this,JobDetailActivity.class);
        String jobID = mlistContent.get(i-1).get_id();
        intent.putExtra(MainActivity.JOB_ID,jobID);
        intent.putExtra(MainActivity.FLAG,flag);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        //when pressed the back button
        if(view.getId() == R.id.back_bn){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
