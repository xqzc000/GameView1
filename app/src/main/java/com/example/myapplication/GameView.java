package com.example.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameView extends FrameLayout {


    private static final String TAG = "GameView";
    private int widthSize; //viewgroup大小
    private int heightSize;
    private int marginWidth;
    private int viewWidth=200;  //实际图片宽高。
    private int viewHeight=100;
    private int itemsWidth;
    private int paddingWidth;
    private int[][] array=new int[][]{
            new int[]{0,0,1},
            new int[]{0,0,2},
            new int[]{0,0,3}
    };
    private float mLastX;
    private float mLastY;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private View currentView;
    private int currentCloumn;
    private int currentCloumn_UP;


    public GameView(Context context) {
        this(context,null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initChild() {

        textView1=new TextView(this.getContext());
         textView2=new TextView(this.getContext());
         textView3=new TextView(this.getContext());
        textView1.setBackground(new ColorDrawable(Color.RED));
        textView2.setBackground(new ColorDrawable(Color.YELLOW));
        textView3.setBackground(new ColorDrawable(Color.GREEN));

        viewWidth=widthSize/3;
        viewHeight=heightSize/3;

        textView1.setLayoutParams(new ViewGroup.LayoutParams(viewWidth,viewHeight));
        textView2.setLayoutParams(new ViewGroup.LayoutParams(viewWidth,viewHeight));
        textView3.setLayoutParams(new ViewGroup.LayoutParams(viewWidth,viewHeight));

        textView1.setText("1");
        textView2.setText("2");
        textView3.setText("3");
        textView1.setTextSize(30);
        textView2.setTextSize(30);
        textView3.setTextSize(30);

        addView(textView1);
        addView(textView2);
        addView(textView3);


        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                viewWidth, MeasureSpec.EXACTLY);
//         int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
//                mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin,
//                lp.height);;
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                viewHeight, MeasureSpec.EXACTLY);
        textView1.measure(childWidthMeasureSpec,childHeightMeasureSpec);
        textView2.measure(childWidthMeasureSpec,childHeightMeasureSpec);
        textView3.measure(childWidthMeasureSpec,childHeightMeasureSpec);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG,"widthSize="+widthSize+",heightSize="+heightSize);

        initChild();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        textView1.layout(0,2*viewHeight,viewWidth,3*viewHeight);
        textView2.layout(viewWidth,2*viewHeight,2*viewWidth,3*viewHeight);
        textView3.layout(2*viewWidth,2*viewHeight,3*viewWidth,3*viewHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX=event.getX();
                mLastY=event.getY();
                if(mLastX<viewWidth){
                    currentCloumn=0;
                }else if(mLastX>viewWidth&mLastX<2*viewWidth){
                    currentCloumn=1;
                }else if(mLastX>viewWidth*2){
                    currentCloumn=2;
                }

                break;
            case MotionEvent.ACTION_MOVE:
//                float x=event.getX();
//                float y=event.getY();
//
//
                break;
            case MotionEvent.ACTION_UP:
                float x_up=event.getX();
                float y_up=event.getX();

                if(x_up<viewWidth){
                    currentCloumn_UP=0;
                }else if(x_up>viewWidth&x_up<2*viewWidth){
                    currentCloumn_UP=1;
                }else if(x_up>viewWidth*2){
                    currentCloumn_UP=2;
                }

                arrangeView(x_up,y_up);
                isFinish();

                break;
        }
        return true;
    }



    private void arrangeView(float x, float y) {
        if(currentCloumn_UP==currentCloumn){
            return;
        }
        if(getNoneZoroSize(currentCloumn)==0){
            return;
        }
        int lastValue=array[currentCloumn][3-getNoneZoroSize(currentCloumn)];
        array[currentCloumn][3-getNoneZoroSize(currentCloumn)]=0;

        for (int i=0;i<array.length;i++){
            if(currentCloumn_UP==i){
               array[currentCloumn_UP][2-getNoneZoroSize(currentCloumn_UP)]=lastValue;
            }
        }


        for (int i=0;i<array.length;i++) {
            for (int j=0;j<array[i].length;j++){
                if(array[i][j]==1){
                    moveView(textView1,i*viewWidth,j*viewHeight);
                }else if(array[i][j]==2){
                    moveView(textView2,i*viewWidth,j*viewHeight);
                }else if(array[i][j]==3){
                    moveView(textView3,i*viewWidth,j*viewHeight);
                }
            }
        }

        for (int i=0;i<array.length;i++) {
            for (int j = 0; j < array[i].length; j++) {
                Log.i(TAG,"i:"+i+",j:"+j+",array="+array[i][j]);
            }

        }
    }

    void moveView(View view,int x,int y){
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x", x);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "y", y);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        animSetXY.start();
    }

    private int getNoneZoroSize(int currentCloumn) {
        int size=0;
        for (int i=0;i<array.length;i++) {
            if(currentCloumn!=i)
                continue;
            for (int j=0;j<3;j++){
                if(array[currentCloumn][j]!=0){
                    size++;
                }
            }
        }
        return size;
    }

    private View getCurrentView(float x, float y) {
        if((x<textView1.getRight()&x>textView1.getLeft())&&(y<textView1.getBottom()&y>textView1.getTop())){
            return textView1;
        }else if((x<textView2.getRight()&x>textView2.getLeft())&&(y<textView2.getBottom()&y>textView2.getTop())){
            return textView2;
        }else if((x<textView3.getRight()&x>textView3.getLeft())&&(y<textView3.getBottom()&y>textView3.getTop())){
            return textView3;
        }

        return null;
    }

    class Coordinate{
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }



    public void isFinish(){
        if(array[1][0]==1&array[1][1]==2&array[1][2]==3){
            Toast.makeText(this.getContext(),"FINISH",Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(this.getContext())
                    .setMessage("恭喜你，过关了")
                    .show();
        }
    }
}

