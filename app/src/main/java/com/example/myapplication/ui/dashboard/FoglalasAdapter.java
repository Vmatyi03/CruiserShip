package com.example.myapplication.ui.dashboard;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FoglalasAdapter extends RecyclerView.Adapter<FoglalasAdapter.FoglalasViewHolder> {

    private List<Jarat> foglalasok;

    public FoglalasAdapter(List<Jarat> foglalasok) {
        this.foglalasok = foglalasok;
    }

    @NonNull
    @Override
    public FoglalasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foglalas, parent, false);
        return new FoglalasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoglalasViewHolder holder, int position) {
        Jarat j = foglalasok.get(position);
        holder.foglalasInfo.setText(j.nev + " – " + j.indulas + " ➜ " + j.erkezes + "\n" +
                j.datum + " • " + j.ar + " Ft");

        holder.btnTorles.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore.getInstance()
                    .collection("users").document(uid)
                    .collection("foglalasok")
                    .whereEqualTo("nev", j.nev) // egyszerű azonosítás
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot doc : querySnapshot) {
                            doc.getReference().delete(); // törlés Firestore-ból
                        }
                        foglalasok.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    });
        });
    }

    @Override
    public int getItemCount() {
        return foglalasok.size();
    }

    public static class FoglalasViewHolder extends RecyclerView.ViewHolder {
        TextView foglalasInfo;
        Button btnTorles;

        public FoglalasViewHolder(@NonNull View itemView) {
            super(itemView);
            foglalasInfo = itemView.findViewById(R.id.foglalasInfo);
            btnTorles = itemView.findViewById(R.id.btnTorles);
        }
    }
}
