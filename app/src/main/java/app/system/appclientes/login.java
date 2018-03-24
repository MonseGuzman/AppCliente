package app.system.appclientes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class login extends AppCompatActivity implements View.OnClickListener
{
    private Button siguiente, salir;
    private EditText usuario, contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        siguiente = (Button) findViewById(R.id.btnIngresar);
        salir = (Button) findViewById(R.id.btnSalir);

        usuario = (EditText) findViewById(R.id.txtUsuario);
        contra = (EditText) findViewById(R.id.txtContra);

        siguiente.setOnClickListener(this);

        salir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection;
        String url;

        switch (v.getId())
        {
            case R.id.btnIngresar:

                ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo info_wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo info_datos = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                String username = usuario.getText().toString();
                String password = contra.getText().toString();

                if (String.valueOf(info_wifi.getState()).equals("CONNECTED") || String.valueOf(info_datos.getState()).equals("CONNECTED"))
                {
                    //wifi

                    try
                    {
                        Class.forName("net.sourceforge.jtds.jdbc.Driver");
                        url = "jdbc:jtds:sqlserver://bdAguaPotable.mssql.somee.com;databaseName=bdAguaPotable;user=SYSTEM-APP_SQLLogin_1;password=pn8akqjwxc";
                        connection = DriverManager.getConnection(url);
                        Statement estatuto = connection.createStatement();
                        String query ="SELECT * FROM Cuentas WHERE correo = '"+ username+"' and password = '"+ password +"'";
                        ResultSet resultado = estatuto.executeQuery(query);

                        if (resultado.next())
                        {
                            Toast.makeText(login.this, "Login Correcto", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(login.this, MenuPerfil.class);
                            try {
                                i.putExtra("usuario", usuario.getText().toString());
                            }catch (Exception ex)
                            {
                                Toast.makeText(login.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            finish();
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(login.this, "Contraseña y usuario incorrectos", Toast.LENGTH_LONG).show();
                            Alerta();
                        }
                        resultado.close();
                        connection.close();

                    }catch (SQLException E){
                        E.printStackTrace();
                        SQL();
                    }catch (ClassNotFoundException e){
                        e.printStackTrace();
                        Alerta();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.i("ahh", "error" + e.getMessage());
                        Alerta();
                    }
                }
                else
                {
                    Alerta();
                    Toast.makeText(login.this, "Sin acceso a Internet", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnSalir:
                finish();
                break;
        }
    }

    public void Alerta()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sin acceso a Internet");
        builder.setMessage("Favor de conectarse a una red ya sea WiFi o datos móviles ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(true);
        builder.show();
    }

    public AlertDialog SQL()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sin acceso a la Base de Datos").setMessage("Error en el servidor, intentelo más tardes")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}