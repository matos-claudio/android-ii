package com.claudio.androidii.aulai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudio.androidii.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> contacts;
    private Context context;
    private OnDeleteClickListener onDeleteClickListener;
    private OnUpdateClickListener onUpdateClickListener;

    public ContactAdapter(Context context, List<Contact> contacts,
                          OnDeleteClickListener onDeleteClickListener,
                          OnUpdateClickListener onUpdateClickListener) {
        this.context = context;
        this.contacts = contacts;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflar nosso contact_item.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = this.contacts.get(position);
        holder.txName.setText(contact.getName());
        holder.txEmail.setText(contact.getEmail());
        holder.txPhone.setText(contact.getPhone());

        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onUpdateClickListener != null) {
                    onUpdateClickListener.onUpdateClick(holder.getAdapterPosition());
                }
            }
        });

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Mapeamento dos componentes do nosso contact_item.xml
        TextView txName, txEmail, txPhone;
        Button btDelete, btEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txName = itemView.findViewById(R.id.txName);
            txEmail = itemView.findViewById(R.id.txEmail);
            txPhone = itemView.findViewById(R.id.txPhone);
            btDelete = itemView.findViewById(R.id.btDelete);
            btEdit = itemView.findViewById(R.id.btEdit);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(int position);
    }
}
