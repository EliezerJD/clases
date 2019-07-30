package app.extra_clases;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PanelActivity extends AppCompatActivity {
    Dialog modalAdd;
    String sessionId;
    String username;
    String emailUser;
    Button btn_login;
    ArrayList<Modelo> clases;
    ArrayList<String> clasesLista;
    ArrayList<Modelo> clasesInscrito;
    ArrayList<String> clasesInscritoLista;
    ArrayList<Modelo> clasesCreadas;
    ArrayList<String> clasesCreadasLista;
    Modelo clase;
    ArrayAdapter<String> arrayTodas;
    ArrayAdapter<String> arrayInscrito;
    ArrayAdapter<String> arrayCreadas;
    ListView listViewTodas;
    ListView listViewInscrito;
    ListView listViewCreadas;
    Toast toast1;
    TabLayout tab;
    Integer auxPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        tab = findViewById(R.id.tab);
        sessionId = getIntent().getStringExtra("id");
        username = getIntent().getStringExtra("name");
        emailUser = getIntent().getStringExtra("email");
        btn_login = findViewById(R.id.btn_login);
        listViewTodas = findViewById(R.id.listViewTodas);
        listViewInscrito = findViewById(R.id.listViewInscrito);
        listViewCreadas = findViewById(R.id.listViewCreadas);
        listViewInscrito.setVisibility(View.GONE);
        listViewCreadas.setVisibility(View.GONE);
        traerTodasLasClases();
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    listViewInscrito.setVisibility(View.GONE);
                    listViewCreadas.setVisibility(View.GONE);
                    listViewTodas.setVisibility(View.VISIBLE);
                    listarTodas();
                } else if (tab.getPosition() == 1) {
                    listViewTodas.setVisibility(View.GONE);
                    listViewCreadas.setVisibility(View.GONE);
                    listViewInscrito.setVisibility(View.VISIBLE);
                }else if (tab.getPosition() == 2) {
                    listViewTodas.setVisibility(View.GONE);
                    listViewInscrito.setVisibility(View.GONE);
                    listViewCreadas.setVisibility(View.VISIBLE);
                    listarCreadas();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void traerTodasLasClases(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(btn_login.getWindowToken(), 0);
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Modelo> call = dataService.getClases();
        call.enqueue(new Callback<Modelo>() {
            @Override
            public void onResponse(Call<Modelo> call, Response<Modelo> response) {
                if(response.message().equals("OK")){
                    clases = new ArrayList<>();
                    clasesLista = new ArrayList<>();
                    String estatus;
                    for(int x=0; x<response.body().getData().size();x++){
                        clase = new Modelo();
                        clase.setId(response.body().getData().get(x).getId());
                        clase.setNombre(response.body().getData().get(x).getNombre());
                        clase.setDescripcion(response.body().getData().get(x).getDescripcion());
                        clase.setFecha(response.body().getData().get(x).getFecha());
                        clase.setCosto(response.body().getData().get(x).getCosto());
                        clase.setEntradas(response.body().getData().get(x).getEntradas());
                        try {
                            estatus = checarStatus(response.body().getData().get(x).getFecha(), response.body().getData().get(x).getEntradas());
                        }catch (Exception ex){
                            estatus = "No disponible";
                        }
                        clase.setEstatus(estatus);
                        clase.setIdInscritos(response.body().getData().get(x).getIdInscritos());
                        clase.setIdCreador(response.body().getData().get(x).getIdCreador());
                        clases.add(clase);
                        clasesLista.add(response.body().getData().get(x).getNombre() +" - " + response.body().getData().get(x).getFecha() + "\n"+
                        "$"+response.body().getData().get(x).getCosto()+ " - Entradas: " + response.body().getData().get(x).getEntradas() + "\n"+estatus);
                    }
                    listarTodas();
                }
            }

            @Override
            public void onFailure(Call<Modelo> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });

    }

    private void listarTodas(){
        arrayTodas= new ArrayAdapter<String>(PanelActivity.this, R.layout.activity_listview, R.id.textView, clasesLista);
        listViewTodas.setAdapter(arrayTodas);
        listarInscrito();
    }

    private void listarInscrito(){
        clasesInscrito = new ArrayList<>();
        clasesInscritoLista = new ArrayList<>();
        for(int x=0; x<clases.size();x++){
            char[] array = clases.get(x).getIdInscritos().toCharArray();
            for(int y=0; y<array.length; y++){
                if(String.valueOf(array[y]).equals(sessionId)){
                    clasesInscrito.add(clases.get(x));
                    clasesInscritoLista.add(clases.get(x).getNombre() +" - " + clases.get(x).getFecha() + "\n"+
                            "$"+clases.get(x).getCosto()+ " - Entradas: " + clases.get(x).getEntradas() + "\n"+clases.get(x).getEstatus());
                }
            }
        }
        arrayInscrito= new ArrayAdapter<String>(PanelActivity.this, R.layout.activity_listviewnscrito, R.id.textView, clasesInscritoLista);
        listViewInscrito.setAdapter(arrayInscrito);

    }

    private void listarCreadas(){
        clasesCreadas = new ArrayList<>();
        clasesCreadasLista = new ArrayList<>();
        for(int x=0; x<clases.size();x++){
            if(clases.get(x).getIdCreador().equals(sessionId)){
                clasesCreadas.add(clases.get(x));
                clasesCreadasLista.add(clases.get(x).getNombre() +" - " + clases.get(x).getFecha() + "\n"
                        + "$"+clases.get(x).getCosto()+ " - Entradas: " + clases.get(x).getEntradas() + "\n"+clases.get(x).getEstatus());
            }
        }
        arrayCreadas= new ArrayAdapter<String>(PanelActivity.this, R.layout.activity_listviewcreadas, R.id.textView, clasesCreadasLista);
        listViewCreadas.setAdapter(arrayCreadas);
    }



    public void addClass(View view) {
        modalAdd = new Dialog(PanelActivity.this);
        modalAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modalAdd.setCancelable(true);
        modalAdd.setContentView(R.layout.activity_crear_curso);
        modalAdd.setCancelable(true);
        modalAdd.show();
    }

    public void conectAddClass(View view) {
        EditText nombreCurso = modalAdd.findViewById(R.id.txt_nombre_curso);
        EditText descripcion = modalAdd.findViewById(R.id.txt_descripcion);
        EditText fecha = modalAdd.findViewById(R.id.txt_fecha_hora);
        EditText costo = modalAdd.findViewById(R.id.txt_costo_gratuito);
        EditText entradas = modalAdd.findViewById(R.id.txt_entradas);
        Modelo clase = new Modelo();
        clase.setNombre(nombreCurso.getText().toString().trim());
        clase.setDescripcion(descripcion.getText().toString().trim());
        clase.setFecha(fecha.getText().toString().trim());
        clase.setCosto(costo.getText().toString().trim());
        clase.setEntradas(entradas.getText().toString().trim());
        clase.setEstatus("Abierta");
        clase.setIdInscritos("0");
        clase.setIdCreador(sessionId);
        connectAddClassMethod(clase);

    }

    private void connectAddClassMethod(Modelo c){

        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Modelo> call = dataService.agregar(c);
        call.enqueue(new Callback<Modelo>() {
            @Override
            public void onResponse(Call<Modelo> call, Response<Modelo> response) {
                if(response.message().equals("Created")){
                    toast1 = Toast.makeText(getApplicationContext(), "Agregado correctamente", Toast.LENGTH_SHORT);
                    toast1.show();
                    modalAdd.dismiss();
                }else{
                    toast1 = Toast.makeText(getApplicationContext(), "Debe introducir un formato de fecha válido", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }

            @Override
            public void onFailure(Call<Modelo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    public void logOut(View view) {
        Intent screen = new Intent(PanelActivity.this, MainActivity.class);
        screen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(screen);
    }


    public void closeModalAdd(View view) {
        modalAdd.dismiss();
    }

    public void inscribirme(View view) {
        View item = (View) view.getParent();
        final int pos = listViewTodas.getPositionForView(item);
        if(comprobarInscritos(clases.get(pos).getIdInscritos())){
            toast1 = Toast.makeText(getApplicationContext(), "Ya estás inscrito a esta clase", Toast.LENGTH_SHORT);
            toast1.show();
        }else if(clases.get(pos).getIdCreador().equals(sessionId)){
            toast1 = Toast.makeText(getApplicationContext(), "No puedes inscribirte a una clase propia", Toast.LENGTH_SHORT);
            toast1.show();
        }else if(clases.get(pos).getEstatus().equals("Cerrado")){
            toast1 = Toast.makeText(getApplicationContext(), "No puedes inscribirte a una clase que esta cerrada", Toast.LENGTH_SHORT);
            toast1.show();
        }else{
            String oldEntradas = clases.get(pos).getEntradas();
            int newEntradas = Integer.parseInt(oldEntradas)-1;
            clases.get(pos).setEntradas(String.valueOf(newEntradas));
            String newInscritos;
            if(clases.get(pos).getIdInscritos().equals("0")){
                newInscritos = sessionId;
            }else{
                newInscritos =clases.get(pos).getIdInscritos()+ sessionId;
            }
            clases.get(pos).setIdInscritos(newInscritos);
            Retrofit retrofit = Connection.getClient();
            DataService dataService = retrofit.create(DataService.class);
            Call<Modelo> call = dataService.update(Integer.parseInt(clases.get(pos).getId()),clases.get(pos));
            call.enqueue(new Callback<Modelo>() {
                @Override
                public void onResponse(Call<Modelo> call, Response<Modelo> response) {
                    if(response.message().equals("OK")){
                        toast1 = Toast.makeText(getApplicationContext(), "Inscrito correctamente", Toast.LENGTH_SHORT);
                        toast1.show();
                        traerTodasLasClases();
                    }else{
                        toast1 = Toast.makeText(getApplicationContext(), "Error al inscribirte", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }

                @Override
                public void onFailure(Call<Modelo> call, Throwable t) {
                    toast1 = Toast.makeText(getApplicationContext(), "Error al inscribirte", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            });

        }
    }

    private boolean comprobarInscritos(String cadena){
        char[] array = cadena.toCharArray();
        for(int x=0; x<array.length;x++){
            if(String.valueOf(array[x]).equals(sessionId)){
                return true;
            }
        }
        return false;
    }

    public void verTodas(View view) {
       System.out.println("aquiiiii");
    }

    private String checarStatus(String fecha, String entradas) throws ParseException {
        if(Integer.parseInt(entradas)<1){
            return "Cerrado";
        }else if(comprobarFecha(fecha)){
            return "Cerrado";
        }else{
            return "Abierta";
        }
    }

    private boolean comprobarFecha(String fecha) throws ParseException {
        Calendar f = new GregorianCalendar();
        int año = f.get(Calendar.YEAR);
        int mes = f.get(Calendar.MONTH)+1;
        int dia = f.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(fecha);
        Date date2 = sdf.parse(año+"-"+mes+"-"+dia);

        if(date1.before(date2)){
            return true;
        }else if(date1.after(date2)){
            return false;
        }
        return true;
    }

    public void editdel(View view){
        View item = (View) view.getParent();
        auxPos = listViewCreadas.getPositionForView(item);
        modalAdd = new Dialog(PanelActivity.this);
        modalAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modalAdd.setCancelable(true);
        modalAdd.setContentView(R.layout.activity_editar_curso);
        modalAdd.setCancelable(true);
        EditText nombreCurso = modalAdd.findViewById(R.id.txt_nombre_curso);
        EditText descripcion = modalAdd.findViewById(R.id.txt_descripcion);
        EditText fecha = modalAdd.findViewById(R.id.txt_fecha_hora);
        EditText costo = modalAdd.findViewById(R.id.txt_costo_gratuito);
        EditText entradas = modalAdd.findViewById(R.id.txt_entradas);
        nombreCurso.setText(clasesCreadas.get(auxPos).getNombre());
        descripcion.setText(clasesCreadas.get(auxPos).getDescripcion());
        fecha.setText(clasesCreadas.get(auxPos).getFecha());
        costo.setText(clasesCreadas.get(auxPos).getCosto());
        entradas.setText(clasesCreadas.get(auxPos).getEntradas());
        modalAdd.show();
    }

    public void editar(View view){
        EditText nombreCurso = modalAdd.findViewById(R.id.txt_nombre_curso);
        EditText descripcion = modalAdd.findViewById(R.id.txt_descripcion);
        EditText fecha = modalAdd.findViewById(R.id.txt_fecha_hora);
        EditText costo = modalAdd.findViewById(R.id.txt_costo_gratuito);
        EditText entradas = modalAdd.findViewById(R.id.txt_entradas);
        clasesCreadas.get(auxPos).setNombre(nombreCurso.getText().toString().trim());
        clasesCreadas.get(auxPos).setDescripcion(descripcion.getText().toString().trim());
        clasesCreadas.get(auxPos).setFecha(fecha.getText().toString().trim());
        clasesCreadas.get(auxPos).setCosto(costo.getText().toString().trim());
        clasesCreadas.get(auxPos).setEntradas(entradas.getText().toString().trim());
        connectEditClassMethod(clasesCreadas.get(auxPos));
    }

    private void connectEditClassMethod(Modelo a){
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Modelo> call = dataService.update(Integer.parseInt(a.getId()),a);
        call.enqueue(new Callback<Modelo>() {
            @Override
            public void onResponse(Call<Modelo> call, Response<Modelo> response) {
                if(response.message().equals("OK")){
                    toast1 = Toast.makeText(getApplicationContext(), "Editado correctamente", Toast.LENGTH_SHORT);
                    toast1.show();
                    modalAdd.dismiss();
                    traerTodasLasClases();
                    listarCreadas();
                }else{
                    toast1 = Toast.makeText(getApplicationContext(), "Error al editar", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }

            @Override
            public void onFailure(Call<Modelo> call, Throwable t) {
                toast1 = Toast.makeText(getApplicationContext(), "Error al editar", Toast.LENGTH_SHORT);
                toast1.show();
            }
        });
    }

    public void eliminar(View view){
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Modelo> call = dataService.eliminar(Integer.parseInt(clasesCreadas.get(auxPos).getId()));
        call.enqueue(new Callback<Modelo>() {
            @Override
            public void onResponse(Call<Modelo> call, Response<Modelo> response) {
                toast1 = Toast.makeText(getApplicationContext(), "Eliminado correctamente", Toast.LENGTH_SHORT);
                toast1.show();
                modalAdd.dismiss();
                traerTodasLasClases();
                listarCreadas();
                tab.getTabAt(0);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                tab.select();
            }
            @Override
            public void onFailure(Call<Modelo> call, Throwable t) {
                toast1 = Toast.makeText(getApplicationContext(), "Error al eliminar", Toast.LENGTH_SHORT);
                toast1.show();
            }
        });

    }

    public void cerrarDialog(View view) {
        modalAdd.dismiss();
    }

    public void vTodas(View view) {
        View item = (View) view.getParent();
        int pos = listViewTodas.getPositionForView(item);
        modalAdd = new Dialog(PanelActivity.this);
        modalAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modalAdd.setCancelable(true);
        modalAdd.setContentView(R.layout.vista);
        modalAdd.setCancelable(true);
        TextView nombre = modalAdd.findViewById(R.id.nombreM);
        TextView fecha = modalAdd.findViewById(R.id.fechaM);
        TextView desc = modalAdd.findViewById(R.id.descripcionM);
        TextView costo = modalAdd.findViewById(R.id.costoM);
        TextView  entradas = modalAdd.findViewById(R.id.entradasM);
        TextView estatus = modalAdd.findViewById(R.id.estatusM);
        nombre.setText(clases.get(pos).getNombre());
        fecha.setText(clases.get(pos).getFecha());
        desc.setText(clases.get(pos).getDescripcion());
        costo.setText(clases.get(pos).getCosto());
        entradas.setText(clases.get(pos).getCosto());
        entradas.setText(clases.get(pos).getEntradas());
        estatus.setText(clases.get(pos).getEstatus());
        modalAdd.show();
    }

    public void verTodasInscrito(View view) {
        View item = (View) view.getParent();
        int pos = listViewInscrito.getPositionForView(item);
        modalAdd = new Dialog(PanelActivity.this);
        modalAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modalAdd.setCancelable(true);
        modalAdd.setContentView(R.layout.vista);
        modalAdd.setCancelable(true);
        TextView nombre = modalAdd.findViewById(R.id.nombreM);
        TextView fecha = modalAdd.findViewById(R.id.fechaM);
        TextView desc = modalAdd.findViewById(R.id.descripcionM);
        TextView costo = modalAdd.findViewById(R.id.costoM);
        TextView  entradas = modalAdd.findViewById(R.id.entradasM);
        TextView estatus = modalAdd.findViewById(R.id.estatusM);
        nombre.setText(clasesInscrito.get(pos).getNombre());
        fecha.setText(clasesInscrito.get(pos).getFecha());
        desc.setText(clasesInscrito.get(pos).getDescripcion());
        costo.setText(clasesInscrito.get(pos).getCosto());
        entradas.setText(clasesInscrito.get(pos).getCosto());
        entradas.setText(clasesInscrito.get(pos).getEntradas());
        estatus.setText(clasesInscrito.get(pos).getEstatus());
        modalAdd.show();
    }

    public void verCreadas(View view) {
        View item = (View) view.getParent();
        int pos = listViewCreadas.getPositionForView(item);
        modalAdd = new Dialog(PanelActivity.this);
        modalAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modalAdd.setCancelable(true);
        modalAdd.setContentView(R.layout.vista);
        modalAdd.setCancelable(true);
        TextView nombre = modalAdd.findViewById(R.id.nombreM);
        TextView fecha = modalAdd.findViewById(R.id.fechaM);
        TextView desc = modalAdd.findViewById(R.id.descripcionM);
        TextView costo = modalAdd.findViewById(R.id.costoM);
        TextView  entradas = modalAdd.findViewById(R.id.entradasM);
        TextView estatus = modalAdd.findViewById(R.id.estatusM);
        nombre.setText(clasesCreadas.get(pos).getNombre());
        fecha.setText(clasesCreadas.get(pos).getFecha());
        desc.setText(clasesCreadas.get(pos).getDescripcion());
        costo.setText(clasesCreadas.get(pos).getCosto());
        entradas.setText(clasesCreadas.get(pos).getCosto());
        entradas.setText(clasesCreadas.get(pos).getEntradas());
        estatus.setText(clasesCreadas.get(pos).getEstatus());
        modalAdd.show();
    }
}
