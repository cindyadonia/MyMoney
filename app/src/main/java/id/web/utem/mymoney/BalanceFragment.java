package id.web.utem.mymoney;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends Fragment {

    private final String apiLink = "http://mymoney-api.utem.web.id/api/v1/";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String user_id = "0";

    TableLayout tlCashBalance;
    TableLayout tlNonCashBalance;

    public BalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_balance, container, false);
        tlCashBalance = rootView.findViewById(R.id.tlCashBalance);
        tlNonCashBalance = rootView.findViewById(R.id.tlNonCashBalance);

        pref = getActivity().getApplicationContext().getSharedPreferences("MyMoney_Pref", 0);
        editor = pref.edit();
        user_id = pref.getString("user_id", "0");

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadData();
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadData() {
        // Remove all data in table
        tlCashBalance.removeAllViews();
        tlNonCashBalance.removeAllViews();

        // Add header for Cash Table
        TableRow cashTableRowHeader = new TableRow(getActivity().getApplicationContext());

        TextView cashTableHeaderNo = new TextView(getActivity().getApplicationContext());
        cashTableHeaderNo.setId(R.id.tvTLHeadNoCashBalance);
        cashTableHeaderNo.setText("No");
        cashTableHeaderNo.setPadding(5, 5, 5, 5);
        cashTableRowHeader.addView(cashTableHeaderNo);

        TextView cashTableHeaderName = new TextView(getActivity().getApplicationContext());
        cashTableHeaderName.setId(R.id.tvTLHeadNameCashBalance);
        cashTableHeaderName.setText("Name");
        cashTableHeaderName.setPadding(5, 5, 5, 5);
        cashTableRowHeader.addView(cashTableHeaderName);

        TextView cashTableHeaderBalance = new TextView(getActivity().getApplicationContext());
        cashTableHeaderBalance.setId(R.id.tvTLHeadBalanceCashBalance);
        cashTableHeaderBalance.setText("Balance");
        cashTableHeaderBalance.setPadding(5, 5, 5, 5);
        cashTableRowHeader.addView(cashTableHeaderBalance);

        TextView cashTableHeaderAction = new TextView(getActivity().getApplicationContext());
        cashTableHeaderAction.setId(R.id.tvTLHeadActionCashBalance);
        cashTableHeaderAction.setText("Action");
        cashTableHeaderAction.setPadding(5, 5, 5, 5);
        cashTableRowHeader.addView(cashTableHeaderAction);

        tlCashBalance.addView(cashTableRowHeader, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT));

        // Add header for Cash Table
        TableRow nonCashTableRowHeader = new TableRow(getActivity().getApplicationContext());

        TextView nonCashTableHeaderNo = new TextView(getActivity().getApplicationContext());
        nonCashTableHeaderNo.setId(R.id.tvTLHeadNoNonCashBalance);
        nonCashTableHeaderNo.setText("No");
        nonCashTableHeaderNo.setPadding(5, 5, 5, 5);
        nonCashTableRowHeader.addView(nonCashTableHeaderNo);

        TextView nonCashTableHeaderBank = new TextView(getActivity().getApplicationContext());
        nonCashTableHeaderBank.setId(R.id.tvTLHeadBankNonCashBalance);
        nonCashTableHeaderBank.setText("Name");
        nonCashTableHeaderBank.setPadding(5, 5, 5, 5);
        nonCashTableRowHeader.addView(nonCashTableHeaderBank);

        TextView nonCashTableHeaderDebitNo = new TextView(getActivity().getApplicationContext());
        nonCashTableHeaderDebitNo.setId(R.id.tvTLHeadDebitNoNonCashBalance);
        nonCashTableHeaderDebitNo.setText("Debit No");
        nonCashTableHeaderDebitNo.setPadding(5, 5, 5, 5);
        nonCashTableRowHeader.addView(nonCashTableHeaderDebitNo);

        TextView nonCashTableHeaderAmount = new TextView(getActivity().getApplicationContext());
        nonCashTableHeaderAmount.setId(R.id.tvTLHeadAmountNoNonCashBalance);
        nonCashTableHeaderAmount.setText("Amount");
        nonCashTableHeaderAmount.setPadding(5, 5, 5, 5);
        nonCashTableRowHeader.addView(nonCashTableHeaderAmount);

        TextView nonCashTableHeaderAction = new TextView(getActivity().getApplicationContext());
        nonCashTableHeaderAction.setId(R.id.tvTLHeadActionNonCashBalance);
        nonCashTableHeaderAction.setText("Action");
        nonCashTableHeaderAction.setPadding(5, 5, 5, 5);
        nonCashTableRowHeader.addView(nonCashTableHeaderAction);

        tlNonCashBalance.addView(nonCashTableRowHeader, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT));


        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequestCashBalance = new StringRequest(Request.Method.GET, apiLink +"balance.php?method=get&cash=1&user_id="+user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() > 0)
                    {
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            int rowId = jsonArray.getJSONObject(i).getInt("id");
                            String rowName = jsonArray.getJSONObject(i).getString("name");
                            String rowAmount = jsonArray.getJSONObject(i).getString("amount");

                            TableRow tableRow = new TableRow(getActivity().getApplicationContext());
                            int tableRowId = 1000 + rowId;
                            tableRow.setId(tableRowId);

                            TextView label_no = new TextView(getActivity().getApplicationContext());
                            label_no.setId(View.generateViewId());
                            String number = String.valueOf(i + 1);
                            label_no.setText(number);
                            label_no.setTextColor(Color.BLACK);
                            label_no.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_no);

                            TextView label_name = new TextView(getActivity().getApplicationContext());
                            label_name.setId(View.generateViewId());
                            label_name.setText(rowName);
                            label_name.setTextColor(Color.BLACK);
                            label_name.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_name);

                            TextView label_amount = new TextView(getActivity().getApplicationContext());
                            label_amount.setId(View.generateViewId());
                            label_amount.setText(rowAmount);
                            label_amount.setTextColor(Color.BLACK);
                            label_amount.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_amount);

                            Button btnRemove = new Button(getActivity().getApplicationContext());
                            btnRemove.setId(View.generateViewId());
                            btnRemove.setText("Delete");
                            btnRemove.setId(rowId);
                            btnRemove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final int dataId = v.getId();
                                    StringRequest stringRequestDeleteBalance = new StringRequest(Request.Method.GET, apiLink +"balance.php?method=delete&id="+dataId, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                                if (jsonObject.getBoolean("success"))
                                                {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Balance deleted successfully!", Toast.LENGTH_SHORT).show();
                                                    loadData();
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "There is an error!", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                Toast.makeText(getActivity().getApplicationContext(), "There is an error!", Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getActivity().getApplicationContext(),"Unable to get data for dashboard", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    requestQueue.add(stringRequestDeleteBalance);
                                }
                            });
                            tableRow.addView(btnRemove);

                            tlCashBalance.addView(tableRow, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT));
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "There is an error!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Unable to get data for dashboard", Toast.LENGTH_SHORT).show();
            }
        });

        StringRequest stringRequestNonCashBalance = new StringRequest(Request.Method.GET, apiLink +"balance.php?method=get&cash=0&user_id="+user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() > 0)
                    {
                        for (int i = 0; i < jsonArray.length(); i++)
                        {

                            int rowId = jsonArray.getJSONObject(i).getInt("id");
                            String rowBank = jsonArray.getJSONObject(i).getString("name");
                            String rowDebitNo = jsonArray.getJSONObject(i).getString("bank_account_no");
                            String rowAmount = jsonArray.getJSONObject(i).getString("amount");

                            TableRow tableRow = new TableRow(getActivity().getApplicationContext());

                            TextView label_no = new TextView(getActivity().getApplicationContext());
                            label_no.setId(View.generateViewId());
                            String number = String.valueOf(i + 1);
                            label_no.setText(number);
                            label_no.setTextColor(Color.BLACK);
                            label_no.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_no);

                            TextView label_bank = new TextView(getActivity().getApplicationContext());
                            label_bank.setId(View.generateViewId());
                            label_bank.setText(rowBank);
                            label_bank.setTextColor(Color.BLACK);
                            label_bank.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_bank);

                            TextView label_debit = new TextView(getActivity().getApplicationContext());
                            label_debit.setId(View.generateViewId());
                            label_debit.setText(rowDebitNo);
                            label_debit.setTextColor(Color.BLACK);
                            label_debit.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_debit);

                            TextView label_amount = new TextView(getActivity().getApplicationContext());
                            label_amount.setId(View.generateViewId());
                            label_amount.setText(rowAmount);
                            label_amount.setTextColor(Color.BLACK);
                            label_amount.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_amount);

                            Button btnRemove = new Button(getActivity().getApplicationContext());
                            btnRemove.setId(View.generateViewId());
                            btnRemove.setText("Delete");
                            btnRemove.setId(rowId);
                            btnRemove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final int dataId = v.getId();

                                    StringRequest stringRequestDeleteBalance = new StringRequest(Request.Method.GET, apiLink +"balance.php?method=delete&id="+dataId, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                                if (jsonObject.getBoolean("success"))
                                                {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Balance deleted successfully!", Toast.LENGTH_SHORT).show();
                                                    loadData();
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "There is an error!", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                Toast.makeText(getActivity().getApplicationContext(), "There is an error!", Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getActivity().getApplicationContext(),"Unable to get data for dashboard", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    requestQueue.add(stringRequestDeleteBalance);
                                }
                            });
                            tableRow.addView(btnRemove);

                            tlNonCashBalance.addView(tableRow, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT));
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "There is an error!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Unable to get data for dashboard", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequestCashBalance);
        requestQueue.add(stringRequestNonCashBalance);
    }

}
