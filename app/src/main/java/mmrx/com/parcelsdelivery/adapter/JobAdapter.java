package mmrx.com.parcelsdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mmrx.com.parcelsdelivery.R;
import mmrx.com.parcelsdelivery.bean.JobListBean;

/**
 * Created by mmrx on 2015/5/24.
 */
public class JobAdapter extends BaseAdapter{

    private Context mContext;
    private List<JobListBean> mList;
    private LayoutInflater mInflater;
    public JobAdapter(){
        super();
    }

    public JobAdapter(List<JobListBean> list,Context context){
        this.mList = list;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setmList(List<JobListBean> mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
        if(view == null){
            view = mInflater.inflate(R.layout.job_list_item_layout,null);
            holder = new ViewHolder();
            holder.sourceTv = (TextView)view.findViewById(R.id.job_list_source_tv);
            holder.destTv = (TextView)view.findViewById(R.id.job_list_desti_tv);
            holder.dateTv = (TextView)view.findViewById(R.id.job_item_date);
            holder.statusTv = (TextView)view.findViewById(R.id.job_item_status);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        JobListBean bean = mList.get(i);
        holder.sourceTv.setText("出发地: " + bean.getSource_addr());
        holder.destTv.setText("目的地: " + bean.getDestination_addr());
        holder.dateTv.setText("要求送达时间: " + bean.getDue_date());
        final String status = bean.getJob_status();
        if(status.equals("1"))
            holder.statusTv.setText("派运状态：等待接单");
        else if(status.equals("2"))
            holder.statusTv.setText("派运状态：正在派运");
        else if(status.equals("3"))
            holder.statusTv.setText("派运状态：已送达");
        else
            holder.statusTv.setText("派运状态：别问我问服务器");

        return view;
    }

    private static class ViewHolder{
        private TextView sourceTv;
        private TextView destTv;
        private TextView dateTv;
        private TextView statusTv;
    }
}
