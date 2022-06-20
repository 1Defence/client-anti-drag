package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("clientantidrag")
public interface ClientAntiDragConfig extends Config
{
	@ConfigItem(
			keyName = "dragDelay",
			name = "Drag Delay",
			description = "If dragged for less than this period of time in milliseconds, client will stay still",
			position = 0
	)
	default int dragDelay()
	{
		return 500;
	}

}
