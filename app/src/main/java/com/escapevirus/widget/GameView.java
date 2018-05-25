package com.escapevirus.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.widget.Toast;

import com.escapevirus.entity.LeadEntity;
import com.escapevirus.entity.OvalEntity;
import com.escapevirus.entity.ScoreEntity;
import com.escapevirus.enumfolder.DirectionEnum;
import com.escapevirus.enumfolder.GameDifficulty;
import com.xpping.windows10.widget.ToastView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 是男人就坚持10秒 游戏绘制view
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final int GAME_OVER=0;
    private int score=0;
    private SurfaceHolder mSurfaceHolder;
    private List<OvalEntity> ovalEntities = new ArrayList<>();
    private LeadEntity leadEntity;
    private ScoreEntity scoreEntity;
    private int[] colors = new int[]{Color.parseColor("#F35325"), Color.parseColor("#81bc06")
            , Color.parseColor("#05a6f0"), Color.parseColor("#ffba08")};

    private int leadColor = Color.BLACK;

    private DirectionEnum[] directionEna=new DirectionEnum[]{DirectionEnum.left,DirectionEnum.right,DirectionEnum.top,DirectionEnum.bottom};

    private GameDifficulty gameDifficulty=GameDifficulty.superSimple;

    private GameProgressListener gameProgressListener;

    public void setGameProgressListener(GameProgressListener gameProgressListener) {
        this.gameProgressListener = gameProgressListener;
    }

    public void setDrawing(boolean drawing) {
        isDrawing = drawing;
    }

    /*
            *设置游戏难度
            */
    public void setGameDifficulty(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
        ovalEntities=new ArrayList<>();
        isDrawing=true;
        if(leadEntity!=null) {
            leadEntity.setX(getScreenWidth(getContext()) / 2);
            leadEntity.setY(getScreenHeight(getContext()) / 2);
            score=0;
            startTime();

        }else{
            init();
        }
        new Thread(GameView.this).start();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GAME_OVER:
                    if(gameProgressListener!=null)
                        gameProgressListener.onGameOver(score);

                    break;
                case 1:
                    break;
            }
        }
    };

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ToastView.getInstaller(getContext()).setText("按住屏幕以开始，挪开屏幕暂停，您操纵的黑色小球碰到其他小球游戏即结束！").show();

        createOval();
        leadEntity = new LeadEntity();
        leadEntity.setX(getScreenWidth(getContext())/2);
        leadEntity.setY(getScreenHeight(getContext()) / 2);
        leadEntity.setRadius(20);
        Paint paint = new Paint();
        paint.setColor(leadColor);
        leadEntity.setPaint(paint);

        scoreEntity=new ScoreEntity();
        scoreEntity.setX(20);
        scoreEntity.setY(getStatusHeight(getContext())+40);
        scoreEntity.setText("");
        paint = new Paint();
        paint.setTextSize(32);
        paint.setAntiAlias(true);
        scoreEntity.setPaint(paint);
        /**通过holder去申请绘图表面的画布，surfaceview其实draw()或dispathDraw()都只是一块默认的黑色区域，并不是用作宿主
         * 真正要做的事情由开发者自行绘制，绘制之前就是通过holder获取一块内存区域的画布，
         * 然后可在UI线程或工作线程在这个画布上进行绘制所需要的视图，最后还是通过holder提交这个画布就可以显示
         * */
        mSurfaceHolder = getHolder();
        //回调
        mSurfaceHolder.addCallback(this);
    }

    private void startTime(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isStarting) {
                    score++;
                    String scoreGameDifficulty=gameDifficulty==GameDifficulty.superSimple?"超简单":
                            gameDifficulty==GameDifficulty.simple?"简单":
                                    gameDifficulty==GameDifficulty.easy?"容易":
                                            gameDifficulty==GameDifficulty.difficulty?"困难":"地狱";
                    scoreEntity.setText("当前游戏模式："+scoreGameDifficulty+"，分数："+score);
                }
                if(isDrawing)
                    startTime();
            }
        }).start();
    }

    private void createOval() {
        boolean isLocation = false;
        OvalEntity ovalEntity = new OvalEntity();
        ovalEntity.setDirectionEnum(directionEna[new Random().nextInt(4)]);
        switch (ovalEntity.getDirectionEnum()){
            case top:
                ovalEntity.setX(new Random().nextInt(getScreenWidth(getContext())));
                ovalEntity.setY(0);
                break;
            case bottom:
                ovalEntity.setX(new Random().nextInt(getScreenWidth(getContext())));
                ovalEntity.setY(getScreenHeight(getContext()));
                break;
            case left:
                ovalEntity.setX(0);
                ovalEntity.setY(new Random().nextInt(getScreenHeight(getContext())));
                break;
            case right:
                ovalEntity.setX(getScreenWidth(getContext()));
                ovalEntity.setY(new Random().nextInt(getScreenHeight(getContext())));
                break;
        }
        Paint paint = new Paint();
        paint.setColor(colors[new Random().nextInt(4)]);
        ovalEntity.setPaint(paint);
        ovalEntity.setRadius(new Random().nextInt(30) + 10);
        int size=gameDifficulty==GameDifficulty.superSimple?50:
                gameDifficulty==GameDifficulty.simple?40:
                gameDifficulty==GameDifficulty.easy?30:
                gameDifficulty==GameDifficulty.difficulty?20:10;
        for (OvalEntity ovalEntity1 : ovalEntities) {
            Rect rect = new Rect((int) ovalEntity.getX()-size, (int) ovalEntity.getY()-size
                    , (int) (ovalEntity.getX() + ovalEntity.getRadius() + size), (int) (ovalEntity.getY() + ovalEntity.getRadius() + size));

            Rect rect1 = new Rect((int) ovalEntity1.getX()-size, (int) ovalEntity1.getY()-size
                    , (int) (ovalEntity1.getX() + ovalEntity1.getRadius() +size), (int) (ovalEntity1.getY() + ovalEntity1.getRadius() +size));
            isLocation = rect.intersect(rect1);

            if (isLocation)
                break;
        }

        Iterator<OvalEntity> stuIter = ovalEntities.iterator();
        while (stuIter.hasNext()) {
            OvalEntity ovalEntity1 = stuIter.next();
            if (ovalEntity1.getX() > getScreenWidth(getContext())||ovalEntity1.getX()<0||ovalEntity1.getY()>getScreenHeight(getContext())||ovalEntity1.getY()<0)
                stuIter.remove();
        }
        if (!isLocation)
            ovalEntities.add(ovalEntity);
    }

    /***
     * 是否在绘制:用于关闭子线程:true则表示一直循环
     */
    private boolean isDrawing = true;
    private boolean isStarting = false;

    /***
     * 注意这个是在子线程中绘制的
     */
    @Override
    public void run() {
        while (isDrawing) {
            draw();
        }

    }

    /***
     * 注意这个是在子线程中绘制的，surface支持子线程更新ui，所以
     */
    private void draw() {
        Canvas canvas = null;
        //给画布加锁，防止线程安全，防止该内存区域被其他线程公用
        canvas = mSurfaceHolder.lockCanvas();
        if (null != canvas) {
            canvas.drawColor(Color.WHITE);

            float speed=gameDifficulty==GameDifficulty.superSimple?0.5f:
                    gameDifficulty==GameDifficulty.simple?0.8f:
                            gameDifficulty==GameDifficulty.easy?1.1f:
                                    gameDifficulty==GameDifficulty.difficulty?2f:3f;
            for (OvalEntity ovalEntity : ovalEntities) {
                switch (ovalEntity.getDirectionEnum()){
                    case top:
                        ovalEntity.setY(ovalEntity.getY()+(isStarting?speed:0));
                        break;
                    case bottom:
                        ovalEntity.setY(ovalEntity.getY()-(isStarting?speed:0));
                        break;
                    case left:
                        ovalEntity.setX(ovalEntity.getX()+(isStarting?speed:0));
                        break;
                    case right:
                        ovalEntity.setX(ovalEntity.getX()-(isStarting?speed:0));
                        break;
                }
                ovalEntity.getPaint().setAntiAlias(true);
                canvas.drawCircle(ovalEntity.getX(), ovalEntity.getY(), ovalEntity.getRadius(), ovalEntity.getPaint());
            }
            leadEntity.getPaint().setAntiAlias(true);
            canvas.drawCircle(leadEntity.getX(), leadEntity.getY(), leadEntity.getRadius(), leadEntity.getPaint());
            canvas.drawText(scoreEntity.getText(),scoreEntity.getX(),scoreEntity.getY(),scoreEntity.getPaint());
            createOval();
            collisionDetection();
            //提交显示视图并解锁，防止长期占用此内存
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void collisionDetection() {
        try {
            for (OvalEntity ovalEntity : ovalEntities) {
                Rect rect = new Rect((int) ovalEntity.getX()-2, (int) ovalEntity.getY()-2
                        , (int) (ovalEntity.getX() + ovalEntity.getRadius() + 5), (int) (ovalEntity.getY() + ovalEntity.getRadius() + 5));

                Rect rect1 = new Rect((int) leadEntity.getX()-2, (int) leadEntity.getY()-2
                        , (int) (leadEntity.getX() + leadEntity.getRadius() + 2), (int) (leadEntity.getY() + leadEntity.getRadius() + 2));
                if (rect.intersect(rect1)&&isDrawing) {
                    handler.sendEmptyMessage(GAME_OVER);
                    isDrawing = false;
                }
            }
        }catch (ConcurrentModificationException ex){

        }

    }

    /***
     * surfaceview的绘图表面（就是activity宿主创建一个透明的表面用于surfaceView绘制）被创建时执行
     * 在updateWindow（）创建宿主（activity的窗口）的绘图表面时会回调，虽然surfaceView是独立于一个线程但还是离不开宿主窗口，
     * 最后还是要粘贴到window中
     *
     * surfaceCreated方法，是当SurfaceView被显示时会调用的方法，所以你需要再这边开启绘制的线 程
     *
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new Thread(GameView.this).start();
    }

    /**
     * 创建、更新会认为发生变化也会回调这个方法
     *
     * @param surfaceHolder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    /***
     *surfaceDestroyed方法是当SurfaceView被隐藏会销毁时调用的方法，在这里你可以关闭绘制的线程
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDrawing = false;
        ovalEntities=null;
        leadEntity=null;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = ctx.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth();
        return w;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = ctx.getResources().getDisplayMetrics();
        int h = dm.heightPixels;
        return h;
    }

    private VelocityTracker vTracker = null;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://游戏开始
                isStarting = true;
                if(vTracker == null){
                    vTracker = VelocityTracker.obtain();
                }else{
                    vTracker.clear();
                }
                vTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE://移动
                vTracker.addMovement(event);
                vTracker.computeCurrentVelocity(15);
                leadEntity.setX(leadEntity.getX()+vTracker.getXVelocity());
                leadEntity.setY(leadEntity.getY()+vTracker.getYVelocity());

                break;
            case MotionEvent.ACTION_UP://游戏暂停
                isStarting = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                vTracker.recycle();
                break;
        }

        return true;
    }

    public interface GameProgressListener{
        void onGameOver(int score);
    }

    public static int getStatusHeight(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return 0;

        Activity activity = (Activity) context;
        int statusHeight = 0;
        Rect localRect = new Rect();
        if (activity != null)
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
