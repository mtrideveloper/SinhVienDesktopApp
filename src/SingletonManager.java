import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.table.DefaultTableModel;

public class SingletonManager 
{
    private SingletonManager(){}

    private static SingletonManager instance;
    public static final SingletonManager GetInstance()
    {
        if (instance == null)
        {
            instance = new SingletonManager();
        }
        return instance;
    }
    private FormKetNoi formKetNoi = new FormKetNoi(PropertiesDefault.WIDTH_FRAME, PropertiesDefault.HEIGHT_FRAME);
    public FormKetNoi GetFormKetNoi()
    {
        return formKetNoi;
    }

    private CapNhatSinhVien capNhatSinhVien = new CapNhatSinhVien(PropertiesDefault.WIDTH_FRAME, PropertiesDefault.HEIGHT_FRAME);
    public CapNhatSinhVien GetCapNhatSinhVien()
    {
        return capNhatSinhVien;
    }

    private HienThiSinhVien hienThiSinhVien = new HienThiSinhVien(PropertiesDefault.WIDTH_FRAME, PropertiesDefault.HEIGHT_FRAME);
    public HienThiSinhVien GetHienThiSinhVien()
    {
        return hienThiSinhVien;
    }

    private SinhVienFilter sinhVienFilter = new SinhVienFilter(PropertiesDefault.WIDTH_FRAME, PropertiesDefault.HEIGHT_FRAME);
    public SinhVienFilter GetSinhVienFilter()
    {
        return sinhVienFilter;
    }

    public static void SelectSinhVien(DefaultTableModel model)
    {
        if (DBConnection.connectInstance() != null)
        {
            try 
            {
                if (!DBConnection.connectInstance().isClosed())
                {
                    model.setRowCount(0);
                 
                    String sql = "SELECT * FROM sinhviensimple";
                    PreparedStatement ps = DBConnection.connectInstance().prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
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
    
    private static String ConvertTodd_MM_yyyy(String dateStr)
    {
        if (!dateStr.equals(""))
        {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return LocalDate.now().toString();
    }
}
