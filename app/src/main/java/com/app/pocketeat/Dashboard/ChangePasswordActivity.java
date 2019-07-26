package com.app.pocketeat.Dashboard;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.app.pocketeat.R;
import com.app.pocketeat.Utils;
import com.app.pocketeat.databinding.ChangepasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {

    ChangepasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        binding= DataBindingUtil.setContentView(this, R.layout.changepassword);
        binding.title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() <= (binding.title.getLeft() + binding.title.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });

        binding.edtOldpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSignin();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtNewpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSignin();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtReenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSignin();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.edtOldpassword.getText().toString().trim().equalsIgnoreCase(binding.edtNewpassword.getText().toString().trim()))
                {
                    //call api
                }else{
                    Utils.showToast(ChangePasswordActivity.this,"New password and re-enter password must be same");
                }
            }
        });
    }

    public void enableSignin()
    {
        if(binding.edtOldpassword.getText().toString().trim().length()>0 && binding.edtOldpassword.getText().toString().trim().length()>0 && binding.edtNewpassword.getText().toString().trim().length()>0)
        {
            binding.btnContinue.setEnabled(true);
        }else {
            binding.btnContinue.setEnabled(false);
        }
    }
}
