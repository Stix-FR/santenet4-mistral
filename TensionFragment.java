package com.example.healthtracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TensionFragment extends Fragment {
    private EditText editTextTension;
    private Button buttonSaveTension;
    private Button buttonExportTension;
    private LineChart lineChartTension;
    private HealthDatabase db;
    private FirebaseFirestore firestoreDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tension, container, false);

        editTextTension = view.findViewById(R.id.editTextTension);
        buttonSaveTension = view.findViewById(R.id.buttonSaveTension);
        buttonExportTension = view.findViewById(R.id.buttonExportTension);
        lineChartTension = view.findViewById(R.id.lineChartTension);

        db = Room.databaseBuilder(getContext(), HealthDatabase.class, "health-db").build();
        firestoreDb = FirebaseFirestore.getInstance();

        buttonSaveTension.setOnClickListener(v -> {
            String tensionValue = editTextTension.getText().toString();
            new Thread(() -> {
                TensionEntity tension = new TensionEntity();
                tension.setValue(tensionValue);
                db.tensionDao().insert(tension);
                syncDataToFirestore(tension);
                getActivity().runOnUiThread(this::loadChartData);
            }).start();
        });

        buttonExportTension.setOnClickListener(v -> {
            new Thread(() -> {
                List<TensionEntity> tensionList = db.tensionDao().getAll();
                Utils.exportDataToCSV(getContext(), tensionList, "tension_data.csv");
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

            LineDataSet dataSet = new LineDataSet(entries, "Tension");
            dataSet.setColor(Color.BLUE);
            dataSet.setValueTextColor(Color.BLACK);

            LineData lineData = new LineData(dataSet);
            lineChartTension.setData(lineData);
            lineChartTension.invalidate();
        }).start();
    }

    private void syncDataToFirestore(TensionEntity tension) {
        Map<String, Object> data = new HashMap<>();
        data.put("value", tension.getValue());
        data.put("timestamp", System.currentTimeMillis());

        firestoreDb.collection("tension")
            .add(data)
            .addOnSuccessListener(documentReference -> {
                // Document successfully written
            })
            .addOnFailureListener(e -> {
                // Error writing document
            });
    }
}
