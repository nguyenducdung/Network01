package rikkeisoft.nguyenducdung.com.network01.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import rikkeisoft.nguyenducdung.com.network01.R;
import rikkeisoft.nguyenducdung.com.network01.custom.UserAdapter;
import rikkeisoft.nguyenducdung.com.network01.model.User;

public class MainActivity extends AppCompatActivity {
    private ArrayList<User> users = new ArrayList<>();
    private UserAdapter userAdapter;
    private RecyclerView rcUser;
    private TextView tvDetail;
    private TextView tvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternetConnection();
        init();
        initView();
        new ReadJSON().execute("https://jsonplaceholder.typicode.com/posts");
    }

    private void init() {
        rcUser = findViewById(R.id.rc_user);
        tvDetail = findViewById(R.id.tv_detail);
        tvList = findViewById(R.id.tv_list);
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(this, "No default network is currently active", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!networkInfo.isConnected()) {
            Toast.makeText(this, "Network is connected", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!networkInfo.isAvailable()) {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, "Network OK", Toast.LENGTH_LONG).show();
        return true;
    }

    private void initView() {
          RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rc_user);
          recyclerView.setHasFixedSize(true);
          LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
          recyclerView.setLayoutManager(layoutManager);
          DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
          recyclerView.addItemDecoration(dividerItemDecoration);
          userAdapter = new UserAdapter(users, getApplicationContext(), new UserAdapter.ItemClick() {
              @Override
              public void onClickItem(int i) {
                 tvList.setVisibility(View.GONE);
                 rcUser.setVisibility(View.GONE);
                 tvDetail.setVisibility(View.VISIBLE);
                 String body = users.get(i).getBody();
                 tvDetail.setText(body);
              }
          });
          recyclerView.setAdapter(userAdapter);
    }

    private class ReadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();

            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }

                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray array = new JSONArray(s);
                for (int i = 0; i<array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    int userId = object.getInt("userId");
                    int id = object.getInt("id");
                    String title = object.getString("title");
                    String body = object.getString("body");
                    users.add(new User(id, userId, title, body));
                }
                userAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
