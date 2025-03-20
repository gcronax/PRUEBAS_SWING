import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class main {
    static JFrame frameMenu = null;

    public static void main(String[] args) {
        Scanner scan= new Scanner(System.in);
        frameMenu = new JFrame("menu");
        frameMenu.setSize(600, 100);

        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        frameMenu.setLocation(dimension.width/4, dimension.height/4);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        JButton btnpisos = new JButton("pisos");
        btnpisos.addActionListener(e -> {
            tablasrefactorizado.menuTablas("piso","pisos");
            frameMenu.setVisible(false);
        });
        btnpisos.setFont(new Font("Arial", Font.BOLD, 18));
        btnpisos.setBackground(Color.lightGray);
        panel.add(btnpisos);

        JButton btnpropietarios = new JButton("propietarios");
        btnpropietarios.addActionListener(e -> {
            tablasrefactorizado.menuTablas("propietario","propietarios");
            frameMenu.setVisible(false);
        });
        btnpropietarios.setFont(new Font("Arial", Font.BOLD, 18));
        btnpropietarios.setBackground(Color.lightGray);
        panel.add(btnpropietarios);

        JButton btninquilinos = new JButton("inquilinos");
        btninquilinos.addActionListener(e -> {
            tablasrefactorizado.menuTablas("inquilino","inquilinos");
            frameMenu.setVisible(false);
        });
        btninquilinos.setFont(new Font("Arial", Font.BOLD, 18));
        btninquilinos.setBackground(Color.lightGray);
        panel.add(btninquilinos);

        JButton btnpoblacions = new JButton("poblacions");
        btnpoblacions.addActionListener(e -> {
            tablasrefactorizado.menuTablas("poblacion","poblacions");
            frameMenu.setVisible(false);
        });
        btnpoblacions.setFont(new Font("Arial", Font.BOLD, 18));
        btnpoblacions.setBackground(Color.lightGray);
        panel.add(btnpoblacions);

        frameMenu.add(panel);
        frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMenu.setVisible(true);



    }
}
