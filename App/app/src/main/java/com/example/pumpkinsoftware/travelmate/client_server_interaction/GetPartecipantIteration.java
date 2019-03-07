package com.example.pumpkinsoftware.travelmate.client_server_interaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pumpkinsoftware.travelmate.trip.Trip;
import com.example.pumpkinsoftware.travelmate.trips_adapter.TripsAdapter;
import com.example.pumpkinsoftware.travelmate.user.User;
import com.example.pumpkinsoftware.travelmate.users_adapter.UsersAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetPartecipantIteration {
    private Context context;
    private RecyclerView rvUsers;
    UsersAdapter adapter;
    private ProgressBar progressBar;

    public GetPartecipantIteration(Context c, RecyclerView rv, ProgressBar progress) {
        context = c;
        rvUsers = rv;
        progressBar = progress;
    }

    public void getPartecipantFromServer(String query, final String owner_uid, RequestQueue mQueue, final ArrayList<User> users) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, query, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject user = response.getJSONObject(i);
                        String uid = user.getString("uid");
                        String name = user.getString("name");
                        String profile = user.getString("avatar");

                        if(!uid.equals(owner_uid))
                            users.add(new User(uid, name, profile));
                    }
                    adapter = new UsersAdapter(users);
                    // Attach the adapter to the recyclerview to populate items
                    rvUsers.setAdapter(adapter);
                    hideProgressBar();

                } catch (JSONException e) {
                    e.printStackTrace();
                    hideProgressBar();
                    Toast.makeText(context, "Errore: connessione fallita", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hideProgressBar();
                Toast.makeText(context, "Errore: connessione assente", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }

    private void hideProgressBar() { if (progressBar != null) progressBar.setVisibility(View.GONE); }
}
