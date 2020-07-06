package org.hqf.architect.demo.pattern;

import org.hqf.architect.demo.pattern.controls.*;
import org.hqf.architect.demo.pattern.controls.base.ContainerControl;
import org.hqf.architect.pattern.controls.*;

/**
 * @author huoquanfu
 * @date 2020/06/24
 */
public class Client {
    private ContainerControl mainControl;


    public Client() {
        mainControl = new WinForm("窗口");
        mainControl.add(new Picture("LOGO图片"));
        mainControl.add(new Frame("FRAME1"));
        mainControl.add(new Button("登陆"));
        mainControl.add(new Button("注册"));
        mainControl.add(new Label("用户名"));
        mainControl.add(new TextBox("文本框"));
        mainControl.add(new Label("密码"));
        mainControl.add(new PasswordBox("密码框"));
        mainControl.add(new CheckBox("复选框"));
        mainControl.add(new Label("请记住用户名"));
        mainControl.add(new LinkLabel("忘记密码"));
    }

    public void print() {
        mainControl.print();
    }
}
