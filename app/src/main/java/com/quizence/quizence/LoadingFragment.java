package com.quizence.quizence;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoadingFragment extends Fragment {

    private TextView mUpdateTextView;
    private Button mTryAgainButton;
    private ProgressBar mProgressBar;
    private NetworkUpdateListener mNetworkUpdateListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading_animation, container, false);
        mProgressBar = view.findViewById(R.id.loading_animation_progress_bar);
        mUpdateTextView = view.findViewById(R.id.loading_animation_update_textview);
        mTryAgainButton = view.findViewById(R.id.loading_animation_try_again);
        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
//                ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ALPHA,
//                        1, 0);
//                animator.setStartDelay(100);
//                animator.setDuration(500).start();
                setProgressBarVisibility(View.VISIBLE);
                mNetworkUpdateListener.onNetworkUpdate();
            }
        });

        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo == null){
           updateTextView("No Internet Access");
        }else{
            if(networkInfo.isConnected()){
                updateTextView("Connected");
            }
        }

        //TODO: update textview to show status of the data fetch
        return view;
    }

    public void updateTextView(String update){
        mUpdateTextView.setText(update);
    }

    public void setTryAgainButtonVisibility(int visibility){
        mTryAgainButton.setVisibility(visibility);
        setProgressBarVisibility(View.INVISIBLE);
    }

    public void setProgressBarVisibility(int visibility){
        mProgressBar.setVisibility(visibility);
    }

    public void setNetworkUpdateListener(NetworkUpdateListener updateListener){
        mNetworkUpdateListener = updateListener;
    }

    public interface NetworkUpdateListener{
        public void onNetworkUpdate();
    }

}
