package org.yangosoft;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;

import android.widget.Toast;

public class SimpleService extends Service  implements  LocationListener {
	
	
	private final int   NOTHING=0;
	private final int ACTIVE_MONITORING=1;
	private final int PASSIVE_MONITORING=4;
	private final int BLOCK_DEVICE=2;
	private final int PLAY_SOUND=3;
	
	public boolean found=false;
	 int perform_action;
	 LocationManager lm;
	 boolean taskDone;
	 String coord;
	///TChecker checker;
	 final Handler mHandler = new Handler();
	 long lastUpdate;
	 String IMEI="";
	 
	 private boolean HasInternetConnection() 
	 {

		 ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

		 // ARE WE CONNECTED TO THE NET

		 if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) 
		 {
			 return true;
		 } else 
		 {

			 Log.v("T1", "Internet Connection Not Present");

			 return false;

		 }

	 }
	 
	 
	 
	 
	@Override
	public void onCreate() {
		
		super.onCreate();
		Toast.makeText(this,"Service started", Toast.LENGTH_LONG).show();
		/*checker = new TChecker(this);
		Thread t = new Thread(checker);
		t.start();*/
		lastUpdate=java.lang.System.currentTimeMillis()-10000;
		TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		IMEI=tm.getDeviceId();
		StartCheckingRemoteStatus();
		 
		
		
		
	}
	
	 final Runnable mUpdateResults = new Runnable() {
	        public void run() {
	            PerformActions();
	        }
	    };
	    
	    //Thread2
	    protected void StartCheckingRemoteStatus() {

	        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
	        Thread t = new Thread() {
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	            public void run() {
	            	while(true)
	            	{
	            		
	            		///Check for a nice message
	            		 URL url;
						try {
							url = new URL("http://somewhere.com/some/webhosted/file");
						    //create the new connection
	            	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

	            	        //set up some things on the connection
	            	        urlConnection.setRequestMethod("GET");
	            	        urlConnection.setDoOutput(true);

	            	        //and connect!
	            	        urlConnection.connect();
	            		
	            	        //this will be used in reading the data from the internet
	            	        InputStream inputStream = urlConnection.getInputStream();

	            	        //this is the total size of the file
	            	        int totalSize = urlConnection.getContentLength();
	            	        //variable to store total downloaded bytes
	            	        int downloadedSize = 0;

	            	        //create a buffer...
	            	        byte[] buffer = new byte[1024];
	            	        String strAction=new String("");
	            	        
	            	        int bufferLength = 0; //used to store a temporary size of the buffer

	            	        //now, read through the input buffer and write the contents to the file
	            	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	            	                //add the data in the buffer to the file in the file output stream (the file on the sd card
	            	               /// fileOutput.write(buffer, 0, bufferLength);
	            	                //add up the size so we know how much is downloaded
	            	        		strAction=strAction + new String(buffer);
	            	        	
	            	                downloadedSize += bufferLength;
	            	              

	            	        }
	            	        
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

	            	    
	            		
	            		
	            		
	            		
	            		
	            		
	            		perform_action=0;
	            		found = false;
	                	mHandler.post(mUpdateResults);
	                	
	                	try {
							Thread.sleep(30*60*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	}
	            }
	        };
	        t.start();
	    }

	    //Main Thread
	    private void PerformActions() 
	    {
	    	Toast.makeText(this,"Found? "+found, Toast.LENGTH_LONG).show();
	    	
	    	
	    	
	    	switch(perform_action)
	    	{
		    	default:
		    	case NOTHING:
		    		break;
		    	case ACTIVE_MONITORING:
		    		break;
		    	case PASSIVE_MONITORING:
		    		break;
		    	case PLAY_SOUND:
		    		break;
	    		
	    		
	    		
	    	}
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	DevicePolicyManager mDPM =
	    	    (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
	    	mDPM.lockNow();
	    	
	    	/*KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
        	KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        	lock.reenableKeyguard();*/
	    	
	    	
	    	
	    	
	    	
	    	if(found==true)
	    	{
	    	
	    	 final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
			 if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) 
			 {
				 final Intent poke = new Intent();
				    poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); //$NON-NLS-1$//$NON-NLS-2$
				    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
				    poke.setData(Uri.parse("3")); //$NON-NLS-1$
				    getBaseContext().sendBroadcast(poke);
				    
				    
				 
				    
				    
				    
				    
				    
				 
				 
			 }
			 
			 //Wait for fine location
			 
			   //Wait a few seconds to obtain location
			    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    
		        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	    	
	    	
	    	}
	    	
	    	
	    	
	    	
	    }
	    
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service destroyed...", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	/*
	
	 class TChecker implements Runnable, LocationListener
     {

		 SimpleService owner;
		 LocationManager lm;
		 boolean taskDone;
		 String coord;
		
		 
		 
		 public TChecker(SimpleService owner)
		 {
			 this.owner=owner;
			 
		 }
		 
		 private boolean HasInternetConnection() 
		 {

			 ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

			 // ARE WE CONNECTED TO THE NET

			 if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) 
			 {
				 return true;
			 } else 
			 {

				 Log.v("T1", "Internet Connection Not Present");

				 return false;

			 }

		 }
	
		 public void doTasks()
			{
				taskDone=false;
				///Toast.makeText(this,"DoTask", Toast.LENGTH_LONG).show();
				
				 final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
				 if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) 
				 {
					 final Intent poke = new Intent();
					    poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); //$NON-NLS-1$//$NON-NLS-2$
					    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
					    poke.setData(Uri.parse("3")); //$NON-NLS-1$
					    getBaseContext().sendBroadcast(poke);
					    
					    
					 
					    
					    
					    
					    
					    
					 
					 
				 }
				 
				 //Wait for fine location
				 
				   //Wait a few seconds to obtain location
				    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    
			        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
				 
				 
				 //Store location
				 
				 //If connection
				   //push location
				 //else
				   //wait
				 if(HasInternetConnection())
				 {
					/// Toast.makeText(this, "Has Internet", Toast.LENGTH_LONG).show();
				 }else
				 {
					/// Toast.makeText(this, "Has Not Internet", Toast.LENGTH_LONG).show();
				 }
				
				
			}
		 
		 
		 
		 
		 
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
			
				doTasks();
				
				
				
				if(taskDone)
				{
					try 
					{
						Thread.sleep(1000*60*30);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onLocationChanged(Location l) {
				// TODO Auto-generated method stub
				float s= (l.getSpeed());
		    	 s=(s*3600)/1000;
		    	 coord=String.format("%.2f Km/h\n", s);
		         
		       
		         coord+=String.format("Bearing: %.2f º\n", l.getBearing());
		         
		        
		         coord+=String.format("Altitude: %.2f m\n", l.getAltitude());
		         
		              
		         coord+=String.format("Coord: %.4fº, %.4fº",l.getLatitude(),l.getLongitude());
						
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
	    	
	    	
	    	
	    }*/






	@Override
	public void onLocationChanged(Location l)
	{
		
		
		if(java.lang.System.currentTimeMillis() - this.lastUpdate > 5000 )
        {
		
		// TODO Auto-generated method stub
			
			
				
		
		float s= (l.getSpeed());
   	 s=(s*3600)/1000;
   	 coord=String.format("%.2f Km/h\n", s);
        
      
        coord+=String.format("Bearing: %.2f º\n", l.getBearing());
        
       
        coord+=String.format("Altitude: %.2f m\n", l.getAltitude());
        
             
        coord+=String.format("Coord: %.8fº, %.8fº\n",l.getLatitude(),l.getLongitude());
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd HH:mmZ", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        
        coord+= "TimeStamp: "+sdf.format( new Date());
        
        
        
        if(HasInternetConnection()==true)
		{
			try {
			    // Construct data
			    String data = URLEncoder.encode("IMEI", "UTF-8") + "=" + URLEncoder.encode(IMEI, "UTF-8");
			    data += "&" + URLEncoder.encode("txtData", "UTF-8") + "=" + URLEncoder.encode(coord, "UTF-8");

			    // Send data
			    URL url = new URL("http://hostname:80/cgi");
			    URLConnection conn = url.openConnection();
			    conn.setDoOutput(true);
			    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			    wr.write(data);
			    wr.flush();

			    // Get the response
			    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    while ((rd.readLine()) != null) {
			        // Process line...
			    }
			    wr.close();
			    rd.close();
			} 
			catch (Exception e) 
			{
			}
			
			
		}
			
        
        
        
        
        lastUpdate=java.lang.System.currentTimeMillis();
        
        }
		
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}

