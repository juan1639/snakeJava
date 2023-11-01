package snakeJon2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

// ===============================================================
public class Tablero extends JPanel implements ActionListener {

    private final int resX = 600;
    private final int resY = 600;
    private final int tileX = 40;
    private final int tileY = 40;
    private final int filas = (int) (resY / tileY);
    private final int columnas = (int) (resX / tileX);

    private final int FPS = 140;

    private final int MAX_LONGITUD_SNAKE = 999;

    private final int x[] = new int[MAX_LONGITUD_SNAKE];
    private final int y[] = new int[MAX_LONGITUD_SNAKE];

    private int longitudSnake;

    private int manzanaX;
    private int manzanaY;

    private boolean leDirection = false;
    private boolean riDirection = true;
    private boolean upDirection = false;
    private boolean doDirection = false;
    private boolean enJuego = true;
    private int marcador;

    private Timer timer;
    private Image segmento;
    private Image manzana;
    private Image cabeza;

    // --------------------------------------------------
    public Tablero() {
        
        inicializa();
    }
    
    private void inicializa() {

        addKeyListener(new TAdapter());
        setBackground(Color.gray);
        setFocusable(true);

        setPreferredSize(new Dimension(resX, resY));
        loadImages();
        comenzar();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("seg_snkVerde.png");
        segmento = iid.getImage();

        //BufferedImage manzana = ImageIO.read(new File(path, "bloque_azul.png"));

        ImageIcon iia = new ImageIcon("appleSnake.png");
        manzana = iia.getImage();

        ImageIcon iih = new ImageIcon("seg_snkVerde.png");
        cabeza = iih.getImage();
    }

    private void comenzar() {

        int xInicial = (int) (columnas / 2);
        int yInicial = (int) (filas / 2);

        marcador = 0;
        longitudSnake = 2;

        for (int i = 0; i < longitudSnake; i++) {
            x[i] = xInicial - i;
            y[i] = yInicial;
        }
        
        nueva_manzana();

        timer = new Timer(FPS, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        renderiza(g);
    }
    
    private void renderiza(Graphics g) {
        
        if (enJuego) {

            // for (int i = 0; i < filas; i ++) {
            //     for (int ii = 0; ii < columnas; ii ++) {

            //         g.setColor(Color.white);
            //         g.drawRect(ii * tileX, i * tileY, tileX, tileY);
            //     }
            // }

            g.drawImage(manzana, manzanaX * tileX, manzanaY * tileY, this);

            for (int i = 0; i < longitudSnake; i++) {

                if (i == 0) {
                    g.drawImage(cabeza, x[i] * tileX, y[i] * tileY, this);
                } else {
                    g.drawImage(segmento, x[i] * tileX, y[i] * tileY, this);
                }
            }

            textos((int) (resX / 20), "Score: " + marcador, 3, g);

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {

        textos((int) (resX / 10), "Game Over", 1, g);
        textos((int) (resX / 30), "Pulse SPACE para jugar otra partida...", 2, g);
    }

    private void textos(int size, String texto, int idTxt, Graphics g) {

        int fontSize = size;
        String msg = texto;

        Font fuente = new Font("Helvetica", Font.BOLD, fontSize);
        FontMetrics metr = getFontMetrics(fuente);

        g.setFont(fuente);

        if (idTxt == 1) {
            g.setColor(Color.orange);
            g.drawString(msg, (resX - metr.stringWidth(msg)) / 2, resY / 2);

        } else if (idTxt == 2) {
            g.setColor(Color.white);
            g.drawString(msg, (resX - metr.stringWidth(msg)) / 2, (int) (resY / 1.2));

        } else if (idTxt == 3) {
            g.setColor(Color.yellow);
            g.drawString(msg, (resX - metr.stringWidth(msg)) / 2, 30);
        }
    }

    private void check_colisionManzana() {

        if ((x[0] == manzanaX) && (y[0] == manzanaY)) {

            longitudSnake++;
            marcador ++;
            nueva_manzana();
        }
    }

    private void update_snake() {

        for (int i = longitudSnake; i > 0; i--) {
            x[i] = x[(i - 1)];
            y[i] = y[(i - 1)];
        }

        if (leDirection) {
            x[0] --;
        }

        if (riDirection) {
            x[0] ++;
        }

        if (upDirection) {
            y[0] --;
        }

        if (doDirection) {
            y[0] ++;
        }
    }

    private void check_colisiones() {

        for (int i = longitudSnake; i > 0; i--) {

            if ((i > 2) && (x[0] == x[i]) && (y[0] == y[i])) {
                enJuego = false;
            }
        }

        if (y[0] >= filas) {
            enJuego = false;
        }

        if (y[0] < 0) {
            enJuego = false;
        }

        if (x[0] >= columnas) {
            enJuego = false;
        }

        if (x[0] < 0) {
            enJuego = false;
        }
        
        if (!enJuego) {
            timer.stop();
        }
    }

    private void nueva_manzana() {

        manzanaX = (int) (Math.random() * columnas);
        manzanaY = (int) (Math.random() * filas);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (enJuego) {

            check_colisionManzana();
            check_colisiones();
            update_snake();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (enJuego) {

                if ((key == KeyEvent.VK_LEFT) && (!riDirection)) {
                    leDirection = true;
                    upDirection = false;
                    doDirection = false;
                }

                if ((key == KeyEvent.VK_RIGHT) && (!leDirection)) {
                    riDirection = true;
                    upDirection = false;
                    doDirection = false;
                }

                if ((key == KeyEvent.VK_UP) && (!doDirection)) {
                    upDirection = true;
                    riDirection = false;
                    leDirection = false;
                }

                if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                    doDirection = true;
                    riDirection = false;
                    leDirection = false;
                }
            }

            if (!enJuego) {

                if (key == KeyEvent.VK_SPACE) {
                    System.out.println("Rejugar");
                    enJuego = true;
                    comenzar();
                }
            }
        }
    }
}
