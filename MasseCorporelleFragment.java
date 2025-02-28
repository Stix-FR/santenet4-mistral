
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

public class MasseCorporelleFragment extends Fragment {
    private EditText editTextPoids;
    private EditText editTextTaille;
    private Button buttonSave;
    private LineChart chartIMC;
    private HealthDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_masse_corporelle, container, false);
        
        // Initialiser la base de données
        db = Room.databaseBuilder(getContext(), HealthDatabase.class, "health-database")
                .allowMainThreadQueries() // Pour la démo seulement
                .build();

        // Initialiser les vues
        editTextPoids = view.findViewById(R.id.editTextPoids);
        editTextTaille = view.findViewById(R.id.editTextTaille);
        buttonSave = view.findViewById(R.id.buttonSaveIMC);
        chartIMC = view.findViewById(R.id.chartIMC);
        
        // Configurer le clic sur le bouton
        buttonSave.setOnClickListener(v -> saveIMC());
        
        // Charger les données
        loadIMCData();
        
        return view;
    }

    private void saveIMC() {
        String poidsStr = editTextPoids.getText().toString();
        String tailleStr = editTextTaille.getText().toString();
        
        if (!poidsStr.isEmpty() && !tailleStr.isEmpty()) {
            float poids = Float.parseFloat(poidsStr);
            float taille = Float.parseFloat(tailleStr) / 100; // cm en m
            
            float imc = poids / (taille * taille);
            
            // Créer et enregistrer dans la base de données
            MasseCorporelleEntity masseCorporelle = new MasseCorporelleEntity();
            masseCorporelle.setValue(String.format("%.2f", imc));
            masseCorporelle.setPoids(poids);
            masseCorporelle.setTaille(taille * 100); // m en cm
            masseCorporelle.setTimestamp(new Date().getTime());
            
            db.masseCorporelleDao().insert(masseCorporelle);
            
            // Rafraîchir le graphique
            loadIMCData();
            
            // Réinitialiser les champs
            editTextPoids.setText("");
            editTextTaille.setText("");
            
            Toast.makeText(getContext(), "IMC calculé et enregistré: " + String.format("%.2f", imc), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Veuillez entrer le poids et la taille", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadIMCData() {
        List<MasseCorporelleEntity> imcList = db.masseCorporelleDao().getAll();
        
        ArrayList<Entry> entries = new ArrayList<>();
        
        for (int i = 0; i < imcList.size(); i++) {
            MasseCorporelleEntity imc = imcList.get(i);
            entries.add(new Entry(i, Float.parseFloat(imc.getValue())));
        }
        
        if (entries.isEmpty()) {
            // Si pas de données, masquer le graphique
            chartIMC.setVisibility(View.GONE);
            return;
        }
        
        chartIMC.setVisibility(View.VISIBLE);
        
        LineDataSet dataSet = new LineDataSet(entries, "IMC");
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.BLACK);
        
        LineData lineData = new LineData(dataSet);
        chartIMC.setData(lineData);
        chartIMC.invalidate(); // Rafraîchir
    }
}
