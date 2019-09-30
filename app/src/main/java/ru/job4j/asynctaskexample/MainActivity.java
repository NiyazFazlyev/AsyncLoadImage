package ru.job4j.asynctaskexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = findViewById(R.id.button_start);
        bar = findViewById(R.id.progressBar);
    }

    public void startAsyncTask(View view) {
        SampleAsyncTask task = new SampleAsyncTask(MainActivity.this);
        task.execute(10);
    }

    private static class SampleAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<MainActivity> activityWeakReference;

        SampleAsyncTask(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            int count = 0;

            while (count < integers[0]) {
                publishProgress((count * 100) / integers[0]);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                count++;
            }
            return "Finish";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            System.out.println(values[0]);
            activity.bar.setProgress(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                System.out.println("no activity");
                return;
            }
            Toast.makeText(activity, "start", Toast.LENGTH_SHORT).show();
            activity.bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.bar.setProgress(0);
            activity.bar.setVisibility(View.INVISIBLE);
        }
    }
}
