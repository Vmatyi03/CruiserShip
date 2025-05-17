package com.example.myapplication.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Jarat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class JaratAdapter extends RecyclerView.Adapter<JaratAdapter.JaratViewHolder> {

    private List<Jarat> jaratok;

    public JaratAdapter(List<Jarat> jaratok) {
        this.jaratok = jaratok;
    }

    @NonNull
    @Override
    public JaratViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jarat, parent, false);
        return new JaratViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JaratViewHolder holder, int position) {
        Jarat j = jaratok.get(position);
        holder.jaratInfo.setText(j.nev + " – " + j.indulas + " ➜ " + j.erkezes + "\n" + j.datum + " • " + j.ar + " Ft");

        holder.btnFoglal.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .collection("foglalasok")
                    .add(j)
                    .addOnSuccessListener(docRef -> {
                        holder.btnFoglal.setText("Lefoglalva");
                        holder.btnFoglal.setEnabled(false);
                    });
        });
    }

    @Override
    public int getItemCount() {
        return jaratok.size();
    }

    public static class JaratViewHolder extends RecyclerView.ViewHolder {
        TextView jaratInfo;
        Button btnFoglal;

        public JaratViewHolder(@NonNull View itemView) {
            super(itemView);
            jaratInfo = itemView.findViewById(R.id.jaratInfo);
            btnFoglal = itemView.findViewById(R.id.btnFoglal);
        }
    }
}
