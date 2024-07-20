package com.sillypantscoder.geometrydash;

public class SceneItem {
    public View view;
    public double x;
    public double y;
    public SceneItem(double x, double y) {
        this.x = x;
        this.y = y;
	}
    public void tick(double amount) {}
    public void destroy() {}
}
