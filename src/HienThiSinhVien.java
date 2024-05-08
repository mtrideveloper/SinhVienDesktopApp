import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class HienThiSinhVien extends JLayeredPane
{
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JButton getDataBtn;
    private JButton goHomeBtn;

    public HienThiSinhVien(int w, int h)
    {
        setSize(w, h);

        SetComponents(w, h);
        
        // add components
        this.add(getDataBtn, JLayeredPane.PALETTE_LAYER);
        this.add(goHomeBtn, JLayeredPane.PALETTE_LAYER);
        this.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
        
        // setVisible(true);
    }
    
    private void SetComponents(int w, int h)
    {
        getDataBtn = new JButton("Get Data From Stored Procedure");
        getDataBtn.setBounds(PropertiesDefault.DISTANCE,
            h - PropertiesDefault.HEIGHT_BTN - PropertiesDefault.DISTANCE * 3, 
            PropertiesDefault.WIDTH_BTN * 2, 
            PropertiesDefault.HEIGHT_BTN);

        goHomeBtn = new JButton("Thoát");
        goHomeBtn.setBounds(
            w - PropertiesDefault.WIDTH_BTN * 2 - (PropertiesDefault.DISTANCE + 10),
            h - PropertiesDefault.HEIGHT_BTN - PropertiesDefault.DISTANCE * 3, 
            PropertiesDefault.WIDTH_BTN * 2, 
            PropertiesDefault.HEIGHT_BTN);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false
               return false;
            }
        };
        model.addColumn(MyDatabase.masv);
        model.addColumn(MyDatabase.hoTen);
        model.addColumn(MyDatabase.ngaysinh);
        model.addColumn(MyDatabase.gt);

        table = new JTable(model);
        // Đặt JTable vào JScrollPane để có thanh cuộn
        scrollPane = new JScrollPane(table);

        scrollPane.setBounds(
            PropertiesDefault.DISTANCE, 
            PropertiesDefault.DISTANCE,
            w - (PropertiesDefault.DISTANCE * 2 + 10), 
            h - (PropertiesDefault.DISTANCE * 6)
        );

        getDataBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                model.setRowCount(0);
                SelectFromSinhVien();
            }
        });

        goHomeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                SingletonManager.GetInstance().GetFormKetNoi().setVisible(true);
                SingletonManager.GetInstance().GetHienThiSinhVien().setVisible(false);
            }
        });
    }

    private void SelectFromSinhVien()
    {
        try 
        {
            if (DBConnection.connectInstance() != null && !DBConnection.connectInstance().isClosed())
            {
                String sql = "SELECT MaSinhVien, HoTen, NgaySinh, GioiTinh FROM sinhviensimple";
                PreparedStatement ps = DBConnection.connectInstance().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
    
                while (rs.next()) 
                {
                    // Thêm hàng vào DefaultTableModel
                    model.addRow(new Object[]
                    {
                        rs.getString(MyDatabase.masv), 
                        rs.getString(MyDatabase.hoTen), 
                        ConvertTodd_MM_yyyy(rs.getString(MyDatabase.ngaysinh)),
                        rs.getString(MyDatabase.gt)
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
