package GameState;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

public class Level1State extends GameState{
    private TileMap tileMap;
    private Background bg;
    private AudioPlayer bgMusic;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private HUD hud;
    public Level1State(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }
    public void init(){
        tileMap = new TileMap(30);
        tileMap.loadTiles("/image/Tilesets/grasstileset.gif");
        tileMap.loadMap("/image/Maps/level1-2.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);
        bg = new Background("/image/Backgrounds/grassbg1.gif", 0.1);
        player = new Player(tileMap);
        player.setPosition(100, 100);
        populateEnemies();
        explosions = new ArrayList<Explosion>();
        hud = new HUD(player);
        bgMusic = new AudioPlayer("/image/Music/level1-1.mp3");
        bgMusic.play();
    }
    private void populateEnemies(){
        enemies = new ArrayList<Enemy>();
        Slugger s;
        Point[] points= new Point[]{
                new Point(50, 100),
                new Point(860, 200),
                new Point(1525, 200),
                new Point(1680, 200),
                new Point(1800, 200),
        };
        for(int i = 0; i < points.length; i++){
            s = new Slugger(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
        Point[] points2 = new Point[]{
                new Point(100, 0)
        };
//        Arachnik a;
//        for(int i = 0; i < points2.length; i++){
//            a = new Arachnik(tileMap);
//            a.setPosition(points2[i].x, points2[i].y);
//            enemies.add(a);
//        }
    }
    public void update(){
        player.update();
        tileMap.setPosition(
                GamePanel.WIDTH /2 - player.getx(),
                GamePanel.HEIGHT /2 - player.gety()
        );
        // set background
        bg.setPosition(tileMap.getx(), tileMap.gety());
        // attack enemies
        player.checkAttack(enemies);
        // update all enemies
        for(int i = 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead()){
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(e.getx(), e.gety()));
            }
        }
        // update explosions
        for(int i = 0;i < explosions.size(); i++){
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove()){
                explosions.remove(i);
                i--;
            }
        }
    }
    public void draw(Graphics2D g){
        //draw bg
        bg.draw(g);
        // draw tilemap
        tileMap.draw(g);
        // draw player
        player.draw(g);
        // draw enemies
        for(int i = 0; i < enemies.size(); i++){
            enemies.get(i).draw(g);
        }
        //draw explosions
        for(int i = 0; i < explosions.size(); i++){
            explosions.get(i).setMapPosition(
                    (int) tileMap.getx(), (int) tileMap.gety()
            );
            explosions.get(i).draw(g);
        }
        //draw hud
        hud.draw(g);
    }
    public void keyPressed(int k){
        if(k == KeyEvent.VK_LEFT){
            player.setLeft(true);
        }
        if(k == KeyEvent.VK_RIGHT){
            player.setRight(true);
        }
        if(k == KeyEvent.VK_UP){
            player.setUp(true);
        }
        if(k == KeyEvent.VK_DOWN){
            player.setDown(true);
        }
        if(k == KeyEvent.VK_W){
            player.setJumping(true);
        }
        if(k == KeyEvent.VK_E){
            player.setGliding(true);
        }
        if(k == KeyEvent.VK_R){
            player.setScratching();
        }
        if(k == KeyEvent.VK_F){
            player.setFiring();
        }

    }
    public void keyReleased(int k){
        if(k == KeyEvent.VK_LEFT){
            player.setLeft(false);
        }
        if(k == KeyEvent.VK_RIGHT){
            player.setRight(false);
        }
        if(k == KeyEvent.VK_UP){
            player.setUp(false);
        }
        if(k == KeyEvent.VK_DOWN){
            player.setDown(false);
        }
        if(k == KeyEvent.VK_W){
            player.setJumping(false);
        }
        if(k == KeyEvent.VK_E){
            player.setGliding(false);
        }
        }

}
