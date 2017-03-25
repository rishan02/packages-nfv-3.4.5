package com.singtel.nfv;

import java.util.concurrent.*;
import org.apache.log4j.Logger;

import com.tailf.navu.NavuException;
import com.tailf.navu.NavuNode;

public class ServiceMonitor {
	private static Logger LOGGER = Logger.getLogger(ServiceMonitor.class);
	ScheduledExecutorService scheduler = null;
	ScheduledFuture<?> handle = null;

	protected void checkServiceState(final String alarmDelay, final NavuNode service, final String csrName,
			final String deviceFullName, final String tenant, final String escName) throws NavuException {
		final String serviceName = service.leaf("service-name").valueAsString();
		try {
			LOGGER.info("Alarm scheduler registered for serviceName: " + serviceName);
			final int delayVal = Integer.parseInt(alarmDelay);
			scheduler = Executors.newSingleThreadScheduledExecutor();
			handle = scheduler.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					try {
						if (Utility.isServiceExists(serviceName)) {
							String serviceReadyState = Utility.getDeploymentPlanStatus(service, csrName, tenant,
									escName);
							if (serviceReadyState.equals(Commons.SERVICE_CREATION_NOT_REACHED)) {
								String alarmText = "The service status is not-reached for " + delayVal
										+ " minute(s), for the service: " + serviceName + ", device: " + deviceFullName;
								AlarmClient.raiseAlarm(deviceFullName, alarmText);
							} else {
								killSchedular(serviceName);
							}
						} else {
							killSchedular(serviceName);
						}
					} catch (Exception e) {
						LOGGER.info("exception raised when submitting alarm for service: " + serviceName);
						killSchedular(serviceName);
					}
				}
			}, delayVal, delayVal, TimeUnit.MINUTES);
		} catch (Exception e) {
			LOGGER.info("exception raised while scheduling alarm for service: " + serviceName);
			killSchedular(serviceName);
		}
	}

	private void killSchedular(String serviceName) {
		if (handle != null) {
			handle.cancel(true);
		}
		if (scheduler != null) {
			scheduler.shutdown();
			LOGGER.info("alarm scheduler stopped for service: " + serviceName);
		}
	}
}