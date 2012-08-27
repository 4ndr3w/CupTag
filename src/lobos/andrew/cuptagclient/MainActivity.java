package lobos.andrew.cuptagclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.zxing.integration.android.IntentIntegrator;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	IntentIntegrator scanIntegrator;
	private SharedPreferences prefs;
	Button ownerChange;
	Socket remoteConn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanIntegrator = new IntentIntegrator(this);
        setContentView(R.layout.activity_main);
        prefs = getPreferences(MODE_PRIVATE);
        ownerChange = (Button) findViewById(R.id.button2);
        
        ownerChange.setText("Name: "+prefs.getString("cupowner", "Unknown"));
        

        ownerChange.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				final EditText inputBox = new EditText(MainActivity.this);
				AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
				dialog.setTitle("Change Name");
				dialog.setView(inputBox);
				
				dialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						String name = inputBox.getText().toString();
						if ( name.equals("") )
							name = "Unknown";
						
						prefs.edit().putString("cupowner", name).apply();
						ownerChange.setText("Name: "+name);
					}
				});
				
				dialog.show();
			}
		});
        
        this.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener()
        {

			public void onClick(View v) {
				scanIntegrator.initiateScan();
			}
        	
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
    	if ( intent == null ) return;
    	
    	String data = intent.getStringExtra("SCAN_RESULT");
    	new CheckCup(MainActivity.this).execute(data, prefs.getString("cupowner", "Unknown"));
    }
}
