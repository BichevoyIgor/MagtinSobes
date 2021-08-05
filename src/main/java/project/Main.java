package project;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        XMLHandler xmlHandler = new XMLHandler();
        SQLHandler sqlHandler = new SQLHandler();

        InitDb initDb = new InitDb();
        System.out.println("Для какой субд используем приложение? \nУкажите 1 - если для Oracle\nУкажите 2 - если для MySQL");
        if (scanner.nextLine().equals("1")) {
            initDb.setType("Oracle");
            System.out.println("Укажите путь к БД Oracle в формате host:порт:sid (пример: localhost:1521:orcl)");
            initDb.setUrl(scanner.nextLine());
        }else {
            initDb.setType("MySQL");
            System.out.println("Укажите путь к БД MySQL в формате host:порт/имя_БД (пример: localhost:3306/test)");
            initDb.setUrl(scanner.nextLine());
        }

        System.out.println("Укажите имя пользователя");
        initDb.setUserName(scanner.nextLine());

        System.out.println("Укажите пароль к базе данных");
        initDb.setPassword(scanner.nextLine());

        System.out.println("Укажите количество создаваемых записей");
        initDb.setCount(Integer.parseInt(scanner.nextLine()));
        initDb.connect();

        try {
            sqlHandler.insertDb(initDb.getConnection(), initDb.getCount());
            List<String> select = sqlHandler.selectDb(initDb.getConnection());
            xmlHandler.writeXml(select);
        } finally {
            initDb.disconnect();
        }
        xmlHandler.readXML();
    }
}
