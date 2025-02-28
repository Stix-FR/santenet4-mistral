
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

public class TensionFragment extends Fragment {
    private EditText editTextTension;
    private Button buttonSave;
    private LineChart chartTension;
    private HealthDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tension, container, false);
        
        // Initialiser la base de données
        db = Room.databaseBuilder(getContext(), HealthDatabase.class, "health-database")
                .allowMainThreadQueries() // Pour la démo seulement
                .build();

        // Initialiser les vues
        editTextTension = view.findViewById(R.id.editTextTension);
        buttonSave = view.findViewById(R.id.buttonSaveTension);
        chartTension = view.findViewById(R.id.chartTension);
        
        // Configurer le clic sur le bouton
        buttonSave.setOnClickListener(v -> saveTension());
        
        // Charger les données
        loadTensionData();
        
        return view;
    }

    private void saveTension() {
        String tensionValue = editTextTension.getText().toString();
        if (!tensionValue.isEmpty()) {
            // Créer et enregistrer dans la base de données
            TensionEntity tension = new TensionEntity();
            tension.setValue(tensionValue);
            tension.setTimestamp(new Date().getTime());
            
            db.tensionDao().insert(tension);
            
            // Rafraîchir le graphique
            loadTensionData();
            
            // Réinitialiser le champ
            editTextTension.setText("");
            
            Toast.makeText(getContext(), "Tension enregistrée", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Veuillez entrer une valeur", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTensionData() {
        List<TensionEntity> tensions = db.tensionDao().getAll();
        
        ArrayList<Entry> entries = new ArrayList<>();
        
        for (int i = 0; i < tensions.size(); i++) {
            TensionEntity tension = tensions.get(i);
            entries.add(new Entry(i, Float.parseFloat(tension.getValue())));
        }
        
        if (entries.isEmpty()) {
            // Si pas de données, masquer le graphique
            chartTension.setVisibility(View.GONE);
            return;
        }
        
        chartTension.setVisibility(View.VISIBLE);
        
        LineDataSet dataSet = new LineDataSet(entries, "Tension");
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.BLACK);
        
        LineData lineData = new LineData(dataSet);
        chartTension.setData(lineData);
        chartTension.invalidate(); // Rafraîchir
    }
}
