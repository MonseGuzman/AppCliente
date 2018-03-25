package app.system.appclientes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistorialFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HistorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistorialFragment newInstance(String param1, String param2) {
        HistorialFragment fragment = new HistorialFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_historial, container, false);
        Bundle args = getArguments();
        String dato = args.getString("idCuenta");

        Tabla tabla = new Tabla(getActivity(), (TableLayout)v.findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tabla);

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

            //cargar la situacion
            String query ="SELECT Pagos.idPago, Pagos.fecha, Pagos.tipo, Agua.mInicial, Agua.mFinal, Situacion.descripcion, Pagos.otros, Agua.descuento, Pagos.total FROM Pagos inner join Agua on Agua.idPago = Pagos.idPago inner join Situacion on Agua.idSituacion = Situacion.idSituacion WHERE Pagos.idCuenta = " + dato;
            ResultSet resultado = estatuto.executeQuery(query);
            int x = 1;
            while (resultado.next())
            {
                ArrayList<String> elementos = new ArrayList<String>();
                elementos.add(Integer.toString(x));
                elementos.add(Integer.toString(resultado.getInt("idPago")));
                elementos.add(String.valueOf(resultado.getDate("fecha")));
                elementos.add(resultado.getString("tipo"));
                elementos.add(Integer.toString(resultado.getInt("mInicial")));
                elementos.add(Integer.toString(resultado.getInt("mFinal")));
                elementos.add(resultado.getString("descripcion"));
                elementos.add(Float.toString(resultado.getFloat("otros")));
                elementos.add(Float.toString(resultado.getFloat("descuento")));
                elementos.add(Float.toString(resultado.getFloat("total")));
                tabla.agregarFilaTabla(elementos);

                x++;
            }

            connection.close();

        }catch (SQLException E) {
            E.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
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
}