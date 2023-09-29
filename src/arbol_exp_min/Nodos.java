
package arbol_exp_min;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author joako
 */
public class Nodos {
    private int x;
    private int y;
    private int raiz;
    private String nombre;
    static final int d = 20;
    
    public Nodos(int x, int y, int i, int raiz){
        this.x = x;
        this.y = y;
        this.nombre = Character.toString((char)(65+i));
        this.raiz = 1 + i;
    }
    public void pintarNodo (Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // alta definición
//        graphics.setColor(Color.PINK); // asigna colores de la paleta básica
        Color COLOR_NODO = new Color(0, 196, 255);
        graphics.setColor(COLOR_NODO); // asigna colores personalizados
        graphics.fillOval(this.getX() - getD()/2, this.getY() - getD()/2, getD(), getD()); // relleno del círculo
        
//        graphics.setColor(Color.RED);
//        graphics.drawOval(this.getX() - getD()/2, this.getY() - getD()/2, getD(), getD()); // *** contorno del círculo
        //drawOval(int x, int y, int width, int height)
        //La esquina superior izquierda esta en (x,y), y se especifica la anchura (width) y la altura (height)
        //Si la anchura y la altura son iguales, se dibuja la circunferencia.
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawString(this.getNombre(), this.getX() - getD()/2, this.getY() - getD()/2); // *** nombre del vértice
        //drawString(String str, int x, int y)
        //Dibuja la string str, empieza en el punto (x,y). x define la posición de la izquierda de la String. y define la altura para la línea base 
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getNombre() {
        return nombre;
    }

    public static int getD() {
        return d;
    }
    public int getRaiz(){
        return raiz;
    }
    
}
