package org.hqf.architect.demo.pattern.controls.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huoquanfu
 * @date 2020/06/24
 */
public abstract class ContainerControl extends Control {

    private List<Control> childrenControls = new ArrayList<>();

    public ContainerControl(String name) {
        super(name);
    }

    public void add(Control control) {
        childrenControls.add(control);
    }

    public void remove(Control control) {
        childrenControls.remove(control);
    }

    @Override
    public void print() {
        super.print();
        printChildren();
    }
    private void printChildren(){
        for (Control control : childrenControls) {
            control.print();
        }
    }
}
