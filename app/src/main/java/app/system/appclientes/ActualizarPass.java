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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActualizarPass.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActualizarPass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActualizarPass extends Fragment
{
    private ProgressDialog pDialog;

    private Button cambiar;
    private EditText contra, nueva;
    String dato;

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
        dato = args.getString("email", "mailNulo");
        //mailNulo sería el valor por defecto en caso de que args no contenga una

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_actualizar_pass, container, false);
        cambiar = (Button) v.findViewById(R.id.btnCambiar);
        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new Actualizar().execute();
                getActivity().getFragmentManager().popBackStack();

            }
        });

        contra = (EditText) v.findViewById(R.id.txtActual);
        nueva = (EditText) v.findViewById(R.id.txtNueva);


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class Actualizar extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Cargando datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String username = dato;
            String password = contra.getText().toString();
            String nueva2 = nueva.getText().toString();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            Connection connection;
            String url;

            try
            {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://bdAguaPotable.mssql.somee.com;databaseName=bdAguaPotable;user=SYSTEM-APP_SQLLogin_1;password=pn8akqjwxc";
                connection = DriverManager.getConnection(url);
                Statement estatuto = connection.createStatement();

                String query ="SELECT * FROM Cuentas WHERE correo = '"+ username +"' AND password = '"+ password +"'";
                ResultSet resultado = estatuto.executeQuery(query);

                if (resultado.next())
                {
                    //actualizar
                    String query2 ="UPDATE Cuentas SET password = '"+ nueva2 +"' WHERE correo = '"+ username +"'";
                    estatuto.executeQuery(query2);

                }
                else
                {
                    Toast.makeText(getContext(), "La contraseña no coincide", Toast.LENGTH_LONG).show();
                }
                connection.close();

            }catch (SQLException E)
            {
                E.printStackTrace();
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // descartar el diálogo una vez que el producto ha sido eliminado
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(getContext(), file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
