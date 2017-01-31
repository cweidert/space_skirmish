package com.heliomug.gui.utils;

import java.io.Serializable;

// have to use this as opposed to a consumer because consumers apparently aren't serializable
public interface MessageDisplayer extends Serializable {
	void accept(String string);
}
