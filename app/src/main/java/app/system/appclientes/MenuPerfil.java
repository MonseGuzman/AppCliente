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
     private TextView email, id, nombre, domicilio, nExterior, nInterior, Alta, ultimo;

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

         try {
             //parámetros para activity
             email.setText(dato);

             View hView = navigationView.getHeaderView(0);
             TextView nav_correo = (TextView) hView.findViewById(R.id.txtCorreo);
             nav_correo.setText(dato);

         }
         catch (Exception ex) {
             Toast.makeText(MenuPerfil.this, "3 " + ex.getMessage(), Toast.LENGTH_LONG).show();
         }

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
         TextView idC = (TextView) findViewById(R.id.txtID);

         // Handle navigation view item clicks here.
         int id = item.getItemId();

         if (id == R.id.nav_calculadora) {
             FragmentCalculadora fragment = new FragmentCalculadora();
             FragmentManager manager = getSupportFragmentManager();
             manager.beginTransaction().replace(R.id.drawer_layout, fragment, fragment.getTag()).addToBackStack(null).commit();
             item.setChecked(true);
         } else if (id == R.id.nav_quejas) {
             FragmentQuejas fragment = new FragmentQuejas();
             FragmentManager manager = getSupportFragmentManager();
             manager.beginTransaction().replace(R.id.drawer_layout, fragment, fragment.getTag()).addToBackStack(null).commit();
             item.setChecked(true);
         } else if (id == R.id.nav_recibos) {
             ReciboFragment fragment = new ReciboFragment();
             try
             {
                 Bundle args = new Bundle();
                 args.putString("idCuenta", idC.getText().toString());
                 fragment.setArguments(args);
             }catch (Exception e)
             {
                 Toast.makeText(MenuPerfil.this, "Algo paso aquí", Toast.LENGTH_LONG).show();
             }
             FragmentManager manager = getSupportFragmentManager();
             manager.beginTransaction().replace(R.id.drawer_layout, fragment, fragment.getTag()).addToBackStack(null).commit();
             item.setChecked(true);
         } else if (id == R.id.nav_Historial) {
             HistorialFragment fragment = new HistorialFragment();
             try
             {
                 Bundle args = new Bundle();
                 args.putString("idCuenta", idC.getText().toString());
                 fragment.setArguments(args);
             }catch (Exception e)
             {
                 Toast.makeText(MenuPerfil.this, "Algo paso aquí", Toast.LENGTH_LONG).show();
             }
             FragmentManager manager = getSupportFragmentManager();
             manager.beginTransaction().replace(R.id.drawer_layout, fragment, fragment.getTag()).addToBackStack(null).commit();
             item.setChecked(true);
         }else if (id == R.id.nav_Actualizar) {
             TextView email = (TextView) findViewById(R.id.txtEmail);

             ActualizarPass fragment = new ActualizarPass();
             try {
                 Bundle args = new Bundle();
                 args.putString("email", email.getText().toString());
                 fragment.setArguments(args);
             } catch (Exception ex) {
                 Toast.makeText(MenuPerfil.this, "Algo paso aquí", Toast.LENGTH_LONG).show();
             }

             FragmentManager manager = getSupportFragmentManager();
             manager.beginTransaction().replace(R.id.drawer_layout, fragment, fragment.getTag()).addToBackStack(null).commit();
             item.setChecked(true);
         }else if (id == R.id.nav_Pago) {
             PagoFragment fragment = new PagoFragment();
             FragmentManager manager = getSupportFragmentManager();
             manager.beginTransaction().replace(R.id.drawer_layout, fragment, fragment.getTag()).addToBackStack(null).commit();
             item.setChecked(true);
         }

         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);
         return true;
     }

     @Override
     public void onFragmentInteraction(Uri uri) {}

 }