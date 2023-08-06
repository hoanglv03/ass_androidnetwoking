package com.example.ass_androidnetworking.form;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ass_androidnetworking.DTO.ServerRequest;
import com.example.ass_androidnetworking.DTO.ServerResponse;
import com.example.ass_androidnetworking.DTO.User;
import com.example.ass_androidnetworking.Interface.RequestInterface;
import com.example.ass_androidnetworking.R;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterFragment extends Fragment{

    private AppCompatButton btn_register;
    private EditText et_email,et_password,et_name;
    private TextView tv_login;
    private ProgressBar progress;
    public RegisterFragment(){

    }
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =
                inflater.inflate(R.layout.fragment_register,container,false);
        initViews(view);
        return view;
    }
    private void initViews(View view){
        btn_register = view.findViewById(R.id.btn_register);
        tv_login =view.findViewById(R.id.tv_login);
        et_name =view.findViewById(R.id.et_name);
        et_email = view.findViewById(R.id.et_email);
        et_password =view.findViewById(R.id.et_password);
        progress =view.findViewById(R.id.progress);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if(!name.isEmpty() && !email.isEmpty()
                        && !password.isEmpty()) {
                    progress.setVisibility(View.VISIBLE);
                    registerProcess(name,email,password);
                } else {
                    Snackbar.make(getView(), "Không được bỏ trống !",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
    }
    private void registerProcess(String name, String email,String password)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        User user = new User();
        user.setUserName(name);
        user.setEmail(email);
        user.setPassWord(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.register(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                Log.d("Result user", "Result: " + response);
                ServerResponse res = response.body();
                Snackbar.make(getView(), res.getMessage(), Snackbar.LENGTH_LONG).show();
                progress.setVisibility(View.INVISIBLE);
                et_email.setText("");
                et_password.setText("");
                et_name.setText("");
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed" + t);
                Snackbar.make(getView(), t.getLocalizedMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }
  private void goToLogin(){
    LoginFragment loginFragment = new LoginFragment();
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, loginFragment);
        fragmentTransaction.addToBackStack("null");
        fragmentTransaction.commit();;

    }
}

