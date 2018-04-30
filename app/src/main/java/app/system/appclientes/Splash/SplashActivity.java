package app.system.appclientes.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import app.system.appclientes.LoginActivity;
import app.system.appclientes.MenuPerfil;
import app.system.appclientes.Utils.Utils;

public class SplashActivity extends AppCompatActivity
{
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);

        Intent perfil = new Intent(this, MenuPerfil.class);
        Intent login = new Intent(this, LoginActivity.class);

        if(!TextUtils.isEmpty(Utils.getPassPreferences(preferences)) && !TextUtils.isEmpty(Utils.getUserPreferences(preferences)))
        {
            String email = Utils.getUserPreferences(preferences);
            perfil.putExtra("usuario", email);
            startActivity(perfil);
        }
        else
            startActivity(login);

        finish();
    }
}
