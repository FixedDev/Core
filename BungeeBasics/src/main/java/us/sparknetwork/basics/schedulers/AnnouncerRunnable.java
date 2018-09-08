package us.sparknetwork.basics.schedulers;

import java.util.Collections;

import us.sparknetwork.basics.BasicsPlugin;
import us.sparknetwork.basics.ConfigurationService;

public class AnnouncerRunnable implements Runnable {

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (!ConfigurationService.ANNOUNCER_ENABLED) {
			return;
		}
		if (ConfigurationService.GLOBAL_ANNOUNCEMENTS.isEmpty()) {
			return;
		}
		String next = ConfigurationService.GLOBAL_ANNOUNCEMENTS.get(0);

		BasicsPlugin.getPlugin().getProxy().broadcast(next);

		Collections.rotate(ConfigurationService.GLOBAL_ANNOUNCEMENTS, -1);
	}

}
