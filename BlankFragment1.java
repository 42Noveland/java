package com.example.text;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.LogRecord;

public class BlankFragment1 extends Fragment {
    private final String N = "MainActivity";
    private ArrayList<Article> myData;
    private int page = 0;
    private sendhttprequest sendhttprequest = new sendhttprequest();
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv1;
    private boolean requset;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView rv1 = Objects.requireNonNull(getView()).findViewById(R.id.rv1);
        rv1.setLayoutManager(new LinearLayoutManager(getContext()));
        sendhttprequest.dorequest("https://www.wanandroid.com/article/list/0/json", "GET", new CallBackListener() {
            @Override
            public void onFinish(String out) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(out);
                    JSONObject data = jsonObject.optJSONObject("data");
                    assert data != null;
                    JSONArray datas = data.optJSONArray("datas");
                    myData = new ArrayList<>();
                    for (int i = 0; i < Objects.requireNonNull(datas).length(); i++) {
                        JSONObject jsonObject1 = datas.optJSONObject(i);
                        if (jsonObject1 != null) {
                            Article temp = new Article();
                            String title = jsonObject1.optString("title");
                            String time = jsonObject1.optString("time");
                            String shareUser = jsonObject1.optString("shareUser");
                            String link = jsonObject1.optString("link");
                            Log.e("MainActivity",link);
                            temp.setUsername(shareUser);
                            temp.setTitle(title);
                            temp.setDate(time);
                            temp.setLink(link);
                            myData.add(temp);
                        }
                    }
                    getActivity().runOnUiThread(() -> rv1.setAdapter(new RecycleViewAdapter(myData)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception ex) {
            }
        });
        rv1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //根据自己的应用场景，也可以在这里调用
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e(N, "--------------------------------------");
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                Log.e(N, "firstCompletelyVisibleItemPosition: " + firstCompletelyVisibleItemPosition);
                if (firstCompletelyVisibleItemPosition == 0)
                    Log.e(N, "滑动到顶部");
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                Log.e(N, "lastCompletelyVisibleItemPosition: " + lastCompletelyVisibleItemPosition);
                if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    page++;
                    sendhttprequest.dorequest("https://www.wanandroid.com/article/list/"+page+"/json", "GET", new CallBackListener() {
                        @Override
                        public void onFinish(String out) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(out);
                                JSONObject data = jsonObject.optJSONObject("data");
                                assert data != null;
                                JSONArray datas = data.optJSONArray("datas");
                                for (int i = 0; i < Objects.requireNonNull(datas).length(); i++) {
                                    JSONObject jsonObject1 = datas.optJSONObject(i);
                                    if (jsonObject1 != null) {
                                        Article temp = new Article();
                                        String title = jsonObject1.optString("title");
                                        String time = jsonObject1.optString("time");
                                        String shareUser = jsonObject1.optString("shareUser");
                                        temp.setUsername(shareUser);
                                        temp.setTitle(title);
                                        temp.setDate(time);
                                        myData.add(temp);
                                    }
                                }
                                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                                    RecycleViewAdapter ra = new RecycleViewAdapter(myData);
                                    rv1.setAdapter(ra);
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception ex) {
                        }
                    });
                }
            }
        });
        initView();
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swrl);
        rv1 = (RecyclerView) getView().findViewById(R.id.rv1);
        myData = new ArrayList<>();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "刷新完毕", Toast.LENGTH_SHORT).show();//刷新期间的action
                                sendhttprequest.dorequest("https://www.wanandroid.com/article/list/0/json", "GET", new CallBackListener() {
                                    @Override
                                    public void onFinish(String out) {
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(out);
                                            JSONObject data = jsonObject.optJSONObject("data");
                                            assert data != null;
                                            JSONArray datas = data.optJSONArray("datas");
                                            myData = new ArrayList<>();
                                            for (int i = 0; i < Objects.requireNonNull(datas).length(); i++) {
                                                JSONObject jsonObject1 = datas.optJSONObject(i);
                                                if (jsonObject1 != null) {
                                                    Article temp = new Article();
                                                    String title = jsonObject1.optString("title");
                                                    String time = jsonObject1.optString("time");
                                                    String shareUser = jsonObject1.optString("shareUser");
                                                    temp.setUsername(shareUser);
                                                    temp.setTitle(title);
                                                    temp.setDate(time);
                                                    myData.add(temp);
                                                }
                                            }
                                            getActivity().runOnUiThread(() -> rv1.setAdapter(new RecycleViewAdapter(myData)));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Exception ex) {
                                    }
                                });
                                swipeRefreshLayout.setRefreshing(false);//终止刷新（不然就会一直转圈圈）
                            }
                        });
                    }
                }, 2000);
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank1, container, false);
    }
}
