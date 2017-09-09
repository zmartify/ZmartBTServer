/**
 * 
 */
package com.zmartify.hub.zmartbtserver.utils;

import org.freedesktop.dbus.Position;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.UInt32;

/**
 * @author Peter Kristensen
 *
 */

public final class StructUU extends Struct {
	@Position(0)
	public final UInt32 a;
	@Position(1)
	public final UInt32 b;

	public StructUU(UInt32 a, UInt32 b) {
		this.a = a;
		this.b = b;
	}
}
