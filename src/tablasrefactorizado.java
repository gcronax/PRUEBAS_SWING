import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class tablasrefactorizado {
    public static String entityName;
    public static String tableName;
    public static String[] headers;
    public static int[] columnTypes;
    private static JFrame frameSubMenu = null;
    private static JFrame frameConsulta = null;
    private static JFrame frameInsertar = null;
    private static JFrame frameEliminar = null;
    private static JFrame frameActualizar = null;

    private static final String URL = "jdbc:postgresql://89.36.214.106:5432/geo_1cfsl_3267g";
    private static final String USER = "geo_1cfsl_3267g";
    private static final String PASSWORD = "geo_1cfsl_3267g";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }
        return conn;
    }

    public static void disconnect(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public static void menuTablas(String entity, String table) {
        entityName = entity;
        tableName = table;
        try {
            headers = getHeaders();
            columnTypes = getColumnTypes();

            frameSubMenu = new JFrame("Gestion de "+tableName);
            frameSubMenu.setSize(600, 100);
            Toolkit mipantalla= Toolkit.getDefaultToolkit();
            Dimension dimension = mipantalla.getScreenSize();
            frameSubMenu.setLocation(dimension.width/4, dimension.height/3);
            JPanel panel = new JPanel();

            JButton btnconsultar = new JButton("consultar");
            btnconsultar.addActionListener(e -> {
                queryData();
            });
            panel.add(btnconsultar);

            JButton btninsertar = new JButton("insertar");
            btninsertar.addActionListener(e -> {
                insertData();
                frameSubMenu.setVisible(false);
            });
            panel.add(btninsertar);

            JButton btneliminar = new JButton("eliminar");
            btneliminar.addActionListener(e -> {
                deleteData();
                frameSubMenu.setVisible(false);
            });
            panel.add(btneliminar);

            JButton btnactualizar = new JButton("actualizar");
            btnactualizar.addActionListener(e -> {
                updateData();
                frameSubMenu.setVisible(false);
            });
            panel.add(btnactualizar);

            JButton btnsalir = new JButton("salir");
            btnsalir.addActionListener(e -> {
                frameSubMenu.dispose();
                main.frameMenu.setVisible(true);
            });
            panel.add(btnsalir);


            frameSubMenu.add(panel);
            frameSubMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frameSubMenu.setVisible(true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void queryData() {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName +" order by "+headers[0]);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnName(i);
            }

            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable table = new JTable(model);
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
            if (frameConsulta == null) {
                frameConsulta = new JFrame("Listado de " + tableName);
                frameConsulta.setSize(900, 400);
                JScrollPane scrollPane = new JScrollPane(table);
                frameConsulta.add(scrollPane);
                frameConsulta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } else {
                frameConsulta.getContentPane().removeAll();
                frameConsulta.setTitle("Listado de " + tableName);
                JScrollPane scrollPane = new JScrollPane(table);
                frameConsulta.add(scrollPane);
                frameConsulta.revalidate();
                frameConsulta.repaint();
            }
            frameConsulta.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) disconnect(conn);
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void insertData() {
        Statement stmt = null;
        ResultSet rs = null;
        String[] columns = new String[0];
        int[] types = new int[0];
        Connection conn = null;

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            columns = new String[columnCount];
            types = new int[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnName(i);
                types[i - 1] = metaData.getColumnType(i);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        Scanner scanner = new Scanner(System.in);
        String[] fieldValues = new String[columns.length];


        frameInsertar = new JFrame("Añadir "+tableName);
        frameInsertar.setSize(300, 600);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        frameInsertar.setLocation(dimension.width/4, dimension.height/3);
        JPanel panel = new JPanel();
        ArrayList<JTextField> textFields=new ArrayList<>();

        for (int i = 1; i < columns.length; i++) {
            JTextField textField = new JTextField(20);
            textFields.add(textField);
            JLabel label = new JLabel("Ingrese " + columns[i]);
            panel.add(label);
            panel.add(textField);
        }

        String[] finalColumns = columns;
        Connection finalConn = conn;
        int[] finalTypes = types;

        JButton btninsertar = new JButton("insertar");
        btninsertar.addActionListener(e -> {

            int i=1;
            for (JTextField text:textFields){
                fieldValues[i++] =text.getText();
            }

            frameSubMenu.setVisible(true);
            frameInsertar.dispose();
            insertar(finalColumns, finalConn, fieldValues, finalTypes);
            queryData();

        });
        panel.add(btninsertar);
        JButton btncancelar = new JButton("cancelar");
        btncancelar.addActionListener(e -> {
            frameSubMenu.setVisible(true);
            frameInsertar.dispose();
        });
        panel.add(btncancelar);

        frameInsertar.add(panel);
        frameInsertar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameInsertar.setVisible(true);


    }

    private static void insertar(String[] columns, Connection conn, String[] fieldValues, int[] types) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder values = new StringBuilder(" VALUES (");

        for (int i = 1; i < columns.length; i++) {
            sql.append(columns[i]);
            values.append("?");
            if (i < columns.length - 1) {
                sql.append(", ");
                values.append(", ");
            }
        }
        sql.append(") ");
        values.append(") ");
        sql.append(values);

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql.toString());

            for (int i = 1; i < fieldValues.length; i++) {
                if (types[i] == 12) {
                    pstmt.setString(i, fieldValues[i]);
                }
                if (types[i] == 4) {
                    pstmt.setInt(i, Integer.parseInt(fieldValues[i]));
                }
                if (types[i] == 2) {
                    pstmt.setDouble(i, Double.parseDouble(fieldValues[i]));
                }
                if (types[i] == 91) {
                    pstmt.setDate(i, Date.valueOf(fieldValues[i]));
                }
            }

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(entityName + " insertado exitosamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al insertar " + entityName + ": " + e.getMessage());
        } finally {
            try {
                if (conn != null) disconnect(conn);
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void deleteData() {

        frameEliminar = new JFrame("Ingrese el ID de " + entityName + " que desea eliminar");
        frameEliminar.setSize(400, 100);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        frameEliminar.setLocation(dimension.width/4, dimension.height/3);
        JPanel panel = new JPanel();

        JTextField textField = new JTextField(20); // Campo de texto
        JButton button = new JButton("eliminar id");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar(Integer.parseInt(textField.getText()));
                queryData();
            }
        });
        panel.add(textField);
        panel.add(button);

        JButton btncancelar = new JButton("cancelar");
        btncancelar.addActionListener(e -> {
            frameSubMenu.setVisible(true);
            frameEliminar.dispose();
        });
        panel.add(btncancelar);

        frameEliminar.add(panel);
        frameEliminar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameEliminar.setVisible(true);


    }

    private static void eliminar(int id) {
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = connect();
            String sql = "DELETE FROM " + tableName + " WHERE " + headers[0] + " = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(entityName + " eliminado exitosamente.");
            } else {
                System.out.println("No se encontró una " + entityName + " con el ID proporcionado.");
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar el " + entityName + ": " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void updateData() {
        //dejar solo id y generar dinamicamente botones por nombre campo a actualizar que contengan la llamada en si a insertar donde su text field sea local y id global
        frameActualizar = new JFrame("Actualizar "+tableName);
        frameActualizar.setSize(430, 600);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        frameActualizar.setLocation(dimension.width/4, dimension.height/3);
        JPanel panel = new JPanel();
        JTextField textFieldid = new JTextField(20);

        JLabel labelid = new JLabel("Ingrese el ID del " + entityName);
        panel.add(labelid);
        panel.add(textFieldid);

        ArrayList<JTextField> textFields=new ArrayList<>();

        for (int i = 1; i < headers.length; i++) {
            JTextField textField = new JTextField(20);
            textFields.add(textField);
            JLabel label = new JLabel("Ingrese " + headers[i]);
            panel.add(label);
            panel.add(textField);
        }

        JButton btnactualizar = new JButton("actualizar");
        btnactualizar.addActionListener(e -> {
            int i=1;
            for (JTextField text:textFields){
                if (!Objects.equals(text.getText(), "")){
                    System.out.println(text.getText());
                    actualizar(i, text.getText(), Integer.parseInt(textFieldid.getText()));
                }
                i++;
            }
            frameActualizar.dispose();
            frameSubMenu.setVisible(true);
            queryData();

        });
        panel.add(btnactualizar);

        JButton btncancelar = new JButton("cancelar");
        btncancelar.addActionListener(e -> {
            frameSubMenu.setVisible(true);
            frameActualizar.dispose();
        });
        panel.add(btncancelar);
        frameActualizar.add(panel);
        frameActualizar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameActualizar.setVisible(true);
    }

    private static void actualizar(int selection, String newValue, int id) {
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = connect();
            String sql = "UPDATE " + tableName + " SET " + headers[selection] + " = ? WHERE " + headers[0] + " = ?";
            pstmt = conn.prepareStatement(sql);

            if (columnTypes[selection] == 12) {
                pstmt.setString(1, newValue);
            }
            if (columnTypes[selection] == 4) {
                pstmt.setInt(1, Integer.parseInt(newValue));
            }
            if (columnTypes[selection] == 2) {
                pstmt.setDouble(1, Double.parseDouble(newValue));
            }
            if (columnTypes[selection] == 91) {
                pstmt.setDate(1, Date.valueOf(newValue));
            }

            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(entityName + " actualizado exitosamente.");
            } else {
                System.out.println("No se encontró el " + entityName + " con el ID proporcionado.");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar el " + entityName + ": " + e.getMessage());
        } finally {
            disconnect(conn);
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static String[] getHeaders() {
        Statement stmt = null;
        ResultSet rs = null;
        String[] columns = new String[0];
        Connection conn = null;

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            columns = new String[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnName(i);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) disconnect(conn);
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return columns;
    }

    public static int[] getColumnTypes() {
        Statement stmt = null;
        ResultSet rs = null;
        int[] types = new int[0];
        Connection conn = null;

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            types = new int[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                types[i - 1] = metaData.getColumnType(i);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) disconnect(conn);
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return types;
    }
}