package app.system.appclientes;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ActualizarPass extends Fragment
{
    private Button cambiar;
    private EditText contra, nueva;
    private String usuario;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ActualizarPass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActualizarPass.
     */
    // TODO: Rename and change types and number of parameters
    public static ActualizarPass newInstance(String param1, String param2) {
        ActualizarPass fragment = new ActualizarPass();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle args = getArguments();
        usuario = args.getString("email", "mailNulo");
        //mailNulo sería el valor por defecto en caso de que args no contenga una

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_actualizar_pass, container, false);
        cambiar = (Button) v.findViewById(R.id.btnCambiar);
        contra = (EditText) v.findViewById(R.id.txtActual);
        nueva = (EditText) v.findViewById(R.id.txtNueva);

        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Actualizar();
            }
        });

        contra.setFocusable(true);
        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void Actualizar ()
    {
        String password = contra.getText().toString();
        String newPassword = nueva.getText().toString();

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

                String query = "SELECT * FROM Cuentas WHERE correo = '" + usuario + "' AND password = '" + password + "'";
                ResultSet resultado = estatuto.executeQuery(query);

                if (resultado.next())
                {
                    //REVISAR LUEGO
                    Toast.makeText(getContext(), "La contraseña ha sido cambiada éxitosamente", Toast.LENGTH_SHORT).show();
                    //actualizar
                    String query2 = "UPDATE Cuentas SET password = '" + newPassword + "' WHERE correo = '" + usuario + "'";
                    estatuto.executeQuery(query2);
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
}
