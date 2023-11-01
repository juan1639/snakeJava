package snakeJon2;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Principal extends JFrame {

    public Principal() {
        
        inicializa();
    }
    
    private void inicializa() {
        
        add(new Tablero());
               
        setResizable(false);
        pack();
        
        setTitle("SnakeJon v2");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame principal = new Principal();
            principal.setVisible(true);
        });
    }
}
