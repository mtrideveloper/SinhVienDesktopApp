import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection 
{
    private static Connection con;

    public static Connection connectInstance()
    {
        return con;
    }

    private DBConnection()
    {
    }

    public static void ConnectionStarted()
    {
        if (!SingletonManager.GetInstance().GetFormKetNoi().GetServerNameTf().getText().equals("Server name")
        && !SingletonManager.GetInstance().GetFormKetNoi().GetUserNameTf().getText().equals("User name")
        || (
            MyDatabase.CONNECTION_STRING == "jdbc:sqlserver://LAPTOP-U3LUOQK4:1433;databaseName=sinhvienform;encrypt=false;loginTimeout=5;"
            && MyDatabase.USER_NAME == "sa"
            && MyDatabase.PASSWORD == "123456789"
            ))
        {
            try
            {
                if (con == null || con.isClosed())
                    con = getConnection();
            }
            catch (SQLException ex)
            {    
                ex.printStackTrace();
            }
        }
    }

    private static Connection getConnection()
    {
        try
        {
            Class.forName(MyDatabase.DRIVER_NAME);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // The connection is closed.
        //try (Connection con = DriverManager.getConnection(MyDatabase.CONNECTION_STRING, MyDatabase.USER_NAME, MyDatabase.PASSWORD))
        // fixed
        try
        {
            Connection con = DriverManager.getConnection(MyDatabase.CONNECTION_STRING, MyDatabase.USER_NAME, MyDatabase.PASSWORD);
            System.out.println("found: " + con.getCatalog() + ", " + con.getSchema());
            return con;
        }
        catch (SQLException sqlEx)
        {
            System.out.println(sqlEx.getMessage());
            return null;
        }
    }
}