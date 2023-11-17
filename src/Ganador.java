import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Ganador extends JPanel {

    private JLabel jLabelNameWinner;
    private JLabel jLabelPartidos;
    private JLabel jLabel2;
    private JList<String> jList1;
    private JScrollPane jScrollPane1;
    private JLabel jLabel3;
    private JButton jButtonVerPropuestas;
    private JLabel jLabelPartidoConMasCandidatos;

    private Candidato ganadorActual;  // Nueva variable para almacenar al ganador actual

    public Ganador(Candidato[] candidatos) {
        initComponents();
        mostrarResultadoElecciones(candidatos);
    }

    private void initComponents() {
        jLabelNameWinner = new JLabel();
        jLabelPartidos = new JLabel();
        jLabel2 = new JLabel();
        jList1 = new JList<>();
        jScrollPane1 = new JScrollPane(jList1);
        jLabel3 = new JLabel();
        jButtonVerPropuestas = new JButton("Ver Propuestas");
        jButtonVerPropuestas.setFont(new Font("Segoe UI", 2, 14));
        jButtonVerPropuestas.addActionListener(e -> {
            if (ganadorActual != null) {
                mostrarPropuestas(ganadorActual);
            }
        });
        jLabelPartidoConMasCandidatos = new JLabel();

        setMaximumSize(new Dimension(643, 337));
        setMinimumSize(new Dimension(643, 337));
        setPreferredSize(new Dimension(643, 337));

        jLabelNameWinner.setFont(new Font("Segoe UI", 2, 14));
        jLabelNameWinner.setText("El ganador de las elecciones es: ");

        jLabelPartidos.setFont(new Font("Segoe UI", 2, 14));
        jLabelPartidos.setText("Partido con más candidatos: ");

        jLabel2.setFont(new Font("Segoe UI", 2, 14));
        jLabel2.setText("Top 3 ciudades con menos candidatos inscritos:");

        jList1.setBackground(new Color(242, 242, 242));
        jList1.setBorder(null);
        jList1.setFont(new Font("Segoe UI", 1, 14));
        jScrollPane1.setViewportView(jList1);

        jLabel3.setIcon(new ImageIcon(getClass().getResource("2560x1600-Fondo-de-pantalla-HD-Full-HD-papel-tapiz-de-color-solido-papel-tapiz-de-color-solido.jpg")));
        jLabel3.setMaximumSize(new Dimension(210, 337));
        jLabel3.setMinimumSize(new Dimension(210, 337));
        jLabel3.setPreferredSize(new Dimension(210, 337));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabelPartidos, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabelNameWinner, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButtonVerPropuestas)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabelPartidoConMasCandidatos)))
                                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabelNameWinner)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelPartidos)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonVerPropuestas)
                                        .addComponent(jLabelPartidoConMasCandidatos))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    private void mostrarResultadoElecciones(Candidato[] candidatos) {
        Map<String, Integer> conteoCiudades = new HashMap<>();
        Map<Partido_p, Integer> conteoPartidos = new HashMap<>();  // Contador de candidatos por partido
        ArrayList<String> ciudades = new ArrayList<>();

        // Iterar sobre los candidatos
        for (Candidato c : candidatos) {
            String ciudadOrigen = c.getC_origen().toString();
            ciudades.add(ciudadOrigen);
            conteoCiudades.merge(ciudadOrigen, 1, Integer::sum);

            // Contar candidatos por partido
            conteoPartidos.merge(c.getPartido_politico(), 1, Integer::sum);
        }

        ArrayList<String> top3Ciudades = new ArrayList<>(conteoCiudades.keySet());
        top3Ciudades.sort(Comparator.comparingInt(conteoCiudades::get));
        top3Ciudades = new ArrayList<>(top3Ciudades.subList(0, Math.min(3, top3Ciudades.size())));

        DefaultListModel<String> ciudadesListModel = new DefaultListModel<>();
        for (String ciudad : top3Ciudades) {
            ciudadesListModel.addElement(ciudad + ": " + conteoCiudades.get(ciudad));
        }
        jList1.setModel(ciudadesListModel);

        Candidato ganador = encontrarGanador(candidatos);

        jLabelNameWinner.setText("El ganador de las elecciones es: " + ganador.getNombre() + "C.C:" + ganador.getCedula());


        // Mostrar propuestas solo si el ganador tiene propuestas
        if (ganador != null && ganador.getPromesas() != null && !ganador.getPromesas().isEmpty()) {
            jButtonVerPropuestas.setEnabled(true);
        } else {
            jButtonVerPropuestas.setEnabled(false);
        }

        // Encontrar el partido con más candidatos
        Partido_p partidoConMasCandidatos = encontrarPartidoConMasCandidatos(conteoPartidos);

        // Mostrar el resultado en el JLabel
        jLabelPartidos.setText("Partido con más candidatos: " + partidoConMasCandidatos.displayNameEnum());

        ganadorActual = ganador;  // Actualizar al ganador actual
    }

    private Partido_p encontrarPartidoConMasCandidatos(Map<Partido_p, Integer> conteoPartidos) {
        Partido_p partidoConMasCandidatos = null;
        int maxCandidatos = 0;

        for (Map.Entry<Partido_p, Integer> entry : conteoPartidos.entrySet()) {
            Partido_p partido = entry.getKey();
            int cantidadCandidatos = entry.getValue();

            if (cantidadCandidatos > maxCandidatos) {
                maxCandidatos = cantidadCandidatos;
                partidoConMasCandidatos = partido;
            }
        }

        return partidoConMasCandidatos;
    }

    private void mostrarPropuestas(Candidato candidato) {
        if (candidato != null) {
            List<String> propuestas = candidato.getPromesas();
            StringBuilder propuestasStr = new StringBuilder("Propuestas del candidato " + candidato.getNombre() + ":\n");
            for (String propuesta : propuestas) {
                propuestasStr.append("- ").append(propuesta).append("\n");
            }

            JOptionPane.showMessageDialog(this, propuestasStr.toString());
        } else {
            JOptionPane.showMessageDialog(this, "No hay un ganador para mostrar propuestas.");
        }
    }

    private Candidato encontrarGanador(Candidato[] candidatos) {
        Arrays.sort(candidatos, Comparator.comparingInt(Candidato::getN_votos).reversed());
        return candidatos.length > 0 ? candidatos[0] : null;
    }
}
