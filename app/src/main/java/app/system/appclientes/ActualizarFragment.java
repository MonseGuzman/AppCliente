package app.system.appclientes;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ActualizarFragment extends Fragment
{
    private Button btCambiar_Actualizar;
    private EditText etActual_Actualizar, etNueva_Actualizar;
    private String usuario;


    public ActualizarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_actualizar, container, false);

        btCambiar_Actualizar = (Button) v.findViewById(R.id.btCambiar_Actualizar);
        etActual_Actualizar = (EditText) v.findViewById(R.id.etActual_Actualizar);
        etNueva_Actualizar = (EditText) v.findViewById(R.id.etNueva_Actualizar);

        Bundle args = getArguments();
        usuario = args.getString("email", "mailNulo");
        //mailNulo sería el valor por defecto en caso de que args no contenga una

        btCambiar_Actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Actualizar();
            }
        });

        etActual_Actualizar.setFocusable(true);

        return v;
    }

    private void Actualizar()
    {
        String password = etActual_Actualizar.getText().toString();
        String newPassword = etNueva_Actualizar.getText().toString();

        if (validacion(newPassword, password)) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection connection;
            String url;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://bdAguaPotable.mssql.somee.com;databaseName=bdAguaPotable;user=SYSTEM-APP_SQLLogin_1;password=pn8akqjwxc";
                connection = DriverManager.getConnection(url);
                Statement estatuto = connection.createStatement();

                PreparedStatement comm;
                String query = "SELECT * FROM Cuentas WHERE correo = '" + usuario + "' AND password = '" + password + "'";
                ResultSet resultado = estatuto.executeQuery(query);

                if (resultado.next())
                {
                    //REVISAR LUEGO
                    Toast.makeText(getContext(), "La contraseña ha sido cambiada éxitosamente", Toast.LENGTH_SHORT).show();
                    //actualizar
                    String query2 = "UPDATE Cuentas SET password = '" + newPassword + "' WHERE correo = '" + usuario + "'";
                    comm = connection.prepareStatement(query2);
                    comm.executeQuery();
                }
                else
                    Toast.makeText(getContext(), "La contraseña no coincide", Toast.LENGTH_SHORT).show();

                resultado.close();
                connection.close();

            } catch (SQLException E) {
                E.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(getContext(), "Revise sus datos ingresados", Toast.LENGTH_SHORT).show();
    }

    private boolean validacion(String nueva, String anterior)
    {
        if (nueva.isEmpty() || anterior.isEmpty())
            return false;
        else if (nueva.equals(anterior))
            return false;
        return true;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
