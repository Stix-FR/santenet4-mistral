
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
import java.util.List;

public class MasseCorporelleFragment extends Fragment {
    private EditText editTextPoids;
    private EditText editTextTaille;
    private Button buttonSaveIMC;
    private LineChart lineChartIMC;
    private HealthDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_masse_corporelle, container, false);

        editTextPoids = view.findViewById(R.id.editTextPoids);
        editTextTaille = view.findViewById(R.id.editTextTaille);
        buttonSaveIMC = view.findViewById(R.id.buttonSaveIMC);
        lineChartIMC = view.findViewById(R.id.lineChartIMC);

        db = Room.databaseBuilder(getContext(), HealthDatabase.class, "health-db").build();

        buttonSaveIMC.setOnClickListener(v -> {
            String poids = editTextPoids.getText().toString();
            String taille = editTextTaille.getText().toString();
            
            if (poids.isEmpty() || taille.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            
            float poidsFloat = Float.parseFloat(poids);
            float tailleFloat = Float.parseFloat(taille) / 100; // cm to m
            float imc = poidsFloat / (tailleFloat * tailleFloat);
            
            new Thread(() -> {
                MasseCorporelleEntity masseCorporelle = new MasseCorporelleEntity();
                masseCorporelle.setValue(String.format("%.1f", imc));
                db.masseCorporelleDao().insert(masseCorporelle);
                
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "IMC calculé: " + String.format("%.1f", imc), Toast.LENGTH_SHORT).show();
                    editTextPoids.setText("");
                    editTextTaille.setText("");
                    loadChartData();
                });
            }).start();
        });

        loadChartData();
        return view;
    }

    private void loadChartData() {
        new Thread(() -> {
            List<MasseCorporelleEntity> imcList = db.masseCorporelleDao().getAll();
            List<Entry> entries = new ArrayList<>();
            
            for (int i = 0; i < imcList.size(); i++) {
                entries.add(new Entry(i, Float.parseFloat(imcList.get(i).getValue())));
            }

            getActivity().runOnUiThread(() -> {
                if (entries.isEmpty()) {
                    lineChartIMC.clear();
                    lineChartIMC.invalidate();
                    return;
                }

                LineDataSet dataSet = new LineDataSet(entries, "IMC");
                dataSet.setColor(Color.GREEN);
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setCircleColor(Color.GREEN);
                dataSet.setCircleRadius(4f);

                LineData lineData = new LineData(dataSet);
                lineChartIMC.setData(lineData);
                lineChartIMC.getDescription().setEnabled(false);
                lineChartIMC.invalidate();
            });
        }).start();
    }
}
