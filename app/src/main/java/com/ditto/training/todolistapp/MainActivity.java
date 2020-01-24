package com.ditto.training.todolistapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

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

        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                       android.R.layout.simple_list_item_1,list);

        loadSharedP();
        lvKegiatan.setAdapter(arrayAdapter);
        //Flying Action Button
        fabku = findViewById(R.id.fa_btn);
        fabku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderKegiatanBaru = new AlertDialog.Builder(MainActivity.this);
                builderKegiatanBaru.setTitle("Tambah Kegiatan");
                builderKegiatanBaru.setMessage("Kegiatan apa yang ingin ditambahkan ?");
                final EditText etKegiatanBaru = new EditText(MainActivity.this);
                builderKegiatanBaru.setView(etKegiatanBaru);

                //Button Menambah Kegiatan
                builderKegiatanBaru.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int new_key = list.size();
                        String listKegiatan = etKegiatanBaru.getText().toString();
                        list.add(listKegiatan);
                        arrayAdapter.notifyDataSetChanged();

                        //Memanggil Method
                        addToSharedP(new_key, listKegiatan);
                    }
                });

                //Button Cancel Menambah Kegiatan
                builderKegiatanBaru.setNegativeButton("Cancel", null);

                builderKegiatanBaru.create().show();
            }
        });

        //Hapus Kegiatan ketika ditekan
        lvKegiatan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builderHapusDaftar = new AlertDialog.Builder(MainActivity.this);
                builderHapusDaftar.setTitle("Hapus Data");
                builderHapusDaftar.setMessage("Apakah yakin ingin dihapus ?");

                builderHapusDaftar.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        delSharedP();
                        arrayAdapter.notifyDataSetChanged();
                    }
                });

                builderHapusDaftar.setNegativeButton("NO", null);
                builderHapusDaftar.show();
                return false;
            }
        });
    }

    private void addToSharedP(int key, String listKegiatan){
        SharedPreferences sharedP = getSharedPreferences("daftar",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedP.edit();
        String k = String.valueOf(key);
        editor.putString(k, listKegiatan);
        editor.commit();
    }

    private void loadSharedP(){
        SharedPreferences sharedP = getSharedPreferences("daftar",MODE_PRIVATE);
        if (sharedP.getAll().size()>0){
            for(int i=0; i<sharedP.getAll().size(); i++){
                String key = String.valueOf(i);
                String daftarKegiatan = sharedP.getString(key, null);
                list.add(daftarKegiatan);
            }
        }
    }

    private void delSharedP(){
        SharedPreferences sharedP = getSharedPreferences("daftar", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedP.edit();
        editor.clear();
        editor.commit();
        for(int i=0; i<list.size(); i++){
            editor.putString(String.valueOf(i),list.get(i));
        }
        editor.commit();
    }


    /*private void saveArrayList(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("size", list.size());

        for(int i=0;i<list.size();i++){
            editor.remove("app"+i);
            editor.putString("app"+1, list.get(i));
        }
    }*/

    /*private void loadArrayList(){
        SharedPreferences sharedPref2 = PreferenceManager.getDefaultSharedPreferences(this);
        list.clear();
        int size = sharedPref2.getInt("size", 0);

        for(int i=0;i<size;i++){
            list.add(sharedPref2.getString("app"+i, null));
        }
    }*/
}
