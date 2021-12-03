package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import java.util.Timer;
import java.util.TimerTask;

/*
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author equipoDinamita
 */
public class Main extends SimpleApplication {

    Quaternion PITCH090 = new Quaternion().fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));//quaternion;
    // Constantes triggers que representan los clicks de la barra y el mouse
    // Los triggers son los objetos que representan las entradas fisicas de los cliks o joystick
    private final static Trigger TRIGGER_SHOT = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);

    //definimos las constantes que nos ayudaran a identificar las acciones por los triggers
    // Recuerda: Una accion puede tener mas de un trigger que la activa
    private final static String MAPPING_SHOT = "Shot";

    //Se define y hace estatico la malla que se podra replicar
    public static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);
    public static Main detener;

    //Spatial s_covicho;
    //Node nodo_covicho;
    //Spatial s_base;
    //Node nodo_base;

    String nombreSpatial = "s_covicho";
    Spatial[] misSpatial = new Spatial[(int) (Math.random()*8 ) + 1];

    //ubicación en X del porton
    float x_porton;

    Sphere miSphere;
    Geometry miGeom;
    String nombreEnemigo = "Covicho";
    Geometry covicho_geom;

    public static void main(String[] args) {

        //modificando la página de carga del game
        AppSettings settings = new AppSettings(true);
        //aplicamos titulo a la ventana
        settings.setTitle("Tower Defense");//nombre del game
        //colocamos imagen a la ventana de inicio
        settings.setSettingsDialogImage("Interface/inicio.jpg");//imagen de inicio
        //establecemos la resolución en la cual iniciará el juego
        settings.setResolution(1280, 960);//resolucion

        //creamos nuestra app
        Main app = new Main();
        //aplicamos las especificaciones de la app
        app.setSettings(settings);

        //app.setShowSettings(false);//cerrando la ventana de ajustes
        detener = app;
        //iniciamos nuestra aplicación
        app.start();
        
    }//fin del método main

    /**
     * SimpleInitApp
     * aqui creamos nuestras geometrias y nuestros enemigos
     */
    @Override
    public void simpleInitApp() {

        //para hacer uso de los triggers y mapping se deben registrar en el inputManager
        // Se utiliz el Rotate mapping para la mira del mouse en la escena
        inputManager.addMapping(MAPPING_SHOT, TRIGGER_SHOT);

        // Para poder activar los mapping debemos estar escuchando para detectar el input
        inputManager.addListener(analogListener, new String[]{MAPPING_SHOT});

        //bloqueamos el movimiento de la cámara para que no pueda usarse las letras A, W, S, D
        flyCam.setMoveSpeed(0);
        //creamos vector para posicionar nuestra camara
        Vector3f posicion = new Vector3f((float) -94.203064, (float) 38.74732, 28);
        //establecemos los valores de la posicion inicial a la cámara
        cam.setLocation(posicion);
        //rotamos nuestra camara
        cam.setRotation(PITCH090);
        
        //-------------- Creación de la base del juego -------------------------
        
        //construimos base del juego
        Box base = new Box(100, 1.0f, 35);
        //creamos nuestra geometria de la base
        Geometry geomBase = new Geometry("Base", base);
        //creamos un material que será colocado en nuestra geometria
        Material matBase = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //establecemos el material a nuestra geometria
        geomBase.setMaterial(matBase);
        //añadimos una textura a nuestra geometria base
        matBase.setTexture("ColorMap", assetManager.loadTexture("Textures/piso_lava.jpg"));
        //añadimos al rootNode nuesta geometria
        rootNode.attachChild(geomBase);

        //-------------- Creación de la Torre No. 1 ----------------------------
        
        //construcción de la primera torre
        Box torre1 = new Box(4, 16, 4);
        //creamos nuestra geometria de la torre No.1
        Geometry geomTorre1 = new Geometry("Torre1", torre1);
        //creamos un material que será colocado en nuestra geometria
        Material matTorre1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //establecemos el material a nuestra geometria
        geomTorre1.setMaterial(matTorre1);
        //acomodamos la torre No. 1 en el lugar definido por el vector, el cual es al final de la base
        geomTorre1.move(new Vector3f(-95, 17, -30));
        //rotamos la torre No. 1
        geomTorre1.rotate(0, FastMath.HALF_PI, 0);
        //añadimos una textura a nuestra geometria Torre No. 1
        matTorre1.setTexture("ColorMap", assetManager.loadTexture("Textures/torre.jpg"));
        //añadimos al rootNode nuesta geometria
        rootNode.attachChild(geomTorre1);

        //-------------- Creación de la Torre No. 2 ----------------------------
        
        //construcción de la segunda torre
        Box torre2 = new Box(4, 16, 4);
        //creamos nuestra geometria de la torre No.2
        Geometry geomTorre2 = new Geometry("Torre2", torre2);
        //creamos un material que será colocado en nuestra geometria
        Material matTorre2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //establecemos el material a nuestra geometria
        geomTorre2.setMaterial(matTorre1);
        //acomodamos la torre No. 2 en el lugar definido por el vector, el cual es al final de la base
        geomTorre2.move(new Vector3f(-95, 17, 30));
        //rotamos la torre No. 2
        geomTorre1.rotate(0, FastMath.HALF_PI, 0);
        //añadimos una textura a nuestra geometria Torre No. 2
        matTorre2.setTexture("ColorMap", assetManager.loadTexture("Textures/torre.jpg"));
        //añadimos al rootNode nuesta geometria
        rootNode.attachChild(geomTorre2);

        //---------- Creación de la base principal o portón --------------------
        
        //creación de la base principal
        Box basePrin = new Box(22, 13, 0);
        //creamos nuestra geometria de la base principal o portón
        Geometry geombasePrin = new Geometry("basePrin", basePrin);
        //creamos un material que será colocado en nuestra geometria
        Material matbasePrin = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //establecemos el material a nuestra geometria
        geombasePrin.setMaterial(matbasePrin);
        //acomodamos la base principal en el lugar definido por el vector, el cual es al final de la base
        geombasePrin.move(new Vector3f(-95, 14.2f, 0.14f));
        //rotamos la base principal o portón
        geombasePrin.rotate(0, FastMath.HALF_PI, 0);
        //obtenemos la distancia del portón en el eje x
        x_porton = geombasePrin.getLocalTranslation().getX();
        //añadimos una textura a nuestra geometria de la base principal o portón
        matbasePrin.setTexture("ColorMap", assetManager.loadTexture("Textures/porton.jpg"));
        //añadimos al rootNode nuesta geometria
        rootNode.attachChild(geombasePrin);
        
        //---------- Fin de la creación de las geometrias estaticas ------------
       
        //llamamos método para crear las geometrias de los enemigos
        crearEnemigos();
        //crearEnemigosTimer();
        
        // La mira que indica la posiicon del mouse es inicializada
        attachCenterMark();
        
    }//fin de método simpleInitApp

    /**
     * myBox regresa un geometry de un covicho
     *
     * @param name String del Nombre para identificar la geometry en el scene
     * @return Geometry de una caja con los parametros especificados
     */
    private Geometry myBox(String name) {
        //creamos numero aleatorio para que este sea establecido a la posicion de un enemigo
        int numero = (int) (Math.random() * -70 + 35);
 
        //creamos los enemigos;
        //Creamos una esfera para nuestros enemigos llamados covichos
        Sphere covicho = new Sphere(20, 20, 2);
        //creamos una geometria para nuestro enemigo
        covicho_geom = new Geometry(name, covicho);
        //creamos un material
        Material covicho_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //establecemos el material a nuestra geometria
        covicho_mat.setTexture("ColorMap", assetManager.loadTexture("Textures/covicho1.png"));
        //aplicamos el material a nuestra geometria
        covicho_geom.setMaterial(covicho_mat);

        //rotamos la geometria del enemigo
        covicho_geom.rotate(FastMath.DEG_TO_RAD * -75, 0, 2);
        //movemos la geometria del enemigo
        covicho_geom.move(new Vector3f(100, 2.8f, numero));

        //retornamos la geometria del enemigo creada
        return covicho_geom;
        
    }//fin del método myBox

    /**
     * Utilizamos el listener analogico ya que la accion de rotacion sera una accion continua.
     */
    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float intensity, float tpf) {
            // se comprueba que el trigger indentificado corresponda a la acción deseada
            if (name.equals(MAPPING_SHOT)) {
                // En esta sección determinamos la accion de rotar la caja que este apuntando 
                // La mira del mouse.
                //colision identificara el objeto al cual se le hace click
                CollisionResults results = new CollisionResults();
                // Se proyecta una linea de acuerdo a la posicion de la camara, en la 
                //direccion donde la camara esta apuntando
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                //calculamos si esta rayo proyectado hace colision con el objeto
                rootNode.collideWith(ray, results);

                //Si el usuario ha hecho click en algo, identificaremos la geometria seleccionada
                if (results.size() > 0) {
                    Geometry target1 = results.getFarthestCollision().getGeometry();
                    Geometry target2 = results.getClosestCollision().getGeometry();
                    //se implementara la accion identificada
                    if (target1.getName().contains("Covicho") || target1.getName().contains("covicho")) {
                        target1.removeFromParent();
                    } else if (target2.getName().contains("Covicho") || target2.getName().contains("covicho")) {
                        target2.removeFromParent();
                    }//fin del if/else
                    
                }//fin del if
                
            }//fin del if
        
        }//fin del onAnalog
        
    };//fin del AnalogListener

    /**
     * Método simpleUpdate para mover nuestros enemigos hasta llegar a la base
     * @param tpf 
     */
    @Override
    public void simpleUpdate(float tpf) {
       
        //crearEnemigosTimer();
        
        /**
         * for para ir iterando en nuestro arreglo de Spatial e ir 
         */
        for (Spatial misSpatial1 : misSpatial) {
            //movemos nuestro enemigo o spatial por el eje x
            misSpatial1.move(-FastMath.HALF_PI / 10, 0, 0);
            //rotamos nuestro enemigo o spatial
            misSpatial1.rotate(FastMath.HALF_PI / 100, 0, tpf * 2);
            
            /**
             * verificamos si nuestro enemigo logra cruzar el portón si es así
             * se acaba el juego
             */
            if (x_porton >= misSpatial1.getLocalTranslation().getX()) {
                //eliminamos el spatial que llegó al portón
                //misSpatial1.deepClone();
                
                //cerramos el juego
                detener.stop();
            }//fin del if
            
        } //fin del for
        
    }//fin del método simpleUpdate

    /*
     * attachCenterMarck crea un objeto geometry que servira de mira para apuntar 
     * diferentes objetos en el escenario. Ya que es una marca 2D, se debe adjuntar 
     * a la interface 2D del usurio "guiNode", este objeto es intanciado en 
     * cualquier SimpleApplication.
     */
    private void attachCenterMark() {

        Sphere mira = new Sphere(20, 20, 2);
        Geometry mira_geom = new Geometry("center mark", mira);
        //creamos un material que será para nuestro sol
        Material mira_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //asignamos a nuestro material la img del sol
        mira_mat.setColor("Color", ColorRGBA.Cyan);
        //covicho_mat.setTexture("ColorMap", assetManager.loadTexture("Textures/covicho1.png"));
        //aplicamos el material a nuestra geometria
        mira_geom.setMaterial(mira_mat);
        //Geometry c = this.myBox("center mark");
        mira_geom.scale(4);
        mira_geom.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2, 0);
        guiNode.attachChild(mira_geom); //adjunta a la interface 2D del usuario
    }//fin del método attachCenterMark

    /**
     * Método para crear enemigos de forma dinámica
     */
    private void crearEnemigos() {
        /**
         * bucle para ir creando enemigos dinamicamente
         */
        for (int i = 0; i < misSpatial.length; i++) {
            //convertimos a i en un String
            String nombre = String.valueOf(i);
            /**
             * concatenamos el nombre del enemigo al número i del bucle parseado
             * esto con la finalidad de que siempre tengan nombres distintos
             * nuestras geometrias 
             */
            nombreEnemigo = nombreEnemigo + nombre;
            /**
             * añadimos al rootNode nuesta geometria llamando al método myBox 
             * y mandando como parámetro nuestro nombre el cuál será distinto 
             * cada vez que se mande llamar
             */
            rootNode.attachChild(this.myBox(nombreEnemigo));
            /**
             * si nuestro spatial posición i del bucle es igual a null colocamos
             * el rootNode a nuestra geometria segun su nombre
             */
            if (misSpatial[i] == null) {
                //colocamos nuestro Spatial al rootNod
                misSpatial[i] = rootNode.getChild(covicho_geom.getName());
            }//fin del if
            /**
             * limpiamos nombreEnemigo para que cada iteración del bucle solo lleve
             * el nombre "Covicho" seguido del i parseado a String
             */
            nombreEnemigo = "Covicho";
        }//fin del for
        
    }//fin del método crearEnemigos
    
    
    /*
    private void crearEnemigosTimer() {
        
        Timer timer = new Timer();
        
        TimerTask task = new TimerTask(){
            
            int tic = 0;
            
            @Override
            public void run() {
                
                crearEnemigos();
                //System.out.println("Esperamos");
                        
            }
            
            
            
        };
        
        timer.schedule(task, 10, 2000);
    }*/

}//fin de la clase main
