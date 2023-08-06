package com.example.ass_androidnetworking.form;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ass_androidnetworking.DTO.ServerRequest;
import com.example.ass_androidnetworking.DTO.ServerResponse;
import com.example.ass_androidnetworking.DTO.User;
import com.example.ass_androidnetworking.HomeScreen;
import com.example.ass_androidnetworking.Interface.RequestInterface;
import com.example.ass_androidnetworking.R;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    private AppCompatButton btn_login;
    private EditText edt_email, edt_password;
    private TextView tv_register;
    private ProgressBar progressBar;
    private SharedPreferences pref ;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container,
                false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        pref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        btn_login =  view.findViewById(R.id.btn_login);
        edt_email = view.findViewById(R.id.et_email);
        edt_password = view.findViewById(R.id.et_password);
        tv_register = view.findViewById(R.id.tv_register);
        progressBar = view.findViewById(R.id.progress);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    loginProcess(email, password);
                } else {
                    Snackbar.make(getView(), "Không được bỏ trống !", Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    private void loginProcess(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        User user = new User();
        user.setEmail(email);
        user.setPassWord(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.login(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    ServerResponse res = response.body();
                    if(res.getResult().equals(Constants.SUCCESS)){
                        Snackbar.make(getView(), res.getMessage(), Snackbar.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(Constants.IS_LOGGED_IN, true);
                        editor.putString(Constants.EMAIL, res.getUser().getEmail());
                        editor.putString(Constants.NAME, res.getUser().getUserName());
                        editor.putString(Constants.UNIQUE_ID, res.getUser().getId());
                        editor.putString("okok","okokkkkkk");
                        editor.commit();
                        goToProfile();
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(getView(),  res.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG, "failed" + t);
                Snackbar.make(getView(), t.getLocalizedMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void goToRegister() {
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, registerFragment);
        fragmentTransaction.addToBackStack("null");
        fragmentTransaction.commit();
    }

    private void goToProfile() {
        Intent intent = new Intent(getActivity(), HomeScreen.class);
        startActivity(intent);
    }
}