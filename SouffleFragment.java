package com.example.healthtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class SouffleFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_souffle, container, false);
    }
}
package com.example.healthtracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SouffleFragment extends Fragment {
    private EditText editTextSouffle;
    private Button buttonSave;
    private LineChart chartSouffle;
    private HealthDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_souffle, container, false);
        
        // Initialiser la base de données
        db = Room.databaseBuilder(getContext(), HealthDatabase.class, "health-database")
                .allowMainThreadQueries() // Pour la démo seulement
                .build();

        // Initialiser les vues
        editTextSouffle = view.findViewById(R.id.editTextSouffle);
        buttonSave = view.findViewById(R.id.buttonSaveSouffle);
        chartSouffle = view.findViewById(R.id.chartSouffle);
        
        // Configurer le clic sur le bouton
        buttonSave.setOnClickListener(v -> saveSouffle());
        
        // Charger les données
        loadSouffleData();
        
        return view;
    }

    private void saveSouffle() {
        String souffleValue = editTextSouffle.getText().toString();
        if (!souffleValue.isEmpty()) {
            // Créer et enregistrer dans la base de données
            SouffleEntity souffle = new SouffleEntity();
            souffle.setValue(souffleValue);
            souffle.setTimestamp(new Date().getTime());
            
            db.souffleDao().insert(souffle);
            
            // Rafraîchir le graphique
            loadSouffleData();
            
            // Réinitialiser le champ
            editTextSouffle.setText("");
            
            Toast.makeText(getContext(), "Capacité pulmonaire enregistrée", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Veuillez entrer une valeur", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSouffleData() {
        List<SouffleEntity> souffleList = db.souffleDao().getAll();
        
        ArrayList<Entry> entries = new ArrayList<>();
        
        for (int i = 0; i < souffleList.size(); i++) {
            SouffleEntity souffle = souffleList.get(i);
            entries.add(new Entry(i, Float.parseFloat(souffle.getValue())));
        }
        
        if (entries.isEmpty()) {
            // Si pas de données, masquer le graphique
            chartSouffle.setVisibility(View.GONE);
            return;
        }
        
        chartSouffle.setVisibility(View.VISIBLE);
        
        LineDataSet dataSet = new LineDataSet(entries, "Capacité pulmonaire");
        dataSet.setColor(Color.YELLOW);
        dataSet.setValueTextColor(Color.BLACK);
        
        LineData lineData = new LineData(dataSet);
        chartSouffle.setData(lineData);
        chartSouffle.invalidate(); // Rafraîchir
    }
}
