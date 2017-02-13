package com.example.sushilverma.mavync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Myservice extends Service {
	
	    private Context mycontext=this;
	    public static boolean isGPSEnabled = false;
	    private String source=null;
	    private String destination=null;
	    private String rate=null;
	    private String kilometer=null;
	    private String Eta=null;
	    private String TT=null;
	    private String shipmentno=null;
      
	    mycallable callab;
	    Thread callabtask;
	    int x=1;
	  //  private final IBinder mBinder = new LocalBinder();
	    private static  String data;
	    
	    // shared prefference to read customer id 
	    private SharedPreferences sharedpreferences;
	    private SharedPreferences.Editor editor;
	    public static final String MyPREFERENCES = "MyPrefs" ;
	    private String userid=null;
	    
	    private boolean alarmstatus=false;
	    private   Timer timer;
	    
	  
	   
	
	 /*   public class LocalBinder extends Binder 
	    {
	    	Myservice getService() 
	    	{
	          return Myservice.this;
	        }
	     }*/
	 

	    @Override
		public IBinder onBind(Intent intent) 
	    {
			
			return null;
		}
	    
	    @Override
	    public void onCreate ()
	    {
	    	
	    	IntentFilter filter = new IntentFilter();
	    	filter.addAction("ON");
	    	filter.addAction("OFF");
	    	filter.addAction("STOPLOGOUT");
	    	registerReceiver(receiver, filter);
	    }
	    
	   
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) 
	 {
    
	  /*  System.out.println("service started.......................");
	    data=intent.getStringExtra("customer_id");*/
	  
	  /* final Handler handler;*/

	  // shared prefference currentlly commented because the customer_id is received from the intent extra
	   
	       
	       
	    timer=new Timer();
	    final Handler handler=new Handler();
	    TimerTask doAsynchronousTask = new TimerTask() {
	        @Override
	        public void run() {

	            //Perform background work here
               
	        	
	        	 handler.post(new Runnable() 
	            {
	                public void run() {
	                	HitServer();
	               
	                }
	            });
	        }
	    };
		
	    timer.scheduleAtFixedRate(doAsynchronousTask, 0,60*1000);
      
	 
        return Service.START_NOT_STICKY;
     }
	
	
	
	
	
	
	
	
	 private void HitServer()
	 {
			
			 Log.i("start uploading data", "uploading");
	    	
			    sharedpreferences = mycontext.getSharedPreferences(MyPREFERENCES, mycontext.MODE_PRIVATE);
			    userid= sharedpreferences.getString("customer_id", "null");
				
					if( isOnline())
					{
						
						
						// Log.i("this is online sending data", " ");
						
					        callab=new mycallable(userid);
					        callabtask=new Thread(callab);
							callabtask.start();
							try {
								callabtask.join();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								System.out.println("Thread Interrupted");
							}
							Log.i("data sent...", " ");
							int alarm_signal=callab.getalarm();        // Receiving alarm signal
                            Toast.makeText(this, "alarm"+alarm_signal, Toast.LENGTH_LONG);
							
					    	if(alarm_signal==1 && alarmstatus==false)
							{
						      startMusic();// this is music or alarm when an order is placed.
							 
						     
						        source=callab.getSource();
								destination=callab.getDestination();
							    rate=callab.getRate();
							    kilometer=callab.getKilometer();
							    Eta=callab.getEta();
							    TT=callab.getTT();
							    shipmentno=callab.getShipmentno();
						      
						  		      
						      Intent   intent = new Intent(this, Rating_popup.class);
						      
						      intent.putExtra("source",source);
						      intent.putExtra("destination",destination);
						      intent.putExtra("rate",rate);
						      intent.putExtra("km",kilometer);
						      intent.putExtra("Eta",Eta);
						      intent.putExtra("Travel_time",TT);
						      intent.putExtra("shipment_no",shipmentno);
							  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						      startActivity(intent);
						 
							}
			         
					}	
				
	  }
	 
	 
	 
	 private final BroadcastReceiver receiver = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {
		      String action = intent.getAction();
		      
		      Log.i("boradcast received","braodcast received");
		      
		      /// this broadcast is receiving from MyAlert activity
		      if(action.equals("ON")){
		    	  alarmstatus=true;
		      }
		      
		      else 
		    	  
		    	  alarmstatus=false;
		      
		      // this broadcast is comming from LogoutAlert activity
		      
		      if(action.equals("STOPLOGOUT"))
		      {
		    	  stopSelf();
		    	  
		    	  System.out.println("Myserive.class");
		      }
		    
		   }
		};
				
		
	
  public boolean isOnline() {
	    ConnectivityManager cm =
	                      (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
	

	
   class mycallable implements Runnable
	{
    	private volatile String customer_id=null;
        private volatile int  alarm=0;
        private volatile String  source=null;
        private volatile String  destination=null;
        private volatile String  rate=null;
        private volatile String  kilometer=null;
        private volatile String  Eta=null;
        private volatile String  TT=null;
        private volatile String  shipmentno=null;
       
        
        public String getCustomer_id() 
        {
			return customer_id;
		}

		
		public String getSource() {
			return source;
		}

		public String getDestination() {
			return destination;
		}

		public String getRate() {
			return rate;
		}

		public String getKilometer() {
			return kilometer;
		}

		public String getEta() {
			return Eta;
		}
		
		public String getTT() {
			return TT;
		}

		public String getShipmentno() {
			return shipmentno;
		}
		
		 public int getalarm()
		  
		   {
			   return alarm;
		   }


		
	
	   mycallable(String customer_id)
		{
		   this.customer_id=customer_id;	
		}
	
	  
	  

		public void run()
		{
			
			try {
				
				   Log.i("ready to hit the server", " ....");
				 
				   
				        final String request="http://121.241.125.91/cc/mavyn/online/ownerwiseupdate.php?msg=alarmstatus&userid="+customer_id; 
				        JSONObject jsonResponse;    
				        Log.d("request", ""+request);
			        	
			        	URL url = new URL(request);
			            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			          
			            System.out.println("data uploades");
			            
			            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
			            String local=null;
						try 
						{
						     local =br.readLine();
							 Log.i("Json data received..",local);
						 } 
						
						 catch (Exception e) 
						 { 
							
							// Toast.makeText(getApplicationContext(),"Error in time reading",Toast.LENGTH_LONG).show();
						 }
							
						 System.out.println("the recived url"+local);
						 
						 try {
							   jsonResponse = new JSONObject(local);
							
							   JSONArray jsonMainNode=jsonResponse.optJSONArray("order");
							
							
							  int lengthJsonArr = jsonMainNode.length();
							  for(int i=0;i<lengthJsonArr;i++)
							  {
								
						    	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							
							     alarm=Integer.parseInt(jsonChildNode.optString("alarmstatus").toString());
							     
							     if(alarm==1)
							     {
							     
							         try {
								   	      source=jsonChildNode.optString("source").toString();
									      destination=jsonChildNode.optString("destination").toString();
									      rate=jsonChildNode.optString("rate").toString();
									      kilometer=jsonChildNode.optString("km").toString();
									      Eta=jsonChildNode.optString("eta").toString();
									      TT=jsonChildNode.optString("transittime").toString();
									      shipmentno=jsonChildNode.optString("shipmentno").toString();
								         } 
							     
							           catch (Exception e) 
							           {
									   // TODO Auto-generated catch block
									       e.printStackTrace();
									  // Log.i("Json data received is null ..",shipmentno);
								       }
							     
							     }
							
							  }
							
							
						   }
						  
						  catch(JSONException js)
						   {
							 js.printStackTrace();
						   }
			              
			           br.close();
			           con.disconnect();
			           
				
		
			} 
			
			catch (NumberFormatException e) 
			{
				e.printStackTrace();
			} 
			
			catch (IOException e) {
				e.printStackTrace();
			}
		    
		}
	}
   
  
     public void startMusic() 
     {
		  Intent i = new Intent("super");
	      sendBroadcast(i);
     }
   
     
    
     
  
  
  
   @Override
   public void onDestroy() 
   {
  
	   super.onDestroy();
	   
	   timer.cancel();
	   
	   //Toast.makeText(this,"Service Stoping",Toast.LENGTH_SHORT).show();
	   
	   unregisterReceiver(receiver);
   }
   
   
}
