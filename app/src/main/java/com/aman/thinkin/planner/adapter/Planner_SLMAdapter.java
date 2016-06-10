package com.aman.thinkin.planner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.thinkin.R;
import com.aman.thinkin.pojos.LineItem;
import com.tonicartos.superslim.GridSLM;

import java.util.ArrayList;

/**
 * Created by 1305274 on 03-05-2016.
 */
public class Planner_SLMAdapter extends RecyclerView.Adapter<Planner_SLMAdapter.DatesViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0x01;

    private static final int VIEW_TYPE_CONTENT = 0x00;

    private static final int LINEAR = 0;

    private final ArrayList<LineItem> mItems;

    private int mHeaderDisplay;

    private boolean mMarginsFixed;

    private final Context mContext;

    public Planner_SLMAdapter(Context context, int headerMode) {
        mContext=context;
        mItems = new ArrayList<>();
        final String[] remindersList=context.getResources().getStringArray(R.array.country_names);
        mHeaderDisplay=headerMode;
        String lastHeader="";
        int sectionManager=-1;
        int headerCount=0;
        int sectionFirstPosition=0;
        for(int i=0;i<remindersList.length;i++){
            String header=remindersList[i].substring(0,2);
            if(!TextUtils.equals(lastHeader,header)){
                sectionManager = (sectionManager + 1) % 2;
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;
                mItems.add(new LineItem(header, true, sectionManager, sectionFirstPosition));
            }
            mItems.add(new LineItem(remindersList[i].substring(2), false, sectionManager, sectionFirstPosition));
        }
    }


    @Override
    public DatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType==VIEW_TYPE_HEADER){
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item,parent,false);
        }
        else
        {
            v=LayoutInflater.from(parent.getContext()).inflate(R.layout.text_line_item, parent, false);
        }
        return new DatesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DatesViewHolder holder, int position) {
        LineItem item=mItems.get(position);
        View itemView=holder.itemView;
        holder.bindItem(item.text);

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        if(item.isHeader){
            lp.headerDisplay=mHeaderDisplay;
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.headerEndMarginIsAuto = !mMarginsFixed;
            lp.headerStartMarginIsAuto = !mMarginsFixed;
            lp.setSlm(LINEAR);
            lp.setColumnWidth(96);
            lp.setFirstPosition(item.sectionFirstPosition);
            itemView.setLayoutParams(lp);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class DatesViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        public DatesViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }

        public void bindItem(String text) {
            mTextView.setText(text);
        }

        @Override
        public String toString() {
            return mTextView.getText().toString();
        }
    }
}
