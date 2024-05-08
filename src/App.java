import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App 
{
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                JFrame mainFrame = new JFrame("Kết Nối");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // mainFrame.setLocationRelativeTo(null);
                mainFrame.setSize(PropertiesDefault.WIDTH_FRAME, PropertiesDefault.HEIGHT_FRAME);
                mainFrame.setResizable(false);
                
                mainFrame.addMouseListener(new MouseAdapter() 
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        mainFrame.requestFocusInWindow();
                    }
                });

                // init
                // add components
                mainFrame.add(SingletonManager.GetInstance().GetFormKetNoi());
                mainFrame.add(SingletonManager.GetInstance().GetCapNhatSinhVien());
                mainFrame.add(SingletonManager.GetInstance().GetHienThiSinhVien());
                mainFrame.add(SingletonManager.GetInstance().GetSinhVienFilter());
                // form ket noi hien dau tien
                SingletonManager.GetInstance().GetCapNhatSinhVien().setVisible(false);
                SingletonManager.GetInstance().GetHienThiSinhVien().setVisible(false);
                SingletonManager.GetInstance().GetSinhVienFilter().setVisible(false);
        
                mainFrame.setVisible(true);
                // sau khi tất cả component được thêm vào frame,
                // gọi hàm này để con trỏ chuột thoát khỏi jtextfield
                mainFrame.requestFocusInWindow();
            }
        });        
    }
}
