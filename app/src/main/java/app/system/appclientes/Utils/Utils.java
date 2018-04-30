package app.system.appclientes.Utils;

import android.content.SharedPreferences;

public class Utils
{
    public static String getUserPreferences(SharedPreferences preferences)
    {
        return preferences.getString("email", "");
    }

    public static String getPassPreferences(SharedPreferences preferences)
    {
        return preferences.getString("contraseña", "");
    }
}
