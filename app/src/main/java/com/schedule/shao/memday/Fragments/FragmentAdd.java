package com.schedule.shao.memday.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.schedule.shao.memday.Obj.MyInfo;
import com.schedule.shao.memday.Obj.Schedule;
import com.schedule.shao.memday.R;
import com.schedule.shao.memday.Utils.ScheduleSQLOperation;
import com.schedule.shao.memday.Utils.SysFunction;

import java.io.FileNotFoundException;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdd extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Context FaContent = getActivity();
    TextView tv_DatePick;
    PopupWindow mPopupWindow;
    EditText et_Content;
    int isAlert = 0;
    Switch btn_isAlert;
    View view;
    ImageButton iv;
    String AlertTime = "";
    String mYear, mMonth, mDay, mHour, mMinutes;
    Button btn_add;
    Calendar cal = Calendar.getInstance();
    private OnFragmentInteractionListener mListener;

    public FragmentAdd() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAdd.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAdd newInstance(String param1, String param2) {
        FragmentAdd fragment = new FragmentAdd();
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
        setHasOptionsMenu(true);
        initTime();
    }
    public void setNumber(){
        final EditText editText = new EditText(getActivity());

        new AlertDialog.Builder(getActivity()).setTitle("请输入要发送的号码")
                .setIcon(android.R.drawable.ic_dialog_info).setView(editText)

                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(editText.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(getActivity(),"请输入电话号码",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(editText.getText().toString().trim().equals(""))
                        {
                            return;
                        }
                        SysFunction.SendMsg(editText.getText().toString().trim(),et_Content.getText().toString(),getActivity());
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_add, container, false);
        // Inflate the layout for this fragment
        tv_DatePick = (TextView) view.findViewById(R.id.DatePick);
        tv_DatePick.setText(cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DATE) + "日");
        tv_DatePick.setOnClickListener(tvOnclickListener);
        et_Content = (EditText) view.findViewById(R.id.et_Content);
        btn_isAlert = (Switch) view.findViewById(R.id.btn_isAlert);
        btn_isAlert.setOnCheckedChangeListener(saveListener);
        btn_add = (Button) view.findViewById(R.id.Save);
        btn_add.setOnClickListener(SaveEvent);
        iv= (ImageButton) view.findViewById(R.id.iv_loadimage);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            Uri uri=data.getData();
            if(!uri.getPath().equals("")) {
                Toast.makeText(getActivity(), "图片路径已保存", Toast.LENGTH_SHORT).show();
                ContentResolver cr = getActivity().getContentResolver();

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                    //将选择的图片，显示在主界面的imageview上
                    ImageView image = (ImageView) view.findViewById(R.id.imageView2);
                    image.setImageBitmap(bitmap);
                    MyInfo.uri=uri;

                }

                catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(),e);
                }
            }
        }

    }

    private void initTime() {
        mYear = cal.get(Calendar.YEAR) + "";
        mMonth = (cal.get(Calendar.MONTH) + 1) + "";
        mDay = cal.get(Calendar.DATE) + "";
        mHour = cal.get(Calendar.HOUR) + "";
        mMinutes = cal.get(Calendar.MINUTE) + "";
        AlertTime = mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinutes;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    protected View.OnClickListener SaveEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SaveContent();
        }
    };

    private void SaveContent() {
        if (et_Content.getText().length() <= 0) {
            Toast.makeText(getActivity(), "请填写内容", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertTime = mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinutes;
        Schedule add = new Schedule(ScheduleSQLOperation.getCode() + "", ""
                , et_Content.getText().toString(), AlertTime, isAlert, AlertTime, 0);
        if (ScheduleSQLOperation.AddSchedule(add)) {
            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
            et_Content.setText("");
            btn_isAlert.setChecked(false);
            Intent intent=new Intent("add");
            getActivity().sendBroadcast(intent);
        } else {
            Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.SaveAndQuit:
                SaveContent();
                break;
            case R.id.SaveAndNew:
                SaveContent();
                break;
            case R.id.btn_SendMsg:
                setNumber();

                break;
            case R.id.btn_Cancel:
                et_Content.setText("");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    View.OnClickListener tvOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShowTimeDialog().show();
        }
    };
    Switch.OnCheckedChangeListener saveListener = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                isAlert = 1;
            } else {
                isAlert = 0;
            }
        }


    };

    public DatePickerDialog.OnDateSetListener mDateListenerDialog = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year + "";
            mMonth = (monthOfYear + 1) + "";
            mDay = dayOfMonth + "";
            tv_DatePick.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
            new TimePickerDialog(getActivity(), mTimeListenerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        }
    };
    public TimePickerDialog.OnTimeSetListener mTimeListenerDialog = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mMinutes = minute + "";
            mHour = hourOfDay + "";
            AlertTime = mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinutes;
            Toast.makeText(getActivity(), AlertTime, Toast.LENGTH_SHORT).show();
        }
    };

    private Dialog ShowTimeDialog() {
        return new DatePickerDialog(getActivity(), mDateListenerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
    }
}
