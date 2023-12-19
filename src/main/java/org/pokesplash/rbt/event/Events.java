package org.pokesplash.rbt.event;

import org.pokesplash.rbt.event.events.ExampleEvent;
import org.pokesplash.rbt.event.obj.Event;

/**
 * Class that holds all of the events.
 */
public abstract class Events {
	public static Event<ExampleEvent> EXAMPLE = new Event<>();

}