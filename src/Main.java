import Core.MazeBoard;
import Helpers.Constants;
import Helpers.Sides;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Kemal BAYINDIR on 12/5/2014.
 */
public class Main {

    static MazeBoard game;
    static boolean handicapMode = true;
    static JFrame frmBoard = new JFrame("Maze Up");

    public static void main(String[] args) {
        // DONE : mazecell leri oluşturma ve ekranı çizdirme (left, right, ... )
        // DONE : start ve exit cell lerinin çizdirilmesi
        // DONE : DFS implementasyonu
        // DONE : adamın yolunu işaretleme
        // DONE : manuel çözme sonrası reload
        // DONE : klavye ile yönlendirme
        // DONE : handikap ekleme
        // DONE : butonlar ve olayların bağlanması
        // DONE : çözme, birinci madde ile ilişkili
        // DONE : solve için s e basılınca random olarak gidilebilir yönü bulması lazım
        // CANCELED : kenardaki start point eğer çizgi ötesinde hamle ile başlarsa sorunu
        // CANCELED : adımların yerine step.png
        // CANCELED : bloklanan kısma daha güzel bir görsel
        // DONE : reload da sorun var ama sonra bakılacak
        // DONE : çıkış için soracak -> esc e basınca
        // DONE : proje dökümanı


        frmBoard.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    game.movePlayer(Sides.up, handicapMode);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    game.movePlayer(Sides.down, handicapMode);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    game.movePlayer(Sides.right, handicapMode);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    game.movePlayer(Sides.left, handicapMode);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !game.isMazeIsReady()) {
                    game.setCurrentCellAsInaccessible();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    draw();

                } else if (e.getKeyCode() == KeyEvent.VK_S) {

                    solve();

                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {

                    restart();

                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

                    exit();

                }
                if (game.isMazeSolved()) {
                    restart();
                }
                //System.out.println("->" + e.getKeyChar());
            }
        });

        loadGame();
    }

    private static void exit() {
        int answer = JOptionPane.showConfirmDialog(frmBoard, "Çıkmak istediğinize emin misiniz ?", "Çıkış", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private static void restart() {
        (new Thread() {
            public void run() {
                loadGame();
                game.repaint();
            }
        }).start();
    }

    private static void solve() {
        (new Thread() {
            public void run() {
                handicapMode = false;
                solveMaze();
            }
        }).start();
    }

    private static void draw() {
        (new Thread() {
            public void run() {
                handicapMode = false;
                continueMazeDrawing();
            }
        }).start();
    }

    public static void loadGame() {
        frmBoard.setLayout(null);
        frmBoard.setSize(Constants.FORM_SIZE + 7, Constants.FORM_SIZE + 130);
        frmBoard.setVisible(true);
        frmBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmBoard.setResizable(false);
        frmBoard.setBackground(Color.black);
        Dimension dim  = Toolkit.getDefaultToolkit().getScreenSize();
        frmBoard.setLocation(dim.width / 2 - frmBoard.getSize().width / 2, dim.height / 2 - frmBoard.getSize().height / 2);

        handicapMode = true;
        if (game!=null)
        frmBoard.remove(game);
        game = new MazeBoard(Constants.FORM_SIZE + 7, Constants.FORM_SIZE);

        //BorderLayout layout = new BorderLayout();
        //frmBoard.setLayout(new FlowLayout());
        //frmBoard.setLayout(new GridBagLayout());

        frmBoard.add(game);

        final Button btnSolve = new Button("Solve");
        final Button btnGenerate = new Button("Generate");
        final Button btnRestart = new Button("Restart");
        final Button btnExit = new Button("Exit");


        btnSolve.setSize(new Dimension(100, 50));
        btnSolve.setLocation(10, frmBoard.getHeight() - btnSolve.getHeight() - 40);
        btnSolve.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                solve();
                btnSolve.setEnabled(false);
                btnGenerate.setEnabled(false);
                btnRestart.setEnabled(true);
                frmBoard.requestFocus();
            }
        });


        btnGenerate.setSize(new Dimension(100, 50));
        btnGenerate.setLocation(120, frmBoard.getHeight() - btnSolve.getHeight() - 40);
        btnGenerate.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                draw();
                btnSolve.setEnabled(true);
                btnGenerate.setEnabled(false);
                btnRestart.setEnabled(true);
                frmBoard.requestFocus();
            }
        });

        btnRestart.setSize(new Dimension(100, 50));
        btnRestart.setLocation(230, frmBoard.getHeight() - btnSolve.getHeight() - 40);
        btnRestart.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restart();
                btnSolve.setEnabled(false);
                btnGenerate.setEnabled(true);
                frmBoard.requestFocus();
            }
        });

        btnExit.setSize(new Dimension(100, 50));
        btnExit.setLocation(340, frmBoard.getHeight() - btnSolve.getHeight() - 40);
        btnExit.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        btnSolve.setEnabled(false);
        btnRestart.setEnabled(false);

        frmBoard.add(btnSolve);
        frmBoard.add(btnGenerate);
        frmBoard.add(btnRestart);
        frmBoard.add(btnExit);

        /*********************************************
         * Maze generation with Depth First Search
         *********************************************/
        // create cells
        game.assignCells();
        // create exit point
        game.createExitCell();
        // create start point
        game.createStartPoint();
    }

    public static void continueMazeDrawing() {
        // relocate start cell
        game.relocateStartCell();
        // draw maze
        game.drawMaze(true);
    }

    public static void solveMaze() {
        // relocate start cell
        game.relocateStartCell();
        // draw maze
        game.solveMaze();
    }

}
