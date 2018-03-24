package app.system.appclientes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentQuejas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentQuejas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentQuejas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentQuejas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentQuejas.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentQuejas newInstance(String param1, String param2) {
        FragmentQuejas fragment = new FragmentQuejas();
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
        View view = inflater.inflate(R.layout.fragment_fragment_quejas, container, false);

        Button enviar = (Button) view.findViewById(R.id.btnEnviar);
        final EditText observaciones = (EditText)view.findViewById(R.id.txtObservacion);
        final RadioButton queja = (RadioButton)view.findViewById(R.id.rbQueja);
        final RadioButton fuga = (RadioButton)view.findViewById(R.id.rbFugas);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (queja.isChecked())
                {
                    Intent itSend = new Intent(Intent.ACTION_SEND);
                    itSend.setData(Uri.parse("mailto:"));
                    itSend.setType("text/plain");
                    //correo a enviar
                    itSend.putExtra(Intent.EXTRA_EMAIL,new String[] {"andreaguzman.lopez23@gmail.com"});
                    // asunto
                    itSend.putExtra(Intent.EXTRA_SUBJECT, "Asunto: Queja");
                    //mensaje
                    itSend.putExtra(Intent.EXTRA_TEXT, observaciones.getText().toString());
                    //enviar
                    try
                    {
                        //enviar
                        startActivity(itSend);
                    }
                    catch(android.content.ActivityNotFoundException e)
                    {
                        Toast.makeText(getContext(), "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                    }
                }
                if(fuga.isChecked())
                {
                    Intent itSend = new Intent(Intent.ACTION_SEND);
                    itSend.setType("text/plain");
                    //correo a enviar
                    itSend.putExtra(Intent.EXTRA_EMAIL, new String [] {"andreaguzman.lopez23@gmail.com"});
                    // asunto
                    itSend.putExtra(Intent.EXTRA_SUBJECT, "Asunto: Fugas");
                    //mensaje
                    itSend.putExtra(Intent.EXTRA_TEXT, observaciones.getText().toString());

                    try
                    {
                        //enviar
                        startActivity(itSend);
                    }
                    catch(android.content.ActivityNotFoundException e)
                    {
                        Toast.makeText(getContext(), "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Seleccione un Asunto", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // Inflate the layout for this fragment
        return view;
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