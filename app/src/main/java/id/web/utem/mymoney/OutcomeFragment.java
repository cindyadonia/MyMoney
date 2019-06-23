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
public class OutcomeFragment extends Fragment {

    private final String apiLink = "http://mymoney-api.utem.web.id/api/v1/";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String user_id = "0";

    TableLayout tlOutcomes;


    public OutcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_outcome, container, false);
        tlOutcomes = rootView.findViewById(R.id.tlIncomes);

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
        tlOutcomes.removeAllViews();

        // Add header for Outcomes Table
        TableRow OutcomesTableRowHeader = new TableRow(getActivity().getApplicationContext());

        TextView OutcomesTableHeaderNo = new TextView(getActivity().getApplicationContext());
        OutcomesTableHeaderNo.setId(R.id.tvTLHeadNoOutcomes);
        OutcomesTableHeaderNo.setText("No");
        OutcomesTableHeaderNo.setPadding(5, 5, 5, 5);
        OutcomesTableRowHeader.addView(OutcomesTableHeaderNo);

        TextView OutcomesTableHeaderDate = new TextView(getActivity().getApplicationContext());
        OutcomesTableHeaderDate.setId(R.id.tvTLHeadDateOutcomes);
        OutcomesTableHeaderDate.setText("Date");
        OutcomesTableHeaderDate.setPadding(5, 5, 5, 5);
        OutcomesTableRowHeader.addView(OutcomesTableHeaderDate);

        TextView OutcomesTableHeaderDescription = new TextView(getActivity().getApplicationContext());
        OutcomesTableHeaderDescription.setId(R.id.tvTLHeadDescriptionOutcomes);
        OutcomesTableHeaderDescription.setText("Description");
        OutcomesTableHeaderDescription.setPadding(5, 5, 5, 5);
        OutcomesTableRowHeader.addView(OutcomesTableHeaderDescription);

        TextView cashTableHeaderAmount = new TextView(getActivity().getApplicationContext());
        cashTableHeaderAmount.setId(R.id.tvTLHeadAmountOutcomes);
        cashTableHeaderAmount.setText("Amount");
        cashTableHeaderAmount.setPadding(5, 5, 5, 5);
        OutcomesTableRowHeader.addView(cashTableHeaderAmount);

        TextView OutcomesTableHeaderType = new TextView(getActivity().getApplicationContext());
        OutcomesTableHeaderType.setId(R.id.tvTLHeadTypeOutcomes);
        OutcomesTableHeaderType.setText("Outcome Type");
        OutcomesTableHeaderType.setPadding(5, 5, 5, 5);
        OutcomesTableRowHeader.addView(OutcomesTableHeaderType);

        TextView OutcomesTableHeaderDestination = new TextView(getActivity().getApplicationContext());
        OutcomesTableHeaderDestination.setId(R.id.tvTLHeadDestinationOutcomes);
        OutcomesTableHeaderDestination.setText("Destination");
        OutcomesTableHeaderDestination.setPadding(5, 5, 5, 5);
        OutcomesTableRowHeader.addView(OutcomesTableHeaderDestination);

        TextView cashTableHeaderAction = new TextView(getActivity().getApplicationContext());
        cashTableHeaderAction.setId(R.id.tvTLHeadActionOutcomes);
        cashTableHeaderAction.setText("Action");
        cashTableHeaderAction.setPadding(5, 5, 5, 5);
        OutcomesTableRowHeader.addView(cashTableHeaderAction);

        tlOutcomes.addView(OutcomesTableRowHeader, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT));

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequestOutcomes = new StringRequest(Request.Method.GET, apiLink +"/Outcome.php?method=get&user_id="+user_id, new Response.Listener<String>() {
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
                            String rowDate = jsonArray.getJSONObject(i).getString("date");
                            String rowDescription = jsonArray.getJSONObject(i).getString("description");
                            String rowAmount =  jsonArray.getJSONObject(i).getString("amount");
                            String rowTypes = jsonArray.getJSONObject(i).getString("outcome_type_name");
                            String rowDestination= jsonArray.getJSONObject(i).getString("balance_name");

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

                            TextView label_Destination = new TextView(getActivity().getApplicationContext());
                            label_Destination.setId(View.generateViewId());
                            label_Destination.setText(rowDestination);
                            label_Destination.setTextColor(Color.BLACK);
                            label_Destination.setPadding(5, 5, 5, 5);
                            tableRow.addView(label_Destination);

                            Button btnRemove = new Button(getActivity().getApplicationContext());
                            btnRemove.setId(View.generateViewId());
                            btnRemove.setText("Delete");
                            btnRemove.setId(rowId);
                            btnRemove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final int dataId = v.getId();
                                    StringRequest stringRequestDeleteBalance = new StringRequest(Request.Method.GET, apiLink +"Outcome.php?method=delete&id+"+dataId, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                                if (jsonObject.getBoolean("success"))
                                                {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Outcomes deleted successfully!", Toast.LENGTH_SHORT).show();
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

                            tlOutcomes.addView(tableRow, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT));
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

        requestQueue.add(stringRequestOutcomes);
    }
}
