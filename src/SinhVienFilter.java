import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SinhVienFilter  extends JLayeredPane
{
    private JTable table;
    
    private DefaultTableModel model;
    public DefaultTableModel GetModel() 
    {
        return model; 
    }

    private JLabel maloFilterLabel;
    private JTextField maLopFilterTf;
    private JButton filterBtn;
    private JButton goHomeBtn;
    private JScrollPane scrollPane;

    public SinhVienFilter(int w, int h)
    {
        setSize(w, h);

        SetLabel();
        SetTextField();
        SetButton();
        SetTableModel(w, h);

        // add components
        this.add(scrollPane);
        this.add(maloFilterLabel);
        this.add(maLopFilterTf);
        this.add(filterBtn);
        this.add(goHomeBtn);

        // setVisible(true);
    }

    private void SetLabel()
    {
        maloFilterLabel = new JLabel();
        maloFilterLabel.setText("Nhập mã lớp");
        maloFilterLabel.setBounds(
            PropertiesDefault.DISTANCE, 
            PropertiesDefault.DISTANCE, 
            PropertiesDefault.WIDTH_LABEL, 
            PropertiesDefault.HEIGHT_BTN);
    }

    private void SetTextField()
    {
        maLopFilterTf = new JTextField();
        maLopFilterTf.setBounds(
            PropertiesDefault.DISTANCE + PropertiesDefault.WIDTH_LABEL, 
            PropertiesDefault.DISTANCE, 
            PropertiesDefault.WIDTH_TEXTFIELD, 
            PropertiesDefault.HEIGHT_BTN);
    }
    
    private void SetButton()
    {
        filterBtn = new JButton("Lọc");
        filterBtn.setBounds(
            PropertiesDefault.DISTANCE * 2 + PropertiesDefault.WIDTH_LABEL + PropertiesDefault.WIDTH_TEXTFIELD, 
            PropertiesDefault.DISTANCE, 
            PropertiesDefault.WIDTH_BTN, 
            PropertiesDefault.HEIGHT_BTN);

        goHomeBtn = new JButton("Thoát");
        goHomeBtn.setBounds(
            // PropertiesDefault.DISTANCE * 3 + PropertiesDefault.WIDTH_LABEL + PropertiesDefault.WIDTH_TEXTFIELD + PropertiesDefault.WIDTH_BTN, 
            PropertiesDefault.WIDTH_FRAME - PropertiesDefault.WIDTH_BTN - (PropertiesDefault.DISTANCE + 10),
            PropertiesDefault.DISTANCE, 
            PropertiesDefault.WIDTH_BTN, 
            PropertiesDefault.HEIGHT_BTN);

        goHomeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                maLopFilterTf.setText("");
                SingletonManager.GetInstance().GetFormKetNoi().setVisible(true);
                SingletonManager.GetInstance().GetSinhVienFilter().setVisible(false);
            }
        });

        filterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                SelectSinhVienWithMaLop(DBConnection.connectInstance(), CheckCode(maLopFilterTf.getText()));
            }
        });
    }

    private void SetTableModel(int w, int h)
    {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        model.addColumn(MyDatabase.masv);
        model.addColumn(MyDatabase.hoTen);
        model.addColumn(MyDatabase.gt);
        model.addColumn(MyDatabase.ngaysinh);
        model.addColumn(MyDatabase.malop);
        
        table = new JTable(model);
        // Đặt JTable vào JScrollPane để có thanh cuộn
        scrollPane = new JScrollPane(table);

        scrollPane.setBounds(
            PropertiesDefault.DISTANCE, 
            (PropertiesDefault.DISTANCE * 2) + PropertiesDefault.HEIGHT_BTN,
            w - (PropertiesDefault.DISTANCE * 2 + 10), 
            h - (PropertiesDefault.DISTANCE * 6)
        );
    }

    private String CheckCode(String code) 
    {
        String regex = "^[a-zA-Z0-9]{1,20}$";
        if (code.matches(regex)) 
        {
            return code.toUpperCase();
        }
        return "";
    }    

    private void SelectSinhVienWithMaLop(Connection con, String malop)
    {
        if (con != null)
        {
            try 
            {
                if (!DBConnection.connectInstance().isClosed())
                {
                    String sql = "SELECT * FROM sinhviensimple WHERE MaLop = ?";
                    PreparedStatement ps = DBConnection.connectInstance().prepareStatement(sql);
                    ps.setString(1, malop);
                    ResultSet rs = ps.executeQuery();
                    // Xóa tất cả các dòng hiện có trong bảng trước khi thêm dữ liệu mới
                    model.setRowCount(0);
                    while (rs.next()) 
                    {
                        model.addRow(new Object[]
                        {
                            rs.getString(MyDatabase.masv), 
                            rs.getString(MyDatabase.hoTen), 
                            rs.getString(MyDatabase.gt),
                            ConvertTodd_MM_yyyy(rs.getString(MyDatabase.ngaysinh)),
                            rs.getString(MyDatabase.malop)
                        });
                    }
                    ps.close();
                    // DBConnection.connectInstance().close();    
                }
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
    
    private String ConvertTodd_MM_yyyy(String dateStr)
    {
        if (!dateStr.equals(""))
        {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return LocalDate.now().toString();
    } 
}