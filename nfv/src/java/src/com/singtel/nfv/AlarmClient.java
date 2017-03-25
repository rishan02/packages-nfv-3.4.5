package com.singtel.nfv;

import org.apache.log4j.Logger;

import com.tailf.conf.ConfBuf;
import com.tailf.conf.ConfDatetime;
import com.tailf.conf.ConfIdentityRef;
import com.tailf.ncs.alarmman.common.Alarm;
import com.tailf.ncs.alarmman.common.ManagedDevice;
import com.tailf.ncs.alarmman.common.ManagedObject;
import com.tailf.ncs.alarmman.common.PerceivedSeverity;
import com.tailf.ncs.alarmman.producer.AlarmSink;
import com.tailf.ncs.ns.NcsAlarms;

public class AlarmClient {
	private static Logger LOGGER = Logger.getLogger(AlarmClient.class);
	
	protected static boolean raiseAlarm(String device,String alarmText)
	{
		boolean status=false;
		try
		{			
			LOGGER.info("Sending ALARM: "+device);
			String managedObject = "/devices/device{"+device+"}";
			ConfIdentityRef alarmType=new ConfIdentityRef(new NcsAlarms().hash(),
			NcsAlarms._final_commit_error);							
			PerceivedSeverity severity = PerceivedSeverity.MAJOR;
			ConfDatetime timeStamp = ConfDatetime.getConfDatetime();			
			@SuppressWarnings("deprecation")
			Alarm al = new Alarm(new ManagedDevice(device),
					new ManagedObject(managedObject),
					alarmType,
					new ConfBuf(String.valueOf(System.currentTimeMillis())),
					severity,
					false,
					alarmText,
					null,
					null,
					null,
					timeStamp);
					AlarmSink sink1 = new AlarmSink();
					sink1.submitAlarm(al);			
			LOGGER.info("Submitted  ALARM!!");			
			status=true;
		}
		catch(Exception e)
		{
			LOGGER.info("exception raised while sending alarm");		
		}
		return status;
	}
}
