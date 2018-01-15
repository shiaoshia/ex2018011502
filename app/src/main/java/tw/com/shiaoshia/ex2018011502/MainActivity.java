package tw.com.shiaoshia.ex2018011502;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    TextView tv,tv2,tv3;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView)findViewById(R.id.imageView);
        tv = (TextView)findViewById(R.id.textView);
        tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);
        pb = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void click01(View v) {
        iv.setVisibility(View.INVISIBLE);   //隱藏圖片
        pb.setVisibility(View.VISIBLE);     //顯示下載條
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
                    final int totalLength = conn.getContentLength();  //讀取總長度
                    int sum =0;                     //存放讀取進度
                    int length;                     //設定讀取長度
                    //讀取完畢時=-1
                    while((length = inputStream.read(buf)) != -1) {
                        sum += length;  //存放讀取進度
                        final int tmp = sum;    //暫存讀取進度
                        bos.write(buf,0,length); //將圖片資料寫入bos裡
                        runOnUiThread(new Runnable() {  //回到主執行緒，顯示文字
                            @Override
                            public void run() {
                                tv.setText(String.valueOf(tmp) + "/" + totalLength);
                                pb.setProgress(100 * tmp / totalLength);    //顯示下載條進度
                            }
                        });
                    }
                    byte[] results = bos.toByteArray(); //將bos的圖片資料轉成陣列存到results
                    //將results轉成圖片格式存到bmp
                    final Bitmap bmp = BitmapFactory.decodeByteArray(results,0,results.length);
                    //顯示圖片需在主執行緒執行
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(bmp); //秀出圖片
                            pb.setVisibility(View.INVISIBLE);   //隱藏下載條
                            iv.setVisibility(View.VISIBLE);     //顯示圖片
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

    public void click02(View v) {
        MyTask task = new MyTask();
        task.execute(10);   //設定計數10次
    }

    //AsyncTask為抽象類別，需繼承它，並實作利用泛型<Params,Progress,Result>
    class MyTask extends AsyncTask<Integer,Integer,String> {

        @Override //主執行緒執行，接收副執行緒傳過來的值
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv3.setText(s);
        }

        @Override   //主執行緒執行，當副執行緒結束會回傳值到這裡執行
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //接收副執行緒執行完回傳的值
            tv2.setText(String.valueOf(values[0]));
        }

        @Override   //在背景執行，副執行緒
        protected String doInBackground(Integer... integers) {
            int i;
            //接收計數值
            for(i=0;i<=integers[0];i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("TASK","doInBackground, i+" + i);
                publishProgress(i); //將i執傳給onPreExecute
            }
            return "Okay";  //將執傳給onProgressUpdate
        }
    }

    public void click03(View v) {
        iv.setVisibility(View.INVISIBLE);   //隱藏圖片
        pb.setVisibility(View.VISIBLE);     //顯示下載條
        MyImageTask task = new MyImageTask();
        task.execute("https://upload.wikimedia.org/wikipedia/en/f/f2/Ubercon_vlad_rgb_250.gif");
    }

    class MyImageTask extends AsyncTask<String,Integer,Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            iv.setImageBitmap(bitmap);
            pb.setVisibility(View.INVISIBLE);     //隱藏下載條
            iv.setVisibility(View.VISIBLE);   //顯示圖片
        }

        @Override

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb.setProgress(values[0]);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String str_url = strings[0];
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
                final int totalLength = conn.getContentLength();  //讀取總長度
                int sum =0;                     //存放讀取進度
                int length;                     //設定讀取長度
                //讀取完畢時=-1
                while((length = inputStream.read(buf)) != -1) {
                    sum += length;  //存放讀取進度
                    final int tmp = sum;    //暫存讀取進度
                    bos.write(buf,0,length); //將圖片資料寫入bos裡
                    publishProgress(100*tmp/totalLength);   //告知onProgressUpdate目前進度
                }
                byte[] results = bos.toByteArray(); //將bos的圖片資料轉成陣列存到results
                //將results轉成圖片格式存到bmp
                final Bitmap bmp = BitmapFactory.decodeByteArray(results,0,results.length);
                return bmp; //將圖片檔傳給onPostExecute
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
