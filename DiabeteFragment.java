
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

public class DiabeteFragment extends Fragment {
    private EditText editTextDiabete;
    private Button buttonSave;
    private LineChart chartDiabete;
    private HealthDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diabete, container, false);
        
        // Initialiser la base de données
        db = Room.databaseBuilder(getContext(), HealthDatabase.class, "health-database")
                .allowMainThreadQueries() // Pour la démo seulement
                .build();

        // Initialiser les vues
        editTextDiabete = view.findViewById(R.id.editTextDiabete);
        buttonSave = view.findViewById(R.id.buttonSaveDiabete);
        chartDiabete = view.findViewById(R.id.chartDiabete);
        
        // Configurer le clic sur le bouton
        buttonSave.setOnClickListener(v -> saveDiabete());
        
        // Charger les données
        loadDiabeteData();
        
        return view;
    }

    private void saveDiabete() {
        String diabeteValue = editTextDiabete.getText().toString();
        if (!diabeteValue.isEmpty()) {
            // Créer et enregistrer dans la base de données
            DiabeteEntity diabete = new DiabeteEntity();
            diabete.setValue(diabeteValue);
            diabete.setTimestamp(new Date().getTime());
            
            db.diabeteDao().insert(diabete);
            
            // Rafraîchir le graphique
            loadDiabeteData();
            
            // Réinitialiser le champ
            editTextDiabete.setText("");
            
            Toast.makeText(getContext(), "Taux de glucose enregistré", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Veuillez entrer une valeur", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDiabeteData() {
        List<DiabeteEntity> diabeteList = db.diabeteDao().getAll();
        
        ArrayList<Entry> entries = new ArrayList<>();
        
        for (int i = 0; i < diabeteList.size(); i++) {
            DiabeteEntity diabete = diabeteList.get(i);
            entries.add(new Entry(i, Float.parseFloat(diabete.getValue())));
        }
        
        if (entries.isEmpty()) {
            // Si pas de données, masquer le graphique
            chartDiabete.setVisibility(View.GONE);
            return;
        }
        
        chartDiabete.setVisibility(View.VISIBLE);
        
        LineDataSet dataSet = new LineDataSet(entries, "Taux de glucose");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        
        LineData lineData = new LineData(dataSet);
        chartDiabete.setData(lineData);
        chartDiabete.invalidate(); // Rafraîchir
    }
}
