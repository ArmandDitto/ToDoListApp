package com.ditto.training.todolistapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabku;
    ListView lvKegiatan;
    ArrayList<String> list;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvKegiatan = findViewById(R.id.lv_kegiatan);

        list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                       android.R.layout.simple_list_item_1,list);


        fabku = findViewById(R.id.fa_btn);
        fabku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderKegiatanBaru = new AlertDialog.Builder(MainActivity.this);
                builderKegiatanBaru.setTitle("Tambah Kegiatan");
                builderKegiatanBaru.setMessage("Kegiatan apa yang ingin ditambahkan ?");
                final EditText etKegiatanBaru = new EditText(MainActivity.this);
                builderKegiatanBaru.setView(etKegiatanBaru);

                builderKegiatanBaru.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String listKegiatan = etKegiatanBaru.getText().toString();
                        list.add(listKegiatan);
                        lvKegiatan.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                    }
                });

                builderKegiatanBaru.setNegativeButton("Cancel", null);

                builderKegiatanBaru.create().show();
            }
        });

        lvKegiatan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builderHapusKegiatan = new AlertDialog.Builder(MainActivity.this);
                builderHapusKegiatan.setTitle("Hapus Kegiatan");
                builderHapusKegiatan.setMessage("Anda yakin ingin menghapus ini ?");
                //final EditText etKegiatanBaru = new EditText(MainActivity.this);
                //builderHapusKegiatan.setView(etKegiatanBaru);

                builderHapusKegiatan.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        lvKegiatan.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                    }
                });

                builderHapusKegiatan.setNegativeButton("Cancel", null);

                builderHapusKegiatan.create().show();
            }
        });
    }


}
