package ru.job4j.asynctaskexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class LoadImageActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_image);
        image = findViewById(R.id.picture);
    }


    public void loadImage(View view) {
        LoadImageActivity.LoadAsyncTask task = new LoadImageActivity.LoadAsyncTask(LoadImageActivity.this);
        task.execute("https://icon-icons.com/icons2/2067/PNG/128/saturn_planet_icon_125419.png");
    }

    private static class LoadAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private WeakReference<LoadImageActivity> activityWeakReference;

        LoadAsyncTask(LoadImageActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            return loadImageFromNetwork(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            LoadImageActivity activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }
             activity.image.setImageBitmap(bitmap);
        }
    }

    private static Bitmap loadImageFromNetwork(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory
                    .decodeStream((InputStream) new URL(url)
                            .getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
