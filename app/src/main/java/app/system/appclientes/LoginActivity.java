package app.system.appclientes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnIngresar, btnSalir;
    private EditText etUsuario, etContra;
    private Switch swRecordarme;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnSalir = (Button) findViewById(R.id.btnSalir);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContra = (EditText) findViewById(R.id.etContra);
        swRecordarme = (Switch) findViewById(R.id.swRecordarme);

        btnIngresar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);

        this.setTitle("Iniciar Sesion");
        preferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        setCredencialsIfExits();
    }

    private void setCredencialsIfExits()
    {
        String email = preferences.getString("email", "");
        String pass = preferences.getString("contraseña", "");

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass))
        {
            etUsuario.setText(email);
            etContra.setText(pass);
        }
    }

    private void guradarPreferencias(String email, String contra) {
        if (swRecordarme.isChecked()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", email);
            editor.putString("contraseña", contra);
            editor.commit(); // empieza a guardar los put*
            editor.apply(); //guarda todos los cambios aunque no se guraden todos
        }
    }

    @Override
    public void onClick(View v) {
        String username = etUsuario.getText().toString();
        String password = etContra.getText().toString();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection;
        String url;

        switch (v.getId()) {
            case R.id.btnIngresar:
                if (validacion(username, password))
                {
                    ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info_wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo info_datos = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                    if (String.valueOf(info_wifi.getState()).equals("CONNECTED") || String.valueOf(info_datos.getState()).equals("CONNECTED"))
                    {
                        try
                        {
                            Class.forName("net.sourceforge.jtds.jdbc.Driver");
                            url = "jdbc:jtds:sqlserver://bdAguaPotable.mssql.somee.com;databaseName=bdAguaPotable;user=SYSTEM-APP_SQLLogin_1;password=pn8akqjwxc";
                            connection = DriverManager.getConnection(url);
                            Statement estatuto = connection.createStatement();
                            String query = "SELECT * FROM Cuentas WHERE correo = '" + username + "' and password = '" + password + "'";
                            ResultSet resultado = estatuto.executeQuery(query);

                            if (resultado.next()) {
                                Intent i = new Intent(this, MenuPerfil.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //cierra esta actividad
                                i.putExtra("usuario", username);
                                startActivity(i);
                                guradarPreferencias(username, password);
                            } else
                                Alerta("¡Oh no!", "Contraseña y usuario incorrectos");

                            resultado.close();
                            connection.close();

                        } catch (SQLException E) {
                            E.printStackTrace();
                            Alerta("Sin acceso a la Base de Datos", "Error en el servidor, inténtelo más tarde");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("ahh", "error" + e.getMessage());
                        }
                    } else
                        Alerta("Sin acceso a Internet", "Favor de conectarse a una red ya sea WiFi o datos móviles");
                } else
                    Toast.makeText(this, "Ingrese datos válidos", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnSalir:
                finish();
                break;
        }
    }

    private void Alerta(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private boolean validacion(String user, String pass) {
        if (user.isEmpty() && pass.isEmpty())
            return false;
        else if (user.isEmpty())
            return false;
        else if (pass.isEmpty())
            return false;

        return true;
    }
}
