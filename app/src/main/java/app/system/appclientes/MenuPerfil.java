 package app.system.appclientes;

 import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

 public class MenuPerfil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCalculadora.OnFragmentInteractionListener, FragmentQuejas.OnFragmentInteractionListener,
         HistorialFragment.OnFragmentInteractionListener, ReciboFragment.OnFragmentInteractionListener, ActualizarPass.OnFragmentInteractionListener, PagoFragment.OnFragmentInteractionListener,
         View.OnClickListener {

     Button anterior;
     private TextView email, id, nombre, domicilio, nExterior, nInterior, Alta, ultimo, idC;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
         StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
         StrictMode.setThreadPolicy(policy);
         Connection connection;
         String url;

         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_menu_perfil);
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         anterior = (Button) findViewById(R.id.btnCerrar);
         email = (TextView) findViewById(R.id.txtEmail);
         id = (TextView) findViewById(R.id.txtID);
         nombre = (TextView) findViewById(R.id.txtNombre);
         domicilio = (TextView) findViewById(R.id.txtDomicilio);
         nExterior = (TextView) findViewById(R.id.txtExterior);
         nInterior = (TextView) findViewById(R.id.txtInterior);
         Alta = (TextView) findViewById(R.id.txtAlta);
         ultimo = (TextView) findViewById(R.id.txtUltimo);

         anterior.setOnClickListener(this);

         //parámetro enviando del login
         String dato = getIntent().getExtras().getString("usuario");

         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.setDrawerListener(toggle);
         toggle.syncState();

         NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);

         //parámetros para activity
         email.setText(dato);

         View hView = navigationView.getHeaderView(0);
         TextView nav_correo = (TextView) hView.findViewById(R.id.txtCorreo);
         nav_correo.setText(dato);

         try
         {
             Class.forName("net.sourceforge.jtds.jdbc.Driver");
             url = "jdbc:jtds:sqlserver://bdAguaPotable.mssql.somee.com;databaseName=bdAguaPotable;user=SYSTEM-APP_SQLLogin_1;password=pn8akqjwxc";
             connection = DriverManager.getConnection(url);
             Statement estatuto = connection.createStatement();
             String query ="SELECT Cuentas.idCuenta, Cuentas.nombre, Calles.nombreC, Cuentas.numExt, Cuentas.numInt, Cuentas.fechaAlta, Cuentas.ultimoPagoMes, Cuentas.ultimoPagoAño FROM Cuentas INNER JOIN Calles on Cuentas.idCalle = Calles.idCalle WHERE Cuentas.correo = '"+ dato+"'";
             ResultSet resultado = estatuto.executeQuery(query);

             if (resultado.next())
             {
                 //datos
                 id.setText(String.valueOf(resultado.getInt("idCuenta")));
                 nombre.setText(resultado.getString("nombre"));
                 domicilio.setText(resultado.getString("nombreC"));
                 nExterior.setText(resultado.getString("numExt"));
                 nInterior.setText(resultado.getString("numInt"));
                 Alta.setText(String.valueOf(resultado.getDate("fechaAlta")));
                 ultimo.setText(String.valueOf(resultado.getInt("ultimoPagoMes")) + "/" + String.valueOf(resultado.getInt("ultimoPagoAño")));
             }
             else
                 Toast.makeText(MenuPerfil.this, "Datos no encontrados", Toast.LENGTH_LONG).show();
             connection.close();

         }catch (SQLException E) {
             E.printStackTrace();
         }catch (ClassNotFoundException e){
             e.printStackTrace();
         }catch (Exception e){
             e.printStackTrace();
         }
     }

     @Override
     public void onClick(View v) {
         switch (v.getId()) {
             case R.id.btnCerrar:
                 finish();
                 break;
         }
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

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.menu_perfil, menu);
         return true;
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
                 args.putString("idCuenta", id.getText().toString());
                 fragmentRecibo.setArguments(args);

                 manager = getSupportFragmentManager();
                 manager.beginTransaction().replace(R.id.drawer_layout, fragmentRecibo, fragmentRecibo.getTag()).addToBackStack(null).commit();
                 item.setChecked(true);
                 break;
             case R.id.nav_Historial:
                 HistorialFragment fragmentHistorial = new HistorialFragment();

                 args = new Bundle();
                 args.putString("idCuenta", id.getText().toString());
                 fragmentHistorial.setArguments(args);

                 manager = getSupportFragmentManager();
                 manager.beginTransaction().replace(R.id.drawer_layout, fragmentHistorial, fragmentHistorial.getTag()).addToBackStack(null).commit();
                 item.setChecked(true);
                 break;
             case  R.id.nav_Actualizar:
                 ActualizarPass fragmentActualizar = new ActualizarPass();

                 args.putString("email", email.getText().toString());
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
         }
         return true;
     }

     @Override
     public void onFragmentInteraction(Uri uri) {}

 }