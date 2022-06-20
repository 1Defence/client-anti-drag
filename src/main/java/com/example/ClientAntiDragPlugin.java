package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pushingpixels.substance.internal.utils.SubstanceTitlePane;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@PluginDescriptor(
	name = "Client Anti Drag",
		description = "Prevent dragging the client unless moved more more than X milliseconds(custom set in config)"
)
public class ClientAntiDragPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientAntiDragConfig config;

	Instant lastFrameMovement = null;

	AWTEventListener titleListener = null;

	@Override
	protected void startUp() throws Exception
	{

		titleListener = event -> {

			if(!(event.getSource() instanceof SubstanceTitlePane)){
				return;
			}

			switch (event.getID()){
				case MouseEvent.MOUSE_PRESSED:
					lastFrameMovement = Instant.now();
					break;
				case MouseEvent.MOUSE_DRAGGED:
					if(lastFrameMovement != null && TimeUnit.NANOSECONDS.toMillis(Duration.between(lastFrameMovement,Instant.now()).toNanos()) <= config.dragDelay()) {
						((MouseEvent)event).consume();
					}
					break;
			}

		};

		Toolkit.getDefaultToolkit().addAWTEventListener(titleListener,AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);

	}

	@Override
	protected void shutDown() throws Exception
	{
		lastFrameMovement = null;
		if(titleListener != null) {
			Toolkit.getDefaultToolkit().removeAWTEventListener(titleListener);
			titleListener = null;
		}
	}

	@Provides
	ClientAntiDragConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ClientAntiDragConfig.class);
	}
}
