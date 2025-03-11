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

        JButton btnpisos = new JButton("pisos");
        btnpisos.addActionListener(e -> {
            tablasrefactorizado.menuTablas("piso","pisos");
            frameMenu.setVisible(false);
        });
        panel.add(btnpisos);

        JButton btnpropietarios = new JButton("propietarios");
        btnpropietarios.addActionListener(e -> {
            tablasrefactorizado.menuTablas("propietario","propietarios");
            frameMenu.setVisible(false);
        });
        panel.add(btnpropietarios);

        JButton btninquilinos = new JButton("inquilinos");
        btninquilinos.addActionListener(e -> {
            tablasrefactorizado.menuTablas("inquilino","inquilinos");
            frameMenu.setVisible(false);
        });
        panel.add(btninquilinos);

        JButton btnpoblacions = new JButton("poblacions");
        btnpoblacions.addActionListener(e -> {
            tablasrefactorizado.menuTablas("poblacion","poblacions");
            frameMenu.setVisible(false);
        });
        panel.add(btnpoblacions);


        frameMenu.add(panel);
        frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMenu.setVisible(true);



    }
}
