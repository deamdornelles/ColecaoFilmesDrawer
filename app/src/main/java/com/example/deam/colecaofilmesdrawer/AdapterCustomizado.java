package com.example.deam.colecaofilmesdrawer;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deam on 08/09/2017.
 */

public class AdapterCustomizado extends ArrayAdapter<Filme> {

    private final Context context;
    private final List<Filme> filmes;
    private final List<Filme> filmes_todos;
    private final List<Filme> filmes_sugestao;

    public AdapterCustomizado(@NonNull Context context, @LayoutRes int resource, @NonNull List<Filme> objects) {
        super(context, resource, objects);
        this.context = context;
        this.filmes = new ArrayList<>(objects);
        this.filmes_todos = new ArrayList<>(objects);
        this.filmes_sugestao = new ArrayList<>();
    }

    public int getCount() {
        return filmes.size();
    }

    public Filme getItem(int position) {
        return filmes.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.meus_filmes_lista, parent, false);
            }
            TextView listItemText = (TextView)convertView.findViewById(R.id.textView2);
            listItemText.setText(filmes.get(position).getNome() + " - " + filmes.get(position).getAno());

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = ((CheckBox)v).isChecked();
                    filmes.get(position).setSelected(isSelected);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Filme) resultValue).nome;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    filmes_sugestao.clear();
                    for (Filme filme : filmes_todos) {
                        if (filme.nome.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            filmes_sugestao.add(filme);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filmes_sugestao;
                    filterResults.count = filmes_sugestao.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filmes.clear();
                if (results != null && results.count > 0) {
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Filme) {
                            filmes.add((Filme) object);
                        }
                    }
                } else if (constraint == null) {
                    filmes.addAll(filmes_todos);
                }
                notifyDataSetChanged();
            }
        };
    }

    public ArrayList<Filme> getFilmes(){
        ArrayList<Filme> lista = new ArrayList<>();
        for(int i = 0; i< filmes.size(); i++){
            if(filmes.get(i).isSelected())
                lista.add(filmes.get(i));
        }
        return lista;
    }
}