package app.system.appclientes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReciboFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReciboFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReciboFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner Sid, SYear;
    RadioButton Rid, RYear;
    Button generar, buscar;
    TextView idPago, fecha, TipoPago, Descripcion, CFRecibo, RecargoRecibo, TarifaRecibo, INFRecibo, OtrosRecibo, DesRecibo, TotalRecibo;

    Boolean ban = false;

    int IdPago;
    String Fecha, Tipo, descrip;
    float cf, recargo, tar, infra, otros, des, total;

    ArrayList<String> datos = new ArrayList<String>();
    ArrayList<String> datos2 = new ArrayList<String>();

    private final static String NOMBRE_DIRECTORIO = "MiPdf";
    private final static String NOMBRE_DOCUMENTO = "recibo.pdf";
    private final static String ETIQUETA_ERROR = "ERROR";
    boolean a = true;

    private OnFragmentInteractionListener mListener;

    public ReciboFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReciboFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReciboFragment newInstance(String param1, String param2) {
        ReciboFragment fragment = new ReciboFragment();
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
        View v = inflater.inflate(R.layout.fragment_recibo, container, false);

        Sid = (Spinner) v.findViewById(R.id.spinnerid);
        SYear = (Spinner) v.findViewById(R.id.spinnerYear);
        Rid = (RadioButton) v.findViewById(R.id.rbID);
        RYear = (RadioButton) v.findViewById(R.id.rbYear);

        generar = (Button) v.findViewById(R.id.btnGenerar);
        buscar = (Button) v.findViewById(R.id.btnCalcularRecibo);
        RadioGroup rdgGrupo = (RadioGroup) v.findViewById(R.id.radioGroup);

        idPago = (TextView) v.findViewById(R.id.txtidPago);
        fecha = (TextView) v.findViewById(R.id.txtFecha);
        TipoPago = (TextView) v.findViewById(R.id.txtTipo);
        Descripcion = (TextView) v.findViewById(R.id.txtDescrip);
        CFRecibo = (TextView) v.findViewById(R.id.txtCFRecibo);
        RecargoRecibo = (TextView) v.findViewById(R.id.txtRecargoRecibo);
        TarifaRecibo = (TextView) v.findViewById(R.id.txtTarifaRecibo);
        INFRecibo = (TextView) v.findViewById(R.id.txtInfraRecibo);
        OtrosRecibo = (TextView) v.findViewById(R.id.txtOtrosC);
        DesRecibo = (TextView) v.findViewById(R.id.txtDescuento);
        TotalRecibo = (TextView) v.findViewById(R.id.txtTotalRecibo);


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

            //cargar la id
            String query ="SELECT * FROM Pagos";
            ResultSet resultado = estatuto.executeQuery(query);
            while (resultado.next())
            {
                String idp = String.valueOf(resultado.getInt("idPago"));
                datos.add(idp);

                String fech = String.valueOf(resultado.getDate("fecha"));
                datos2.add(fech);
            }


            //spinner id
            ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, datos);
            Sid.setAdapter(adapter);

            //spinner fecha
            ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, datos2);
            SYear.setAdapter(adapter2);

            connection.close();

        }catch (SQLException E)
        {
            E.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        //generar.setOnClickListener(this);
        buscar.setOnClickListener(this);
        rdgGrupo.setOnCheckedChangeListener(this);
        SYear.setOnItemSelectedListener(this);
        Sid.setOnItemSelectedListener(this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection;
        String url;

        Bundle args = getArguments();
        String idC = args.getString("idCuentas");


        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            url = "jdbc:jtds:sqlserver://bdAguaPotable.mssql.somee.com;databaseName=bdAguaPotable;user=SYSTEM-APP_SQLLogin_1;password=pn8akqjwxc";
            connection = DriverManager.getConnection(url);
            Statement estatuto = connection.createStatement();


            if (ban)
            {
                String idP = Sid.getSelectedItem().toString();
                System.out.println(idP);

                String query = "SELECT Pagos.idPago, Pagos.fecha, Pagos.tipo, Situacion.descripcion, Agua.cuotaFija, Agua.recargo, Agua.tar, Agua.infraestructura, Pagos.otros, Agua.descuento, Pagos.total FROM Pagos inner join Agua on Agua.idPago = Pagos.idPago inner join situacion on Agua.idSituacion = Situacion.idSituacion WHERE Pagos.idPago = " + idP + " AND Pagos.idCuenta = " + idC;
                ResultSet resultado = estatuto.executeQuery(query);
                if (resultado.next()) {

                    //llenar tablas

                    IdPago = resultado.getInt("idPago");
                    Fecha = String.valueOf(resultado.getDate("fecha"));
                    Tipo = resultado.getString("tipo");
                    descrip = resultado.getString("descripcion");
                    cf = resultado.getFloat("cuotaFija");
                    recargo = resultado.getFloat("recargo");
                    tar = resultado.getFloat("tar");
                    infra = resultado.getFloat("infraestructura");
                    otros = resultado.getFloat("otros");
                    des = resultado.getFloat("descuento");
                    total = resultado.getFloat("total");
                }
                resultado.close();

            }else
            {
                String tip = SYear.getSelectedItem().toString();
                System.out.println(tip);
                //cargar
                String query2 = "SELECT Pagos.idPago, Pagos.fecha, Pagos.tipo, Situacion.descripcion, Agua.cuotaFija, Agua.recargo, Agua.tar, Agua.infraestructura, Pagos.otros, Agua.descuento, Pagos.total FROM Pagos inner join Agua on Agua.idPago = Pagos.idPago inner join Situacion on Agua.idSituacion = Situacion.idSituacion WHERE Pagos.Fecha = " + tip + " AND Pagos.idCuenta = " + idC;
                ResultSet resultado2 = estatuto.executeQuery(query2);
                if (resultado2.next()) {
                    //llenar tablas
                    IdPago = resultado2.getInt("idPago");
                    Fecha = String.valueOf(resultado2.getDate("fecha"));
                    Tipo = resultado2.getString("tipo");
                    descrip = resultado2.getString("descripcion");
                    cf = resultado2.getFloat("cuotaFija");
                    recargo = resultado2.getFloat("recargo");
                    tar = resultado2.getFloat("tar");
                    infra = resultado2.getFloat("infraestructura");
                    otros = resultado2.getFloat("otros");
                    des = resultado2.getFloat("descuento");
                    total = resultado2.getFloat("total");
                }
                resultado2.close();
            }
            connection.close();
        }catch (SQLException E) {
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

    @Override
    public void onClick(View v) {
        Document documento = new Document();

        //falta mensaje
        //acomoda datos
        switch (v.getId())
        {
            case R.id.btnGenerar:
                try {

                    // Creamos el fichero con el nombre que deseemos.
                    File f = crearFichero(NOMBRE_DOCUMENTO);

                    // Creamos el flujo de datos de salida para el fichero donde
                    // guardaremos el pdf.
                    FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());

                    // Asociamos el flujo que acabamos de crear al documento.
                    PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

                    // Incluimos el píe de página y una cabecera
                    HeaderFooter cabecera = new HeaderFooter(new Phrase("SYSTEM-APP"), false);
                    HeaderFooter pie = new HeaderFooter(new Phrase(
                            "Ciudad Guzmán, Jalisco"), false);

                    documento.setHeader(cabecera);
                    documento.setFooter(pie);

                    // Abrimos el documento.
                    documento.open();

                    // Añadimos un título con la fuente por defecto.
                    documento.add(new Paragraph("Recibo"));

                    // Añadimos un título con una fuente personalizada.
                    documento.add(new Paragraph("Ayuntamiento de Ciudad Guzmán, Jalisco"));

                    // Insertamos una tabla.
                    PdfPTable tabla = new PdfPTable(5);
                    for (int i = 0; i < 15; i++) {
                        tabla.addCell("Celda " + i);
                    }
                    documento.add(tabla);



                } catch (DocumentException e) {

                    Log.e(ETIQUETA_ERROR, e.getMessage());

                } catch (IOException e) {

                    Log.e(ETIQUETA_ERROR, e.getMessage());

                } finally {

                    // Cerramos el documento.
                    documento.close();
                }
                break;
            
            //mensaje
            //DialogoAlerta();
            case R.id.btnCalcularRecibo:
                if (ban)
                {
                    Sid.setOnItemSelectedListener(this);
                }else
                {
                    SYear.setOnItemSelectedListener(this);
                }
                idPago.setText(String.valueOf(IdPago));
                fecha.setText(Fecha);
                TipoPago.setText(Tipo);
                Descripcion.setText(descrip);
                CFRecibo.setText(String.valueOf(cf));
                RecargoRecibo.setText(String.valueOf(recargo));
                TarifaRecibo.setText(String.valueOf(tar));
                INFRecibo.setText(String.valueOf(infra));
                OtrosRecibo.setText(String.valueOf(otros));
                DesRecibo.setText(String.valueOf(des));
                TotalRecibo.setText(String.valueOf(total));
                break;
        }

    }

    /*public class DialogoAlerta extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setMessage("Esto es un mensaje de alerta.")
                    .setTitle("Información")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            return builder.create();
        }
    }*/

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId)
    {
        if (checkedId == R.id.rbID)
        {
            Sid.setVisibility(View.VISIBLE);
            SYear.setVisibility(View.INVISIBLE);
            ban = true;
            Sid.setOnItemSelectedListener(this);

        }
        else if (checkedId == R.id.rbYear)
        {
            Sid.setVisibility(View.INVISIBLE);
            SYear.setVisibility(View.VISIBLE);
            ban = false;
            SYear.setOnItemSelectedListener(this);
        }

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


    /**
     * Crea un fichero con el nombre que se le pasa a la función y en la ruta
     * especificada.
     *
     * @param nombreFichero
     * @return
     * @throws IOException
     */
    public static File crearFichero(String nombreFichero) throws IOException
    {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    /**
     * Obtenemos la ruta donde vamos a almacenar el fichero.
     *
     * @return
     */
    public static File getRuta() {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), NOMBRE_DIRECTORIO);

            if (ruta != null)
            {
                if (!ruta.mkdirs())
                {
                    if (!ruta.exists())
                    {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }

}