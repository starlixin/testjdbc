package com.testjdbc;


import com.mongodb.client.*;
import org.bson.Document;

import java.sql.*;

public class JdbcConnectionTest {
    /**
     *测试示例：
     * mysql 参数1：连接地址，参数2：查询SQL
     * java -jar testjdbc-1.0-SNAPSHOT.jar "jdbc:mysql://172.16.104.104:3306/test?user=root&password=12345678" "select 1"
     * pgsql
     * java -jar testjdbc-1.0-SNAPSHOT.jar "jdbc:postgresql://172.16.120.113:5432/user?user=basicframe&password=xxx" "select 1"
     * Oracle
     * java -jar testjdbc-1.0-SNAPSHOT.jar "jdbc:oracle:thin:@//172.16.104.104:21521/helowin?user=system&password=test_cc" "SELECT * FROM v$version"
     * Mongo 参数1：连接地址，参数2：集合名称
     * java -jar testjdbc-1.0-SNAPSHOT.jar "mongodb://dsmp:123456@172.16.105.135:20000/dsmp_database" "dsmp_collection"
     * java -jar testjdbc-1.0-SNAPSHOT.jar "mongodb://username:password@172.16.105.135:20000/dsmp_database" "dsmp_collection"
     *
     *  @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        // mongoDB args[0]连接地址；args[1]：连接集合
        if(args[0].contains("mongodb")){
            String connectionString = args[0];
            String databaseName = connectionString.substring(connectionString.lastIndexOf("/")+1);
            // 连接 MongoDB 数据库
            try (MongoClient mongoClient = MongoClients.create(connectionString)) {
                MongoDatabase database = mongoClient.getDatabase(databaseName);
                MongoCollection<Document> collection = database.getCollection(args[1]);
                // 执行查询
                FindIterable<Document> documents = collection.find().limit(10);
                int i=0;
                for (Document document : documents) {
                    System.out.println("结果"+i+": "+document.toJson());
                    i++;
                }
            } catch (Exception e) {
                System.out.println("mongoDB连接异常");
                e.printStackTrace();
            }
            return;
        }

        Connection connection= DriverManager.getConnection(args[0]);
        Statement statement=connection.createStatement();
        System.out.println("连接成功："+ args[0]);
        System.out.println("执行SQL："+args[1]);
        ResultSet resultSet = statement.executeQuery(args[1]);
        int i=0;
        while (resultSet.next()){
            System.out.println("结果"+i+": "+resultSet.getString(1));
            i++;
        }
        statement.close();
        connection.close();
    }  
}