import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;

import java.util.Arrays;
import java.util.Collections;

public class GameOfTheYear extends JFrame {

	private JPanel contentPane;
	private JTextField txtPuntuacion;
	private JTextField txtRecord;
	private JButton[] casillas;
	private boolean partidaIniciada = false;
	private int maximaPuntuacion = 0;
	private int puntuacionActual = 0;
	private String[] simbolos = { "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O" };
	private String[] copiaSimbolos;

	/**
	 * Iniciar aplicación
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameOfTheYear frame = new GameOfTheYear();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JButton crearBotonIniciarPartida() {

		JButton btnReiniciar = new JButton("Iniciar Partida");

		// Configurar el botón
		btnReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Cambiar el texto del botón
				if (!partidaIniciada) {
					partidaIniciada = true;
					btnReiniciar.setText("Reiniciar Pantalla");
				}

				// Habilitar las casillas
				for (int i = 0; i < casillas.length; i++) {
					casillas[i].setEnabled(true);
					casillas[i].setText("?");
				}

				// Crear una copia de la lista simbolos
				copiaSimbolos = Arrays.copyOf(simbolos, simbolos.length);

				// Añadir de 1 a 3 "W"
				int numWs = (int) (Math.random() * 3) + 1;
				for (int i = 0; i < numWs; i++) {
					copiaSimbolos[(int) (Math.random() * (copiaSimbolos.length - 2)) + 2] = "W";
				}

				// Mezclar los elementos de la lista de símbolos
				Collections.shuffle(Arrays.asList(copiaSimbolos));

			}
		});

		return btnReiniciar;
	}

	public void comprobarCasilla(String texto) {

		if (texto.equalsIgnoreCase("O")) {
			puntuacionActual++;
			txtPuntuacion.setText(Integer.toString(puntuacionActual));

		} else if (texto.equalsIgnoreCase("W")) {
			puntuacionActual *= 2;
			txtPuntuacion.setText(Integer.toString(puntuacionActual));

		} else {

			// Fin de la partida
			for (int i = 0; i < casillas.length; i++) {
				casillas[i].setEnabled(false);
				partidaIniciada = false;

			}

			if (Integer.parseInt(txtPuntuacion.getText()) > Integer.parseInt(txtRecord.getText())) {
				txtRecord.setText(txtPuntuacion.getText());
			}

			JFrame frame = new JFrame("Puntuación actual");
			frame.setSize(300, 200);

			JLabel label = new JLabel("La puntuación obtenida es: " + puntuacionActual);
			label.setHorizontalAlignment(JLabel.CENTER);

			frame.add(label);
			frame.setVisible(true);

			if (puntuacionActual > maximaPuntuacion) {
				maximaPuntuacion = puntuacionActual;
				txtRecord.setText(Integer.toString(maximaPuntuacion));
				guardarRecordEnArchivo(maximaPuntuacion); // Llamada al método para guardar el récord en el archivo
			}

			puntuacionActual = 0;
			txtPuntuacion.setText(Integer.toString(puntuacionActual));

		}

	}

	private JButton crearBotonSalir() {
		JButton btnSalir = new JButton("Salir");
		// Configurar el botón
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		return btnSalir;
	}

	public void guardarRecordEnArchivo(int record) {
		try {
			FileWriter writer = new FileWriter("record.txt");
			writer.write(Integer.toString(record));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int leerRecordDesdeArchivo() {
		int record = 0;
		try {
			FileReader reader = new FileReader("record.txt");
			BufferedReader br = new BufferedReader(reader);
			String recordString = br.readLine();
			if (recordString != null) {
				record = Integer.parseInt(recordString);
			}
			br.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return record;
	}
	
	/**
	 * Create the frame.
	 */
	public GameOfTheYear() {

		// Llamada al método para leer la puntuación máxima desde el archivo
		maximaPuntuacion = leerRecordDesdeArchivo();

		setTitle("Game Of The Year GOTY");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 454, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelBotones = new JPanel();

		// No quiero bordes
		panelBotones.setBorder(null);
		panelBotones.setBounds(10, 10, 189, 140);
		contentPane.add(panelBotones);
		panelBotones.setLayout(new GridLayout(4, 4, 5, 5));

		// Crear los botones de las casillas
		casillas = new JButton[16];
		for (int i = 0; i < casillas.length; i++) {
			casillas[i] = new JButton("?");
			casillas[i].setFont(new Font("Arial", Font.BOLD, 9));
			casillas[i].setEnabled(false);

			casillas[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Obtener el botón pulsado
					JButton boton = (JButton) e.getSource();

					// Obtener el índice del botón pulsado
					int indiceBoton = Arrays.asList(casillas).indexOf(boton);

					// Obtener el valor correspondiente del array copiaSimbolos
					String textoDelArray = copiaSimbolos[indiceBoton];

					boton.setEnabled(false);
					boton.setText(textoDelArray);
					comprobarCasilla(textoDelArray);
				}
			});

			panelBotones.add(casillas[i]);
		}

		JLabel record = new JLabel("R\u00E9cord:");
		record.setBounds(209, 16, 109, 13);
		contentPane.add(record);
		record.setFont(new Font("Tahoma", Font.BOLD, 10));

		JPanel botonesControl = new JPanel();
		botonesControl.setBounds(200, 106, 230, 44);
		contentPane.add(botonesControl);
		botonesControl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		botonesControl.add(crearBotonSalir());
		botonesControl.add(crearBotonIniciarPartida());

		JLabel lblPuntuacion = new JLabel("Puntuaci\u00F3n:");
		lblPuntuacion.setBounds(209, 39, 221, 13);
		contentPane.add(lblPuntuacion);
		lblPuntuacion.setHorizontalAlignment(SwingConstants.LEFT);

		txtPuntuacion = new JTextField();
		txtPuntuacion.setBounds(209, 56, 221, 19);

		txtPuntuacion.setEditable(false);
		txtPuntuacion.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPuntuacion.setColumns(10);
		contentPane.add(txtPuntuacion);

		txtRecord = new JTextField();
		txtRecord.setHorizontalAlignment(SwingConstants.RIGHT);
		txtRecord.setFont(new Font("Tahoma", Font.BOLD, 10));
		txtRecord.setEditable(false);
		txtRecord.setBounds(321, 13, 109, 19);
		txtRecord.setText(Integer.toString(maximaPuntuacion));

		contentPane.add(txtRecord);
		txtRecord.setColumns(10);

		// Establecer el valor del JTextField de la puntuación máxima
		txtRecord.setText(Integer.toString(maximaPuntuacion));

	}

}
