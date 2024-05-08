import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.event.*;
import java.sql.*;

public class FormKetNoi extends JLayeredPane
{
    private JTextField serverNameTf;
    public JTextField GetServerNameTf()
    {
        return serverNameTf;
    }
    // private JTextField databaseNameTf; // khong can vi dung database sinhvienform
    private JTextField userNameTf;
    public JTextField GetUserNameTf()
    {
        return userNameTf;
    }
    private JTextField passwordTf;
    public JTextField GetPasswordTf()
    {
        return passwordTf;
    }

    private JButton ketNoiBtn; // 1
    private JButton hienThiSinhVienBtn; // 2
    /**
     * 3. Center x, center y of main frame
     */
    private JButton sinhVienFilterBtn; // 4
    private JButton capNhatSinhVienBtn; // 5
    private JButton dongKetNoiBtn;
    private JLabel ketNoiLabel;

    private int wBtn = 320;
    private int hBtn = 50;

    public FormKetNoi(int w, int h)
    {
        ketNoiBtn = new JButton("Kết nối CSDL");
        hienThiSinhVienBtn = new JButton("Hiển thị CSDL");
        sinhVienFilterBtn = new JButton("Lọc danh sách sinh viên");
        capNhatSinhVienBtn = new JButton("Nhập dữ liệu cho sinh viên");
        dongKetNoiBtn = new JButton("Đóng CSDL");
        ketNoiLabel = new JLabel("Không có kết nối");

        ketNoiBtn.setFont(PropertiesDefault.fontLarge);
        hienThiSinhVienBtn.setFont(PropertiesDefault.fontLarge);
        sinhVienFilterBtn.setFont(PropertiesDefault.fontLarge);
        capNhatSinhVienBtn.setFont(PropertiesDefault.fontLarge);
        dongKetNoiBtn.setFont(PropertiesDefault.fontLarge);
        ketNoiLabel.setFont(PropertiesDefault.fontLarge);

        ketNoiBtn.setSize(wBtn, hBtn);
        hienThiSinhVienBtn.setSize(wBtn, hBtn);
        sinhVienFilterBtn.setSize(wBtn, hBtn);
        capNhatSinhVienBtn.setSize(wBtn, hBtn);
        dongKetNoiBtn.setSize(wBtn, hBtn);

        sinhVienFilterBtn.setLocation(GetCenterXBtn(w, wBtn), GetCenterYBtn(h, hBtn));
        hienThiSinhVienBtn.setLocation(GetCenterXBtn(w, wBtn), GetCenterYBtn(h, hBtn) - (hBtn + PropertiesDefault.DISTANCE));
        ketNoiBtn.setLocation(GetCenterXBtn(w, wBtn), GetCenterYBtn(h, hBtn) - (hBtn + PropertiesDefault.DISTANCE) * 2);
        capNhatSinhVienBtn.setLocation(GetCenterXBtn(w, wBtn), GetCenterYBtn(h, hBtn) + (hBtn + PropertiesDefault.DISTANCE));
        dongKetNoiBtn.setLocation(GetCenterXBtn(w, wBtn), GetCenterYBtn(h, hBtn) + (hBtn + PropertiesDefault.DISTANCE) * 2);
        
        ketNoiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ketNoiLabel.setBounds(0, h - hBtn - PropertiesDefault.DISTANCE, w, PropertiesDefault.HEIGHT_BTN);

        serverNameTf = createFieldWithHint("Server name", 
            10, GetCenterYBtn(h, hBtn) - (hBtn + PropertiesDefault.DISTANCE) * 2,
            PropertiesDefault.WIDTH_TEXTFIELD, hBtn);

        userNameTf = createFieldWithHint("User name (e.g: sa)", 
            10, 
            GetCenterYBtn(h, hBtn) - (hBtn + PropertiesDefault.DISTANCE),
            PropertiesDefault.WIDTH_TEXTFIELD, hBtn);

        passwordTf = createFieldWithHint("Password", 
            10, 
            GetCenterYBtn(h, hBtn),
            PropertiesDefault.WIDTH_TEXTFIELD, hBtn);

        ketNoiBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                if (!serverNameTf.getText().equals("Server name"))
                    MyDatabase.CONNECTION_STRING = "jdbc:sqlserver://" + serverNameTf.getText() + ":1433;databaseName=sinhvienform;encrypt=false;loginTimeout=5;";
                if (!userNameTf.getText().equals("User name (e.g: sa)"))
                    MyDatabase.USER_NAME = userNameTf.getText();
                if (!serverNameTf.getText().equals("Server name")
                 && !userNameTf.getText().equals("User name (e.g: sa)"))
                {
                    if (!passwordTf.getText().equals("Password"))
                        MyDatabase.PASSWORD = passwordTf.getText();
                }
                
                DBConnection.ConnectionStarted();
                if (DBConnection.connectInstance() != null)
                {
                    ketNoiLabel.setText("Đã kết nối");
                }
                else
                    ketNoiLabel.setText("Sai server hoặc tên user hoặc password");
                // System.out.println(MyDatabase.CONNECTION_STRING);
                // System.out.println(MyDatabase.USER_NAME);
                // System.out.println(MyDatabase.PASSWORD);
            }
        });

        hienThiSinhVienBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                SingletonManager.GetInstance().GetFormKetNoi().setVisible(false);
                SingletonManager.GetInstance().GetHienThiSinhVien().setVisible(true);
            }
        });

        sinhVienFilterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                SingletonManager.GetInstance().GetFormKetNoi().setVisible(false);
                SingletonManager.GetInstance().GetSinhVienFilter().setVisible(true);

                SingletonManager.SelectSinhVien(SingletonManager.GetInstance().GetSinhVienFilter().GetModel());
            }
        });
        
        capNhatSinhVienBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                SingletonManager.GetInstance().GetFormKetNoi().setVisible(false);
                SingletonManager.GetInstance().GetCapNhatSinhVien().setVisible(true);
            }
        });
                
        dongKetNoiBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try 
                {
                    if (DBConnection.connectInstance() != null && !DBConnection.connectInstance().isClosed()) 
                    {
                        DBConnection.connectInstance().close();
                        ketNoiLabel.setText("Không có kết nối");
                    }
                } 
                catch (SQLException ex) 
                {
                    ex.printStackTrace();
                }
            }
        });

        setSize(w, h);
        
        add(serverNameTf, JLayeredPane.DEFAULT_LAYER);
        add(userNameTf, JLayeredPane.DEFAULT_LAYER);
        add(passwordTf, JLayeredPane.DEFAULT_LAYER);
        add(ketNoiBtn, JLayeredPane.DEFAULT_LAYER);
        add(hienThiSinhVienBtn, JLayeredPane.DEFAULT_LAYER);
        add(sinhVienFilterBtn, JLayeredPane.DEFAULT_LAYER);
        add(capNhatSinhVienBtn, JLayeredPane.DEFAULT_LAYER);
        add(dongKetNoiBtn, JLayeredPane.DEFAULT_LAYER);
        add(ketNoiLabel, JLayeredPane.DEFAULT_LAYER);

        setVisible(true);
    }

    private int GetCenterXBtn(int wContainer, int wBtn)
    {
        return (wContainer - wBtn) / 2;
    }

    /**
     * Cho nó đẹp thôi, công thức (hContainer - hBtn) / 2 mới đúng!
     * @param hContainer
     * @param hBtn
     * @return
     */
    private int GetCenterYBtn(int hContainer, int hBtn)
    {
        return (hContainer - hBtn*2) / 2;
    }

    /**
     * 
     * @param hint : gợi ý
     * @param x : pos
     * @param y : pos
     * @return
     */
    private JTextField createFieldWithHint(String hint, int x, int y, int w, int h) 
    {
        JTextField txtField = new JTextField(hint);
        txtField.setBounds(x, y, w, h);
        txtField.setFont(PropertiesDefault.fontLarge);
        // txtField.setHorizontalAlignment(JTextField.CENTER);
        txtField.setForeground(Color.GRAY);
        Border border = new LineBorder(Color.darkGray, 3);
        txtField.setBorder(border);

        txtField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) 
            {
                if (txtField.getText().equals(hint)) 
                {
                    txtField.setText("");
                    
                    txtField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) 
            {
                if (txtField.getText().isEmpty())
                {
                    MakeNullText(txtField, hint);
                }
            }
        });
        
        return txtField;
    }

    private void MakeNullText(JTextField txtField, String hint)
    {
        txtField.setForeground(Color.GRAY);
        txtField.setText(hint);    
    }

    public static Connection GetConnectionOnClicked()
    {
        try 
        {
            if (DBConnection.connectInstance() != null && !DBConnection.connectInstance().isClosed()) 
                return DBConnection.connectInstance();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return null;
    }
}
