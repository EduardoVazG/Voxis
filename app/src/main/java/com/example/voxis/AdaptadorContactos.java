package com.example.voxis;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorContactos extends RecyclerView.Adapter<AdaptadorContactos.ContactViewHolder> {

    private List<Contactos> contactosList;
    private Context context;

    public AdaptadorContactos(List<Contactos> contactList, Context context) {
        this.contactosList = contactList != null ? contactList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_contactos, parent, false);
        return new ContactViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contactos contactos = contactosList.get(position);
        holder.nombre.setText(contactos.getName());
        holder.numero_contacto.setText(contactos.getNumero_contacto());
        holder.perfil_icono.setImageResource(contactos.getProfileImage());

        holder.relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, MostrarContactoActivity.class);
            intent.putExtra("id", contactos.getId());
            intent.putExtra("nombre", contactos.getName());
            intent.putExtra("numero", contactos.getNumero_contacto());
            intent.putExtra("correo", contactos.getCorreo());
            intent.putExtra("categoria", contactos.getCategoria()); // Añadido
            intent.putExtra("perfilResId", contactos.getProfileImage());
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return contactosList.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView perfil_icono;
        TextView nombre;
        TextView numero_contacto;
        RelativeLayout relativeLayout;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            perfil_icono = itemView.findViewById(R.id.perfil_icono);
            nombre = itemView.findViewById(R.id.nombre);
            numero_contacto = itemView.findViewById(R.id.numero);
            relativeLayout = itemView.findViewById(R.id.relative_layout);
        }
    }

    public void updateContactList(List<Contactos> newContactList) {
        contactosList.clear();
        contactosList.addAll(newContactList);
        notifyDataSetChanged();
    }
}

