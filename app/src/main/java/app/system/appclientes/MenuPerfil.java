package app.system.appclientes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

 public class MenuPerfil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCalculadora.OnFragmentInteractionListener, FragmentQuejas.OnFragmentInteractionListener,
         HistorialFragment.OnFragmentInteractionListener, ReciboFragment.OnFragmentInteractionListener, ActualizarPass.OnFragmentInteractionListener, PagoFragment.OnFragmentInteractionListener
 {

     private TextView tvAlta_Perfil, tvUltimo_Perfil, tvID_Perfil;
     private EditText etNombre_Perfil, etDomicilio_Perfil, etExterior_Perfil, etInterior_Perfil, etEmail_Perfil;
     private NavigationView navigationView;
     private ActionBarDrawerToggle mDrawerToggle;
     private SharedPreferences preferences;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_menu_perfil);

         //DECLARACIÓN
         etEmail_Perfil = (EditText) findViewById(R.id.etEmail_Perfil);
         tvID_Perfil = (TextView) findViewById(R.id.tvID_Perfil);
         etNombre_Perfil = (EditText) findViewById(R.id.etNombre_Perfil);
         etDomicilio_Perfil = (EditText) findViewById(R.id.etDomicilio_Perfil);
         etExterior_Perfil = (EditText) findViewById(R.id.etExterior_Perfil);
         etInterior_Perfil = (EditText) findViewById(R.id.etInterior_Perfil);
         tvAlta_Perfil = (TextView) findViewById(R.id.tvAlta_Perfil);
         tvUltimo_Perfil = (TextView) findViewById(R.id.tvUltimo_Perfil);
         navigationView = (NavigationView) findViewById(R.id.nav_view);

         if (conectado())
         {
             //getSupportActionBar().setIcon(R.drawable.menu);

             StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
             StrictMode.setThreadPolicy(policy);
             Connection connection;
             String url;

             //PREFERENCIAS
             preferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
             //parámetro enviando del login
             String dato = getIntent().getExtras().getString("usuario");
             etEmail_Perfil.setText(dato);
             //seleccion de items
             navigationView.setNavigationItemSelectedListener(this);
             //correo en header
             View hView = navigationView.getHeaderView(0);
             TextView nav_correo = (TextView) hView.findViewById(R.id.txtCorreo);
             nav_correo.setText(dato);
             //extracción de la bd *
             try {
                 Class.forName("net.sourceforge.jtds.jdbc.Driver");
                 url = "jdbc:jtds:sqlserver://bdAguaPotable.mssql.somee.com;databaseName=bdAguaPotable;user=SYSTEM-APP_SQLLogin_1;password=pn8akqjwxc";
                 connection = DriverManager.getConnection(url);
                 Statement estatuto = connection.createStatement();
                 String query = "SELECT Cuentas.idCuenta, Cuentas.nombre, Calles.nombreC, Cuentas.numExt, Cuentas.numInt, Cuentas.fechaAlta, Cuentas.ultimoPagoMes, Cuentas.ultimoPagoAño FROM Cuentas INNER JOIN Calles on Cuentas.idCalle = Calles.idCalle WHERE Cuentas.correo = '" + dato + "'";
                 ResultSet resultado = estatuto.executeQuery(query);

                 if (resultado.next()) {
                     //datos
                     tvID_Perfil.setText(String.valueOf(resultado.getInt("idCuenta")));
                     etNombre_Perfil.setText(resultado.getString("nombre"));
                     etDomicilio_Perfil.setText(resultado.getString("nombreC"));
                     etExterior_Perfil.setText(resultado.getString("numExt"));
                     etInterior_Perfil.setText(resultado.getString("numInt"));
                     tvAlta_Perfil.setText(String.valueOf(resultado.getDate("fechaAlta")));
                     tvUltimo_Perfil.setText(String.valueOf(resultado.getInt("ultimoPagoMes")) + "/" + String.valueOf(resultado.getInt("ultimoPagoAño")));
                 } else
                     Toast.makeText(this, "Datos no encontrados", Toast.LENGTH_LONG).show();
                 connection.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         else
            Alerta("Sin acceso a Internet", "Favor de conectarse a una red ya sea WiFi o datos móviles");

         //EN PRUEBA
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

         mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.addDrawerListener(mDrawerToggle);
         //setSupportActionBar(toolbar);
         mDrawerToggle.syncState();
     }

     @Override
     protected void onPostCreate(Bundle savedInstanceState) {
         super.onPostCreate(savedInstanceState);
         // Sync the toggle state after onRestoreInstanceState has occurred.
         mDrawerToggle.syncState();
     }

     @Override
     public void onConfigurationChanged(Configuration newConfig) {
         super.onConfigurationChanged(newConfig);
         mDrawerToggle.onConfigurationChanged(newConfig);
     }


     @Override
     public void onBackPressed() {
         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

         if (drawer.isDrawerOpen(GravityCompat.START)) {
             drawer.closeDrawer(GravityCompat.START);
         } else {
             if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                 getSupportFragmentManager().popBackStack();
             } else {
                 super.onBackPressed();
             }
         }
     }

     //MÉTODOS PROPIOS
     private void logOut()
     {
         Intent i = new Intent(this, LoginActivity.class);
         i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //cierra esta actividad
         startActivity(i);
     }

     private boolean conectado()
     {
         ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo info_wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
         NetworkInfo info_datos = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

         if (String.valueOf(info_wifi.getState()).equals("CONNECTED") || String.valueOf(info_datos.getState()).equals("CONNECTED"))
             return true;
         else
             return false;
     }

     private void Alerta(String titulo, String mensaje) {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);

         builder.setTitle(titulo);
         builder.setMessage(mensaje);
         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 finish();
             }
         });
         builder.setCancelable(false);
         builder.show();
     }

     @SuppressWarnings("StatementWithEmptyBody")
     @Override
     public boolean onNavigationItemSelected(MenuItem item)
     {
         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);

         // Handle navigation view item clicks here.
         FragmentManager manager;
         Bundle args = new Bundle();

         switch (item.getItemId())
         {
             case R.id.nav_calculadora:
                 FragmentCalculadora fragment = new FragmentCalculadora();
                 manager = getSupportFragmentManager();
                 manager.beginTransaction().replace(R.id.drawer_layout, fragment, fragment.getTag()).addToBackStack(null).commit();
                 item.setChecked(true);
                 break;
             case R.id.nav_quejas:
                 FragmentQuejas fragmentQuejas = new FragmentQuejas();
                 manager = getSupportFragmentManager();
                 manager.beginTransaction().replace(R.id.drawer_layout, fragmentQuejas, fragmentQuejas.getTag()).addToBackStack(null).commit();
                 item.setChecked(true);
                 break;
             case R.id.nav_recibos:
                 ReciboFragment fragmentRecibo = new ReciboFragment();
                 args = new Bundle();
                 args.putString("idCuenta", tvID_Perfil.getText().toString());
                 fragmentRecibo.setArguments(args);

                 manager = getSupportFragmentManager();
                 manager.beginTransaction().replace(R.id.drawer_layout, fragmentRecibo, fragmentRecibo.getTag()).addToBackStack(null).commit();
                 item.setChecked(true);
                 break;
             case R.id.nav_Historial:
                 HistorialFragment fragmentHistorial = new HistorialFragment();

                 args = new Bundle();
                 args.putString("idCuenta", tvID_Perfil.getText().toString());
                 fragmentHistorial.setArguments(args);

                 manager = getSupportFragmentManager();
                 manager.beginTransaction().replace(R.id.drawer_layout, fragmentHistorial, fragmentHistorial.getTag()).addToBackStack(null).commit();
                 item.setChecked(true);
                 break;
             case  R.id.nav_Actualizar:
                 ActualizarPass fragmentActualizar = new ActualizarPass();

                 args.putString("email", etEmail_Perfil.getText().toString());
                 fragmentActualizar.setArguments(args);

                 manager = getSupportFragmentManager();
                 manager.beginTransaction().replace(R.id.drawer_layout, fragmentActualizar, fragmentActualizar.getTag()).addToBackStack(null).commit();
                 item.setChecked(true);
                 break;
             case R.id.nav_Pago:
                 PagoFragment fragmentPago = new PagoFragment();
                 manager = getSupportFragmentManager();
                 manager.beginTransaction().replace(R.id.drawer_layout, fragmentPago, fragmentPago.getTag()).addToBackStack(null).commit();
                 item.setChecked(false);
                 break;
             case R.id.nav_Cerrar:
                 preferences.edit().clear().apply();
                 logOut();
                 break;
         }
         return true;
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater menuInflater = getMenuInflater();
         menuInflater.inflate(R.menu.edit_menu_activity, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId())
         {
             case R.id.nav_edit:
                 return true;
             default:
                return super.onOptionsItemSelected(item);
         }
     }

     @Override
     public void onFragmentInteraction(Uri uri) {}

 }