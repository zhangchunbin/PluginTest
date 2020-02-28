package ui;

import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ================================
 *
 * @author: zcb
 * @email: zcbin2@grgbanking.com
 * @time: 2020/2/25 1:37 下午
 * @version: 1.0
 * @desc: =================================
 */
public class MvvmForm {
    private JTextField tfAtyName;
    private JLabel activityName;
    private JTextField tfViewModelName;
    private JLabel viewModelName;
    private JButton OKButton;
    private JButton cancelButton;

    private String atyName;
    private String vmName;

    public MvvmForm() {
        OKButton.addActionListener(e->{
            String content = "activityName:"+atyName+"viewModel:"+vmName;
            Messages.showMessageDialog(content,"title",Messages.getInformationIcon());
        });
        cancelButton.addActionListener(e->{

        });
    }

    public String getAtyName() {
        return atyName;
    }

    public void setAtyName(final String atyName) {
        this.atyName = atyName;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(final String vmName) {
        this.vmName = vmName;
    }

    public void setData(MvvmForm data) {
        tfAtyName.setText(data.getAtyName());
        tfViewModelName.setText(data.getVmName());
    }

    public void getData(MvvmForm data) {
        data.setAtyName(tfAtyName.getText());
        data.setVmName(tfViewModelName.getText());
    }

    public boolean isModified(MvvmForm data) {
        if (tfAtyName.getText() != null ? !tfAtyName.getText().equals(data.getAtyName()) : data.getAtyName() != null)
            return true;
        if (tfViewModelName.getText() != null ? !tfViewModelName.getText().equals(data.getVmName()) : data.getVmName() != null)
            return true;
        return false;
    }
}
