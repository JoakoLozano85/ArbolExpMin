/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbol_exp_min;

import static arbol_exp_min.Kruskas.aristas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author joako
 */
public class Kruskas extends javax.swing.JFrame {
    static final int t = 20;  // tamaño  
    static int peso = 0;
    static ArrayList<Nodos> nodos = new ArrayList();
    static ArrayList<Aristas> aristas = new ArrayList();
    static ArrayList<Aristas> min = new ArrayList();
    static int[][] matrizAd = new int[t][t]; // cambia tamaño
    static int[][] matrizAr = new int[t][t];
    static int[][] matrizIn = new int[t][t*(t-1)/2]; // posibles aristas
    public static int [] padre = new int[50]; //Variable padre de los nodos
    static int c = 0;
    static int arco =0, kk = 0;
    static int pesoFin = 0, pesoArbol = 0;
    static String sth= "";
    
    Nodos nodoSelec1 = null;
    
    Nodos nodoSelec2 = null;
    
    
    public Kruskas() {
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource ("/imagenes/grafo.png")).getImage());
    }

    
    static String definicion(ArrayList<Nodos> nodo, ArrayList<Aristas> arista) {
        String cadena = "V = { ";
        if (!nodo.isEmpty()) {
            for (int i = 0; i < nodo.size()-1; i++) { // recorre todos los nodos sin el último
                cadena += nodo.get(i).getNombre() + ", ";
            }
            cadena += nodo.get(nodo.size()-1).getNombre(); // concadena el último nodo
        }
        cadena += " }\nA = { "; // cierra el conjunto V } y abre el conjungo A={ en otra línea
        if (!arista.isEmpty()) {
            for (int i = 0; i < arista.size()-1; i++) { // recorre todos las aristas sin la última
                cadena += arista.get(i).getNombreA() + ", " ;  
            }
            cadena += arista.get(arista.size()-1).getNombreA();// concadena la última arista
        }
        cadena += " }";
        return cadena;
    }
    static String mostrarAd(int m[][]) {
        String cadena = "";
        char filas = 65;
        char columnas = 65;
        for (int k = 65; k < (65 + nodos.size()); k++) { //concadena títulos de columnas (A, B, ...)
            cadena += "\t" + columnas;  // 3 espacios entre columna o \t
            columnas++;
        }
        cadena += "\n";
        for (int i = 0; i < nodos.size(); i++) {
            cadena += filas + "\t";  //concadena los títulos de las filas
            for (int j = 0; j < nodos.size(); j++) {
                cadena += String.valueOf(m[i][j]) + "\t"; //contadena las relaciones (0,1) en String
                
            }
            filas++;
            cadena = cadena + "\n";
        }
        return (cadena);
    }
    // se encadena texto con las colmunas y filas + contenido de la matrizAd de adyacencia
    static String mostrarAr(int m[][]) {
        String cadena = "";
        char filas = 65;
        char columnas = 65;
        for (int k = 65; k < (65 + nodos.size()); k++) { //concadena títulos de columnas (A, B, ...)
            cadena += "\t" + columnas;  // 3 espacios entre columna o \t
            columnas++;
        }
        cadena += "\n";
        for (int i = 0; i < nodos.size(); i++) {
            cadena += filas + "\t";  //concadena los títulos de las filas
            for (int j = 0; j < nodos.size(); j++) {
                cadena += String.valueOf(m[i][j]) + "\t"; //contadena las relaciones (0,1) en String
                
            }
            filas++;
            cadena = cadena + "\n";
        }
        return (cadena);
    }
    static int buscaIndiceNodo(ArrayList<Nodos> nodo, String h) {
        for (int i = 0; i < nodos.size(); i++) {
            //equalsIgnoreCase() Compara dos strings para ver si son iguales
            //ignorando las diferencias entre mayúsculas y minúsculas
            if (nodo.get(i).getNombre().equalsIgnoreCase(h)) {
                return i;
            }
        }
        return -1;
    }
    static Nodos buscaNodo(ArrayList<Nodos> nodo, int xx, int yy) {
        for (int i = 0; i < nodos.size(); i++) {
            int x = nodo.get(i).getX();
            int y = nodo.get(i).getY();
            int radio = nodo.get(i).getD() + 7;
            if (xx > (x - radio) && xx < (x + radio) && yy > (y - radio) && yy < (y + radio)) {
                return nodo.get(i);
            }
        }
        return null;
    }
    boolean buscarArista(Nodos n1, Nodos n2) {
        for (Aristas arista: aristas) {
            if (arista != null) {
                boolean a = arista.getN1().equals(n1), b = arista.getN2().equals(n2);
                boolean c = arista.getN2().equals(n1), d = arista.getN1().equals(n2);
                if ((a && b) || (c && d)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void combinaciones(String[] A, int i, int k,Set<List<String>> arreg,List<String> f){
		if (A.length == 0 || k > A.length) {
			return;
		}
		if (k == 0) {
			arreg.add(new ArrayList<>(f));
			return;
		}
		for (int j = i; j < A.length; j++)
		{
			f.add(A[j]);
			combinaciones(A, j + 1, k - 1, arreg, f);
			f.remove(f.size() - 1);
		}
	}
    public static Set<List<String>> combi(String[] A, int k){
		Set<List<String>> arreg = new HashSet<>();
		combinaciones(A, 0, k, arreg, new ArrayList<>());
		return arreg;
	}
    
    //Encontrar el padre raiz
    public static int encontrar(int x){
        if(padre[x] == x){
            return x;
        }
        return encontrar(padre[x]);
    }
    //Unir los nodos que tienen el mismo padre
    public static void unir(int x, int y){
        int fx = x;
        int fy = y;
        padre[fx] = fy;
    }
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpanel_grafo = new javax.swing.JPanel();
        jpanel_arbol = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_ad = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_arbol = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_combi = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txt_defi = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpanel_grafo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Grafo"));
        jpanel_grafo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpanel_grafoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpanel_grafoLayout = new javax.swing.GroupLayout(jpanel_grafo);
        jpanel_grafo.setLayout(jpanel_grafoLayout);
        jpanel_grafoLayout.setHorizontalGroup(
            jpanel_grafoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 304, Short.MAX_VALUE)
        );
        jpanel_grafoLayout.setVerticalGroup(
            jpanel_grafoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 305, Short.MAX_VALUE)
        );

        jpanel_arbol.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Arbol de expansion minima"));

        javax.swing.GroupLayout jpanel_arbolLayout = new javax.swing.GroupLayout(jpanel_arbol);
        jpanel_arbol.setLayout(jpanel_arbolLayout);
        jpanel_arbolLayout.setHorizontalGroup(
            jpanel_arbolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 304, Short.MAX_VALUE)
        );
        jpanel_arbolLayout.setVerticalGroup(
            jpanel_arbolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Matriz de adyacencia"));

        txt_ad.setColumns(20);
        txt_ad.setRows(5);
        jScrollPane1.setViewportView(txt_ad);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Matriz del arbol"));

        txt_arbol.setColumns(20);
        txt_arbol.setRows(5);
        jScrollPane2.setViewportView(txt_arbol);

        jButton1.setText("MOSTRAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Peso"));

        txt_combi.setColumns(20);
        txt_combi.setRows(5);
        jScrollPane3.setViewportView(txt_combi);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Instrucciones"));

        jLabel1.setText("ARTISTAS: Agrega artistas haiendo clic derecho sobre el par de nodos que va a unir");

        jLabel2.setText("NODOS: Agrega nodos haciendo clic sobre el panel \"grafo\"");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(15, 15, 15))
        );

        jButton2.setText("NUEVO GRAFO");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("SALIR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Definicion"));

        txt_defi.setColumns(20);
        txt_defi.setRows(5);
        jScrollPane4.setViewportView(txt_defi);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton3))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpanel_grafo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jpanel_arbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jpanel_grafo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpanel_arbol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(149, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jpanel_grafoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpanel_grafoMouseClicked
        // TODO add your handling code here:
        String str= "";
        int x = evt.getX();
        int y = evt.getY();
        if (SwingUtilities.isLeftMouseButton(evt)) {
                if (buscaNodo(nodos, x, y) == null) {
                    Nodos nodo = new Nodos(x, y, nodos.size(), nodos.size());
                    nodo.pintarNodo(jpanel_grafo.getGraphics());
                    nodos.add(nodo);
                    txt_arbol.setText(mostrarAr(matrizAr));
                    txt_arbol.setEditable(false);
                    txt_ad.setText(mostrarAd(matrizAd));
                    txt_ad.setEditable(false);
                    txt_defi.setText(definicion(nodos, aristas));
                    txt_defi.setEditable(false);
                } else JOptionPane.showMessageDialog(null, "Ya existe un nodo en esta área");
        }
        if (SwingUtilities.isRightMouseButton(evt)) {
            Nodos n = buscaNodo(nodos, x, y);
            if (n != null) {
                if (nodoSelec1 == null) {
                    nodoSelec1 = n;
                } else if (nodoSelec2 == null && !n.equals(nodoSelec1)) {
                    nodoSelec2 = n;
                    if (!buscarArista(nodoSelec1, nodoSelec2)) {
//                        try {
                            int xx = buscaIndiceNodo(nodos, nodoSelec1.getNombre());
                            int yy = buscaIndiceNodo(nodos, nodoSelec2.getNombre());
                            
                            
                            
                            try{
                                peso = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el peso"));
                            }catch(Exception e){
                                JOptionPane.showMessageDialog(null, "Ingrese un peso valido");
                            }
                            if(peso<=0){
                                JOptionPane.showMessageDialog(this, "El peso no puede ser menor o igual a 0");
                                peso = 0;
                            }else{
                                matrizAd[xx][yy] = 1;
                                matrizAd[yy][xx] = 1;
                                matrizAr[xx][yy] = peso;
                                matrizAr[yy][xx] = peso;
                                pesoFin = pesoFin + peso;
                                Aristas arista = new Aristas(nodoSelec1, nodoSelec2, peso);
                                aristas.add(arista);
                                arista.pintarArista(jpanel_grafo.getGraphics()); // Graphics
                                str += "Peso del arbol = { ";
                                str += String.valueOf(pesoFin);
                                str += " }";
                                str += "\n";
                            }
                            
                            
                            
                    } else JOptionPane.showMessageDialog(null, "Ya existe una arista entre los nodos seleccionados");
                    
                    txt_ad.setText(mostrarAd(matrizAd));
                    txt_arbol.setText(mostrarAr(matrizAr));
                    txt_arbol.setEditable(false);
                    txt_ad.setEditable(false);
                    txt_combi.setText(str);
                    
                    txt_defi.setText(definicion(nodos, aristas));
                    txt_defi.setEditable(false);
                    nodoSelec1 = null;
                    nodoSelec2 = null;
                }
            }
        }
    }//GEN-LAST:event_jpanel_grafoMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int nodoA=0;
        int nodoB=0;
        int ame = 0;
        int peso = 0;
        Aristas arista;
        ArrayList<Aristas> copiaarista = (ArrayList<Aristas>)aristas.clone();
        ArrayList<Nodos> copianodo = (ArrayList<Nodos>)nodos.clone();
        
        //Para controlar que los nodos no se pinten muchas veces
        while(kk < copianodo.size()){
            copianodo.get(kk).pintarNodo(jpanel_arbol.getGraphics());
            kk++;
        }
        //inicializando el vector padre
        for(int i = 0; i< copianodo.size(); i++){
              padre[i] = i;
           }
        
        //Ordenar de menor a mayor por peso
       Collections.sort(copiaarista, new Comparator<Aristas>(){
           @Override
           public int compare(Aristas p1, Aristas p2){
               return p1.getPeso() - p2.getPeso();//Si peso 1 es mayor que peso 2, dara positivo, si son iguales dara 0 y si es negativo el peso 2 es mayor
           }
        });
       
       //Kruskas
       while(arco<copianodo.size()-1 && ame<copiaarista.size()){
           nodoA = copiaarista.get(ame).getN1().getRaiz();
           nodoB = copiaarista.get(ame).getN2().getRaiz();
           if (encontrar(nodoA) != encontrar(nodoB)) {
               unir(nodoA, nodoB);
               arista = new Aristas(copiaarista.get(ame).getN1(), copiaarista.get(ame).getN2(), copiaarista.get(ame).getPeso());
               min.add(arista);
           }
           ame++;
           
       }
       //Cuando el arbol este completo
       if (arco >=copianodo.size() - 1) {
               JOptionPane.showMessageDialog(null, "Arbol de expansion minima completo");
               c = 0;
           }
        
       //Pintar el grafo
        min.get(c).pintarArista2(jpanel_arbol.getGraphics());
        //min.get(c).repintarArista(jpanel_grafo.getGraphics());
        
        
        if(arco < copianodo.size()-1){
            pesoArbol = pesoArbol + min.get(arco).getPeso();
            sth += "\nPeso del arbol de expansion minima = { ";
            sth += pesoArbol;
            sth += " }";
            sth += "\n";
        }
        txt_combi.setText(txt_combi.getText()+sth);
        c++;
        arco++;
        sth = "";
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jpanel_grafo.removeAll();
        jpanel_grafo.repaint();
        
        jpanel_arbol.removeAll();
        jpanel_arbol.repaint();
        txt_ad.setText("");
        txt_arbol.setText("");
        txt_defi.setText("");
        txt_combi.setText("");
        nodos = new ArrayList();
        aristas = new ArrayList();
        min = new ArrayList();
        matrizAd = new int[t][t];
        matrizAr = new int[t][t];
        matrizIn = new int[t][t*(t-1)/2];
        padre = new int[50];
        c = 0;
        arco =0;
        kk = 0;
        pesoFin = 0;
        pesoArbol = 0;
        sth= "";
        nodoSelec1 = null;
        nodoSelec2 = null;
        peso = 0;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Kruskas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Kruskas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Kruskas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Kruskas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Kruskas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel jpanel_arbol;
    private javax.swing.JPanel jpanel_grafo;
    private javax.swing.JTextArea txt_ad;
    private javax.swing.JTextArea txt_arbol;
    private javax.swing.JTextArea txt_combi;
    private javax.swing.JTextArea txt_defi;
    // End of variables declaration//GEN-END:variables
}
