package ch.hsr_heroes.gadgeothek;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomServerPartialFragment extends Fragment {
    private SwitchCompat switchCustomServer;
    private TextInputLayout inputLayoutCustomServer;
    private TextInputEditText inputCustomServer;

    public interface CustomServerListener {
        void onServerChanged(String newServer, boolean valid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_server_partial, container, false);

        switchCustomServer = (SwitchCompat) view.findViewById(R.id.switch_custom_server);
        inputLayoutCustomServer = (TextInputLayout) view.findViewById(R.id.input_layout_custom_server_uri);
        inputCustomServer = (TextInputEditText) view.findViewById(R.id.input_custom_server_uri);

        inputCustomServer.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String uriString = inputCustomServer.getText().toString().trim();

                Uri uri = Uri.parse(uriString);
                boolean isValid = uri.toString().length() > 5;

                if (isValid) {
                    inputLayoutCustomServer.setErrorEnabled(false);
                } else {
                    inputLayoutCustomServer.setError(getString(R.string.enter_valid_uri));
                }

                ((CustomServerListener) getActivity()).onServerChanged(uri.toString(), isValid);
            }
        });

        switchCustomServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCustomServer.isChecked()) {
                    inputLayoutCustomServer.setVisibility(View.VISIBLE);
                } else {
                    inputLayoutCustomServer.setVisibility(View.INVISIBLE);
                }
            }
        });

        return view;
    }

    public boolean isChecked() {
        return switchCustomServer.isChecked();
    }

    public boolean hasError() {
        return inputLayoutCustomServer.isErrorEnabled();
    }

    public String getServer() {
        String customServer = inputCustomServer.getText().toString();

        if(switchCustomServer.isChecked()) {
            return customServer;
        } else {
            return getString(R.string.default_server);
        }
    }


}
