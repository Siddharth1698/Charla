package ml.siddharthm.charla;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {
    private View groupFragmentView;
    private ListView list_View;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    private DatabaseReference groupRef;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = (View) inflater.inflate(R.layout.fragment_groups, container, false);
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        InitializeFeilds();
        RetriveAndDisplayGroups();

        list_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String currentGroupName = adapterView.getItemAtPosition(position).toString();
                Intent groupIntent = new Intent(getContext(),GroupChatActivity.class);
                groupIntent.putExtra("groupName",currentGroupName);
                startActivity(groupIntent);

            }
        });
        return groupFragmentView;
    }



    private void RetriveAndDisplayGroups() {
           groupRef.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   Set<String> set = new HashSet<>();
                   Iterator iterator = dataSnapshot.getChildren().iterator();

                   while (iterator.hasNext()){
                       set.add(((DataSnapshot)iterator.next()).getKey());
                   }
                   list_of_groups.clear();
                   list_of_groups.addAll(set);
                   arrayAdapter.notifyDataSetChanged();
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {


               }
           });

    }

    private void InitializeFeilds() {
        list_View = (ListView)groupFragmentView.findViewById(R.id.groupid);
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_groups);
        list_View.setAdapter(arrayAdapter);

    }

}
