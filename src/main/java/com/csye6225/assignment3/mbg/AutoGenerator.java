package com.csye6225.assignment3.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AutoGenerator {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://csye6225-spring2020.c0ucbgzrob39.us-east-1.rds.amazonaws.com";
    static final String USER = "dbuser";
    static final String PASSWORD = "Jennifer202212";

    //private final static String initDir = "/home/ubuntu/csye6225_file_disk/";

    private static final String CREATE_Database="CREATE DATABASE csye6225_assignment3";
/*
    private static final String CREATE_TABLE_Account="CREATE TABLE csye6225_assignment3.account ("
            + "USER_ID VARCHAR(36) NOT NULL,"
            + "FIRST_NAME VARCHAR(64) NOT NULL,"
            + "LAST_NAME VARCHAR(64) NOT NULL,"
            + "EMAIL_ADDRESS VARCHAR(64) NOT NULL,"
            + "PASSWORD VARCHAR(64) NOT NULL,"
            + "EMAIL_ADDRESS VARCHAR(64) NOT NULL,"
            + "account_create timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
            + "account_updated timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
            + "PRIMARY KEY (USER_ID))"
            + "ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    private static final String CREATE_TABLE_Bill="CREATE TABLE csye6225_assignment3.bill ("
            + "bill_id VARCHAR(36) NOT NULL,"
            + "created_ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "updated_ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
            + "owner_id varchar(36) NOT NUL,"
            + "vendor varchar(64) NOT NULL,"
            + "EMAIL_ADDRESS VARCHAR(64) NOT NULL,"
            + "PASSWORD VARCHAR(64) NOT NULL,"
            + "EMAIL_ADDRESS VARCHAR(64) NOT NULL,"
            + "account_create timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
            + "account_updated timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
            + "PRIMARY KEY (USER_ID))"
            + "ENGINE=InnoDB DEFAULT CHARSET=utf8;";

 */
    private static final String CREATE_TABLE_Account="CREATE TABLE `account` (\n" +
        "  `user_id` varchar(36) NOT NULL,\n" +
        "  `first_name` varchar(64) NOT NULL,\n" +
        "  `last_name` varchar(64) NOT NULL,\n" +
        "  `email_address` varchar(64) NOT NULL,\n" +
        "  `password` varchar(64) NOT NULL,\n" +
        "  `account_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,\n" +
        "  `account_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
        "  PRIMARY KEY (`user_id`)\n" +
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8";

    private static final String CREATE_TABLE_Bill="CREATE TABLE `bill` (\n" +
        "  `bill_id` varchar(36) NOT NULL,\n" +
        "  `created_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
        "  `updated_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
        "  `owner_id` varchar(36) NOT NULL,\n" +
        "  `vendor` varchar(64) NOT NULL,\n" +
        "  `bill_date` date NOT NULL,\n" +
        "  `due_date` date NOT NULL,\n" +
        "  `amount_due` double(6,2) NOT NULL,\n" +
        "  `categories` varchar(256) NOT NULL,\n" +
        "  `payment_status` enum('paid','due','past_due','no_payment_required') NOT NULL,\n" +
        "  `file_id` varchar(36) DEFAULT NULL,\n" +
        "  PRIMARY KEY (`bill_id`)\n" +
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8";

    private static final String CREATE_TABLE_AttachedFile="CREATE TABLE `attached_file` (\n" +
            "  `file_name` varchar(256) NOT NULL,\n" +
            "  `file_id` varchar(36) NOT NULL,\n" +
            "  `url` varchar(1000) NOT NULL,\n" +
            "  `upload_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  `file_md5` varchar(40) NOT NULL,\n" +
            "  `file_size` bigint(10) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`file_id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8";

    public void createDatabaseTableConnection() throws IOException, XMLParserException, SQLException, InterruptedException, InvalidConfigurationException {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        InputStream is = generator.class.getResourceAsStream("/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        is.close();

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        //
        myBatisGenerator.generate(null);
        //
        for (String warning : warnings) {
            System.out.println(warning);
        }
    }

    public void createDatabaseTable() {

        Connection conn = null;
        Statement stmt = null;

        try {

            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();

            stmt.executeUpdate(CREATE_Database);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close connection
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String jdbcUrl = "jdbc:mysql://csye6225-spring2020.c0ucbgzrob39.us-east-1.rds.amazonaws.com:3306/csye6225_assignment3";
        String username = "dbuser";
        String password = "Jennifer202212";

        try {

            conn = DriverManager.getConnection(jdbcUrl, username, password);
            stmt = conn.createStatement();

            stmt.executeUpdate(CREATE_TABLE_Account);
            stmt.executeUpdate(CREATE_TABLE_Bill);
            stmt.executeUpdate(CREATE_TABLE_AttachedFile);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close connection
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
/*
    public boolean createDir() {
        String destDirName = initDir;
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }

 */

}
