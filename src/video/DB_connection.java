/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package video;



import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author sujan
 */
public final class DB_connection {
    public  Connection conn;
    private Statement statement;
    private static DB_connection instance;
    
   protected DB_connection() {
        String url= "jdbc:sqlserver://localhost:1433;";
        String dbName = "vtti";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String  userName= "sa";
        String password = "password";
        
        String conUrl= url+"databaseName="+dbName+";user="+userName+";password="+password;
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection)DriverManager.getConnection(conUrl);
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }
    /**
     *
     * @return DB_connection Database connection object
     */
    public static synchronized DB_connection getDbCon() {
       
            instance = new DB_connection();
      
        return instance;
 
    }
    
    public static ResultSet query(String query, Connection conn) throws SQLException{
    Statement st =  conn.createStatement();
                
            
    ResultSet rs = st.executeQuery(query);  

return rs;

    }
    
    public static int readInt(ResultSet rs,String column) throws SQLException{
        int id=0;
    while(rs.next()){
               
              id=rs.getInt(column);
              }  

    return id;}
 
 
}