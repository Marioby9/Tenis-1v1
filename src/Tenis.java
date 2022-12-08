

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;


public class Tenis extends JFrame {
		Juego juego;
		
		int ancho = 700;
		int alto =400;
		
		int anchoCampo = ancho-28;
		int altoCampo =  alto-50;
		
		
		
	public static void main(String[] args) {
		
		new Tenis();
		

	}
	
	public Tenis() {
		setTitle("Tenis Mario");	
		
		//-----Ubicación-----
		
		setBounds(400, 200, ancho, alto);
		setResizable(false);
		
		juego = new Juego();
		
		getContentPane().add(juego);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	
		
		setVisible(true);
	}
	
	public class Juego extends JPanel implements KeyListener, ActionListener {
		
		
		private static final long serialVersionUID = 1L;

		private boolean game = false; //Nos dice si el juego está iniciado o no
		private boolean gameover = false; //PARA CUANDO ACABE LA PARTIDA
		
		private String direccion1 = "STOP"; //3 direcciones (STOP, UP Y DOWN). Para movimientos
		private String direccion2 = "STOP";
		
		private Timer timer;
		private int tiempo = 7;
		
		private int jugador1X = 655;
		private int jugador1Y = 140;
		private int jugador2X = 15;
		private int jugador2Y = 140;
		
		private int ballX = 340;
		private int ballY = 160;
		private int dirX = 6; //Valores de incremento para valocidad de la bola
		private int dirY = 6;
		private int punt1 = 0; //Declaramos las puntuaciones
		private int punt2 = 0;

		
		
		
		public Juego() {
			
			timer = new Timer(tiempo, this); //La imagen se recarga cada 7 milisegundos (cambiar en la variable).
			timer.start();
			setFocusable(true); //Para que se implemente bien el Actionlistener y KeyListener
			addKeyListener(this);
			
			
			
		}
		public void paint(Graphics g) {
			moveBall();
			moveJugador();
			
			super.paintComponent(g);
			
			juego.setBackground(Color.ORANGE);
			
			g.setColor(Color.BLACK); //Campo tennis
			g.fillRect(7, 7, anchoCampo, altoCampo); //Campo
			
			//Jugador derecha:
			g.setColor(Color.WHITE); 
			g.fillRect(jugador1X, jugador1Y, 15, 60); 
			
			//Jugador izquierda:
			g.setColor(Color.WHITE); 
			g.fillRect(jugador2X, jugador2Y, 15, 60);
			
			//Pelota:
			g.setColor(Color.YELLOW);
			g.fillOval(ballX, ballY, 20, 20);
			
			//PUNTOS:
			g.setColor(Color.GREEN);
			g.setFont(new Font("ARIAL", Font.BOLD, 13));
			g.drawString("PUNTOS: "+punt1, anchoCampo-75, 35);
			g.drawString("PUNTOS: "+punt2, 20, 35);
			
			//TEXTOS:
			if(game==false) {
				g.setColor(Color.GREEN);
				g.setFont(new Font(Font.SERIF, Font.BOLD, 20)); //SERIF ES DE LAS POCAS FUENTES QUE CONTIENE EL SIGNO DE PAUSA
				g.drawString("PAUSA⏸", 290, 35);
				g.setFont(new Font("ARIAL", Font.BOLD, 10));
				g.drawString("ESPACIO para reanudar", 275, 50);
			}
			
			if(punt1==10 || punt2==10) {   //Para que al llegar a 10, acabe el juego.
				gameover=true;
			}
			
			//GAMEOVER: PARA CUANDO ACABE LA PARTIDA
			if(gameover==true) { 
				game=false;
				g.setColor(Color.BLACK); //REPINTAMOS EL FONDO
				g.fillRect(7,7, anchoCampo, altoCampo); 
				g.setFont(new Font("Agency FB", Font.BOLD, 60));
				g.setColor(Color.GREEN);
				
					if(punt1==10) {
						g.drawString("HA GANADO EL JUGADOR 1", 75, 80);
						g.drawString(punt1+" - "+punt2, 300, 150);
						
					}
					else if(punt2==10) {
						g.drawString("HA GANADO EL JUGADOR 2", 75, 80);
						g.drawString(punt1+" - "+punt2, 300, 150);
					}
				g.setFont(new Font("Agency FB", Font.BOLD, 50));
				g.drawString("ESC = SALIR", 240, 280); 
				g.setFont(new Font("Agency FB", Font.BOLD, 15));
				g.drawString("MARIO MARTIN DEV", 15, 350);
			
				
				
				
				
				
			} //CIERRE GAMEOVER
			
		}
		
		public void moveBall() {
			if(game==true) {
				ballX += dirX;
				ballY += dirY;
				//PARA QUE LA BOLA REBOTE EN CADA "RAQUETA":
				if(ballX>=(jugador1X-15) && ballY>=jugador1Y &&ballY<=jugador1Y+60) { //15 es el ancho de cada jugador, por tanto es necesario que rebote 15 pixeles antes.
					dirX = -dirX; 
				}
				if(ballX<=(jugador2X+15) && ballY>=jugador2Y &&ballY<=jugador2Y+60) {
					dirX = -dirX;
				}
				
				
				
				if(ballX > anchoCampo) { //Punto para el jugador izquierdo. La bola vuelve al medio
					ballX = 340; 
					ballY = 160; 
					punt2++; 				//Sumamos punto al jugador izquierdo
					game=false;
				}						
				if(ballX < 7) {       //Punto para el jugador izquierdo. La bola vuelve al medio
					ballX = 340;
					ballY = 160;
					punt1++;				//Sumamos punto al jugador derecho
					game=false; 			//Cuando llega a la pared del principio, se vuelve a restar y pasa a ser positivo. Juega con los signos  
				}			
				//ACCIONES PARA QUE LA BOLA REBOTE 			// Le vamos cambiando el valor al signo
				if(ballY < 7+7) {
					dirY = -dirY; 				//Entonces cuando llega al final de cada pared, dirX
													//Y pasa a ser negativo.		// Y en la suma de arriba, se empieza a restar.
				}
				if(ballY > altoCampo-7) {
					dirY = -dirY;
				}
			}
		}
		
		public void moveJugador() {
			
			if(game==true) { 
				if(direccion1=="UP") {
					jugador1Y -= 3;
				}
				else if(direccion1=="DOWN") {
					jugador1Y+=3;
				}
				if(direccion2=="UP") {
					jugador2Y -= 3;
				}
				else if(direccion2=="DOWN") {
					jugador2Y += 3;
				}
				
				//PARA QUE LOS JUGADORES NO SE SALGAN DEL CAMPO:
				if(jugador1Y <= 10) {          //USO 10 EN VEZ DE 7 PORQUE QUEDA MEJOR SI HAY UN PEQUEÑO MARGEN
					jugador1Y=10;
				}
				if(jugador1Y >= altoCampo-55) {
					jugador1Y=altoCampo-55;
				}
				if(jugador2Y <= 10) {
					jugador2Y=10;
				}
				if(jugador2Y >= altoCampo-55) {
					jugador2Y=altoCampo-55;
				}
				
				
				
			}	
			
		
		}
		
		public void actionPerformed(ActionEvent e) {
			repaint();
			timer.start();
		}

		
		public void keyPressed(KeyEvent e) { //CUANDO PULSEMOS UNA TECLA...
			
			if(gameover==true) { //Solo se puede salir cuando se ha acabado la partida
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
				
				
			}
			 else {
				 
				if(e.getKeyCode() == KeyEvent.VK_UP) {
			 
					if(direccion1 != "DOWN") {
						direccion1 = "UP";
					}
				} 
				else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					if(direccion1 != "UP") {
						direccion1 = "DOWN";
					}			
				}
				
				if(e.getKeyCode() == KeyEvent.VK_W) {
					 
					if(direccion2 != "DOWN") {
						direccion2 = "UP";
					}
				} 
				else if(e.getKeyCode() == KeyEvent.VK_S) {
					if(direccion2 != "UP") {
						direccion2 = "DOWN";
					}			
				}
				if(e.getKeyCode() == KeyEvent.VK_SPACE && game==false) { //INICIAR JUEGO
					game=true;
				}
				else if(e.getKeyCode() == KeyEvent.VK_SPACE && game==true) { //PAUSA
					game=false;
				}
				
				
			 }
		}
		
		
		public void keyReleased(KeyEvent e) { //SI SOLTAMOS LA TECLA:
			
			if(e.getKeyCode() == KeyEvent.VK_UP) { 
					direccion1 = "STOP";
				
			} 
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					direccion1 = "STOP";		
			}
			
			if(e.getKeyCode() == KeyEvent.VK_W) {
					direccion2 = "STOP";
			} 
			if(e.getKeyCode() == KeyEvent.VK_S) {
					direccion2 = "STOP";		
			}
			
			
		}
		//NO HACER CASO
		public void keyTyped(KeyEvent e) {}
		
		
	}
	
	
	
	

}
