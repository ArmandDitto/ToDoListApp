package com.ditto.training.todolistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabku;
    ListView lvKegiatan;
    EditText etTodo;
    ArrayList<String> list;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvKegiatan = findViewById(R.id.lv_kegiatan);
        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this,
                       R.layout.todo_content_layout,R.id.tv_todo_content, list);

        //Panggil Method
        loadSharedP();
        lvKegiatan.setAdapter(arrayAdapter);

        //Fitur Tambah Kegiatan [Melalui Floating Action Button]
        fabku = findViewById(R.id.fa_btn);
        fabku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Panggil Method
                showAddKegiatan();
            }
        });

        //Fitur Memanipulasi Kegiatan
        lvKegiatan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builderPilihAksi = new AlertDialog.Builder(MainActivity.this);
                builderPilihAksi.setTitle("''"+arrayAdapter.getItem(position)+"''");
                builderPilihAksi.setMessage("Bingung ya mau diapain ? :(");

                //Fitur Mengubah Kegiatan
                builderPilihAksi.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Panggil Method
                        showEditKegiatan(position);
                    }
                });

                //Fitur Menghapus Kegiatan
                builderPilihAksi.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Panggil Method
                        showDeleteKegiatan(position);
                    }
                });

                builderPilihAksi.setNeutralButton("Batal", null);

                builderPilihAksi.create();
                builderPilihAksi.show();
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

    private  void showAddKegiatan (){
        View view = View.inflate(MainActivity.this, R.layout.todo_layout, null);
        etTodo = view.findViewById(R.id.et_todo);
        AlertDialog.Builder builderKegiatanBaru = new AlertDialog.Builder(MainActivity.this);
        builderKegiatanBaru.setTitle("Tambah Kegiatan");
        builderKegiatanBaru.setMessage("Produktif terus ya...");
        builderKegiatanBaru.setView(view);

        builderKegiatanBaru.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int new_key = list.size();
                String listKegiatan = etTodo.getText().toString().trim();

                if(!listKegiatan.equals("")){
                    list.add(listKegiatan);
                    arrayAdapter.notifyDataSetChanged();
                    //Panggil Method
                    addToSharedP(new_key, listKegiatan);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Dibilangin gaboleh kosong :(",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builderKegiatanBaru.setNegativeButton("Batal", null);

        builderKegiatanBaru.create();
        builderKegiatanBaru.show();
    }

    private void showDeleteKegiatan(final int position){
        AlertDialog.Builder builderHapusDaftar = new AlertDialog.Builder(MainActivity.this);
        builderHapusDaftar.setTitle("Hapus Kegiatan");
        builderHapusDaftar.setMessage("Kamu beneran pengen hapus ''"+arrayAdapter.getItem(position)+"'' ? :(");

        builderHapusDaftar.setPositiveButton("Iya Dong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.remove(position);
                //Panggil Method
                delSharedP();
                arrayAdapter.notifyDataSetChanged();
            }
        });
        builderHapusDaftar.setNegativeButton("Gajadi Deh", null);

        builderHapusDaftar.create();
        builderHapusDaftar.show();
    }

    private void showEditKegiatan(final int position){
        View view = View.inflate(this, R.layout.todo_layout, null);
        etTodo = view.findViewById(R.id.et_todo);
        etTodo.setText(arrayAdapter.getItem(position));
        etTodo.setSelection(etTodo.getText().length());

        AlertDialog.Builder editKegiatanBuilder = new AlertDialog.Builder(this);
        editKegiatanBuilder.setTitle("Ubah Kegiatan");
        editKegiatanBuilder.setView(view);
        editKegiatanBuilder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editListKegiatan = etTodo.getText().toString();
                if(editListKegiatan.equals("")){
                    Toast.makeText(getApplicationContext(), "Dibilangin gaboleh kosong :(",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Panggil Method
                    editItem(position, etTodo.getText().toString());
                }
            }
        });
        editKegiatanBuilder.setNegativeButton("Batal", null);

        editKegiatanBuilder.create();
        editKegiatanBuilder.show();
    }

    private void editItem(int position, String newItem){
        list.set(position, newItem);
        //Panggil Method
        delSharedP();

        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucustom_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_exit){
            AlertDialog.Builder alertDialogExit = new AlertDialog.Builder(this);
            alertDialogExit.setTitle("Keluar Aplikasi");
            alertDialogExit.setMessage("Kamu beneran pengen keluar ? :(");
            alertDialogExit.setPositiveButton("Iya Dong", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialogExit.setNegativeButton("Gajadi Deh", null);
            alertDialogExit.create();
            alertDialogExit.show();
        }
        else if(id == R.id.item_github){
            String githubku = "github.com/ArmandDitto/ToDoListApp";
            Uri domain = Uri.parse("http://"+githubku);
            Intent visit = new Intent(Intent.ACTION_VIEW, domain);
            Intent chooseBrowser = Intent.createChooser(visit, "Kunjungi Githubku melalui:");
            if(chooseBrowser.resolveActivity(getPackageManager())!=null){
                startActivity(chooseBrowser);
            }
        }
        else if(id == R.id.item_clear){
            String messageClear = "Kamu beneran pengen <b>HAPUS SEMUA</b> kegiatan secara <b>PERMANEN</b> ? :(";
            AlertDialog.Builder alertDialogClearAll = new AlertDialog.Builder(this);
            alertDialogClearAll.setTitle("Hapus Semua Data");
            alertDialogClearAll.setMessage(Html.fromHtml(messageClear));
            alertDialogClearAll.setPositiveButton("IYA DONG", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    list.clear();
                    delSharedP();
                    arrayAdapter.notifyDataSetChanged();
                }
            });
            alertDialogClearAll.setNegativeButton("GAJADI DEH", null);
            alertDialogClearAll.create();
            alertDialogClearAll.show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogExit = new AlertDialog.Builder(this);
        alertDialogExit.setTitle("Keluar Aplikasi");
        alertDialogExit.setMessage("Kamu beneran pengen keluar ? :(");
        alertDialogExit.setPositiveButton("Iya Dong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialogExit.setNegativeButton("Gajadi Deh", null);
        alertDialogExit.create();
        alertDialogExit.show();
    }
}
