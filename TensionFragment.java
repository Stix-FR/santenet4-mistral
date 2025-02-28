
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

public class TensionFragment extends Fragment {
    private EditText editTextTension;
    private Button buttonSaveTension;
    private Button buttonExportTension;
    private LineChart lineChartTension;
    private HealthDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tension, container, false);

        editTextTension = view.findViewById(R.id.editTextTension);
        buttonSaveTension = view.findViewById(R.id.buttonSaveTension);
        buttonExportTension = view.findViewById(R.id.buttonExportTension);
        lineChartTension = view.findViewById(R.id.lineChartTension);

        db = Room.databaseBuilder(getContext(), HealthDatabase.class, "health-db").build();

        buttonSaveTension.setOnClickListener(v -> {
            String tensionValue = editTextTension.getText().toString();
            if (tensionValue.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez entrer une valeur", Toast.LENGTH_SHORT).show();
                return;
            }
            
            new Thread(() -> {
                TensionEntity tension = new TensionEntity();
                tension.setValue(tensionValue);
                tension.setTimestamp(System.currentTimeMillis());
                db.tensionDao().insert(tension);
                
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Données enregistrées avec succès", Toast.LENGTH_SHORT).show();
                    editTextTension.setText("");
                    loadChartData();
                });
            }).start();
        });

        buttonExportTension.setOnClickListener(v -> {
            new Thread(() -> {
                List<TensionEntity> tensionList = db.tensionDao().getAll();
                Utils.exportDataToCSV(getContext(), tensionList, "tension_data.csv");
                
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Données exportées avec succès", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });

        loadChartData();
        return view;
    }

    private void loadChartData() {
        new Thread(() -> {
            List<TensionEntity> tensionList = db.tensionDao().getAll();
            List<Entry> entries = new ArrayList<>();
            
            for (int i = 0; i < tensionList.size(); i++) {
                entries.add(new Entry(i, Float.parseFloat(tensionList.get(i).getValue())));
            }

            getActivity().runOnUiThread(() -> {
                if (entries.isEmpty()) {
                    lineChartTension.clear();
                    lineChartTension.invalidate();
                    return;
                }

                LineDataSet dataSet = new LineDataSet(entries, "Tension");
                dataSet.setColor(Color.BLUE);
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setCircleColor(Color.BLUE);
                dataSet.setCircleRadius(4f);

                LineData lineData = new LineData(dataSet);
                lineChartTension.setData(lineData);
                lineChartTension.getDescription().setEnabled(false);
                lineChartTension.invalidate();
            });
        }).start();
    }
}
