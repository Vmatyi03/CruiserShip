package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Jarat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Jarat> jaratokList = new ArrayList<>();
    private JaratAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.jaratokRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new JaratAdapter(jaratokList);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Lekérjük a felhasználó foglalásait
        db.collection("users").document(uid).collection("foglalasok")
                .get()
                .addOnSuccessListener(foglalasokSnap -> {

                    // Összes lefoglalt járat nevét kigyűjtjük
                    Set<String> lefoglaltJaratok = new HashSet<>();
                    for (DocumentSnapshot doc : foglalasokSnap) {
                        Jarat j = doc.toObject(Jarat.class);
                        if (j != null) lefoglaltJaratok.add(j.nev);
                    }

                    // Most lekérjük az összes elérhető járatot
                    db.collection("jaratok")
                            .get()
                            .addOnSuccessListener(jaratokSnap -> {
                                jaratokList.clear();
                                for (DocumentSnapshot doc : jaratokSnap) {
                                    Jarat j = doc.toObject(Jarat.class);
                                    if (j != null && !lefoglaltJaratok.contains(j.nev)) {
                                        jaratokList.add(j);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            });

                });

        return root;
    }
}
