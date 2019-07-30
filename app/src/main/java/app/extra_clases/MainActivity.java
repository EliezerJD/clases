package app.extra_clases;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    EditText mail;
    EditText pass;
    Modelo data;
    Dialog modalAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mail= findViewById(R.id.txt_email);
        pass= findViewById(R.id.txt_password);
    }

    public void login(View view) {
        if(verificarCampos()){
            data = new Modelo();
            data.setEmail(mail.getText().toString().trim());
            data.setPassword(pass.getText().toString().trim());
            conectLogin(data);
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(), "Debes completar todos los campos", Toast.LENGTH_SHORT);
            toast1.show();
        }
        //Intent screen = new Intent(MainActivity.this, PanelActivity.class);
        //startActivity(screen);
    }

    private void conectLogin(Modelo m){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mail.getWindowToken(), 0);
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Modelo> call = dataService.login(m);
        call.enqueue(new Callback<Modelo>() {
            @Override
            public void onResponse(Call<Modelo> call, Response<Modelo> response) {
                if(response.message().equals("OK")) {
                    Intent screen = new Intent(MainActivity.this, PanelActivity.class);
                    screen.putExtra("id", response.body().getId());
                    screen.putExtra("name", response.body().getNombre());
                    screen.putExtra("email", response.body().getEmail());
                    startActivity(screen);
                }else{
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }

            @Override
            public void onFailure(Call<Modelo> call, Throwable t) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_SHORT);
                toast1.show();
                System.out.println(t.getMessage());
            }
        });
    }

    private boolean verificarCampos(){
        if(mail.getText().toString().trim().equals("")){
            return false;
        }else if(pass.getText().toString().trim().equals("")){
            return false;
        }else{
            return true;
        }

    }

    public void register(View view){
        modalAdd = new Dialog(MainActivity.this);
        modalAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modalAdd.setCancelable(true);
        modalAdd.setContentView(R.layout.activity_registro);
        modalAdd.setCancelable(true);
        modalAdd.show();
    }

    public void btnRegister(View view) {
        EditText nombre = modalAdd.findViewById(R.id.txt_nombre);
        EditText email = modalAdd.findViewById(R.id.txt_email);
        EditText pass = modalAdd.findViewById(R.id.txt_password);
        Modelo usuario = new Modelo();
        usuario.setName(nombre.getText().toString().trim());
        usuario.setEmail(email.getText().toString().trim());
        usuario.setPassword(pass.getText().toString().trim());
        usuario.setCpassword(pass.getText().toString().trim());
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Modelo> call = dataService.register(usuario);
        call.enqueue(new Callback<Modelo>() {
            @Override
            public void onResponse(Call<Modelo> call, Response<Modelo> response) {
                if(response.message().equals("OK")){
                    modalAdd.dismiss();
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT);
                    toast1.show();
                }else{
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Error al registrarse", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }

            @Override
            public void onFailure(Call<Modelo> call, Throwable t) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error al registrarse", Toast.LENGTH_SHORT);
                toast1.show();
            }
        });



    }
}
