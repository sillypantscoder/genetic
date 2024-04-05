package com.sillypantscoder.geometrydash;

public class SceneItem {
    public View view;
    public double x;
    public double y;
    public double rotation = 0;
    /**
     * @param view
     * @param x
     * @param y
     */
    public SceneItem(View view, double x, double y) {
        this.view = view;
        this.x = x;
        this.y = y;
	}
    public void tick(double amount) {}
    public void destroy() {}
}
