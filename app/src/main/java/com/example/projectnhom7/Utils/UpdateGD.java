package com.example.projectnhom7.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.projectnhom7.DialogCloseListener;
import com.example.projectnhom7.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class UpdateGD extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newNameText, newMoneyText, newDateText;
    private Button UpdateButton;

    private GiaoDichHandler db;

    public static UpdateGD newInstance(){
        return new UpdateGD();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.update_giaodich, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newNameText = requireView().findViewById(R.id.UpdateGDNameText);
        newMoneyText = requireView().findViewById(R.id.UpdateGDMoneyText);
        newDateText = requireView().findViewById(R.id.UpdateGDDateText);
        UpdateButton = getView().findViewById(R.id.UpdateGDButton);

        final Bundle bundle = getArguments();
        if(bundle != null){
            String name = bundle.getString("name");
            String money = String.valueOf(bundle.getInt("money"));
            String date = bundle.getString("date");

            newNameText.setText(name);
            newMoneyText.setText(money);
            newDateText.setText(date);
            assert name != null;
            if(name.length()>0)
                UpdateButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        }

        db = new GiaoDichHandler(getActivity());

        newNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    UpdateButton.setEnabled(false);
                    UpdateButton.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    UpdateButton.setEnabled(false);
                    UpdateButton.setTextColor(Color.GRAY);
                }
                else{
                    UpdateButton.setEnabled(true);
                    UpdateButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newMoneyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    UpdateButton.setEnabled(false);
                    UpdateButton.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    UpdateButton.setEnabled(false);
                    UpdateButton.setTextColor(Color.GRAY);
                }
                else{
                    UpdateButton.setEnabled(true);
                    UpdateButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newDateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    UpdateButton.setEnabled(false);
                    UpdateButton.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    UpdateButton.setEnabled(false);
                    UpdateButton.setTextColor(Color.GRAY);
                }
                else{
                    UpdateButton.setEnabled(true);
                    UpdateButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newNameText.getText().toString();
                int money = Integer.parseInt(newMoneyText.getText().toString());
                String date = newDateText.getText().toString();

                db.updateGiaoDich(bundle.getInt("id"), name, money, date);
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}