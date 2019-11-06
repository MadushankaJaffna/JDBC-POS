package sample.DBConnection;

import java.sql.*;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;


    private DBConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poswithsql?createDatabaseIfNotExist=true&allowMultiQueries=true","root","123");
            PreparedStatement show_tables = connection.prepareStatement("SHOW TABLES");
            ResultSet execute = show_tables.executeQuery();
            if(!execute.next()){
              String sql = "create table customer(Id varchar(20) not null primary key,Name varchar(20) null,Address varchar(30) null);\n" +
                      "create table item(Code varchar(20) not null primary key,Description varchar(50) not null,QtyOnHand int(20) not null,UnitPrice decimal(8,2) not null);\n" +
                      "create table `order`(Id varchar(20) not null primary key,CustomerId varchar(20) not null,Date date not null,constraint Order_customer__fk foreign key (CustomerId) references customer (Id) on update cascade on delete cascade);\n" +
                      "create table orderdetails(Qty int not null,UnitPrice decimal(8,2) not null,OrderId varchar(20) default'' not null,ItemId varchar(20) not null,primary key (OrderId,ItemId),constraint OrderDetails_order__fk foreign key (OrderId) references `order` (Id) on update cascade on delete cascade,constraint orderdetails_item__fk foreign key (ItemId) references item (Code) on update cascade on delete cascade);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static DBConnection getInstance(){
        return (dbConnection==null)?(dbConnection=new DBConnection()):dbConnection;
    }

    public Connection getConnection(){return connection; }
}
