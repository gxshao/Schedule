package com.schedule.shao.memday.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.schedule.shao.memday.Detials;
import com.schedule.shao.memday.R;
import com.schedule.shao.memday.Utils.DateUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.schedule.shao.memday.R.id.SearchByDate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMonth#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMonth extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SimpleAdapter mAdapter;
    List<HashMap<String, Object>> data = new ArrayList<>();
    List<HashMap<String, Object>> mBackupData = new ArrayList<>();
    Calendar cal = Calendar.getInstance();
    Menu mMenu;
    int curSortWay = R.id.SortByTime;
    int curSearchWay = 0;
    boolean isHideCom = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String mToday;
    int[] SortGroup = new int[]{R.id.SortByTime, R.id.SortByTitle,
            R.id.SortByPri};
    int[] SearchGroup = new int[]{R.id.SearchByTitle, SearchByDate};
    private OnFragmentInteractionListener mListener;

    public FragmentMonth() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMonth.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMonth newInstance(String param1, String param2) {
        FragmentMonth fragment = new FragmentMonth();
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
        Calendar cal = Calendar.getInstance();
        if (isAdded()) {
            setHasOptionsMenu(true);
            mToday = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /**
         * 载入数据先
         */
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        if (isAdded()) {

        }
        return view;
    }

    AdapterView.OnItemClickListener viewDetials = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), Detials.class);
            Bundle bundle = new Bundle();
            HashMap<String, Object> map = (HashMap<String, Object>) parent.getItemAtPosition(position);
            if (Integer.parseInt(map.get("State").toString()) == 1) {
                return;
            }
            Drawable pri = (Drawable) map.get("prior");
            map.put("prior", "x");
            bundle.putSerializable("map", map);
            intent.putExtra("obj", bundle);
            startActivity(intent);
            map.put("prior", pri);
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            mMenu = menu;
            MenuItem item = menu.findItem(R.id.hideCompleted);
            if (item != null) {
                item.setCheckable(isHideCom);
                item.setChecked(isHideCom);
            }
            SortState(menu.findItem(curSortWay), curSortWay);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public synchronized boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hideCompleted:
                break;
            case R.id.SortByTime:
                break;
            case R.id.SortByTitle:

                break;
            case R.id.SortByPri:
                break;
            case SearchByDate:

                break;
            case R.id.SearchByTitle:
                break;
            case R.id.thisMonth:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SearchByDate() {
        new DatePickerDialog(getActivity(), mDateListenerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
    }

    public DatePickerDialog.OnDateSetListener mDateListenerDialog = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            if (date.equals(mToday)) {
                date = "今天";
            }
            ShowSearchResult("ModTime", date);
        }
    };

    public void SearchByTitle() {
        final EditText editText = new EditText(getActivity());

        new AlertDialog.Builder(getActivity()).setTitle("请输入标题")
                .setIcon(android.R.drawable.ic_dialog_info).setView(editText)

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String keywords = editText.getText().toString().trim();
                        if (keywords.equals("")) {
                            Toast.makeText(getActivity(), "请输入标题", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ShowSearchResult("Content", keywords);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void ShowSearchResult(String content, String keywords) {
        String colmName = content;
        List<HashMap<String, Object>> objs = new ArrayList<>();
        for (HashMap<String, Object> obj : data) {
            if (obj.get(colmName).toString().indexOf(keywords) < 0) {
                objs.add(obj);
            }
        }
        for (int i = 0; i < objs.size(); i++) {
            data.remove(objs.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * SortState
     */
    private void SortState(MenuItem item, int Rid) {
        for (int mid : SortGroup) {
            if (mid == Rid) {
                item.setCheckable(true);
                item.setChecked(true);
            } else {
                mMenu.findItem(mid).setCheckable(false);
                mMenu.findItem(mid).setChecked(false);
            }
        }
    }

    /**
     * SearchState
     */
    private void SearchState(MenuItem item, int Rid) {
        for (int mid : SearchGroup) {
            if (mid == Rid) {
                item.setCheckable(true);
                item.setChecked(true);
            } else {
                mMenu.findItem(mid).setCheckable(false);
                mMenu.findItem(mid).setChecked(false);
            }
        }
    }

    public void SortList(String key) {
        final String con = key;
        if (!data.isEmpty()) {
            Collections.sort(data, new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> lhs, HashMap<String, Object> rhs) {
                    if (isResumed()) {
                        switch (con) {
                            case "AlertTime":
                                Date date1 = DateUtils.stringToDate(lhs.get(con).toString());
                                Date date2 = DateUtils.stringToDate(rhs.get(con).toString());
                                // 对日期字段进行升序，如果欲降序可采用after方法
                                if (date1.after(date2)) {
                                    return 1;
                                }
                                break;
                            case "Content":
                                if (lhs.get(con).toString().compareTo(rhs.get(con).toString()) == 1) {
                                    return 1;
                                }
                                break;
                            case "level":
                                if (Integer.parseInt(lhs.get(con).toString()) > Integer.parseInt(rhs.get(con).toString())) {
                                    return 1;
                                }
                                break;
                        }
                    }
                    return -1;
                }
            });

        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        if (isResumed()) {
        }

        super.onResume();
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
