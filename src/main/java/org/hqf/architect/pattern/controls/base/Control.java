package org.hqf.architect.pattern.controls.base;

/**
 * @author huoquanfu
 * @date 2020/06/23
 */
public abstract class Control {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Control(String name) {
        this.name = name;
    }

    /**
     * print
     */
    public  void print() {
        System.out.println("print " + this.getClass().getSimpleName()+" ("+name+")");
    }
}
