package id.web.utem.mymoney;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;


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
        return inflater.inflate(R.layout.fragment_outcome, container, false);
    }

}
