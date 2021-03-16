package com.example.tfai;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ContactFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> contacts = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    DBHelper db;
    Dialog d;
    ItemAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.contact_layout,container,false);recyclerView = v.findViewById(R.id.recyclerView);
        floatingActionButton = v.findViewById(R.id.floatingActionButton);
        db=new DBHelper(getContext(),"ContactList");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new ItemAdapter(contacts);
        recyclerView.setAdapter(adapter);
        contacts.addAll(db.getAllContacts());
        adapter.notifyDataSetChanged();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(getContext());
                d.setContentView(R.layout.add_contact_dialog);

                d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                d.show();

                final TextInputEditText contactinp=d.findViewById(R.id.contact_inp);
                RelativeLayout addcontact=d.findViewById(R.id.add_contact);
                addcontact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(contactinp.getText()!=null){
                            db.insertValue(contactinp.getText().toString());
                            Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
                            d.dismiss();
                            contacts.clear();
                            contacts.addAll(db.getAllContacts());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        return v;
    }
}
