package com.example.sushilverma.mavync;





import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class T_C extends Fragment implements OnClickListener
{
    Context context;
    private Intent intent;
    TextView privacy_policy;
    TextView tandc;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.t_c, null);


        privacy_policy=(TextView)root.findViewById(R.id.privacy);
        tandc=(TextView)root.findViewById(R.id.tandc);


        privacy_policy.setOnClickListener(this);
        tandc.setOnClickListener(this);
        
        
        return root;
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
       super.onActivityCreated(savedInstanceState);
       //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
       
    }


	@Override
	public void onClick(View v) {
        // TODO Auto-generated method stub


        switch (v.getId()) {


            case R.id.privacy:
            {
                Intent intent = new Intent(getActivity(),Privacy.class);
                startActivity(intent);
                break;
            }

            case R.id.tandc:
            {
                Intent intent = new Intent(getActivity(),Terms_condition.class);
                startActivity(intent);
                break;
            }


            default:
                break;


        }

    }




}
