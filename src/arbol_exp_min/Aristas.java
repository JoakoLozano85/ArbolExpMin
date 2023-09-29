
package arbol_exp_min;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

/**
 *
 * @author joako
 */
public class Aristas {
    private Nodos n1, n2, nodo;
    private String nombreA;
    private int peso;
    private String padres[] = new String[100];

    public Aristas(Nodos n1, Nodos n2, int peso) {
        this.n1 = n1;
        this.n2 = n2;
        this.peso = peso;
        this.nombreA = n1.getNombre() + n2.getNombre();
  
    }
    public void pintarArista (Graphics g){ //(Graphics2D g)
        Graphics2D graphics = (Graphics2D) g;
        // http://swing-facil.blogspot.com/2011/12/renderinghints-renderizados-y.html
//        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); //REBDERIZADO
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //SUAVISADO
//        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //INTERPOLACIÓN
        graphics.setColor(Color.GRAY);
        
        graphics.setStroke(new BasicStroke(2)); // Graphics2D grosor de línea
        graphics.drawLine(this.getN1().getX(), this.getN1().getY(), this.getN2().getX(), this.getN2().getY());
        
        graphics.setFont( new Font( "Tahoma", Font.BOLD, 13 ) );
        graphics.setColor(Color.BLACK);
        graphics.drawString(peso + "", (this.getN1().getX() + this.getN2().getX()) / 2, (this.getN1().getY() + this.getN2().getY()) / 2);
    }
    
    public void repintarArista (Graphics g){ //(Graphics2D g)
        Graphics2D graphics = (Graphics2D) g;
        graphics.setColor(Color.RED);
        graphics.setStroke(new BasicStroke(2)); // Graphics2D grosor de línea
        graphics.drawLine(this.getN1().getX(), this.getN1().getY(), this.getN2().getX(), this.getN2().getY());        
    }
    public void pintarArista2 (Graphics g){ //(Graphics2D g)
        Graphics2D graphics = (Graphics2D) g;
        // http://swing-facil.blogspot.com/2011/12/renderinghints-renderizados-y.html
//        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); //REBDERIZADO
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //SUAVISADO
//        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //INTERPOLACIÓN
        graphics.setColor(Color.GRAY);
        
        graphics.setStroke(new BasicStroke(2)); // Graphics2D grosor de línea
        graphics.drawLine(this.getN1().getX(), this.getN1().getY(), this.getN2().getX(), this.getN2().getY());
        
        graphics.setFont( new Font( "Tahoma", Font.BOLD, 13 ) );
        graphics.setColor(Color.RED);
        graphics.drawString(peso + "", (this.getN1().getX() + this.getN2().getX()) / 2, (this.getN1().getY() + this.getN2().getY()) / 2);
    }
    

    public Nodos getN1() {
        return n1;
    }

    public void setN1(Nodos n1) {
        this.n1 = n1;
    }

    public Nodos getN2() {
        return n2;
    }

    public void setN2(Nodos n2) {
        this.n2 = n2;
    }

    public String getNombreA() {
        return nombreA;
    }

    public void setNombreA(String nombreA) {
        this.nombreA = nombreA;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    
}
