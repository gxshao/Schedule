package com.schedule.shao.memday.CustomFilter;

import android.widget.Filter;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shao on 2017/1/6.
 */

public class SFilter extends Filter {
    List<HashMap<String, Object>> source;
    SimpleAdapter mAdapter;

    public SFilter(List<HashMap<String, Object>> data, SimpleAdapter adapter) {
        mAdapter = adapter;
        source = data;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults fr = new FilterResults();
        List<HashMap<String, Object>> objs = new ArrayList<>();
        List<Integer> num=new ArrayList<>();
        int i = 0;
        for (HashMap<String, Object> map : source) {
            if (map.get("State").toString().equals(constraint)) {
                num.add(i);
                objs.add(map);
            }else{
                mAdapter.getItem(i);
            }
            i++;
        }

        for (i=0;i<objs.size();i++) {
            source.remove(objs.get(i));
        }
        fr.values = source;
        fr.count = source.size();
        return fr;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        source = (ArrayList<HashMap<String, Object>>) results.values;
        mAdapter.notifyDataSetChanged();
    }
}
