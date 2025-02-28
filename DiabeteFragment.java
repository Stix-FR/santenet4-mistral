
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

public class DiabeteFragment extends Fragment {
    private EditText editTextDiabete;
    private Button buttonSaveDiabete;
    private LineChart lineChartDiabete;
    private HealthDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diabete, container, false);

        editTextDiabete = view.findViewById(R.id.editTextDiabete);
        buttonSaveDiabete = view.findViewById(R.id.buttonSaveDiabete);
        lineChartDiabete = view.findViewById(R.id.lineChartDiabete);

        db = Room.databaseBuilder(getContext(), HealthDatabase.class, "health-db").build();

        buttonSaveDiabete.setOnClickListener(v -> {
            String diabeteValue = editTextDiabete.getText().toString();
            if (diabeteValue.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez entrer une valeur", Toast.LENGTH_SHORT).show();
                return;
            }
            
            new Thread(() -> {
                DiabeteEntity diabete = new DiabeteEntity();
                diabete.setValue(diabeteValue);
                db.diabeteDao().insert(diabete);
                
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Données enregistrées avec succès", Toast.LENGTH_SHORT).show();
                    editTextDiabete.setText("");
                    loadChartData();
                });
            }).start();
        });

        loadChartData();
        return view;
    }

    private void loadChartData() {
        new Thread(() -> {
            List<DiabeteEntity> diabeteList = db.diabeteDao().getAll();
            List<Entry> entries = new ArrayList<>();
            
            for (int i = 0; i < diabeteList.size(); i++) {
                entries.add(new Entry(i, Float.parseFloat(diabeteList.get(i).getValue())));
            }

            getActivity().runOnUiThread(() -> {
                if (entries.isEmpty()) {
                    lineChartDiabete.clear();
                    lineChartDiabete.invalidate();
                    return;
                }

                LineDataSet dataSet = new LineDataSet(entries, "Glycémie");
                dataSet.setColor(Color.RED);
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setCircleColor(Color.RED);
                dataSet.setCircleRadius(4f);

                LineData lineData = new LineData(dataSet);
                lineChartDiabete.setData(lineData);
                lineChartDiabete.getDescription().setEnabled(false);
                lineChartDiabete.invalidate();
            });
        }).start();
    }
}
