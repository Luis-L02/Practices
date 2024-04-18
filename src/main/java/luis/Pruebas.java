package luis;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pruebas extends JFrame implements Runnable{
    private JButton btnIniciar, btnReiniciar;
    private JLabel lblGanador;
    private JProgressBar [] pBar;
    private Thread [] hilos;

    public static void main(String[] args) {
        new Pruebas();
    }
    public Pruebas(){
        super("Carrera de tortugas");
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        init();
    }

    private void init() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel panelBotones = new JPanel(new FlowLayout());
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int borderGap = 10;
        int x = 5;
        btnIniciar = new JButton("Iniciar carrera");
        btnReiniciar = new JButton("Reiniciar carrera");
        pBar = new JProgressBar[x];
        hilos = new Thread[x];
        String name ="Tortuga ";

        for (int i = 0; i < pBar.length; i++) {
            c.gridy = i;
            c.gridx = 0;
            JLabel aux = new JLabel(name + (i + 1));
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
        lblGanador = new JLabel("Los/El ganador(es): ");
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

        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == btnReiniciar) {
                    for (int i = 0; i < pBar.length; i++) {
                        pBar[i].setValue(0);

                        if (hilos[i].isAlive()) {
                            hilos[i].interrupt();
                        }

                        hilos[i] = new Thread(new Tortuga(pBar[i], i+1));
                    }

                    lblGanador.setText("Los/El ganador(es): ");
                    btnIniciar.setEnabled(true);
                }
            }
        });

        panelBotones.add(btnIniciar);
        panelBotones.add(btnReiniciar);
        contentPane.add(panelCentral, BorderLayout.CENTER);
        contentPane.add(panelBotones, BorderLayout.SOUTH);
    }

    @Override
    public void run() {
    }

    class Tortuga implements Runnable {
        private JProgressBar barra;
        private int num;

        public Tortuga(JProgressBar barra, int numTortuga) {
            this.barra = barra;
            this.num = numTortuga;
        }
        //
        @Override
        public void run() {
            while (barra.getValue() < barra.getMaximum()) {
                try {
                    Thread.sleep(200);

                    barra.setValue(barra.getValue() + (int) (Math.random() * 20));

                    System.out.println("Tortuga #"+num+" Avanzo: "+barra.getValue());
                    if (barra.getValue() >= barra.getMaximum()) {
                        lblGanador.setText(lblGanador.getText() + "Tortuga "+ num+" ");
                        System.out.println("Tortuga #"+num+" Ganó");
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