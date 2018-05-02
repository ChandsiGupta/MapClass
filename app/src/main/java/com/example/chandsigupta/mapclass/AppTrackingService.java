package com.example.chandsigupta.mapclass;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;

/**
 * Created by chandsi.gupta on 13-04-2018.
 */
public class AppTrackingService extends Service {

    private RunnableThread thread;
    private Context ctx;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        ctx = AppTrackingService.this;
        thread = new RunnableThread();
    }

    public void onStart(Intent intent, int startid) {
        try{
            if(thread==null) thread = new RunnableThread();
            thread.startThread();
        }catch(Exception e){  }
    }

    class RunnableThread extends Thread {

        Handler back_handler = new Handler();
        boolean isContinue = false;

        public RunnableThread(){
            isContinue = false;
        }

        public void setIsContinue(boolean val){
            this.isContinue = val;
        }

        public void startThread(){
            isContinue = true;
            start();
        }

        public void run(){
            ActivityManager actMngr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            while(isContinue){
                try{
                    //Maintain a boolean "isyourapprunning" to know if your app was running or not....
                    //if(isyourapprunning){
                        String runningPkg = actMngr.getRunningTasks(1).get(0).topActivity.getPackageName();
                        if (!runningPkg.equals(ctx.getPackageName())){
                            launchApp(ctx.getPackageName());
                        }
                        Thread.sleep(2500);  //2.5 secs
                   /*else{
                        isContinue = false;
                        stopSelf();
                    }*/

                }catch(Exception e){ }
            }//end of while loop
        }

        protected void launchApp(String packageName) {
            Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            if (null != mIntent) {
                try {
                    startActivity(mIntent);
                } catch(Exception e) { }
            }
        }
    }

}