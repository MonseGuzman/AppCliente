package app.system.appclientes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FragmentCalculadora extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner Sptarifa, SpSituacion, SpMeses;
    Button calcular;
    TextView CFcalculadora, TARCalculadora, INFCalculadora, RecargoCalculadora, TotalCalculadora, DescuentoCalculadora ;

    ArrayList<String> datos = new ArrayList<String>();
    ArrayList<String> datos2 = new ArrayList<String>();
    ArrayList<String> datos3 = new ArrayList<String>();

    float cf, tar, infra, rec, des = 0;
    int descue;
    float n1, n2, n3, n4, n5;


    private FragmentCalculadora.OnFragmentInteractionListener mListener;

    public FragmentCalculadora() {
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
    public static FragmentCalculadora newInstance(String param1, String param2) {
        FragmentCalculadora fragment = new FragmentCalculadora();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCalculadora.OnFragmentInteractionListener) {
            mListener = (FragmentCalculadora.OnFragmentInteractionListener) context;
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

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnCalcular:

                try {
                    CFcalculadora.setText(String.valueOf(n1));
                    TARCalculadora.setText(String.valueOf(n2));
                    INFCalculadora.setText(String.valueOf(n3));
                    RecargoCalculadora.setText(String.valueOf(n4));
                    DescuentoCalculadora.setText(String.valueOf(des));
                    TotalCalculadora.setText(String.valueOf(n5));

                }catch (Exception e) {
                    Toast.makeText(getContext(), "hey onclick! " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        int idT = 0;
        String situa = SpSituacion.getSelectedItem().toString();
        String tari = Sptarifa.getSelectedItem().toString();
        String mes = SpMeses.getSelectedItem().toString();


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
            String query ="SELECT * FROM Situacion WHERE descripcion = '" + situa + "'";
            ResultSet resultado = estatuto.executeQuery(query);
            if (resultado.next())
            {
                descue = resultado.getInt("descuento");
                idT = resultado.getInt("idSituacion");
            }
            resultado.close();


            //cargar la tarifas
            String query2 ="SELECT * FROM Tarifas WHERE año = " + tari;
            resultado = estatuto.executeQuery(query2);
            if (resultado.next())
            {
                cf = resultado.getFloat("cf");
                tar = resultado.getFloat("tar");
                infra = resultado.getFloat("infra");
                rec = resultado.getFloat("recargos");

                n1= (cf / 12) *  Integer.parseInt(mes);
                n2 = (tar / 12) *  Integer.parseInt(mes);
                n3 = (infra / 12) *  Integer.parseInt(mes);
                n4 = (rec / 12) *  Integer.parseInt(mes);
            }
            resultado.close();

            float n = n1 + n2 + n3 + n4;

            if (idT != 6)
            {
                des = (n * descue)/ 100;
                n5 = n - des;
            }
            else {
                n5 = n;
            }
            System.out.println(n5 + " total");

            connection.close();
        }catch (SQLException E){
            E.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v =  inflater.inflate(R.layout.fragment_fragment_calculadora, container, false);

        SpMeses = (Spinner) v.findViewById(R.id.spinnerMes);
        SpSituacion = (Spinner) v.findViewById(R.id.spinnerSituacion);
        Sptarifa = (Spinner) v.findViewById(R.id.spinnerTarifa);
        calcular = (Button) v.findViewById(R.id.btnCalcular);

        CFcalculadora = (TextView) v.findViewById(R.id.txtCFCalculadora);
        TARCalculadora = (TextView) v.findViewById(R.id.txtTarifaCalculadora);
        INFCalculadora = (TextView) v.findViewById(R.id.txtInfraCalculadora);
        RecargoCalculadora = (TextView) v.findViewById(R.id.txtRecargoCalculadora);
        TotalCalculadora = (TextView) v.findViewById(R.id.txtTotalCalculadora);
        DescuentoCalculadora = (TextView) v.findViewById(R.id.txtDescuentoCalculadora);

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
            String query ="SELECT * FROM Situacion";
            ResultSet resultado = estatuto.executeQuery(query);
            while (resultado.next())
            {
                String descripcion = resultado.getString("descripcion");
                datos.add(descripcion);
            }
            resultado.close();

            //cargar la tarifas
            String query2 ="SELECT * FROM Tarifas";
            resultado = estatuto.executeQuery(query2);
            while (resultado.next())
            {
                String fech = resultado.getString("año");
                datos2.add(fech);
            }
            resultado.close();

            for (int x=1; x<=12; x++)
            {
                datos3.add(String.valueOf(x));
            }


            //spinner situacion
            ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, datos);
            SpSituacion.setAdapter(adapter);

            //spinner tarifas
            ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, datos2);
            Sptarifa.setAdapter(adapter2);

            //spinner meses
            ArrayAdapter adapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, datos3);
            SpMeses.setAdapter(adapter3);

            connection.close();

        }catch (SQLException E)
        {
            E.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        SpSituacion.setOnItemSelectedListener(this);
        SpMeses.setOnItemSelectedListener(this);
        Sptarifa.setOnItemSelectedListener(this);

        calcular.setOnClickListener(this);

        return v;
    }
}