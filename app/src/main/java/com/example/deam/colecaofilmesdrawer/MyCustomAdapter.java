package com.example.deam.colecaofilmesdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deam on 03/09/2017.
 */

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private List<Filme> list = new ArrayList<Filme>();
    private Context context;

    public MyCustomAdapter(List<Filme> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.meus_filmes_lista, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.textView2);
        listItemText.setText(list.get(position).getNome() + " - " + list.get(position).getAno());

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((CheckBox)v).isChecked();
                list.get(position).setSelected(isSelected);
            }
        });

        return view;
    }

    public ArrayList<Filme> getFilmes(){
        ArrayList<Filme> lista = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).isSelected())
                lista.add(list.get(i));
        }
        return lista;
    }
}