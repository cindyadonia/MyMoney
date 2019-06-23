package id.web.utem.mymoney;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class IncomeFragment extends Fragment {

    private final String apiLink = "http://mymoney-api.utem.web.id/api/v1/";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String user_id = "0";

    TableLayout tlIncomes;



    public IncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_balance, container, false);
        tlIncomes = rootView.findViewById(R.id.tlIncomes);

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
        tlIncomes.removeAllViews();

        // Add header for Incomes Table
        TableRow incomesTableRowHeader = new TableRow(getActivity().getApplicationContext());

        TextView incomesTableHeaderNo = new TextView(getActivity().getApplicationContext());
        incomesTableHeaderNo.setId(R.id.tvTLHeadNoIncomes);
        incomesTableHeaderNo.setText("No");
        incomesTableHeaderNo.setPadding(5, 5, 5, 5);
        incomesTableRowHeader.addView(incomesTableHeaderNo);

        TextView incomesTableHeaderDate = new TextView(getActivity().getApplicationContext());
        incomesTableHeaderDate.setId(R.id.tvTLHeadDateIncomes);
        incomesTableHeaderDate.setText("Date");
        incomesTableHeaderDate.setPadding(5, 5, 5, 5);
        incomesTableRowHeader.addView(incomesTableHeaderDate);

        TextView incomesTableHeaderDescription = new TextView(getActivity().getApplicationContext());
        incomesTableHeaderDescription.setId(R.id.tvTLHeadDescriptionIncomes);
        incomesTableHeaderDescription.setText("Description");
        incomesTableHeaderDescription.setPadding(5, 5, 5, 5);
        incomesTableRowHeader.addView(incomesTableHeaderDescription);

        TextView cashTableHeaderAmount = new TextView(getActivity().getApplicationContext());
        cashTableHeaderAmount.setId(R.id.tvTLHeadAmountIncomes);
        cashTableHeaderAmount.setText("Amount");
        cashTableHeaderAmount.setPadding(5, 5, 5, 5);
        incomesTableRowHeader.addView(cashTableHeaderAmount);

        TextView incomesTableHeaderType = new TextView(getActivity().getApplicationContext());
        incomesTableHeaderType.setId(R.id.tvTLHeadTypeIncomes);
        incomesTableHeaderType.setText("Income Type");
        incomesTableHeaderType.setPadding(5, 5, 5, 5);
        incomesTableRowHeader.addView(incomesTableHeaderType);

        TextView incomesTableHeaderSource = new TextView(getActivity().getApplicationContext());
        incomesTableHeaderSource.setId(R.id.tvTLHeadSourceIncomes);
        incomesTableHeaderSource.setText("Source");
        incomesTableHeaderSource.setPadding(5, 5, 5, 5);
        incomesTableRowHeader.addView(incomesTableHeaderSource);

        TextView cashTableHeaderAction = new TextView(getActivity().getApplicationContext());
        cashTableHeaderAction.setId(R.id.tvTLHeadActionIncomes);
        cashTableHeaderAction.setText("Action");
        cashTableHeaderAction.setPadding(5, 5, 5, 5);
        incomesTableRowHeader.addView(cashTableHeaderAction);

        tlIncomes.addView(incomesTableRowHeader, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT));

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequestIncomes = new StringRequest(Request.Method.GET, apiLink +"/income.php?method=get&user_id="+user_id, new Response.Listener<String>() {
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
                            String rowDate = jsonArray.getJSONObject(i).getString("Date");
                            String rowDescription = jsonArray.getJSONObject(i).getString("Description");
                            String rowAmount =  jsonArray.getJSONObject(i).getString("amount");
                            String rowTypes = jsonArray.getJSONObject(i).getString("income_tyoe_name");
                            String rowSource= jsonArray.getJSONObject(i).getString("balance_name");

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

                            TextView label_date = new TextView(getActivity().getApplicationContext());
                            label_date.setId(View.generateViewId());
                            label_date.setText(rowDate);
                            label_date.setTextColor(Color.BLACK);
                            label_date.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_date);

                            TextView label_description = new TextView(getActivity().getApplicationContext());
                            label_description.setId(View.generateViewId());
                            label_description.setText(rowDescription);
                            label_description.setTextColor(Color.BLACK);
                            label_description.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_description);

                            TextView label_amount = new TextView(getActivity().getApplicationContext());
                            label_amount.setId(View.generateViewId());
                            label_amount.setText(rowAmount);
                            label_amount.setTextColor(Color.BLACK);
                            label_amount.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_amount);

                            TextView label_types = new TextView(getActivity().getApplicationContext());
                            label_types.setId(View.generateViewId());
                            label_types.setText(rowTypes);
                            label_types.setTextColor(Color.BLACK);
                            label_types.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_types);

                            TextView label_source = new TextView(getActivity().getApplicationContext());
                            label_source.setId(View.generateViewId());
                            label_source.setText(rowSource);
                            label_source.setTextColor(Color.BLACK);
                            label_source.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_source);

                            Button btnRemove = new Button(getActivity().getApplicationContext());
                            btnRemove.setId(View.generateViewId());
                            btnRemove.setText("Delete");
                            btnRemove.setId(rowId);
                            btnRemove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final int dataId = v.getId();
                                    StringRequest stringRequestDeleteBalance = new StringRequest(Request.Method.GET, apiLink +"income.php?method=delete&id+"+dataId, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                                if (jsonObject.getBoolean("success"))
                                                {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Incomes deleted successfully!", Toast.LENGTH_SHORT).show();
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

                            tlIncomes.addView(tableRow, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT));
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

        requestQueue.add(stringRequestIncomes);
    }
}
