package com.example.vonlion.kupao;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Vonlion on 2016/2/11.
 */
public class Histroy_chart extends Activity implements AMap.OnMapScreenShotListener{
    private static final String APP_ID = "wx701d502de528777e";
    private IWXAPI api;
    private TextView distance1;
    private TextView time;
    private TextView energy;
    private TextView speed;
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private String starttime;
    private AMap aMap;
    private MapView traceMapView;
    private TextView steps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histroy_chart);
        Intent intent = getIntent();
        starttime = intent.getStringExtra("date");
        distance1 = (TextView)findViewById(R.id.distance1);
        time= (TextView)findViewById(R.id.time);
        speed = (TextView)findViewById(R.id.speed);
        energy = (TextView)findViewById(R.id.energy);
        traceMapView = (MapView) findViewById(R.id.traceMap);
        steps = (TextView) findViewById(R.id.steps);
        traceMapView.onCreate(savedInstanceState);

        //接入微信
        api = WXAPIFactory.createWXAPI(this,APP_ID,true);
        api.registerApp(APP_ID);

        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("usertb", null, "date like?", new String[]{starttime}, null, null, "date");
        if(cursor!=null){
            while(cursor.moveToNext()){
                distance1.setText(cursor.getString(cursor.getColumnIndex("distance")));
                time.setText(cursor.getString(cursor.getColumnIndex("time")));
                energy.setText(cursor.getString(cursor.getColumnIndex("energy")));
                speed.setText(cursor.getString(cursor.getColumnIndex("speed")));
                steps.setText(cursor.getString(cursor.getColumnIndex("theyCount")));
            }
        }
        db.close();
        showTrace();
        drawLineChart();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void drawLineChart(){
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("charttb", null, "starttime like?", new String[]{starttime}, null, null, "starttime");

        LineChart lineChart = (LineChart)findViewById(R.id.lineChart);
        xVals=new ArrayList<>();
        yVals=new ArrayList<>();

        int i = 0;
        if(cursor!=null){
            while (cursor.moveToNext()){
                yVals.add(new Entry(cursor.getFloat(cursor.getColumnIndex("curspeed")),i));
                xVals.add(cursor.getString(cursor.getColumnIndex("curtime")));
                i++;
                Log.i("DB",cursor.getString(cursor.getColumnIndex("curspeed")));
                Log.i("DB",cursor.getString(cursor.getColumnIndex("curtime")));
            }
        }
        Toast.makeText(getApplicationContext(),"本次采集"+i+"个"+"数据",Toast.LENGTH_LONG).show();
        dataSet=new LineDataSet(yVals,"");
        dataSet.setDrawFilled(true);
        dataSet.setColors(new int[]{0xffffffff});//设置曲线颜色
        dataSet.setDrawCubic(true);//将折线设置成曲线
        dataSet.setDrawValues(false);//不显示曲线上的数据
        dataSet.setCircleSize(3f);//数据点大小
        dataSet.setLineWidth(2.6f);//曲线宽度
        dataSet.setCircleColor(0xffffffff);//数据点外围颜色
        dataSet.setCircleColorHole(0xffffffff);//数据点中心颜色
        dataSet.setDrawCircles(true);//显示数据点
        data=new LineData(xVals,dataSet);
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftYAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setStartAtZero(true);//Y轴从0开始

        Bitmap bitmap;
        BitmapDrawable bitmapDrawable;
        Drawable drawable = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither=false;                     //Disable Dithering mode
        options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
        options.inMutable = true;
        options.inSampleSize = 2;
        options.inTempStorage=new byte[16 * 1024];

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.chart_bg3,options);
        bitmapDrawable = new BitmapDrawable(bitmap);
        drawable = bitmapDrawable;
        System.gc();

        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(0xeeffffff);
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setTextColor(0xeeffffff);
        rightYAxis.setEnabled(false);//取消显示右Y轴
        lineChart.setDrawGridBackground(false);//取消显示网格背景
        lineChart.setBackgroundColor(0x08000000);//地图背景颜色
        lineChart.setData(data);
        lineChart.setSelected(false);
        lineChart.dispatchSetSelected(false);
        lineChart.setDescriptionColor(0x11ffffff);//图表介绍文本的颜色
        lineChart.setBackground(drawable); //图表背景图片
        lineChart.setDescription("");
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        lineChart.setBorderColor(0xeeffffff);//轴线颜色
        lineChart.animateXY(3000,3000);//X、Y轴动画2s、2s
    }

    public void change_alpha(View v){
        Intent intent = new Intent(this,Histroy.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void showTrace(){
        initMap();
        LatLng[] latLng = new LatLng[3];
        int cnt = 0;
        latLng[1] = new LatLng(31.231706,121.472644);
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("tracetb", null, "starttime like?", new String[]{starttime}, null, null, "starttime");
        if(cursor!=null){
            while (cursor.moveToNext()){
                float speed = Float.parseFloat(cursor.getString(cursor.getColumnIndex("speed")));
                latLng[cnt] = new LatLng(Float.parseFloat(cursor.getString(cursor.getColumnIndex("latitude"))),
                        Float.parseFloat(cursor.getString(cursor.getColumnIndex("longitude"))));
                if(cnt == 2){
                    drawArc(latLng,speed);
                    latLng[0]=latLng[2];
                    cnt = 0;
                }
                cnt++;
                Log.i("DataBase",cursor.getString(cursor.getColumnIndex("starttime")));
                Log.i("DataBase",cursor.getString(cursor.getColumnIndex("latitude")));
                Log.i("DataBase",cursor.getString(cursor.getColumnIndex("longitude")));
                Log.i("DataBase",cursor.getString(cursor.getColumnIndex("speed")));
            }
            camMoveToCurPos(latLng[1]);
        }
    }

    public void drawArc(LatLng[] latLngs,float speed) {
        ArcOptions arcOptions;
        arcOptions = new ArcOptions();
        arcOptions.visible(true);
        arcOptions.strokeWidth(18f);
        arcOptions.strokeColor(choseColor(speed));
        aMap.addArc(arcOptions.point(latLngs[0], latLngs[1], latLngs[2]));
    }

    public int choseColor(float speed){
        int color = 0xFF4BEE12;
        float interval = 0.35f;
        int COLOR[] = { 0xff4bee12,0xff88ff16,0xffb4ff19,0xffdeff1d,0xffe9f71d,
                0xffeeec1d,0xfff2de1d,0xfff6ce1d,0xfff9bd1d,0xfffbae1d,
                0xfffb9e1d,0xfffc8d1d,0xfffd7e1d,0xfffc711d,0xfffe611d,
                0xfffd521d
        };
        for(int index = 0; index < 16; index++){
            if(index*interval<speed && speed<=(index+1)*interval){
                color = COLOR[index];
                break;
            }
            else if(3f<speed){
                color = COLOR[15];
                break;
            }
        }
        return color;
    }

    public void camMoveToCurPos(LatLng latLng) {
        CameraPosition cameraPosition;
        CameraUpdate cameraUpadate;
        cameraPosition = new CameraPosition(latLng, 17.0f, 0.0f, 0.0f);
        cameraUpadate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        aMap.animateCamera(cameraUpadate);
    }

    private void initMap() {
        if (aMap == null) {
            aMap = traceMapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        CameraUpdateFactory.zoomTo(18.0f);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
    }

    // 获取截图
    private static Bitmap myShot(Activity activity) {

        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;

        //
        int contentTop = activity.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight;
        int cutHeight = titleBarHeight + statusBarHeight;

        int bHeight = b1.getHeight();
        int nHeight=height - cutHeight;
        if((cutHeight +nHeight)>bHeight){
            nHeight = bHeight - cutHeight;
        }
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, cutHeight, width,  nHeight);
        view.destroyDrawingCache();
        //问题
        //       if (y + height > source.getHeight()) {
        //              throw new IllegalArgumentException("y + height must be <= bitmap.height()");
        //          }
        File f = new File("/sdcard/", "朋友圈.png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    //将bitmap转为byte格式数组
    public byte[] bmpToByteArray(final Bitmap bitmap,final boolean needRecycle){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,output);
        if(needRecycle){
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try{
            output.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //获取唯一标识请求
    private String buildTransaction(final String type){
        return (type == null)?String.valueOf(System.currentTimeMillis()):type+System.currentTimeMillis();
    }

    //分享到微信朋友圈n
    public void share(View view) {
        (new Thread(new Runnable() {
            public void run() {
                // Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.index);
                aMap.getMapScreenShot(Histroy_chart.this);
//                Bitmap bmp = myShot(Histroy_chart.this);
                myShot(Histroy_chart.this);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inDither=false;                     //Disable Dithering mode
                options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
                options.inMutable = true;
                options.inSampleSize = 1;
                options.inTempStorage=new byte[16 * 1024];
                Bitmap bitmap1 = BitmapFactory.decodeFile("/sdcard/history_map_trace.png/",options);
                Bitmap bitmap2 = BitmapFactory.decodeFile("/sdcard/朋友圈.png/",options);
                JointBitmapView jointBitmapView = new JointBitmapView(getApplicationContext(),bitmap1,bitmap2);

                Bitmap bmp = jointBitmapView.bitmap;
                WXImageObject imgObj = new WXImageObject(bmp);
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = imgObj;

                //设置缩列图
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp,120,150,true);
                bmp.recycle();
                msg.thumbData = bmpToByteArray(thumbBmp,true);

                //构造一个Req
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("img");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;

                api.sendReq(req);

            }
        })).start();
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap) {
        File f = new File("/sdcard/", "history_map_trace.png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap, int i) {

    }

    public class JointBitmapView extends View {
        public Bitmap bitmap;
        public JointBitmapView(Context context, Bitmap bit1, Bitmap bit2) {
            super(context);
            bitmap = newBitmap(bit1,bit2);
        }
        public Bitmap newBitmap(Bitmap bit1,Bitmap bit2){
            int width = bit1.getWidth();
            int height = bit2.getHeight();
            //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(bit2, 0, 0, null);
            canvas.drawBitmap(bit1, 0, 100, null);
            return bitmap;
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(bitmap, 0, 0, null);
            bitmap.recycle();
        }
    }
}
