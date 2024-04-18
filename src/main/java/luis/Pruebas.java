package luis;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents a GUI application for a race game.
 * It extends JFrame and implements Runnable.
 */
public class Pruebas extends JFrame implements Runnable{
    private JButton btnIniciar;
    private JLabel lblGanador;
    private JProgressBar [] pBar;
    private Thread [] hilos;

    /*
     * The main method that starts the application.
     * @param args command line arguments
     */

    public static void main(String[] args) {
        new Pruebas();
    }

    /*
     * Constructor for the Pruebas class.
     * It sets up the JFrame and initializes the GUI components.
     */
    public Pruebas(){
        super("Carrera de tortugas");
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        init();
    }

    /*
     * This method initializes the GUI components.
     *
     */

    private void init() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int borderGap = 10;
        int x = 5;
        btnIniciar = new JButton("Iniciar carrera");
        pBar = new JProgressBar[x];
        hilos = new Thread[x];
        String name ="Tortuga ";

        for (int i = 0; i < pBar.length; i++) {
            c.gridy = i;
            c.gridx = 0;
            JLabel aux = new JLabel(name + (i + 1)); // Se crea un label con el nombre del caballo
            aux.setBorder(BorderFactory.createEmptyBorder(borderGap, borderGap, borderGap, borderGap));
            panelCentral.add(aux, c);

            c.gridx = 1;
            pBar[i] = new JProgressBar();
            pBar[i].setMaximum(100);
            pBar[i].setValue(0);
            pBar[i].setStringPainted(true);
            pBar[i].setForeground(Color.RED);
            pBar[i].setBorder(BorderFactory.createEmptyBorder(borderGap, borderGap, borderGap, borderGap));
            panelCentral.add(pBar[i], c);
            hilos[i] = new Thread(new Tortuga(pBar[i], i+1));
        }
        lblGanador = new JLabel("El ganador es: ");
        c.gridx = 0;
        c.gridy = pBar.length + 1;

        panelCentral.add(lblGanador, c);
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnIniciar) {
                    btnIniciar.setText("Carrera Iniciada");
                    btnIniciar.setEnabled(false);
                    for (int i = 0; i < hilos.length; i++) {
                        hilos[i].start();
                    }
                }
            }
        });
        contentPane.add(panelCentral, BorderLayout.CENTER);
        add(btnIniciar, BorderLayout.SOUTH);
    }

    /**
     * The run method for the Runnable interface.
     * Currently, it does nothing.
     */
    @Override
    public void run() {
    }

    /**
     * This class represents a runnable horse for the race game.
     * It implements Runnable.
     */


    class Tortuga implements Runnable {
        private JProgressBar barra;
        private int num;
        /**
         * Constructor for the Caballo class.
         * @param barra the progress bar representing the horse
         * @param numTortuga the number of the tortuga
         */

        public Tortuga(JProgressBar barra, int numTortuga) {
            this.barra = barra;
            this.num = numTortuga;
        }

        /**
         * The run method for the Runnable interface.
         * It updates the progress bar and checks for a winner.
         */

        @Override
        public void run() {
            while (barra.getValue() < barra.getMaximum()) {
                try {
                    Thread.sleep(1000);
                    barra.setValue(barra.getValue() + (int) (Math.random() * 20));
                    if (barra.getValue() >= barra.getMaximum()) {
                        lblGanador.setText(lblGanador.getText()+"Tortuga "+num);
                        for (int i = 0; i < hilos.length ; i++) {
                            hilos[i].interrupt();
                        }
                        break;
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}