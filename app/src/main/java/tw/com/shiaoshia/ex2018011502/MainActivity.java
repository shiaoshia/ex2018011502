package tw.com.shiaoshia.ex2018011502;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView)findViewById(R.id.imageView);
    }

    public void click01(View v) {
        //建立執行緒下載圖片
        new Thread() {
            @Override
            public void run() {
                super.run();
                String str_url = "https://upload.wikimedia.org/wikipedia/en/f/f2/Ubercon_vlad_rgb_250.gif";
                URL url;

                try {
                    url = new URL(str_url);
                    HttpURLConnection conn = null;
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];    //設定一次下載1024byte
                    int length;                     //設定讀取長度
                    //讀取完畢時=-1
                    while((length = inputStream.read(buf)) != -1) {
                        bos.write(buf,0,length); //將圖片資料寫入bos裡
                    }
                    byte[] results = bos.toByteArray(); //將bos的圖片資料轉成陣列存到results
                    //將results轉成圖片格式存到bmp
                    final Bitmap bmp = BitmapFactory.decodeByteArray(results,0,results.length);
                    //顯示圖片需在主執行緒執行
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(bmp); //秀出圖片
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
