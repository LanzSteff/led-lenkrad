package application;

import java.awt.Color;

public interface LedInterface {

	public abstract void setOffset(int offset);

	public abstract void setColor(Color color);

	public abstract void setBrightness(int brightness);

	public abstract void setBwPattern(byte[] a);

	public abstract void setColorPattern(byte[] a);

}