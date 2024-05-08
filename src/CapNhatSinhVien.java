import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapNhatSinhVien extends JLayeredPane
{
    private JLabel maSinhVienLabel;
    private JLabel hoTenLabel;
    private JLabel maLopLabel;
    private JLabel ngaySinhLabel;
    private JLabel gioiTinhLabel;
    private JTextField maSinhVienTf;
    private JTextField hoTenTf;
    private JTextField maLopTf;
    private JTextField ngaySinhTf;
    private JTextField gioiTinhTf;
    /**
     * 0: Thêm;
     * 1: Lưu;
     * 2: Thoát
     */
    private JButton[] btns = new JButton[3];
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;
    
    private List<SinhVienSimple> sinhVienList = new ArrayList<>();

    private int day, month, year;
    private boolean isHotenOk = false, isCodeOk = false, 
                    isNgaysinhOk = false, isGioitinhOk = false;
    
    public CapNhatSinhVien(int w, int h)
    {
        setSize(w, h);

        maSinhVienLabel = new JLabel("Mã sinh viên");
        hoTenLabel = new JLabel("Họ tên");
        gioiTinhLabel = new JLabel("Giới tính");
        ngaySinhLabel = new JLabel("Ngày sinh");
        maLopLabel = new JLabel("Mã lớp");

        maSinhVienTf = new JTextField();
        hoTenTf = new JTextField();
        gioiTinhTf = new JTextField();
        ngaySinhTf = new JTextField();
        maLopTf = new JTextField();

        btns[0] = new JButton("Thêm");
        btns[1] = new JButton("Lưu");
        btns[2] = new JButton("Thoát");

        //for label
        maSinhVienLabel.setBounds(PropertiesDefault.DISTANCE, PropertiesDefault.DISTANCE,
        PropertiesDefault.WIDTH_LABEL, PropertiesDefault.HEIGHT_BTN);

        hoTenLabel.setBounds(PropertiesDefault.DISTANCE, 
        PropertiesDefault.DISTANCE + PropertiesDefault.HEIGHT_BTN * 2,
        PropertiesDefault.WIDTH_LABEL, PropertiesDefault.HEIGHT_BTN);

        maLopLabel.setBounds(PropertiesDefault.DISTANCE, 
        PropertiesDefault.HEIGHT_BTN * 4 + PropertiesDefault.DISTANCE,
        PropertiesDefault.WIDTH_LABEL, PropertiesDefault.HEIGHT_BTN);         
   
        ngaySinhLabel.setBounds(PropertiesDefault.DISTANCE * 3 + PropertiesDefault.WIDTH_LABEL + PropertiesDefault.WIDTH_TEXTFIELD, 
        PropertiesDefault.DISTANCE,
        PropertiesDefault.WIDTH_LABEL, PropertiesDefault.HEIGHT_BTN);
       
        gioiTinhLabel.setBounds(PropertiesDefault.DISTANCE * 3 + PropertiesDefault.WIDTH_LABEL + PropertiesDefault.WIDTH_TEXTFIELD, 
        PropertiesDefault.DISTANCE + PropertiesDefault.HEIGHT_BTN * 2,
        PropertiesDefault.WIDTH_LABEL, PropertiesDefault.HEIGHT_BTN);

        // for textfield
        maSinhVienTf.setBounds(PropertiesDefault.DISTANCE + PropertiesDefault.WIDTH_LABEL, 
        PropertiesDefault.DISTANCE,
        PropertiesDefault.WIDTH_TEXTFIELD, PropertiesDefault.HEIGHT_BTN);

        hoTenTf.setBounds(PropertiesDefault.DISTANCE + PropertiesDefault.WIDTH_LABEL, 
        PropertiesDefault.DISTANCE + PropertiesDefault.HEIGHT_BTN * 2,
        PropertiesDefault.WIDTH_TEXTFIELD, PropertiesDefault.HEIGHT_BTN);

        maLopTf.setBounds(PropertiesDefault.DISTANCE + PropertiesDefault.WIDTH_LABEL, 
        PropertiesDefault.HEIGHT_BTN * 4 + PropertiesDefault.DISTANCE,
        PropertiesDefault.WIDTH_TEXTFIELD, PropertiesDefault.HEIGHT_BTN);         
   
        ngaySinhTf.setBounds(PropertiesDefault.DISTANCE * 2 + PropertiesDefault.WIDTH_LABEL * 2 + PropertiesDefault.WIDTH_TEXTFIELD, 
        PropertiesDefault.DISTANCE,
        PropertiesDefault.WIDTH_TEXTFIELD, PropertiesDefault.HEIGHT_BTN);
       
        gioiTinhTf.setBounds(PropertiesDefault.DISTANCE * 2 + PropertiesDefault.WIDTH_LABEL * 2 + PropertiesDefault.WIDTH_TEXTFIELD, 
        PropertiesDefault.DISTANCE + PropertiesDefault.HEIGHT_BTN * 2,
        PropertiesDefault.WIDTH_TEXTFIELD, PropertiesDefault.HEIGHT_BTN);

        // int gap = (w - btns.length * PropertiesDefault.WIDTH_BTN) / (btns.length + 1);
        // for (int i = 0; i < btns.length; i++)
        // {
        //     btns[i].setFont(PropertiesDefault.fontStandard);

        //     btns[i].setBounds(gap + i * (PropertiesDefault.WIDTH_BTN + gap),
        //     h - PropertiesDefault.HEIGHT_BTN * 4, 
        //     PropertiesDefault.WIDTH_BTN, 
        //     PropertiesDefault.HEIGHT_BTN);
        // }

        btns[0].setBounds(((w - PropertiesDefault.WIDTH_BTN) / 2) - PropertiesDefault.WIDTH_BTN - PropertiesDefault.DISTANCE, 
        h - PropertiesDefault.HEIGHT_BTN * 4, 
        PropertiesDefault.WIDTH_BTN, 
        PropertiesDefault.HEIGHT_BTN);

        btns[1].setBounds((w - PropertiesDefault.WIDTH_BTN) / 2, 
        h - PropertiesDefault.HEIGHT_BTN * 4, 
        PropertiesDefault.WIDTH_BTN, 
        PropertiesDefault.HEIGHT_BTN);
        
        btns[2].setBounds(((w - PropertiesDefault.WIDTH_BTN) / 2) + PropertiesDefault.WIDTH_BTN + PropertiesDefault.DISTANCE, 
        h - PropertiesDefault.HEIGHT_BTN * 4, 
        PropertiesDefault.WIDTH_BTN, 
        PropertiesDefault.HEIGHT_BTN);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false
               return false;
            }
        };  
        model.addColumn(MyDatabase.masv);
        model.addColumn(MyDatabase.malop);              
        model.addColumn(MyDatabase.hoTen);
        model.addColumn(MyDatabase.ngaysinh);
        model.addColumn(MyDatabase.gt);

        // Tạo JTable với DefaultTableModel
        table = new JTable(model);

        // Đặt kích thước cho bảng bằng cách tính toán dựa trên số lượng dòng và cột
        // table.setPreferredScrollableViewportSize(new Dimension(w - PropertiesDefault.DISTANCE, h / 4));

        // Đặt các thuộc tính scroll cho bảng
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(PropertiesDefault.DISTANCE, 
        PropertiesDefault.HEIGHT_BTN * 5 + PropertiesDefault.DISTANCE * 2,
            PropertiesDefault.WIDTH_LABEL * 2 + PropertiesDefault.WIDTH_TEXTFIELD * 2 + PropertiesDefault.DISTANCE, 
            h / 3
        );

        // thêm
        btns[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if (!maSinhVienTf.getText().equals("") &&
                    !hoTenTf.getText().equals("") &&
                    !maLopTf.getText().equals("") &&
                    !ngaySinhTf.getText().equals("") &&
                    !gioiTinhTf.getText().equals(""))
                {
                    String masvChecked = CheckCode(maSinhVienTf.getText());
                    String malopChecked = CheckCode(maLopTf.getText());
                    String hotenChecked = CheckHoTenValid(hoTenTf.getText());
                    String ngaysinhChecked = CheckDateFormat(ngaySinhTf.getText());
                    String gioitinhChecked = CheckGioiTinh(gioiTinhTf.getText());

                    // System.out.println("iscode: " + isCodeOk + "; isHoten: " + isHotenOk
                    // + "; isGt: " + isGioitinhOk + "; isNgaysinh: " + isNgaysinhOk);                    
                    if (isCodeOk && isHotenOk && isGioitinhOk && isNgaysinhOk)
                    {
                        model.addRow(new Object[] 
                        {
                            masvChecked, malopChecked, hotenChecked, 
                            ngaysinhChecked, gioitinhChecked
                        });

                        AddValueStandands(masvChecked, malopChecked, hotenChecked, ngaysinhChecked, gioitinhChecked);

                        ClearAllTextFields();
                    }
                }
            }
        });

        // lưu vào bang [sinhvienform].[dbo].[sinhvien] 
        btns[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                InsertValuesIntoTable();
            }
        });
        
        // thoát
        btns[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // while (model.getRowCount() > 0)
                //     model.removeRow(0);
                ClearData();

                SingletonManager.GetInstance().GetFormKetNoi().setVisible(true);
                SingletonManager.GetInstance().GetCapNhatSinhVien().setVisible(false);
            }
        });

        this.add(maSinhVienLabel, JLayeredPane.DEFAULT_LAYER);
        this.add(hoTenLabel, JLayeredPane.DEFAULT_LAYER);
        this.add(maLopLabel, JLayeredPane.DEFAULT_LAYER);
        this.add(ngaySinhLabel, JLayeredPane.DEFAULT_LAYER);
        this.add(gioiTinhLabel, JLayeredPane.DEFAULT_LAYER);
        this.add(maSinhVienTf, JLayeredPane.DEFAULT_LAYER);
        this.add(hoTenTf, JLayeredPane.DEFAULT_LAYER);
        this.add(maLopTf, JLayeredPane.DEFAULT_LAYER);
        this.add(ngaySinhTf, JLayeredPane.DEFAULT_LAYER);
        this.add(gioiTinhTf, JLayeredPane.DEFAULT_LAYER);

        for (int i = 0; i < btns.length; i++)
        {
            this.add(btns[i], JLayeredPane.DEFAULT_LAYER);
        }

        this.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
    }

    private void ClearAllTextFields()
    {
        maSinhVienTf.setText("");
        maLopTf.setText("");
        hoTenTf.setText("");
        gioiTinhTf.setText("");
        ngaySinhTf.setText("");
        isCodeOk = isGioitinhOk = isHotenOk = isNgaysinhOk = false;
    }

    private void AddValueStandands(String masv, String malop, String hoten, String ngaysinh, String gioitinh)
    {
        SinhVienSimple sv = new SinhVienSimple();
        
        sv.setMaSinhVien(masv);
        sv.setHoTen(hoten);
        sv.setMaLop(malop);
        sv.setNgaySinh(ConvertToStandardDateSQLServer(ngaysinh));
        sv.setGioiTinh(gioitinh);

        sinhVienList.add(sv);
    }

    private void InsertValuesIntoTable()
    {
        if (DBConnection.connectInstance() != null)
        {
            if (model.getRowCount() > 0)
            {
                try
                {
                    if (!DBConnection.connectInstance().isClosed())
                    {
                        String sql = "INSERT INTO sinhviensimple(MaSinhVien, HoTen, GioiTinh, NgaySinh, MaLop) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement ps = DBConnection.connectInstance().prepareStatement(sql);
            
                        for (SinhVienSimple sinhVien : sinhVienList) 
                        {
                            // Thiết lập giá trị cho các tham số trong câu lệnh SQL
                            ps.setString(1, sinhVien.getMaSinhVien());
                            ps.setString(2, sinhVien.getHoTen());
                            ps.setString(3, sinhVien.getGioiTinh());
                            ps.setString(4, sinhVien.getNgaySinh());
                            ps.setString(5, sinhVien.getMaLop());
                            
                            // Thêm câu lệnh vào batch
                            ps.addBatch();
                        }
                        // Thực thi một nhóm các lệnh SQL đã được nhóm lại với nhau
                        int[] rowsAffected = ps.executeBatch();
                        // ps.clearBatch() // xóa các lệnh đã được thêm vào batch trước đó
                        for (int r : rowsAffected) {
                            System.out.println(r);
                        }
                        ClearData();
                        ps.close();    
                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }            
            }
    
        }
    }

    private void ClearData()
    {
        model.setRowCount(0);
        sinhVienList.clear();        
    }

    private String CheckDateFormat(String input)
    {
        String regex = "(\\d{1,2})[-/](\\d{1,2})[-/](\\d{4})|"
        +"(\\d{4})[-/](\\d{1,2})[-/](\\d{1,2})";
        Matcher matcher = Pattern.compile(regex).matcher(input);

        if (matcher.matches()) 
        {
            if (matcher.group(1) != null) 
            {
                day = Integer.parseInt(matcher.group(1));
                month = Integer.parseInt(matcher.group(2));
                year = Integer.parseInt(matcher.group(3));
            } 
            else 
            {
                day = Integer.parseInt(matcher.group(6));
                month = Integer.parseInt(matcher.group(5));
                year = Integer.parseInt(matcher.group(4));
            }   
            LocalDate date = LocalDate.of(year, month, day);
            
            isNgaysinhOk = true;
            
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        // return LocalDate.now().toString();
        return "";     
    }

    private String ConvertToStandardDateSQLServer(String dateFormatStr)
    {
        if (!dateFormatStr.equals(""))
        {
            return LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return LocalDate.now().toString();
    }

    private String CheckGioiTinh(String input)
    {
        String upper = input.toUpperCase();
        if (!upper.equals("NAM") && !upper.equals("NỮ"))
            return "";
        
        isGioitinhOk = true;
        return upper;
    }

    private String CheckHoTenValid(String input)
    {
        String regex = "^[\\p{L}\\s]*$";
        if (input.matches(regex))
        {
            isHotenOk = true;
            return input;
        }
        return "";
    }

    private String CheckCode(String code) 
    {
        String regex = "^[a-zA-Z0-9]{1,20}$";
        if (code.matches(regex)) 
        {
            isCodeOk = true;
    
            return code.toUpperCase();
        }
        return "";
    }
}
