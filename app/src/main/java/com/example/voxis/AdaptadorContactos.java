package com.example.voxis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorContactos extends RecyclerView.Adapter<AdaptadorContactos.ContactViewHolder> {

    private List<Contactos> contactostList;

    public AdaptadorContactos(List<Contactos> contactList) {
        this.contactostList = contactList != null ? contactList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_contactos, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contactos contactos = contactostList.get(position);
        holder.nombre.setText(contactos.getName());
        holder.hora_llamada.setText(contactos.getCallTime());
        holder.numero_contacto.setText(contactos.getNumero_contacto());
        holder.perfil_icono.setImageResource(contactos.getProfileImage());
    }

    @Override
    public int getItemCount() {
        return contactostList.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView perfil_icono;
        TextView nombre;
        TextView hora_llamada;

        TextView numero_contacto;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            perfil_icono = itemView.findViewById(R.id.perfil_icono);
            nombre = itemView.findViewById(R.id.nombre);
            hora_llamada = itemView.findViewById(R.id.hora_llamada);
            numero_contacto = itemView.findViewById(R.id.numero);
        }
    }
}
