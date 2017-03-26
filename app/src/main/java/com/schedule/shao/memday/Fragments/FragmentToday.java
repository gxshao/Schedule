package com.schedule.shao.memday.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.schedule.shao.memday.Detials;
import com.schedule.shao.memday.Obj.Schedule;
import com.schedule.shao.memday.R;
import com.schedule.shao.memday.Service.ScheduleService;
import com.schedule.shao.memday.Utils.ScheduleSQLOperation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentToday#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentToday extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HashMap<String, Object> mSelectedMap;
    private int mSelectPos;
    List<HashMap<String, Object>> dataUncom, dataCom;
    Calendar cal = Calendar.getInstance();
    ListView lv_uncom;
    SimpleAdapter mAdapterUncom, mAdapterCom;
    private boolean isCreate = true;
    private OnFragmentInteractionListener mListener;

    public FragmentToday() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentToday.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentToday newInstance(String param1, String param2) {
        FragmentToday fragment = new FragmentToday();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        lv_uncom = (ListView) view.findViewById(R.id.list_Uncom);
        return view;
    }

    @Override
    public void onResume() {
        loadUnComSchedule();
        loadComSchedule();
        if (isCreate) {
            if (dataCom.size() == 0 && dataUncom.size() == 0) {
                Toast.makeText(getActivity(), "没有记录!", Toast.LENGTH_SHORT).show();
            }
            isCreate = false;
            StartService();
        }

        super.onResume();
    }

    private void StartService() {
        Intent intent = new Intent(getActivity(), ScheduleService.class);
        Bundle bundle = new Bundle();
        List<Schedule> schedules = new ArrayList<>();
        for (int i = 0; i < dataUncom.size(); i++) {
            HashMap<String, Object> map = dataUncom.get(i);
            Schedule temp = new Schedule(map.get("sCode").toString(), map.get("sTitle").toString(), map.get("Content").toString(), map.get("AlertTime").toString(), Integer.parseInt(map.get("isAlert").toString()), map.get("ModTime").toString(), Integer.parseInt(map.get("State").toString()));
            schedules.add(temp);
        }
        bundle.putSerializable("list", (Serializable) schedules);
        intent.putExtra("obj", bundle);

        getActivity().startService(intent);
    }

    /**
     * 导入已完成数据
     */
    private void loadComSchedule() {
        List<Schedule> schedules = ScheduleSQLOperation.QueryToday(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE), 1);
        dataCom = new ArrayList<>();
        for (Schedule item : schedules) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("sTitle", item.getsTitle());
            map.put("sCode", item.getsCode());
            map.put("Content", item.getContent());
            map.put("AlertTime", item.getAlertTime());
            map.put("ModTime", item.getModTime());
            map.put("isAlert", item.getIsAlert());
            map.put("State", item.getState());
            //设定优先级图标

            dataCom.add(map);
        }

        mAdapterCom = new SimpleAdapter(getActivity(), dataCom, R.layout.schedulelv, new String[]{"Content", "prior"}, new int[]{R.id.btn_chk, R.id.img_pri}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox cb = (CheckBox) view.findViewById(R.id.btn_chk);
                HashMap<String, Object> map = (HashMap<String, Object>) this.getItem(position);
                //设定cb状态
                cb.setChecked(true);
                cb.setEnabled(false);
                return view;
            }
        };
        mAdapterCom.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else
                    return false;
            }
        });
    }
    // Inflate the layout for this fragment

    /**
     * 导入未完成数据
     */
    public void loadUnComSchedule() {
        List<Schedule> schedules = ScheduleSQLOperation.QueryToday(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE), 0);
        dataUncom = new ArrayList<>();
        for (Schedule item : schedules) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("sTitle", item.getsTitle());
            map.put("sCode", item.getsCode());
            map.put("Content", item.getContent());
            map.put("AlertTime", item.getAlertTime());
            map.put("ModTime", item.getModTime());
            map.put("isAlert", item.getIsAlert());
            map.put("State", item.getState());
            //设定优先级图标
            dataUncom.add(map);
        }
        mAdapterUncom = new SimpleAdapter(getActivity(), dataUncom, R.layout.schedulelv, new String[]{"Content", "prior"}, new int[]{R.id.btn_chk, R.id.img_pri}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox cb = (CheckBox) view.findViewById(R.id.btn_chk);
                cb.setClickable(false);
                //设定cb状态
                return view;
            }
        };
        mAdapterUncom.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else
                    return false;
            }
        });
        registerForContextMenu(lv_uncom);
        lv_uncom.setOnItemClickListener(viewDetials);
        lv_uncom.setAdapter(mAdapterUncom);
        lv_uncom.setOnItemLongClickListener(changeState);

    }

    AdapterView.OnItemClickListener viewDetials = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(getActivity(), Detials.class);
            Bundle bundle=new Bundle();
            HashMap<String,Object> map= (HashMap<String, Object>) parent.getItemAtPosition(position);
            bundle.putSerializable("map",map);
            intent.putExtra("obj", bundle);
            startActivity(intent);
        }
    };
    AdapterView.OnItemLongClickListener changeState = new AdapterView.OnItemLongClickListener() {
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mSelectedMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
            mSelectPos = position;
            return false;
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, "删除");
        menu.add(0, 1, 0, "取消");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                ScheduleSQLOperation.DelScheduleByScode(mSelectedMap.get("sCode").toString());
                Toast.makeText(getActivity(),"删除成功!",Toast.LENGTH_SHORT).show();
                loadUnComSchedule();
                break;
            case 1:
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void ModData(Schedule mSelectedObj) {
        boolean flag = ScheduleSQLOperation.ModSchedule(mSelectedObj);
        if (flag) {
            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
        }
        dataUncom.remove(mSelectedMap);
        dataCom.add(mSelectedMap);
        mAdapterUncom.notifyDataSetChanged();
        mAdapterCom.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context) {
        super.onAttach((Activity) context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
